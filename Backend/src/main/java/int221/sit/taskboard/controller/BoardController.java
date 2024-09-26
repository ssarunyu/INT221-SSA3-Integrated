package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.boards.BoardDTO;
import int221.sit.taskboard.DTO.boards.BoardForCreated;
import int221.sit.taskboard.DTO.boards.BoardUpdateResponse;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.UserList;
import int221.sit.taskboard.exceptions.AccessDeniedException;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.exceptions.NotCreatedException;
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
@RequestMapping("/v3")
@CrossOrigin(origins = "http://localhost:5173")
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

    @GetMapping("/boards")
    public ResponseEntity<List<BoardDTO>> getAllBoardsForUser(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
            List<BoardDTO> boards = boardService.getAllBoardsForUser(userId);

            return ResponseEntity.ok(boards);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/boards/{board_id}")
    public ResponseEntity<Object> getBoardById(@PathVariable("board_id") String boardId, @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        BoardDTO boardDTO = boardService.getBoardById(boardId);

        if (boardDTO == null) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        boolean isOwner = boardDTO.getOwner() != null && boardDTO.getOwner().getUserId().equals(userId);
        boolean isPublic = "public".equalsIgnoreCase(boardDTO.getVisibility());

        if (isOwner || isPublic) {
            return ResponseEntity.ok(boardDTO); // ส่งข้อมูลบอร์ดกลับไป
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @PostMapping("/boards")
    public ResponseEntity<BoardForCreated> createBoard(@RequestBody(required = false) BoardForCreated bfc, HttpServletRequest request) {
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

    @PatchMapping("/boards/{board_id}")
    public ResponseEntity<Object> updateBoardVisibility(
            @PathVariable("board_id") String boardId,
            @RequestBody Map<String, String> requestBody,
            @RequestHeader("Authorization") String token) {

        String visibility = requestBody.get("visibility");

        // ตรวจสอบว่า visibility มีค่าเป็น public หรือ private หรือไม่
        if (!"private".equalsIgnoreCase(visibility) && !"public".equalsIgnoreCase(visibility)) {
            throw new BadRequestException("Invalid visibility value");
        }

        // ดึง userId จาก JWT token
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        // ดึงข้อมูลบอร์ดจาก repository
        Boards boardEntity = boardRepository.findById(boardId)
                .orElseThrow(() -> new ItemNotFoundException("Board not found"));

        // ตรวจสอบว่าผู้ใช้เป็นเจ้าของบอร์ดหรือไม่
        if (!boardEntity.getOwnerId().equals(userId)) {
            throw new AccessDeniedException("You are not the owner of this board !!! ");
        }

        // อัปเดตสถานะการมองเห็นใน entity
        Boards.Visibility boardVisibility = Boards.Visibility.valueOf(visibility.toUpperCase());
        boardEntity.setBoardVisibility(boardVisibility);
        boardRepository.save(boardEntity);

        // สร้าง response object
        BoardUpdateResponse response = new BoardUpdateResponse(
                boardEntity.getBoardId(),
                "Board visibility updated successfully",
                boardEntity.getBoardVisibility().name()
        );

        return ResponseEntity.ok(response);
    }
}