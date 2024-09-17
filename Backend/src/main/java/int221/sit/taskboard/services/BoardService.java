package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.BoardDTO;
import int221.sit.taskboard.DTO.BoardForCreated;
import int221.sit.taskboard.DTO.UserListResponse;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.SharedBoard;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import int221.sit.taskboard.entities.itbkk_db.UserList;
import int221.sit.taskboard.repositories.auth.UserRepository;
import int221.sit.taskboard.repositories.task.BoardRepository;
import int221.sit.taskboard.repositories.task.SharedBoardRepository;
import int221.sit.taskboard.repositories.task.StatusListRepository;
import int221.sit.taskboard.repositories.task.UserListRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserListRepository userListRepository;

    @Autowired
    private StatusListRepository statusListRepository;

    @Autowired
    private SharedBoardRepository sharedBoardRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    ModelMapper modelMapper;

    private final List<String> defaultStatuses = List.of("TODO", "DOING", "DONE", "NO STATUS");

    @Transactional("taskBoardTransactionManager")
    public BoardForCreated createBoard(BoardForCreated bfc, String token) {

        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserList owner = userListRepository.findByUsername(username);

        if (owner == null) {
            throw new IllegalArgumentException("User not found");
        }

        Boards newBoard = new Boards();
        newBoard.setBoardName(bfc.getName());
        newBoard.setOwnerId(owner.getUserListId()); // Set the owner ID
        newBoard.setCreatedOn(ZonedDateTime.now()); // Set created time
        newBoard.setUpdatedOn(ZonedDateTime.now()); // Set updated time
        boardRepository.save(newBoard);

        SharedBoard sharedBoard = new SharedBoard();
        sharedBoard.setBoard(newBoard);
        sharedBoard.setOwner(owner);
        sharedBoardRepository.save(sharedBoard);

        for (String statusName : defaultStatuses) {
            StatusList status = new StatusList();
            status.setName(statusName);
            status.setBoard(newBoard);
            status.setCreatedOn(ZonedDateTime.now());
            status.setUpdatedOn(ZonedDateTime.now());
            statusListRepository.save(status);
        }

        BoardForCreated resultDTO = new BoardForCreated();
        resultDTO.setId(newBoard.getBoardId());
        resultDTO.setName(newBoard.getBoardName());

        UserListResponse ownerResponse = modelMapper.map(owner, UserListResponse.class);
        resultDTO.setOwner(ownerResponse);

        return resultDTO;
    }

    public List<BoardDTO> getAllBoardsForUser(String userId) {
        List<Boards> boards = boardRepository.findAllByOwnerId(userId);

        return boards.stream()
                .map(board -> {
                    BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
                    SharedBoard sharedBoard = sharedBoardRepository.findByBoard(board);
                    UserList owner = sharedBoard.getOwner();
                    UserListResponse ownerDTO = modelMapper.map(owner, UserListResponse.class);
                    boardDTO.setOwner(ownerDTO);
                    return boardDTO;
                })
                .collect(Collectors.toList());
    }

    public BoardDTO getBoardById(String boardId, String userId) {
        // ค้นหา board ตาม boardId
        Boards board = boardRepository.findByBoardId(boardId);
        if (board == null) {
            return null; // ถ้าไม่มี board ให้คืนค่า null
        }
        // ค้นหา sharedBoard ตาม board
        SharedBoard sharedBoard = sharedBoardRepository.findByBoard(board);
        // ตรวจสอบว่า sharedBoard มีอยู่และ owner ตรงกับ userId หรือไม่
        if (sharedBoard == null || !sharedBoard.getOwner().getUserListId().equals(userId)) {
            return null; // ถ้าไม่มี sharedBoard หรือ owner ไม่ตรงกับ userId ให้คืนค่า null
        }
        // แปลง Boards เป็น BoardDTO
        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        // แปลง owner (UserList) เป็น UserListResponse และตั้งค่าให้กับ boardDTO
        UserList owner = sharedBoard.getOwner();
        UserListResponse ownerResponse = modelMapper.map(owner, UserListResponse.class);
        boardDTO.setOwner(ownerResponse);
        // คืนค่า BoardDTO
        return boardDTO;
    }


}
