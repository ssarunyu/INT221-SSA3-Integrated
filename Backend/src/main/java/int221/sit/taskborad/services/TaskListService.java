package int221.sit.taskborad.services;

import int221.sit.taskborad.DTO.TaskListByIdDto;
import int221.sit.taskborad.DTO.TaskListDto;
import int221.sit.taskborad.entities.TaskList;
import int221.sit.taskborad.repositories.TaskListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
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
              ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "TaskList Number ' "+ id + " ' does not exist"));
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

}
