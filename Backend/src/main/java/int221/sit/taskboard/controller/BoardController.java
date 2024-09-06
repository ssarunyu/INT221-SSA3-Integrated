package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.BoardDTO;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.Boards;
import int221.sit.taskboard.services.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public List<Boards> getAllBoardsForUser(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
        return boardService.getAllBoardsForUser(userId);
    }

    @PostMapping("")
    public ResponseEntity<BoardDTO> createBoard(@RequestBody BoardDTO boardDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Boards newBoard = modelMapper.map(boardDTO, Boards.class);

        BoardDTO createdBoard = boardService.createBoard(boardDTO, token);
        return ResponseEntity.ok(createdBoard);
    }
}
