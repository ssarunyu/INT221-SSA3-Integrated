package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.*;
import int221.sit.taskboard.entities.TaskList;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.services.StatusListService;
import int221.sit.taskboard.services.TaskListService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/tasks")
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th"})
public class TaskListController {
    @Autowired
    private TaskListService service;

    @Autowired
    private StatusListService statusService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TaskListService taskListService;

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

//    @GetMapping("")
//    public List<TaskListDto> getAllTaskListDto(@RequestParam(required = false) String[] param) {
//        return service.getAllTaskListDto();
//    }

    @PostMapping("")
    public ResponseEntity<NewTaskListDto> addNewTaskList(@RequestBody NewTaskListDtoV2 newTaskList) {

        if (newTaskList.getTitle() != null) {
            newTaskList.setTitle(newTaskList.getTitle().trim());
        }

        if (newTaskList.getDescription() != null) {
            if (newTaskList.getDescription().isEmpty()) {
                newTaskList.setDescription(null);
            } else {
                newTaskList.setDescription(newTaskList.getDescription().trim());
            }

        }

        if (newTaskList.getAssignees() != null) {

            if (newTaskList.getAssignees().isEmpty()) {
                newTaskList.setAssignees(null);
            } else {
                newTaskList.setAssignees(newTaskList.getAssignees().trim());
            }
        }

        Integer status = newTaskList.getStatus();
        NewTaskListDto createdTaskList = service.createNewTaskList(newTaskList, status);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewTaskListDto> updateTaskList(@RequestBody NewTaskListDtoV2 taskLists, @PathVariable Integer id) {
        try {
            Integer status = taskLists.getStatus();
            NewTaskListDto updatedTask = service.updateTaskListById(id, taskLists, status);
            return ResponseEntity.ok(updatedTask);
        } catch (ItemNotFoundException e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskListDto> deletedTaskListById(@PathVariable Integer id) {
        TaskListDto deletedTaskListDto = service.removeTaskListById(id);
        return ResponseEntity.ok().body(deletedTaskListDto);
    }

    @GetMapping("")
    public List<TaskListSortingDto> getTasksSortedByStatusName(
    @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
    @RequestParam(name = "filterStatuses", required = false) List<String> filterStatuses ) {
        return service.getTasksSortedByStatusName(sortBy, filterStatuses);
    }

}
