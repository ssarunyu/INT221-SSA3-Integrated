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
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th", "https://localhost:5173"})
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
    public ResponseEntity<TaskAndStatusObject> addNewTask(
            @PathVariable("board_id") String boardId,
            @Valid @RequestBody(required = false) TaskAndStatusInt newTask,
            @RequestHeader("Authorization") String token) {

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบ visibility ของบอร์ด
        boolean isPrivate = "private".equalsIgnoreCase(boardDTO.getVisibility());

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7); // ตัด "Bearer " ออก
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        // ถ้าบอร์ดเป็น private และผู้ใช้ไม่ใช่เจ้าของ ให้ return 403 Forbidden
        if (isPrivate && !isOwner) {
            throw new AccessDeniedException("You are not authorized to create a task on this private board.");
        }

        // ถ้าเป็นบอร์ด public และผู้ใช้ไม่ใช่เจ้าของ ให้ return 403 Forbidden
        if (!isPrivate && !isOwner) {
            throw new AccessDeniedException("You are not authorized to create a task on this public board.");
        }

        // ถ้าเป็นบอร์ด public และผู้ใช้เป็น owner แต่ไม่มีข้อมูล task ให้ return 400 Bad Request
        if (!isPrivate && isOwner && newTask == null) {
            throw new BadRequestException("Task cannot be null for the board owner.");
        }

        // ตรวจสอบเฉพาะกรณีที่ผู้ใช้เป็นเจ้าของบอร์ดหรือเป็นบอร์ด private
        if ((isPrivate || isOwner) && (newTask == null || newTask.getTitle() == null || newTask.getTitle().trim().isEmpty())) {
            throw new BadRequestException("Title must not be null or empty.");
        }

        // ตรวจสอบ description (สามารถเป็นค่าว่างหรือ null ได้)
        if (newTask != null && newTask.getDescription() != null && newTask.getDescription().trim().isEmpty()) {
            newTask.setDescription(null);
        }

        // ตรวจสอบ assignees (สามารถเป็นค่าว่างหรือ null ได้)
        if (newTask != null && newTask.getAssignees() != null && newTask.getAssignees().trim().isEmpty()) {
            newTask.setAssignees(null);
        }

        // ตรวจสอบสถานะ
        Integer status = newTask != null ? newTask.getStatus() : null;

        // สร้าง task ใหม่
        TaskAndStatusObject createdTask = service.createNewTask(newTask, boardId, status);

        // ส่ง response เมื่อสร้าง task สำเร็จ
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }


    @GetMapping("/{board_id}/tasks")
    public ResponseEntity<Object> getAllTasks(
            @PathVariable("board_id") String boardId,
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(value = "status", required = false) List<String> statuses,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
            @RequestParam(value = "sortBy", defaultValue = "createdOn") String sortBy) {

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่าบอร์ดเป็น public หรือ private
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());

        // ถ้าเป็น private และไม่มีการส่ง Authorization header
        if (!isPublic && (token == null || !token.startsWith("Bearer "))) {
            throw new AccessDeniedException("Access denied! Private board requires authentication.");
        }

        // ถ้า token ถูกส่งเข้ามา ดึง userId จาก JWT token
        String userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
        }

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = (userId != null) && boardDTO.getOwner().getUserId().equals(userId);

        // ถ้าบอร์ดเป็น private และผู้ใช้ไม่ใช่เจ้าของ ให้ส่ง response ว่า access denied
        if (!isOwner && !isPublic) {
            throw new AccessDeniedException("Access denied!!");
        }

        // ถ้าผู้ใช้มีสิทธิ์เข้าถึงบอร์ดนี้ ให้ดึง task list และส่งข้อมูลกลับไป
        List<TaskShortDetail> tasks = service.getAllTasksSortedAndFilterForBoard(boardId, statuses, sortDirection, sortBy);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{board_id}/tasks/{task_id}")
    public ResponseEntity<Object> getTaskById(
            @PathVariable("board_id") String boardId,
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable("task_id") Integer taskId) {

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่าบอร์ดเป็น public หรือ private
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());

        // ถ้าเป็น private และไม่มีการส่ง Authorization header
        if (!isPublic && (token == null || !token.startsWith("Bearer "))) {
            throw new AccessDeniedException("Access denied! Private board requires authentication.");
        }

        // ถ้า token ถูกส่งเข้ามา ดึง userId จาก JWT token
        String userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
        }

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = (userId != null) && boardDTO.getOwner().getUserId().equals(userId);

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
    public ResponseEntity<TaskAndStatusObject> updateTask(
            @Valid @PathVariable("board_id") String boardId,
            @PathVariable Integer task_id,
            @RequestBody(required = false) TaskAndStatusInt taskLists,
            @RequestHeader("Authorization") String token) {

        // ตรวจสอบว่า boardId ถูกต้องหรือไม่
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found");
        }

        // ตรวจสอบ visibility ของบอร์ด
        boolean isPrivate = "private".equalsIgnoreCase(boardDTO.getVisibility());

        // ตรวจสอบ JWT token และดึง userId
        String jwtToken = token.substring(7); // ตัด "Bearer " ออก
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        // กรณี visibility เป็น private และ user ไม่ใช่ owner ให้ return 403 Forbidden
        if (isPrivate && !isOwner) {
            throw new AccessDeniedException("You are not authorized to update this task");
        }

        // กรณี visibility เป็น public และ user ไม่ใช่ owner ให้ return 403 Forbidden
        if (!isPrivate && !isOwner) {
            throw new AccessDeniedException("You are not authorized to update tasks on this public board.");
        }

        // ตรวจสอบว่า body มีข้อมูล taskLists หรือไม่ ถ้าเป็น null ให้ return 400
        if (taskLists == null) {
            throw new BadRequestException("Task cannot be null");
        }

        // ตรวจสอบค่า statusId จาก taskLists
        Integer statusId = taskLists.getStatus();

        // ถ้า statusId เป็น null หรือเป็น 0 ให้ตั้งค่าเป็น "No Status"
        if (statusId == null || statusId == 0) {
            List<StatusList> noStatus = statusService.getStatusByNameAndBoardId(boardId, "No Status");
            if (noStatus != null && !noStatus.isEmpty()) {
                statusId = noStatus.get(0).getId(); // ตั้งค่า statusId เป็น "No Status"
            } else {
                throw new ItemNotFoundException("No Status not found in the system");
            }
        }

        // อัปเดต task
        TaskAndStatusObject updatedTask = service.updateTaskListById(boardId, task_id, taskLists, statusId);

        // ถ้าอัปเดตสำเร็จ ส่ง response 200 OK
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            throw new ItemNotFoundException("Task not found");
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
