package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.boards.BoardDTO;
import int221.sit.taskboard.DTO.boards.BoardForCreated;
import int221.sit.taskboard.DTO.UserListResponse;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.entities.itbkk_db.*;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.repositories.auth.UserRepository;
import int221.sit.taskboard.repositories.task.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    ModelMapper modelMapper;

    private final List<String> defaultStatuses = List.of("NO STATUS", "TODO", "DOING", "DONE");
    private final String defaultDescription = "Default status description";
    private final String defaultStatusColor = "#FFFFFF";

    @Transactional("taskBoardTransactionManager")
    public BoardForCreated createBoard(BoardForCreated bfc, String token) {

        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserList owner = userListRepository.findByUsername(username);

        if (owner == null) {
            throw new IllegalArgumentException("User not found");
        }

        Boards newBoard = new Boards();
        newBoard.setBoardName(bfc.getName());
        newBoard.setOwnerId(owner.getUserListId());
        newBoard.setCreatedOn(ZonedDateTime.now());
        newBoard.setUpdatedOn(ZonedDateTime.now());

        if (bfc.getVisibility() != null &&
                ("private".equalsIgnoreCase(bfc.getVisibility()) || "public".equalsIgnoreCase(bfc.getVisibility()))) {
            newBoard.setBoardVisibility(Boards.Visibility.valueOf(bfc.getVisibility().toUpperCase()));
        } else if (bfc.getVisibility() == null || bfc.getVisibility().isEmpty() || bfc.getVisibility().isBlank()) {
            // ตั้งค่าเริ่มต้นให้ visibility เป็น private
            newBoard.setBoardVisibility(Boards.Visibility.PRIVATE);
        } else {
            throw new IllegalArgumentException("Invalid visibility value, must be 'private' or 'public'");
        }

        boardRepository.save(newBoard);

        SharedBoard sharedBoard = new SharedBoard();
        sharedBoard.setBoard(newBoard);
        sharedBoard.setOwner(owner);
        sharedBoardRepository.save(sharedBoard);

        for (String statusName : defaultStatuses) {
            StatusList status = new StatusList();
            status.setName(statusName);
            status.setBoard(newBoard);
            status.setDescription(defaultDescription);
            status.setStatusColor(defaultStatusColor);
            status.setCreatedOn(ZonedDateTime.now());
            status.setUpdatedOn(ZonedDateTime.now());
            statusListRepository.save(status);
        }

        BoardForCreated resultDTO = new BoardForCreated();
        resultDTO.setId(newBoard.getBoardId());
        resultDTO.setName(newBoard.getBoardName());
        resultDTO.setVisibility(newBoard.getBoardVisibility().toString());

        UserListResponse ownerResponse = modelMapper.map(owner, UserListResponse.class);
        resultDTO.setOwner(ownerResponse);

        return resultDTO;
    }

    public Map<String, List<BoardDTO>> getPersonalAndCollabBoards(String userId) {
        List<Boards> ownedBoards = boardRepository.findAllBoardByUserId(userId);
        List<BoardDTO> personalBoards = ownedBoards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        List<Collaborator> collaboratorBoards = collaboratorRepository.findAllByUser_UserListId(userId);
        List<BoardDTO> collabBoards = collaboratorBoards.stream()
                .map(collaborator -> convertToDTO(collaborator.getBoard()))
                .collect(Collectors.toList());

        Map<String, List<BoardDTO>> boardMap = new HashMap<>();
        boardMap.put("personalBoards", personalBoards);
        boardMap.put("collabBoards", collabBoards);

        return boardMap;
    }

    private BoardDTO convertToDTO(Boards board) {
        BoardDTO boardDTO = new BoardDTO();

        // แมปฟิลด์ทีละฟิลด์จาก Boards ไปยัง BoardDTO
        boardDTO.setId(board.getBoardId()); // map boardId
        boardDTO.setBoardName(board.getBoardName()); // map boardName
        boardDTO.setVisibility(board.getBoardVisibility().toString()); // map visibility

        SharedBoard sharedBoard = sharedBoardRepository.findByBoard(board);
        if (sharedBoard != null && sharedBoard.getOwner() != null) {
            UserList owner = sharedBoard.getOwner();
            UserListResponse ownerResponse = new UserListResponse();
            ownerResponse.setUserId(owner.getUserListId());
            ownerResponse.setUsername(owner.getUsername());
            ownerResponse.setName(owner.getName());
            ownerResponse.setEmail(owner.getEmail());

            boardDTO.setOwner(ownerResponse);
        } else {
            boardDTO.setOwner(null);
        }

        return boardDTO;
    }

    public BoardDTO getBoardByIdAndUser(String boardId, String userId) {

        Boards board = boardRepository.findByBoardId(boardId);
        if (board == null) {
            return null;
        }

        SharedBoard sharedBoard = sharedBoardRepository.findByBoard(board);
        if (sharedBoard == null || !sharedBoard.getOwner().getUserListId().equals(userId)) {
            return null;
        }

        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        UserList owner = sharedBoard.getOwner();
        UserListResponse ownerResponse = modelMapper.map(owner, UserListResponse.class);
        boardDTO.setOwner(ownerResponse);
        return boardDTO;
    }

    public BoardDTO getBoardById(String boardId) {
        // ดึงข้อมูลบอร์ดจาก boardId โดยไม่ต้องกรอง userId
        Optional<Boards> optionalBoard = boardRepository.findById(boardId);

        if (!optionalBoard.isPresent()) {
            throw new ItemNotFoundException("Board not found !!!");
        }

        Boards board = optionalBoard.get();

        // แปลงข้อมูลจาก Boards ไปเป็น BoardDTO
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getBoardId()); // map boardId
        boardDTO.setBoardName(board.getBoardName()); // map boardName
        boardDTO.setVisibility(board.getBoardVisibility().toString()); // map visibility

        // ดึงข้อมูล SharedBoard เพื่อนำข้อมูล owner มา
        SharedBoard sharedBoard = sharedBoardRepository.findByBoard(board);
        if (sharedBoard != null && sharedBoard.getOwner() != null) {
            UserList owner = sharedBoard.getOwner();
            UserListResponse ownerResponse = new UserListResponse();
            ownerResponse.setUserId(owner.getUserListId()); // ตั้งค่า userId
            ownerResponse.setUsername(owner.getUsername());
            ownerResponse.setName(owner.getName());
            ownerResponse.setEmail(owner.getEmail());

            // ตั้งค่า owner ให้กับ BoardDTO
            boardDTO.setOwner(ownerResponse);
        } else {
            // ถ้าไม่มี owner
            throw new BadRequestException("Invalid board");
        }

        return boardDTO;
    }

    @Transactional("taskBoardTransactionManager")
    public Boards updateBoard(BoardDTO boardDTO) {
        // แปลงจาก BoardDTO เป็น Boards entity
        Boards boardEntity = boardRepository.findById(boardDTO.getId())
                .orElseThrow(() -> new ItemNotFoundException("Board not found"));

        boardEntity.setBoardName(boardDTO.getBoardName());
        boardEntity.setBoardVisibility(Boards.Visibility.valueOf(boardDTO.getVisibility().toUpperCase()));

        // บันทึก entity หลังจากทำการอัปเดต
        return boardRepository.save(boardEntity);
    }
}