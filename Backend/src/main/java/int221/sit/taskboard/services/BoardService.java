package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.BoardDTO;
import int221.sit.taskboard.DTO.UserDTO;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.Boards;
import int221.sit.taskboard.entities.StatusList;
import int221.sit.taskboard.entities.Users;
import int221.sit.taskboard.repositories.auth.UserRepository;
import int221.sit.taskboard.repositories.task.BoardRepository;
import int221.sit.taskboard.repositories.task.StatusListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatusListRepository statusListRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    ModelMapper modelMapper;

    private final List<String> defaultStatuses = List.of("TODO", "DOING", "DONE", "NO STATUS");

    @Transactional("taskBoardTransactionManager")
    public BoardDTO createBoard(BoardDTO boardDTO, String token) {

        // Get user info from JWT token
        String username = jwtTokenUtil.getUsernameFromToken(token);
        Users owner = userRepository.findByUsername(username);

        if (owner == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Create new board
        Boards newBoard = new Boards();
        newBoard.setBoardName(boardDTO.getBoardName());
        newBoard.setOwner(owner);

        // Save board to database
        boardRepository.save(newBoard);

        // Create default statuses for the new board
        for (String statusName : defaultStatuses) {
            StatusList status = new StatusList();
            status.setName(statusName);
            status.setBoard(newBoard);
            statusListRepository.save(status);
        }

        // Optionally, share the board with the owner
        // newBoard.getSharedUsers().add(owner);
        // boardRepository.save(newBoard);

        return modelMapper.map(newBoard, BoardDTO.class);
    }

//    public void shareBoard(String boardId, String userId) {
//        Boards board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));
//        Users user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        board.getSharedUsers().add(user);
//        boardRepository.save(board);
//    }

    public List<Boards> getAllBoardsForUser(String userId) {
        Users owner = userRepository.findByUserId(userId);
        return boardRepository.findAll();
    }

}
