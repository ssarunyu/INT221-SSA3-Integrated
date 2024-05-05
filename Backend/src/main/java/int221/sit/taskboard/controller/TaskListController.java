package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.NewTaskListDto;
import int221.sit.taskboard.DTO.TaskListByIdDto;
import int221.sit.taskboard.DTO.TaskListDto;
import int221.sit.taskboard.entities.TaskList;
import int221.sit.taskboard.services.TaskListService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th"})
public class TaskListController {
    @Autowired
    private TaskListService service;

    @Autowired
    private ModelMapper modelMapper;

//    @GetMapping("")
//    public List<TaskList> getAllTaskList(@RequestParam(required = false) String[] param) {
//        return service.getAllTaskList(param);
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Object> getTaskListById(@PathVariable Integer id){
//        TaskList taskList = service.getTaskListById(id);
//        if (taskList != null) {
//            return ResponseEntity.ok(taskList);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TaskList " + id + " does not exist !!");
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskListByIdDto(@PathVariable Integer id){
        TaskList taskList = service.findById(id);
        TaskListByIdDto taskListByIdDto = modelMapper.map(taskList, TaskListByIdDto.class);
        return ResponseEntity.ok(taskListByIdDto);
    }

    @GetMapping("")
    public List<TaskListDto> getAllTaskListDto(@RequestParam(required = false) String[] param) {
        return service.getAllTaskListDto(param);
    }

    @PostMapping("")
    public ResponseEntity<NewTaskListDto> addNewTaskList(@RequestBody NewTaskListDto newTaskList) {
        NewTaskListDto createdTaskList = service.createNewTaskList(newTaskList);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskList);
    }

    @PutMapping("/{id}")
    public TaskList updateTaskList(@RequestBody TaskList taskList,@PathVariable Integer id) {
        return service.updateTaskListById(id, taskList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskListDto> deletedTaskListById(@PathVariable Integer id) {
        TaskListDto deletedTaskListDto = service.removeTaskListById(id);
        return ResponseEntity.ok().body(deletedTaskListDto);
    }

}
