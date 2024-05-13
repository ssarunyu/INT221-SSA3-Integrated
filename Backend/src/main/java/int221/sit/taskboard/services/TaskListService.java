package int221.sit.taskboard.services;

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
import java.util.Optional;

@Service
public class TaskListService {
    @Autowired
    private TaskListRepository repository;

    @Autowired
    public StatusListRepository statusListRepository;

    @Autowired
    public ModelMapper modelMapper;

    @Autowired
    public ListMapper listMapper;

    public List<TaskList> getAllTaskList(String[] param) {
        if (param == null) {
            return repository.findAll();
        } else {
            return repository.findAllById(List.of());
        }
    }
    public TaskList findById(Integer id) {
        return repository.findById(id).orElseThrow(
              ()-> new ItemNotFoundException(
                "Task id " + id + " does not exist!"));
    }

    public TaskList getTaskListById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task " + id + " does not exist !!"));
    }

    public List<TaskListDto> getAllTaskListDto(String[] param) {
        List<TaskList> taskList = repository.findAll();
        return listMapper.mapList(taskList, TaskListDto.class, modelMapper);
    }

    public List<TaskListByIdDto> getTaskListByIdDto(Integer id) {
        TaskList taskList = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task id"+ id + "does not exist!"));
        List<TaskList> taskListSingleton = Collections.singletonList(taskList);
        return listMapper.mapList(taskListSingleton, TaskListByIdDto.class, modelMapper);
    }

    @Transactional
    public NewTaskListDto createNewTaskList(NewTaskListDto newTaskListDto,Integer statusId) {
        TaskList newTaskList = modelMapper.map(newTaskListDto, TaskList.class);
        newTaskList.trimValues();
        if(statusId != null) {
            StatusList statusList = statusListRepository.findById(statusId).orElse(null);
            if (statusList != null) {
                newTaskList.setStatus(statusList);
            }
        }

        if(newTaskList.getStatus().getId() == null) {
            StatusList defaultStatus = statusListRepository.findById(1).orElseThrow(() -> new ItemNotFoundException("Default status not found"));
            newTaskList.setStatus(defaultStatus);
        }

        TaskList createdTaskList = repository.saveAndFlush(newTaskList);

        return modelMapper.map(createdTaskList, NewTaskListDto.class);
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
    public NewTaskListDto updateTaskListById(Integer id, TaskList taskList, Integer statusId) {
        TaskList taskListUpdated = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Task id " + id + " does not exist!"));

        taskList.setId(id);
        taskList.trimValues();

        if (statusId != null) {
            StatusList statusList = statusListRepository.findById(statusId).orElse(null);
            if (statusList != null) {
                taskListUpdated.setStatus(statusList);
            }
        }

        TaskList updateTaskList = repository.save(taskListUpdated);

        return modelMapper.map(updateTaskList, NewTaskListDto.class);
    }
}
