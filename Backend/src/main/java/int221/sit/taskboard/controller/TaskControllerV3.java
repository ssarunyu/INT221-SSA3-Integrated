package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.boards.BoardDTO;
import int221.sit.taskboard.DTO.tasks.TaskAndStatusInt;
import int221.sit.taskboard.DTO.tasks.TaskAndStatusObject;
import int221.sit.taskboard.DTO.tasks.TaskListDetail;
import int221.sit.taskboard.DTO.tasks.TaskShortDetail;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import int221.sit.taskboard.exceptions.AccessDeniedException;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.services.BoardService;
import int221.sit.taskboard.services.StatusListService;
import int221.sit.taskboard.services.StatusServiceV3;
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
    private StatusServiceV3 statusService;

    @Autowired
    private TaskServiceV3 service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
    public ResponseEntity<Object> getAllTasks(
            @PathVariable("board_id") String boardId,
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "status", required = false) List<String> statuses,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            @RequestParam(value = "sortBy", defaultValue = "createdOn") String sortBy) {

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner().getUserId().equals(userId);
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());

        // ถ้าบอร์ดเป็น private และผู้ใช้ไม่ใช่เจ้าของ ให้ส่ง response ว่า access denied
        if (!isOwner && !isPublic) {
            throw new AccessDeniedException("Access denied!!");
        }

        // ถ้าผู้ใช้มีสิทธิ์เข้าถึงบอร์ดนี้ ให้ดึง task list และส่งข้อมูลกลับไป
        List<TaskShortDetail> tasks = service.getAllTasksSortedAndFilterForBoard(boardId, statuses, sortDirection, sortBy);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{board_id}/tasks/{task_id}")
    public ResponseEntity<Object> getTaskById(@PathVariable("board_id") String boardId,
                                      @RequestHeader("Authorization") String token,
                                      @PathVariable("task_id") Integer taskId) {
        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner().getUserId().equals(userId);
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());

        // ถ้าบอร์ดเป็น private และผู้ใช้ไม่ใช่เจ้าของ ให้ส่ง response ว่า access denied
        if (!isOwner && !isPublic) {
            throw new AccessDeniedException("Access denied!!");
        }
        // ดึง task ตาม boardId และ taskId
        TaskListDetail task = service.getTaskById(boardId, taskId);

        // ถ้าไม่เจอ task ให้โยนข้อผิดพลาดว่าไม่พบ task
        if (task == null) {
            throw new ItemNotFoundException("Task not found !!!");
        }

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{board_id}/tasks/{task_id}")
    public ResponseEntity<TaskAndStatusObject> updateTask(@Valid @PathVariable("board_id") String boardId,
                                                          @PathVariable Integer task_id,
                                                          @RequestBody TaskAndStatusInt taskLists,
                                                          @RequestHeader("Authorization") String token) {

        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found");
        }

        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        if (!isOwner) {
            // ถ้าผู้ใช้ไม่ใช่เจ้าของบอร์ด ขว้างข้อยกเว้น
            throw new AccessDeniedException("You are not authorized to update this task");
        }

        Integer statusId = taskLists.getStatus();

        // ตรวจสอบว่าค่า statusId เป็น null หรือไม่ และตั้งค่าเป็น "No Status" หากเป็น null หรือว่างเปล่า
        if (statusId == null || statusId == 0) {
            List<StatusList> noStatus = statusService.getStatusByNameAndBoardId(boardId, "No Status");
            if (noStatus != null && !noStatus.isEmpty()) {
                statusId = noStatus.get(0).getId();// ตั้งค่า statusId เป็น "No Status"
            } else {
                throw new ItemNotFoundException("No Status not found in the system");
            }
        }

        TaskAndStatusObject updatedTask = service.updateTaskListById(boardId, task_id, taskLists, statusId);

        if(updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            throw new ItemNotFoundException("Task is not found");
        }
    }

    @DeleteMapping("/{board_id}/tasks/{task_id}")
    public ResponseEntity<TaskShortDetail> deletedTaskListById(@Valid @PathVariable("board_id") String boardId,
                                                               @PathVariable Integer task_id,
                                                               @RequestHeader("Authorization") String token) {

        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found");
        }

        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        if (!isOwner) {
            // ถ้าผู้ใช้ไม่ใช่เจ้าของบอร์ด ขว้างข้อยกเว้น
            throw new AccessDeniedException("You are not authorized to update this task");
        }

        TaskShortDetail deletedTaskListDto = service.removeTaskById(boardId, task_id);
        return ResponseEntity.ok().body(deletedTaskListDto);
    }
}
