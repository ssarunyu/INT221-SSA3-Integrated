package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.BoardDTO;
import int221.sit.taskboard.DTO.BoardForCreated;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.UserList;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.NotCreatedException;
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

@RestController
@RequestMapping("/v3")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserListRepository userListRepository;

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
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable("board_id") String boardId, @RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);

        BoardDTO boardDTO = boardService.getBoardById(boardId, userId);

        if (boardDTO == null) {
            throw new BadRequestException("Board not found !!!");
        }

        return ResponseEntity.ok(boardDTO);
    }


    @PostMapping("/boards")
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
}
