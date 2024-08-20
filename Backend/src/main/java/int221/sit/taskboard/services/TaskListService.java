package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.*;
import int221.sit.taskboard.entities.*;
import int221.sit.taskboard.exceptions.*;
import int221.sit.taskboard.repositories.task.StatusListRepository;
import int221.sit.taskboard.repositories.task.TaskListRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskListService {
    @Autowired
    private TaskListRepository repository;

    @Autowired
    public StatusListRepository statusListRepository;

    @Autowired
    public ModelMapper modelMapper;

    public List<TaskList> getAllTaskList(String[] param) {
        if (param == null) {
            return repository.findAll();
        } else {
            return repository.findAllById(List.of());
        }
    }

    public TaskList findById(Integer id) {
        return repository.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Task id " + id + " does not exist!"));
    }

    public TaskList getTaskListById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Task " + id + " does not exist !!"));
    }

    public List<TaskShortDetail> getAllTaskListDto() {
        List<TaskList> taskLists = repository.findAll();
        return taskLists.stream()
                .map(taskList -> modelMapper.map(taskList, TaskShortDetail.class))
                .collect(Collectors.toList());
    }

    public TaskListDetail getTaskListByIdDto(Integer id) {
        TaskList taskList = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task id" + id + "does not exist!"));
        List<TaskList> taskListSingleton = Collections.singletonList(taskList);
        return modelMapper.map(taskListSingleton, TaskListDetail.class);
    }

    @Transactional
    public TaskAndStatusObject createNewTaskList(TaskAndStatusInt newTaskListDto, Integer statusId) {
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
        taskList.setStatus(statusList);

//        TaskListValidation.validateTaskDataLength(newTaskListDto);

        try {
            TaskList savedTaskList = repository.saveAndFlush(taskList);
            TaskAndStatusObject taskListDto = modelMapper.map(savedTaskList, TaskAndStatusObject.class);
            return taskListDto;
        } catch (DataAccessException e) {
            throw new BadRequestException("Failed to create new task list");
        }
    }

    @Transactional
    public TaskShortDetail removeTaskListById(Integer id) {
        TaskList taskList = repository.findById(id).orElse(null);
        if (taskList != null) {
            TaskShortDetail deletedTaskListDto = modelMapper.map(taskList, TaskShortDetail.class);
            repository.delete(taskList);
            return modelMapper.map(deletedTaskListDto, TaskShortDetail.class);
        } else {
            throw new ItemNotFoundException("Task id " + id + " does not exist!");
        }
    }

    @Transactional
    public TaskAndStatusObject updateTaskListById(Integer id, TaskAndStatusInt newTaskListDto, Integer statusId) {

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

//        TaskListValidation.validateTaskDataLength(newTaskListDto);
        TaskList updatedTaskList = repository.saveAndFlush(taskListUpdated);

        return modelMapper.map(updatedTaskList, TaskAndStatusObject.class);
    }

    @Transactional
    public TaskAndStatusInt transferTasking(Integer oldStatusId, Integer newStatusId){
        StatusList newStatus = statusListRepository.findById(newStatusId)
                .orElseThrow(() -> new BadRequestException("the specified status for task transfer does not exist"));

        List<TaskList> taskLists = repository.findByStatusId(oldStatusId);
        for (TaskList taskList : taskLists) {
            taskList.setStatus(newStatus);
            repository.saveAndFlush(taskList);
        }
        return null;
    }

    public List<TaskShortDetail> getTasksSortedByStatusName(String sortBy, List<String> filterStatuses) {
        List<TaskList> tasks;

        if (filterStatuses == null || filterStatuses.isEmpty()) {
            tasks = repository.findAll();
        } else {
            List<String> validStatuses = statusListRepository.findAll().stream()
                    .map(StatusList::getName)
                    .map(String::toLowerCase)
                    .map(String::trim)
                    .collect(Collectors.toList());

            boolean allValidStatuses = filterStatuses.stream().allMatch(validStatuses::contains);
            if (allValidStatuses) {
                tasks = repository.findByStatusNameIn(filterStatuses);
            } else {
                throw new BadRequestException("Invalid filterStatuses");
            }
        }
        try {
            tasks = sortTasks(tasks, sortBy);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }

        return tasks.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private List<TaskList> sortTasks(List<TaskList> tasks, String sortBy) {
        switch (sortBy) {
            case "status.name":
                tasks.sort(Comparator.comparing(task -> task.getStatus().getName()));
                break;
            case "status.name.desc":
                tasks.sort((task1, task2) -> task2.getStatus().getName().compareTo(task1.getStatus().getName()));
                break;
            case "createdDate":
                tasks.sort(Comparator.comparing(TaskList::getCreatedOn));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort option: " + sortBy);
        }
        return tasks;
    }

    private TaskShortDetail convertToDto(TaskList task) {
        TaskShortDetail dto = modelMapper.map(task, TaskShortDetail.class);
        dto.setStatus(task.getStatus() != null ? task.getStatus() : null);
        return dto;
    }
}
