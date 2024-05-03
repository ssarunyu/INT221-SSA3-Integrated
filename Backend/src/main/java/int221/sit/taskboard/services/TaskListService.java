package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.TaskListByIdDto;
import int221.sit.taskboard.DTO.TaskListDto;
import int221.sit.taskboard.DTO.NewTaskListDto;
import int221.sit.taskboard.entities.TaskList;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.repositories.TaskListRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@Service
public class TaskListService {
    @Autowired
    private TaskListRepository repository;

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
                "TaskList Number ' "+ id + " ' does not exist!"));
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task " + id + " does not exist !!"));
        List<TaskList> taskListSingleton = Collections.singletonList(taskList);
        return listMapper.mapList(taskListSingleton, TaskListByIdDto.class, modelMapper);
    }

    @Transactional
    public NewTaskListDto createNewTaskList(NewTaskListDto newTaskListDto) {
        TaskList taskList = modelMapper.map(newTaskListDto, TaskList.class);
        return modelMapper.map(repository.saveAndFlush(taskList), newTaskListDto.getClass());
    }

    @Transactional
    public String removeTaskList(Integer id) {
        TaskList taskList = repository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Task Id " + id + " DOES NOT EXIST !!!"));
        repository.delete(taskList);
        return "Task ID " + id + " has been successfully deleted.";
    }

    @Transactional
    public TaskList updateTaskList(Integer id, TaskList taskList) {
        TaskList taskListUpdated= repository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Task Id" + id + "DOES NOT EXIST !!!"));
        taskList.setId(id);
        return repository.save(taskList);
    }
}
