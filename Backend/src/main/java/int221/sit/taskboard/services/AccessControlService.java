package int221.sit.taskboard.services;

import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.exceptions.AccessDeniedException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.repositories.task.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    @Autowired
    private BoardRepository boardRepository;

    public boolean hasAccess(String userId, String boardId, String token) {

        Boards board = boardRepository.findById(boardId).orElseThrow(() -> new ItemNotFoundException("Board not found"));

        boolean isOwner = board.getOwnerId().equals(userId);
        boolean isPrivate = board.getBoardVisibility() == Boards.Visibility.PRIVATE;
        boolean isPublic = board.getBoardVisibility() == Boards.Visibility.PUBLIC;

        // ถ้า board เป็น public และไม่มี token ก็อนุญาต
        if (isPublic && (token == null || token.isEmpty())) {
            return true;
        }

        // ถ้าเป็น private และไม่มี token ให้แจ้งว่า access denied
        if (isPrivate && (token == null || token.isEmpty())) {
            throw new AccessDeniedException("Access denied: private board requires authentication.");
        }

        // ถ้าผู้ใช้เป็นเจ้าของบอร์ด หรือบอร์ดเป็น public อนุญาตเข้าถึง
        if (isOwner || isPublic) {
            return true;
        }

        // ถ้าไม่เป็นเจ้าของและบอร์ดเป็น private ให้แจ้งว่า access denied
        return false;
    }
}
