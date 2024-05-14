package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.NewTaskListDtoV2;
import int221.sit.taskboard.DTO.TaskListByIdDto;
import int221.sit.taskboard.DTO.TaskListDto;
import int221.sit.taskboard.DTO.NewTaskListDto;
import int221.sit.taskboard.entities.StatusList;
import int221.sit.taskboard.entities.TaskList;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.repositories.StatusListRepository;
import int221.sit.taskboard.repositories.TaskListRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
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
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task " + id + " does not exist !!"));
    }

    public List<TaskListDto> getAllTaskListDto() {
        List<TaskList> taskLists = repository.findAll();
        return taskLists.stream()
                .map(taskList -> modelMapper.map(taskList, TaskListDto.class))
                .collect(Collectors.toList());
    }

    public TaskListByIdDto getTaskListByIdDto(Integer id) {
        TaskList taskList = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task id" + id + "does not exist!"));
        List<TaskList> taskListSingleton = Collections.singletonList(taskList);
        return modelMapper.map(taskListSingleton, TaskListByIdDto.class);
    }

    @Transactional
    public NewTaskListDto createNewTaskList(NewTaskListDtoV2 newTaskListDto, Integer statusId) {
        newTaskListDto.trimValues();

        StatusList statusList;
        if (statusId != null) {
            statusList = statusListRepository.findById(statusId)
                    .orElseThrow(() -> new ItemNotFoundException("Status id " + statusId + " does not exist!"));
        } else {
            statusList = statusListRepository.findById(1)
                    .orElseThrow(() -> new ItemNotFoundException("Status id 1 does not exist!"));
        }


        TaskList taskList = modelMapper.map(newTaskListDto, TaskList.class);
        taskList.setStatus(statusList);

        TaskList savedTaskList = repository.saveAndFlush(taskList);
        NewTaskListDto taskListDto = modelMapper.map(savedTaskList, NewTaskListDto.class);

        return taskListDto;
    }

    @Transactional
    public TaskListDto removeTaskListById(Integer id) {
        TaskList taskList = repository.findById(id).orElse(null);
        if (taskList != null) {
            TaskListDto deletedTaskListDto = modelMapper.map(taskList, TaskListDto.class);
            repository.delete(taskList);
            return modelMapper.map(deletedTaskListDto, TaskListDto.class);
        } else {
            throw new ItemNotFoundException("Task id " + id + " does not exist!");
        }
    }

    @Transactional
    public NewTaskListDto updateTaskListById(Integer id, NewTaskListDtoV2 newTaskListDto, Integer statusId) {

        TaskList taskListUpdated = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task id " + id + " does not exist!"));

        newTaskListDto.trimValues();

        taskListUpdated.setTitle(newTaskListDto.getTitle());
        taskListUpdated.setDescription(newTaskListDto.getDescription());
        taskListUpdated.setAssignees(newTaskListDto.getAssignees());

        if (statusId != null) {
            StatusList statusList = statusListRepository.findById(statusId)
                    .orElseThrow(() -> new ItemNotFoundException("Status id " + statusId + " does not exist!"));
            taskListUpdated.setStatus(statusList);
        }

        TaskList updatedTaskList = repository.saveAndFlush(taskListUpdated);

        return modelMapper.map(updatedTaskList, NewTaskListDto.class);
    }
}
