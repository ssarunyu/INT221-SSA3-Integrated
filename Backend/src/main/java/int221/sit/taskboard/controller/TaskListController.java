package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.tasks.TaskAndStatusInt;
import int221.sit.taskboard.DTO.tasks.TaskAndStatusObject;
import int221.sit.taskboard.DTO.tasks.TaskListDetail;
import int221.sit.taskboard.DTO.tasks.TaskShortDetail;
import int221.sit.taskboard.entities.itbkk_db.TaskList;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.services.StatusListService;
import int221.sit.taskboard.services.TaskListService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/tasks")
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th", "https://intproj23.sit.kmutt.ac.th/"})
public class TaskListController {
    @Autowired
    private TaskListService service;

    @Autowired
    private StatusListService statusService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TaskListService taskListService;


    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskListByIdDto(@PathVariable Integer id){
        TaskList taskList = service.findById(id);
        TaskListDetail taskListDetail = modelMapper.map(taskList, TaskListDetail.class);
        return ResponseEntity.ok(taskListDetail);
    }

    @PostMapping("")
    public ResponseEntity<TaskAndStatusObject> addNewTaskList(@Valid @RequestBody TaskAndStatusInt newTaskList) {

        if (newTaskList.getTitle() != null) {
            newTaskList.setTitle(newTaskList.getTitle().trim());
        } else {
            throw new BadRequestException("Title must not be null");
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
        TaskAndStatusObject createdTaskList = service.createNewTaskList(newTaskList, status);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskAndStatusObject> updateTaskList(@Valid @RequestBody TaskAndStatusInt taskLists, @PathVariable Integer id) {
        try {
            Integer status = taskLists.getStatus();
            TaskAndStatusObject updatedTask = service.updateTaskListById(id, taskLists, status);
            return ResponseEntity.ok(updatedTask);
        } catch (ItemNotFoundException e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskShortDetail> deletedTaskListById(@PathVariable Integer id) {
        TaskShortDetail deletedTaskListDto = service.removeTaskListById(id);
        return ResponseEntity.ok().body(deletedTaskListDto);
    }

    @GetMapping("")
    public List<TaskShortDetail> getTasksSortedByStatusName(
    @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
    @RequestParam(name = "filterStatuses", required = false) List<String> filterStatuses ) {
        return service.getTasksSortedByStatusName(sortBy, filterStatuses);
    }

}
