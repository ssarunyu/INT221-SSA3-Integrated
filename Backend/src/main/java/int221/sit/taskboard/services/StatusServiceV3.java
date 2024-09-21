package int221.sit.taskboard.services;

import int221.sit.taskboard.DTO.statuses.StatusWithTaskCountDTO;
import int221.sit.taskboard.entities.itbkk_db.Boards;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import int221.sit.taskboard.entities.itbkk_db.TaskList;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.repositories.task.BoardRepository;
import int221.sit.taskboard.repositories.task.StatusListRepository;
import int221.sit.taskboard.repositories.task.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatusServiceV3 {
    @Autowired
    private StatusListRepository statusRepository;

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private BoardRepository boardRepository;


    @Transactional("taskBoardTransactionManager")
    public List<StatusWithTaskCountDTO> getAllStatusesByBoardId(String boardId) {
        List<StatusList> allStatus = statusRepository.findAllStatus(boardId);
        List<Object[]> taskCountResults = taskListRepository.countTasksByStatusForBoard(boardId);

        Map<Integer, Long> taskCountMap = new HashMap<>();
        for (Object[] result : taskCountResults) {
            Integer statusId = (Integer) result[0];
            Long count = (Long) result[1];
            taskCountMap.put(statusId, count);
        }

        List<StatusWithTaskCountDTO> statusWithTaskCountList = new ArrayList<>();
        for (StatusList status : allStatus) {
            Long taskCount = taskCountMap.getOrDefault(status.getId(), 0L);
            statusWithTaskCountList.add(new StatusWithTaskCountDTO(
                    status.getId(),
                    status.getName(),
                    status.getBoard().getBoardId(),
                    taskCount.toString()
            ));
        }

        return statusWithTaskCountList;
    }

    @Transactional("taskBoardTransactionManager")
    public List<StatusList> getStatusByNameAndBoardId(String boardId, String statusName) {
        List<StatusList> status = statusRepository.findByNameAndBoard(boardId, statusName.toLowerCase().trim());
        if (status == null || status.isEmpty()) {
            throw new ItemNotFoundException("Status " + statusName + "is not found!, please try again.");
        }
        System.out.println(status);
        return status;
    }

    @Transactional("taskBoardTransactionManager")
    public StatusList addNewStatus(String boardId, StatusList newStatus){

        if(newStatus.getName() != null || !newStatus.getName().isEmpty()) {
            newStatus.trimValues();
            Boards board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new ItemNotFoundException("Board " + boardId + " not found"));

            newStatus.setBoard(board);
            newStatus.setCreatedOn(ZonedDateTime.now());
            newStatus.setUpdatedOn(ZonedDateTime.now());

            return statusRepository.saveAndFlush(newStatus);
        } else {
            throw new BadRequestException("Status name must not be null");
        }
    }

    @Transactional("taskBoardTransactionManager")
    public StatusList updateStatus(String boardId, Integer statusId, StatusList newStatus){
        if(newStatus.getName() != null || !newStatus.getName().isEmpty()) {
            newStatus.trimValues();
            StatusList existingStatus = statusRepository.findByIdAndBoard(statusId, boardId)
                   .orElseThrow(() -> new ItemNotFoundException("Status id " + statusId + " does not exist for board " + boardId));

            if (existingStatus.getName().equals("No Status") || existingStatus.getName().equals("DONE")) {
                existingStatus.setStatusColor(newStatus.getStatusColor());
                throw new BadRequestException("Only Status Color can be updated for Status Id: " + statusId + ", (No Status, DONE)");
            }

            existingStatus.setName(newStatus.getName());
            existingStatus.setDescription(newStatus.getDescription());
            existingStatus.setStatusColor(newStatus.getStatusColor());
            existingStatus.setUpdatedOn(ZonedDateTime.now());

            return statusRepository.saveAndFlush(existingStatus);
        } else {
            throw new BadRequestException("Status name must not be null");
        }
    }

    @Transactional("taskBoardTransactionManager")
    public ResponseEntity<StatusList> deleteStatus(String boardId, Integer statusId) {
        StatusList existingStatus = statusRepository.findByIdAndBoard(statusId, boardId)
               .orElseThrow(() -> new ItemNotFoundException("Status id " + statusId + " does not exist for board " + boardId));

        if (taskListRepository.existsByStatusId(statusId)) {
            throw new BadRequestException("Cannot delete Status Id: " + statusId + " because it is referenced by existing tasks.");
        }

        if (existingStatus.getName().equals("No Status") || existingStatus.getName().equals("DONE")) {
            throw new BadRequestException("Cannot delete Status Id: " + statusId + ", (No Status, DONE)");
        }

        statusRepository.deleteByIdAndBoard(statusId, boardId);
        return ResponseEntity.ok(existingStatus);
    }

}
