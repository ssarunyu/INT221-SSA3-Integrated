package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.boards.BoardDTO;
import int221.sit.taskboard.DTO.statuses.StatusWithTaskCountDTO;
import int221.sit.taskboard.DTO.tasks.TaskAndStatusInt;
import int221.sit.taskboard.DTO.tasks.TaskAndStatusObjShort;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import int221.sit.taskboard.entities.itbkk_db.TaskList;
import int221.sit.taskboard.exceptions.AccessDeniedException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.services.BoardService;
import int221.sit.taskboard.services.StatusServiceV3;
import int221.sit.taskboard.services.TaskListService;
import int221.sit.taskboard.services.TaskServiceV3;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v3/boards")
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th"})
public class StatusControllerV3 {
    @Autowired
    private StatusServiceV3 statusService;

    @Autowired
    private TaskServiceV3 taskService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //แสดง status ทั้งหมดใน boards
    @GetMapping("/{board_id}/statuses")
    public ResponseEntity<Object> getAllStatuses(@PathVariable("board_id") String boardId,
                                                 @RequestHeader("Authorization") String token) {
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
            throw new AccessDeniedException("Access denied!!!");
        }

        List<StatusWithTaskCountDTO> status = statusService.getAllStatusesByBoardId(boardId);
        return ResponseEntity.ok(status);
    }

    //ใช้ status name ในการแสดงเฉพาะ status ที่ต้องการ
//    @GetMapping("/{board_id}/statuses/{status_name}")
//    public List<StatusList> getStatusByName(@PathVariable("board_id") String boardId, @PathVariable("status_name") String statusName) {
//        return statusService.getStatusByNameAndBoardId(boardId, statusName);
//    }

    //ใช้ status id ในการแสดงเฉพาะ status ที่ต้องการ
    @GetMapping("/{board_id}/statuses/{status_id}")
    public ResponseEntity<Object> getStatusById(@PathVariable("board_id") String boardId,
                                          @PathVariable("status_id") Integer statusId,
                                          @RequestHeader("Authorization") String token) {

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
            throw new AccessDeniedException("Access denied!!!");
        }

        List<StatusList> statusList = statusService.getStatusByIdAndBoardId(boardId, statusId);
        return ResponseEntity.ok(statusList);
    }

    //สร้าง status ของบอร์ดนั้นๆ
    @PostMapping("/{board_id}/statuses")
    public ResponseEntity<Object> createNewStatus(@PathVariable("board_id") String boardId,
                                      @RequestBody StatusList newStatus,
                                      @RequestHeader("Authorization") String token) {

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่า userId ตรงกับเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        if (!isOwner) {
            // ถ้าผู้ใช้ไม่ใช่เจ้าของบอร์ด ขว้างข้อยกเว้น
            throw new AccessDeniedException("You are not authorized to update this task");
        }

        StatusList statusList = statusService.addNewStatus(boardId, newStatus);
        return ResponseEntity.ok(statusList);
    }

    //แก้ไข status โดยรับ id ของ status นั้นมา
    @PutMapping("/{board_id}/statuses/{status_id}")
    public ResponseEntity<Object> updateStatusById(@PathVariable("board_id") String boardId,
                                                   @PathVariable("status_id") Integer statusId,
                                                   @RequestBody StatusList newStatus,
                                                   @RequestHeader("Authorization") String token) {

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่า userId ตรงกับเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        if (!isOwner) {
            // ถ้าผู้ใช้ไม่ใช่เจ้าของบอร์ด ขว้างข้อยกเว้น
            throw new AccessDeniedException("You are not authorized to update this task");
        }

        StatusList statusList = statusService.updateStatus(boardId, statusId, newStatus);
        return ResponseEntity.ok(statusList);
    }

    //ลบ status ที่ต้องการออกจาก board Id
    @DeleteMapping("/{board_id}/statuses/{status_id}")
    public ResponseEntity<StatusList> deleteStatusById(@PathVariable("board_id") String boardId,
                                                       @PathVariable("status_id") Integer statusId,
                                                       @RequestHeader("Authorization") String token) {

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่า userId ตรงกับเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        if (!isOwner) {
            // ถ้าผู้ใช้ไม่ใช่เจ้าของบอร์ด ขว้างข้อยกเว้น
            throw new AccessDeniedException("You are not authorized to update this task");
        }

        return statusService.deleteStatus(boardId, statusId);
    }

    @DeleteMapping("/{board_id}/statuses/{status_id}/{status_newId}")
    public ResponseEntity<List<TaskAndStatusObjShort>> deleteStatusByIdAndTransfer(@PathVariable("board_id") String boardId,
                                                                                   @PathVariable("status_id") Integer statusId,
                                                                                   @PathVariable("status_newId") Integer statusNewId,
                                                                                   @RequestHeader("Authorization") String token) {

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่า userId ตรงกับเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        if (!isOwner) {
            // ถ้าผู้ใช้ไม่ใช่เจ้าของบอร์ด ขว้างข้อยกเว้น
            throw new AccessDeniedException("You are not authorized to update this task");
        }

        List<TaskAndStatusObjShort> transferTask = taskService.transferTasking(boardId, statusId, statusNewId);
        statusService.deleteStatus(boardId, statusId);
        return ResponseEntity.status(HttpStatus.OK).body(transferTask);
    }
}
