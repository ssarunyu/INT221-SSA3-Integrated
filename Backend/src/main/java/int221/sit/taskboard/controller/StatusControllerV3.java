package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.boards.BoardDTO;
import int221.sit.taskboard.DTO.statuses.StatusWithTaskCountDTO;
import int221.sit.taskboard.DTO.tasks.TaskAndStatusInt;
import int221.sit.taskboard.DTO.tasks.TaskAndStatusObjShort;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import int221.sit.taskboard.entities.itbkk_db.TaskList;
import int221.sit.taskboard.exceptions.AccessDeniedException;
import int221.sit.taskboard.exceptions.BadRequestException;
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
                                                 @RequestHeader(value = "Authorization", required = false) String token) {

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่าบอร์ดเป็น public หรือ private
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());

        // ถ้าเป็น public สามารถเข้าถึงได้โดยไม่ต้องใช้ token
        if (isPublic) {
            List<StatusWithTaskCountDTO> statuses = statusService.getAllStatusesByBoardId(boardId);
            return ResponseEntity.ok(statuses);
        }

        // ถ้าไม่มีการส่ง Authorization header และบอร์ดเป็น private
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied! Private board requires authentication.");
        }

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner().getUserId().equals(userId);

        // ถ้าบอร์ดเป็น private และผู้ใช้ไม่ใช่เจ้าของ ให้ส่ง response ว่า access denied
        if (!isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied! You do not have permission to access this board.");
        }

        // ถ้าเป็นเจ้าของหรือบอร์ดเป็น public ให้ดึงข้อมูลสถานะทั้งหมด
        List<StatusWithTaskCountDTO> statuses = statusService.getAllStatusesByBoardId(boardId);
        return ResponseEntity.ok(statuses);
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
                                                @RequestHeader(value = "Authorization", required = false) String token) {
        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่าบอร์ดเป็น public หรือ private
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());

        // ถ้าเป็น public สามารถเข้าถึงได้โดยไม่ต้องใช้ token
        if (isPublic) {
            StatusList statusList = statusService.getStatusByIdAndBoardId(boardId, statusId);
            return ResponseEntity.ok(statusList);
        }

        // ถ้าไม่มีการส่ง Authorization header และบอร์ดเป็น private
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied! Private board requires authentication.");
        }

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner().getUserId().equals(userId);

        // ถ้าเป็นเจ้าของบอร์ด ให้ดึงสถานะตาม boardId และ statusId
        if (isOwner) {
            StatusList statusList = statusService.getStatusByIdAndBoardId(boardId, statusId);
            return ResponseEntity.ok(statusList);
        } else {
            // ถ้าผู้ใช้ไม่ใช่เจ้าของบอร์ด ให้ return 403 Forbidden
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied! You are not the owner of this private board.");
        }
    }

    //สร้าง status ของบอร์ดนั้นๆ
    @PostMapping("/{board_id}/statuses")
    public ResponseEntity<Object> createNewStatus(@PathVariable("board_id") String boardId,
                                                  @RequestBody(required = false) StatusList newStatus,
                                                  @RequestHeader("Authorization") String token) {

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบว่า user เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        // ตรวจสอบว่า visibility ของบอร์ดเป็น public หรือ private
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());
        boolean isPrivate = "private".equalsIgnoreCase(boardDTO.getVisibility());

        // กรณี visibility เป็น private และ user ไม่ใช่ owner ให้ return 403 Forbidden
        if (isPrivate && !isOwner) {
            throw new AccessDeniedException("Access denied! You are not authorized to add status on this private board.");
        }

        // กรณี visibility เป็น public และ user ไม่ใช่ owner ให้ return 403 Forbidden
        if (isPublic && !isOwner) {
            throw new AccessDeniedException("Access denied! You are not authorized to add status on this public board.");
        }

        // กรณีเป็นเจ้าของบอร์ดและไม่ได้ส่ง body มาให้ return 400 Bad Request
        if (isOwner && newStatus == null) {
            throw new BadRequestException("Status cannot be null");
        }

        // ถ้าเป็นเจ้าของบอร์ด
        if (isOwner) {
            StatusList statusList = statusService.addNewStatus(boardId, newStatus);
            return ResponseEntity.status(HttpStatus.CREATED).body(statusList);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }



    //แก้ไข status โดยรับ id ของ status นั้นมา
    @PutMapping("/{board_id}/statuses/{status_id}")
    public ResponseEntity<Object> updateStatusById(@PathVariable("board_id") String boardId,
                                                   @PathVariable("status_id") Integer statusId,
                                                   @RequestBody(required = false) StatusList newStatus,
                                                   @RequestHeader("Authorization") String token) {

        // ตรวจสอบบอร์ดและดึงข้อมูลบอร์ด
        BoardDTO boardDTO = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบ visibility ของบอร์ด
        boolean isPrivate = "private".equalsIgnoreCase(boardDTO.getVisibility());
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7); // ตัด "Bearer " ออก
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบว่า userId ตรงกับเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        // ถ้า visibility เป็น private และ user ไม่ใช่ owner ให้ return 403 Forbidden
        if (isPrivate && !isOwner) {
            throw new AccessDeniedException("You are not authorized to update this status on this private board.");
        }

        // ถ้า visibility เป็น public และ user ไม่ใช่ owner ให้ return 403 Forbidden
        if (isPublic && !isOwner) {
            throw new AccessDeniedException("You are not authorized to update this status on this public board.");
        }

        // ถ้าเป็น owner แต่ไม่มีข้อมูล status ใน body ให้ return 400 Bad Request
        if (isOwner && newStatus == null) {
            throw new BadRequestException("Status cannot be null");
        }

        // อัปเดต status
        StatusList statusList = statusService.updateStatus(boardId, statusId, newStatus);

        // ส่ง response เมื่ออัปเดต status สำเร็จ
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
