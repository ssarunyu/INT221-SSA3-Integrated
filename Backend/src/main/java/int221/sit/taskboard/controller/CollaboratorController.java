package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.boards.BoardDTO;
import int221.sit.taskboard.DTO.collaborators.CollaboratorListResponseDTO;
import int221.sit.taskboard.DTO.collaborators.CollaboratorResponseDTO;
import int221.sit.taskboard.Jwt.JwtTokenUtil;
import int221.sit.taskboard.exceptions.AccessDeniedException;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.services.AccessControlService;
import int221.sit.taskboard.services.BoardService;
import int221.sit.taskboard.services.CollaboratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/boards")
@CrossOrigin(origins = { "http://localhost:5173", "https://intproj23.sit.kmutt.ac.th/"} )
public class CollaboratorController {
    @Autowired
    private CollaboratorService collaboratorService;

    @Autowired
    private BoardService boardService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccessControlService accessControlService;

    @GetMapping("/{board_id}/collabs")
    public ResponseEntity<CollaboratorListResponseDTO> getCollaborators(@PathVariable("board_id") String boardId,
                                                                              @RequestHeader(value = "Authorization", required = false) String token) {
        BoardDTO board = boardService.getBoardById(boardId);

        // ตรวจสอบการเข้าถึงผ่าน AccessControlService
        String access_token = (token != null && !token.isEmpty()) ? token.substring(7) : null;
        String userId = (access_token != null) ? jwtTokenUtil.getUserIdFromToken(access_token) : null;

        boolean checkAccess = accessControlService.hasAccess(userId, boardId, token);
        if (!checkAccess) {
            throw new AccessDeniedException("Access denied");
        }

        CollaboratorListResponseDTO response = collaboratorService.getCollaboratorsByBoardId(boardId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{board_id}/collabs/{collab_id}")
    public ResponseEntity<CollaboratorResponseDTO> getCollaboratorById(@PathVariable("board_id") String boardId,
                                                                       @PathVariable("collab_id") String collabId,
                                                                       @RequestHeader(value = "Authorization", required = false) String token) {
        BoardDTO board = boardService.getBoardById(boardId);

        // ตรวจสอบการเข้าถึงผ่าน AccessControlService
        String access_token = (token != null && !token.isEmpty()) ? token.substring(7) : null;
        String userId = (access_token != null) ? jwtTokenUtil.getUserIdFromToken(access_token) : null;

        boolean checkAccess = accessControlService.hasAccess(userId, boardId, token);
        if (!checkAccess) {
            throw new AccessDeniedException("Access denied");
        }

        CollaboratorResponseDTO response = collaboratorService.getCollaboratorByCollabId(boardId, collabId);
        return ResponseEntity.ok(response);
    }


}