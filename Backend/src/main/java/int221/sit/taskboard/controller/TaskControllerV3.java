package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.TaskAndStatusInt;
import int221.sit.taskboard.DTO.TaskAndStatusObject;
import int221.sit.taskboard.exceptions.BadRequestException;
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
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th"})
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

//    @GetMapping("/{board_id}/tasks")
//    public ResponseEntity<List<TaskAndStatusObject>> getAllTasksByBoardId(@PathVariable String board_id) {
//
//    }
}
