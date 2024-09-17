package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.TaskAndStatusInt;
import int221.sit.taskboard.DTO.TaskAndStatusObject;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;

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


}
