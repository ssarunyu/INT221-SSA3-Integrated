package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.collaborators.CollaboratorListResponseDTO;
import int221.sit.taskboard.DTO.collaborators.CollaboratorResponseDTO;
import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.Collaborator;
import int221.sit.taskboard.entities.itbkk_db.SharedBoard;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.repositories.auth.UserRepository;
import int221.sit.taskboard.repositories.task.BoardRepository;
import int221.sit.taskboard.repositories.task.CollaboratorRepository;
import int221.sit.taskboard.repositories.task.SharedBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollaboratorService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    // ดึงรายชื่อผู้ร่วมงานทั้งหมดในบอร์ด
    public CollaboratorListResponseDTO getCollaboratorsByBoardId(String boardId) {
        Boards board = boardRepository.findByBoardId(boardId);
        List<Collaborator> collaborators = collaboratorRepository.findByBoard(board);

        List<CollaboratorResponseDTO> collaboratorList = collaborators.stream()
                .map(collaborator -> new CollaboratorResponseDTO(
                        collaborator.getUser().getUserListId(),
                        collaborator.getUser().getName(),
                        collaborator.getUser().getEmail(),
                        collaborator.getAccessRight(),
                        collaborator.getAddedOn()))
                .collect(Collectors.toList());

        CollaboratorListResponseDTO response = new CollaboratorListResponseDTO();
        response.setCollaborators(collaboratorList);

        return response;
    }

    public CollaboratorResponseDTO getCollaboratorByCollabId(String boardId, String collabId) {
        Boards board = boardRepository.findByBoardId(boardId);

        Long collabIdLong = Long.valueOf(collabId);
        Collaborator collaborator = collaboratorRepository.findCollaboratorByCollabId(collabIdLong);

        if (collaborator == null) {
            throw new ItemNotFoundException("Collaborator not found!");
        }

        CollaboratorResponseDTO response = new CollaboratorResponseDTO(
                collaborator.getUser().getUserListId(),
                collaborator.getUser().getName(),
                collaborator.getUser().getEmail(),
                collaborator.getAccessRight(),
                collaborator.getAddedOn());

        return response;
    }
}

