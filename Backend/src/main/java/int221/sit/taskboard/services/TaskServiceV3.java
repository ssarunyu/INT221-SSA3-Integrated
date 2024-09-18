package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.*;
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
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceV3 {
    @Autowired
    private TaskListRepository repository;

    @Autowired
    public StatusListRepository statusListRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    public ModelMapper modelMapper;

    @Transactional("taskBoardTransactionManager")
    public TaskAndStatusObject createNewTask(TaskAndStatusInt newTaskListDto, String boardId, Integer statusId) {
        Boards board = boardRepository.findByBoardId(boardId);
        if (board == null) {
            throw new ItemNotFoundException("Board not found!");
        }

        newTaskListDto.trimValues();

        StatusList statusList;

        if (statusId != null) {
            statusList = statusListRepository.findById(statusId)
                    .orElseThrow(() -> new BadRequestException("Status id " + statusId + " does not exist!"));
        } else {
            statusList = statusListRepository.findById(1)
                    .orElseThrow(() -> new ItemNotFoundException("Default status does not exist!"));
        }

        TaskList taskList = modelMapper.map(newTaskListDto, TaskList.class);
        taskList.setBoard(board);
        taskList.setStatus(statusList);
        taskList.setCreatedOn(ZonedDateTime.now());
        taskList.setUpdatedOn(ZonedDateTime.now());

        try {
            TaskList savedTaskList = repository.saveAndFlush(taskList);
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
            tasks = repository.findAllByBoardIdOrderAndFilterByDesc(boardId, filterStatuses, sortBy);
        } else {
            tasks = repository.findAllByBoardIdOrderAndFilterByAsc(boardId, filterStatuses, sortBy);
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
        TaskList taskList = repository.findByBoardIdAndTaskId(boardId, taskId)
                .orElseThrow(() -> new ItemNotFoundException("Task id " + taskId + " does not exist!"));

        TaskListDetail taskListDetail = modelMapper.map(taskList, TaskListDetail.class);
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

        TaskList taskListUpdated = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task id " + id + " does not exist!"));

        taskListUpdated.setTitle(newTaskListDto.getTitle());
        taskListUpdated.setDescription(newTaskListDto.getDescription());
        taskListUpdated.setAssignees(newTaskListDto.getAssignees());

        if (statusId != null) {
            StatusList statusList = statusListRepository.findById(statusId)
                    .orElseThrow(() -> new BadRequestException("Status id " + statusId + " does not exist!"));
            taskListUpdated.setStatus(statusList);
        }

        TaskList updatedTaskList = repository.saveAndFlush(taskListUpdated);

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
        TaskList taskList = repository.findById(taskId)
                .orElseThrow(() -> new ItemNotFoundException("Task id " + taskId + " does not exist!"));

        // ตรวจสอบว่า TaskList เป็นของ boardId ที่กำหนดหรือไม่
        if (!taskList.getBoard().getBoardId().equals(boardId)) {
            throw new BadRequestException("Task id " + taskId + " does not belong to board " + boardId);
        }

        // แปลง TaskList เป็น TaskShortDetail ก่อนลบ
        TaskShortDetail deletedTaskListDto = modelMapper.map(taskList, TaskShortDetail.class);

        // ลบ TaskList
        repository.delete(taskList);

        // ส่งคืน TaskShortDetail ที่แปลงแล้ว
        return deletedTaskListDto;
    }


}
