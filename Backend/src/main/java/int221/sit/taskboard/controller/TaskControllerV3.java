package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.TaskAndStatusInt;
import int221.sit.taskboard.DTO.TaskAndStatusObject;
import int221.sit.taskboard.DTO.TaskListDetail;
import int221.sit.taskboard.DTO.TaskShortDetail;
import int221.sit.taskboard.entities.itbkk_db.TaskList;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.services.BoardService;
import int221.sit.taskboard.services.StatusListService;
import int221.sit.taskboard.services.TaskListService;
import int221.sit.taskboard.services.TaskServiceV3;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/boards")
@CrossOrigin(origins = {"http://localhost:5173", "http://ip23ssa3.sit.kmutt.ac.th"})
public class TaskControllerV3 {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BoardService boardService;

    @Autowired
    private StatusListService statusService;

    @Autowired
    private TaskServiceV3 service;

    @PostMapping("/{board_id}/tasks")
    public ResponseEntity<TaskAndStatusObject> addNewTask(@PathVariable String board_id,
                                                          @Valid @RequestBody TaskAndStatusInt newtask) {
        if (newtask.getTitle() != null) {
            newtask.setTitle(newtask.getTitle().trim());
        } else {
            throw new BadRequestException("Title must not be null");
        }

        if (newtask.getDescription() != null) {
            if (newtask.getDescription().isEmpty()) {
                newtask.setDescription(null);
            } else {
                newtask.setDescription(newtask.getDescription().trim());
            }

        }

        if (newtask.getAssignees() != null) {

            if (newtask.getAssignees().isEmpty()) {
                newtask.setAssignees(null);
            } else {
                newtask.setAssignees(newtask.getAssignees().trim());
            }
        }

        Integer status = newtask.getStatus();
        TaskAndStatusObject createdTaskList = service.createNewTask(newtask, board_id, status);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskList);
    }

    @GetMapping("/{board_id}/tasks")
    public List<TaskShortDetail> getAllTasks(
            @PathVariable("board_id") String boardId,
            @RequestParam(value = "status", required = false) List<String> statuses,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            @RequestParam(value = "sortBy", defaultValue = "createdOn") String sortBy) {
        return service.getAllTasksSortedAndFilterForBoard(boardId, statuses, sortDirection, sortBy);
    }

    @GetMapping("/{board_id}/task/{task_id}")
    public TaskListDetail getTaskById(@PathVariable("board_id") String boardId, @PathVariable("task_id") Integer taskId) {
        return service.getTaskById(boardId, taskId);
    }

    @PutMapping("/{board_id}/task/{task_id}")
    public ResponseEntity<TaskAndStatusObject> updateTask(@Valid @PathVariable("board_id") String boardId, @PathVariable Integer task_id, @RequestBody TaskAndStatusInt taskLists) {
        try {
            Integer status = taskLists.getStatus();
            TaskAndStatusObject updatedTask = service.updateTaskListById(boardId, task_id, taskLists, status);
            return ResponseEntity.ok(updatedTask);
        } catch (ItemNotFoundException e) {
            throw new ItemNotFoundException("Task is not found");
        }
    }

    @DeleteMapping("/{board_id}/task/{task_id}")
    public ResponseEntity<TaskShortDetail> deletedTaskListById(@Valid @PathVariable("board_id") String boardId, @PathVariable Integer task_id) {
        TaskShortDetail deletedTaskListDto = service.removeTaskById(boardId, task_id);
        return ResponseEntity.ok().body(deletedTaskListDto);
    }
}
