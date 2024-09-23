package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.boards.BoardDTO;
import int221.sit.taskboard.DTO.boards.BoardShortDetail;
import int221.sit.taskboard.DTO.tasks.*;
import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import int221.sit.taskboard.entities.itbkk_db.TaskList;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.repositories.task.BoardRepository;
import int221.sit.taskboard.repositories.task.StatusListRepository;
import int221.sit.taskboard.repositories.task.TaskListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceV3 {
    @Autowired
    private TaskListRepository taskRepository;

    @Autowired
    public StatusListRepository statusRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    public ModelMapper modelMapper;

    @Autowired
    private BoardService boardService;

    @Transactional("taskBoardTransactionManager")
    public TaskAndStatusObject createNewTask(TaskAndStatusInt newTaskListDto, String boardId, Integer statusId) {
        Boards board = boardRepository.findByBoardId(boardId);
        if (board == null) {
            throw new ItemNotFoundException("Board not found!");
        }

        newTaskListDto.trimValues();

        StatusList statusList;

        if (statusId != null) {
            statusList = statusRepository.findById(statusId)
                    .orElseThrow(() -> new BadRequestException("Status id " + statusId + " does not exist!"));
            if (!statusList.getBoard().getBoardId().equals(boardId)) {
                throw new BadRequestException("Status does not belong to the specified board!");
            }
        } else {
            statusList = statusRepository.findById(1)
                    .orElseThrow(() -> new ItemNotFoundException("Default status does not exist!"));
        }

        TaskList taskList = modelMapper.map(newTaskListDto, TaskList.class);
        taskList.setBoard(board);
        taskList.setStatus(statusList);
        taskList.setCreatedOn(ZonedDateTime.now());
        taskList.setUpdatedOn(ZonedDateTime.now());

        try {
            TaskList savedTaskList = taskRepository.saveAndFlush(taskList);
            TaskAndStatusObject taskAndStatusObject = new TaskAndStatusObject();
            taskAndStatusObject.setId(savedTaskList.getId());
            taskAndStatusObject.setTitle(savedTaskList.getTitle());
            taskAndStatusObject.setDescription(savedTaskList.getDescription());
            taskAndStatusObject.setAssignees(savedTaskList.getAssignees());
            taskAndStatusObject.setStatus(savedTaskList.getStatus());
            taskAndStatusObject.setBoardId(savedTaskList.getBoard().getBoardId());
            taskAndStatusObject.setCreatedOn(savedTaskList.getCreatedOn());
            taskAndStatusObject.setUpdatedOn(savedTaskList.getUpdatedOn());
            return taskAndStatusObject;
        } catch (DataAccessException e) {
            throw new BadRequestException("Failed to create new task list: " + e.getMessage());
        }
    }

    @Transactional("taskBoardTransactionManager")
    public List<TaskShortDetail> getAllTasksSortedAndFilterForBoard(String boardId, List<String> filterStatuses, String sortDirection, String sortBy) {
        List<TaskList> tasks;
        if ("desc".equalsIgnoreCase(sortDirection)) {
            tasks = taskRepository.findAllByBoardIdOrderAndFilterByDesc(boardId, filterStatuses, sortBy);
        } else {
            tasks = taskRepository.findAllByBoardIdOrderAndFilterByAsc(boardId, filterStatuses, sortBy);
        }
        List<TaskShortDetail> taskDTOs = new ArrayList<>();
        for (TaskList task : tasks) {
            TaskShortDetail taskShortDetail = new TaskShortDetail();
            taskShortDetail.setId(task.getId());
            taskShortDetail.setTitle(task.getTitle());
            taskShortDetail.setAssignees(task.getAssignees());
            taskShortDetail.setStatus(task.getStatus());
            taskShortDetail.trimValues();
            taskDTOs.add(taskShortDetail);
        }
        return taskDTOs;
    }

    @Transactional("taskBoardTransactionManager")
    public TaskListDetail getTaskById(String boardId, Integer taskId) {
        TaskList taskList = taskRepository.findByBoardIdAndTaskId(boardId, taskId)
                .orElseThrow(() -> new ItemNotFoundException("Task id " + taskId + " does not exist!"));

        TaskListDetail taskListDetail = modelMapper.map(taskList, TaskListDetail.class);

        BoardShortDetail boardShortDetail = taskListDetail.getBoard();
        BoardDTO boardDTO = boardService.getBoardById(boardId);
        if (boardDTO != null && boardDTO.getOwner() != null) {
            boardShortDetail.setOwnerUsername(boardDTO.getOwner().getUsername());
        }
        taskListDetail.setBoard(boardShortDetail);

        return taskListDetail;
    }

    @Transactional("taskBoardTransactionManager")
    public TaskAndStatusObject updateTaskListById(String boardId, Integer id, TaskAndStatusInt newTaskListDto, Integer statusId) {
        Boards board = boardRepository.findByBoardId(boardId);
        if (board == null) {
            throw new ItemNotFoundException("Board not found!");
        }

        if (newTaskListDto.getTitle() != null && !newTaskListDto.getTitle().isEmpty()) {
            newTaskListDto.setTitle(newTaskListDto.getTitle().trim());
        } else {
            throw new BadRequestException("Title must not be null");
        }

        TaskList taskListUpdated = taskRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task id " + id + " does not exist!"));

        taskListUpdated.setTitle(newTaskListDto.getTitle());
        taskListUpdated.setDescription(newTaskListDto.getDescription());
        taskListUpdated.setAssignees(newTaskListDto.getAssignees());

        if (statusId != null) {
            StatusList statusList = statusRepository.findById(statusId)
                    .orElseThrow(() -> new BadRequestException("Status id " + statusId + " does not exist!"));
            if (!statusList.getBoard().getBoardId().equals(boardId)) {
                throw new BadRequestException("Status does not belong to the specified board!");
            }
            taskListUpdated.setStatus(statusList);
        }

        TaskList updatedTaskList = taskRepository.saveAndFlush(taskListUpdated);

        return convertToDTO(updatedTaskList); // ใช้เมธอดแปลงข้อมูลที่เขียนด้วยมือ
    }

    public TaskAndStatusObject convertToDTO(TaskList taskList) {
        TaskAndStatusObject taskAndStatusObject = new TaskAndStatusObject();
        taskAndStatusObject.setId(taskList.getId());
        taskAndStatusObject.setTitle(taskList.getTitle());
        taskAndStatusObject.setDescription(taskList.getDescription());
        taskAndStatusObject.setAssignees(taskList.getAssignees());
        taskAndStatusObject.setBoardId(taskList.getBoard().getBoardId());
        taskAndStatusObject.setCreatedOn(taskList.getCreatedOn());
        taskAndStatusObject.setUpdatedOn(taskList.getUpdatedOn());
        taskAndStatusObject.setStatus(taskList.getStatus());

        return taskAndStatusObject;
    }

    @Transactional("taskBoardTransactionManager")
    public TaskShortDetail removeTaskById(String boardId, Integer taskId) {
        // ค้นหา TaskList โดย id
        TaskList taskList = taskRepository.findById(taskId)
                .orElseThrow(() -> new ItemNotFoundException("Task id " + taskId + " does not exist!"));

        // ตรวจสอบว่า TaskList เป็นของ boardId ที่กำหนดหรือไม่
        if (!taskList.getBoard().getBoardId().equals(boardId)) {
            throw new BadRequestException("Task id " + taskId + " does not belong to board " + boardId);
        }

        // แปลง TaskList เป็น TaskShortDetail ก่อนลบ
        TaskShortDetail deletedTaskListDto = modelMapper.map(taskList, TaskShortDetail.class);

        // ลบ TaskList
        taskRepository.delete(taskList);

        // ส่งคืน TaskShortDetail ที่แปลงแล้ว
        return deletedTaskListDto;
    }

    @Transactional("taskBoardTransactionManager")
    public List<TaskAndStatusObjShort> transferTasking(String boardId, Integer statusId, Integer statusNewId) {
        StatusList status = statusRepository.findByIdAndBoard(statusId, boardId)
                .orElseThrow(() -> new ItemNotFoundException("Status id " + statusId + " does not exist for board " + boardId));
        StatusList newStatus = statusRepository.findByIdAndBoard(statusNewId, boardId)
                .orElseThrow(() -> new BadRequestException("The specified status id " + statusNewId + " for task transfer does not exist!"));

        List<TaskList> tasksToTransfer = taskRepository.findByStatusIdAndBoard(statusId, boardId);

        List<TaskAndStatusObjShort> transferredTasks = tasksToTransfer.stream().map(task -> {
            task.setStatus(newStatus);

            TaskAndStatusObjShort dto = new TaskAndStatusObjShort();
            dto.setId(task.getId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setAssignees(task.getAssignees());
            dto.setStatus(newStatus);
            return dto;
        }).collect(Collectors.toList());

        taskRepository.saveAll(tasksToTransfer);
        return transferredTasks;
    }

}
