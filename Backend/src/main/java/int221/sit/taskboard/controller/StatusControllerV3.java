package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.statuses.StatusWithTaskCountDTO;
import int221.sit.taskboard.DTO.tasks.TaskAndStatusInt;
import int221.sit.taskboard.DTO.tasks.TaskAndStatusObjShort;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import int221.sit.taskboard.entities.itbkk_db.TaskList;
import int221.sit.taskboard.services.StatusServiceV3;
import int221.sit.taskboard.services.TaskListService;
import int221.sit.taskboard.services.TaskServiceV3;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v3/boards")
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th"})
public class StatusControllerV3 {
    @Autowired
    private StatusServiceV3 statusService;

    @Autowired
    private TaskServiceV3 taskService;

    @Autowired
    private ModelMapper modelMapper;

    //แสดง status ทั้งหมดใน boards
    @GetMapping("/{board_id}/statuses")
    public List<StatusWithTaskCountDTO> getAllStatuses(@PathVariable("board_id") String boardId) {
        return statusService.getAllStatusesByBoardId(boardId);
    }

    //ใช้ status name ในการแสดงเฉพาะ status ที่ต้องการ
    @GetMapping("/{board_id}/statuses/{status_name}")
    public List<StatusList> getStatusByName(@PathVariable("board_id") String boardId, @PathVariable("status_name") String statusName) {
        return statusService.getStatusByNameAndBoardId(boardId, statusName);
    }

    //สร้าง status ของบอร์ดนั้นๆ
    @PostMapping("/{board_id}/statuses")
    public StatusList createNewStatus(@PathVariable("board_id") String boardId, @RequestBody StatusList newStatus) {
        return statusService.addNewStatus(boardId, newStatus);
    }

    //แก้ไข status โดยรับ id ของ status นั้นมา
    @PutMapping("/{board_id}/statuses/{status_id}")
    public StatusList updateStatusById(@PathVariable("board_id") String boardId, @PathVariable("status_id") Integer statusId, @RequestBody StatusList newStatus) {
        return statusService.updateStatus(boardId, statusId, newStatus);
    }

    //ลบ status ที่ต้องการออกจาก board Id
    @DeleteMapping("/{board_id}/statuses/{status_id}")
    public ResponseEntity<StatusList> deleteStatusById(@PathVariable("board_id") String boardId, @PathVariable("status_id") Integer statusId) {
        return statusService.deleteStatus(boardId, statusId);
    }

    @DeleteMapping("/{board_id}/statuses/{status_id}/{status_newId}")
    public ResponseEntity<List<TaskAndStatusObjShort>> deleteStatusByIdAndTransfer(@PathVariable("board_id") String boardId, @PathVariable("status_id") Integer statusId, @PathVariable("status_newId") Integer statusNewId) {
        List<TaskAndStatusObjShort> transferTask = taskService.transferTasking(boardId, statusId, statusNewId);
        statusService.deleteStatus(boardId, statusId);
        return ResponseEntity.status(HttpStatus.OK).body(transferTask);
    }
}
