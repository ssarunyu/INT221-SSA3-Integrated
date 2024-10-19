package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.boards.BoardDTO;
import int221.sit.taskboard.DTO.boards.BoardForCreated;
import int221.sit.taskboard.DTO.boards.BoardUpdateResponse;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.UserList;
import int221.sit.taskboard.exceptions.*;
import int221.sit.taskboard.repositories.task.BoardRepository;
import int221.sit.taskboard.repositories.task.UserListRepository;
import int221.sit.taskboard.services.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v3/boards")
@CrossOrigin(origins = { "http://localhost:5173", "https://localhost:5173" } )
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserListRepository userListRepository;

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("")
    public ResponseEntity<Map<String, List<BoardDTO>>> getAllBoards(@RequestHeader(value = "Authorization", required = false) String token) {

        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

            Map<String, List<BoardDTO>> boards = boardService.getPersonalAndCollabBoards(userId);

            return ResponseEntity.ok(boards);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/{board_id}")
    public ResponseEntity<Object> getBoardById(@PathVariable("board_id") String boardId, @RequestHeader(value = "Authorization", required = false) String token) {

        BoardDTO boardDTO = boardService.getBoardById(boardId);

        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ตรวจสอบว่าบอร์ดเป็น public หรือ private
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());

        // ถ้าเป็น public สามารถเข้าถึงได้โดยไม่ต้องใช้ token
        if (isPublic) {
            return ResponseEntity.ok(boardDTO);
        }

        // ถ้าไม่มี token หรือ token ไม่ถูกต้อง ให้ return 403 Forbidden
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied! Private board requires authentication.");
        }

        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);

        if (isOwner) {
            return ResponseEntity.ok(boardDTO); // ส่งข้อมูลบอร์ดกลับไป
        } else {
            throw new AccessDeniedException("Access denied");
        }

    }

    @PostMapping("")
    public ResponseEntity<BoardForCreated> createBoard(@Valid @RequestBody(required = false) BoardForCreated bfc, HttpServletRequest request) {
        if (bfc == null) {
            throw new BadRequestException("Please fill in the board information completely!");
        }

        String token = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        UserList userOptional = userListRepository.findByUsername(username);
        if (userOptional == null) {
            throw new NotCreatedException("User " + username + " not found");
        }

        Boards newBoard = modelMapper.map(bfc, Boards.class);

        BoardForCreated createdBoard = boardService.createBoard(bfc, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoard);
    }

    @PatchMapping("/{board_id}")
    public ResponseEntity<Object> updateBoardVisibility(
            @PathVariable("board_id") String boardId,
            @RequestBody(required = false) Map<String, String> requestBody,
            @RequestHeader(value = "Authorization", required = false) String token) {

        // ตรวจสอบว่าเจอบอร์ดหรือไม่
        BoardDTO boardEntity = boardService.getBoardById(boardId);

        // ถ้าไม่เจอบอร์ด ให้ส่ง response ว่าไม่พบข้อมูล
        if (boardEntity == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        // ถ้าไม่มี token และบอร์ดเป็น private ให้ return 403
        boolean isPrivateBoard = "private".equalsIgnoreCase(boardEntity.getVisibility());
        if (token == null && isPrivateBoard) {
            throw new AccessDeniedException("Access denied! Private board requires authentication.");
        }

        // ตรวจสอบว่าถ้ามี token ให้ทำการดึง userId
        String userId = null;
        boolean isOwner = false;
        if (token != null) {
            String jwtToken = token.substring(7); // ตัดคำว่า "Bearer " ออก
            userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

            // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
            isOwner = boardEntity.getOwner() != null && boardEntity.getOwner().getUserId().equals(userId);
        }

        // กรณีที่บอร์ดเป็น public และผู้ใช้ไม่ใช่เจ้าของ ให้ return 403 Forbidden
        if (!isPrivateBoard && !isOwner) {
            throw new AccessDeniedException("Access denied! You are not authorized to update the visibility of this public board.");
        }

        // กรณีที่บอร์ดเป็น private และ user ไม่ใช่ owner ให้ return 403
        if (isPrivateBoard && !isOwner) {
            throw new AccessDeniedException("Access denied! You are not the owner of this private board.");
        }

        // กรณีที่เป็น owner และไม่ได้ส่ง body หรือ requestBody ไม่มีค่า visibility ให้ return 400
        if (isOwner && (requestBody == null || !requestBody.containsKey("visibility"))) {
            throw new BadRequestException("Visibility cannot be null or missing.");
        }

        // ตรวจสอบว่า visibility มีค่าเป็น public หรือ private หรือไม่
        String visibility = requestBody.get("visibility");
        if (visibility == null || (!"private".equalsIgnoreCase(visibility) && !"public".equalsIgnoreCase(visibility))) {
            throw new BadRequestException("Invalid visibility value.");
        }

        // อัปเดตสถานะการมองเห็นใน entity
        Boards.Visibility boardVisibility = Boards.Visibility.valueOf(visibility.toUpperCase());
        boardEntity.setVisibility(String.valueOf(boardVisibility));

        // บันทึกการเปลี่ยนแปลงลงฐานข้อมูล
        boardService.updateBoard(boardEntity);  // เรียก method เพื่อบันทึกการเปลี่ยนแปลง

        // สร้าง response object
        BoardUpdateResponse response = new BoardUpdateResponse(
                boardEntity.getId(),
                "Board visibility updated successfully",
                boardEntity.getVisibility()
        );

        return ResponseEntity.ok(response);
    }
}