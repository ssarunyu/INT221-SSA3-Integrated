package int221.sit.taskboard.controller;

import int221.sit.taskboard.DTO.tasks.TaskAndStatusInt;
import int221.sit.taskboard.entities.itbkk_db.StatusList;
import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.services.StatusListService;
import int221.sit.taskboard.services.TaskListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/statuses")
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th", "https://intproj23.sit.kmutt.ac.th"})
public class StatusListController {

    @Autowired
    private StatusListService service;

    @Autowired
    private TaskListService tasklistService;

    @GetMapping("")
    public List<StatusList> getAllStatusList() {
        return service.getALl();
    }

    @GetMapping("/{id}")
    public StatusList getStatusListById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping("")
    public ResponseEntity<StatusList> addNewStatus(@Valid @RequestBody StatusList newStatus) {
        try {
            StatusList createNewStatus = service.addStatus(newStatus);
            return ResponseEntity.status(HttpStatus.CREATED).body(createNewStatus);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Status name '" + newStatus.getName() + "' already exists, (must be unique)");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StatusList> updateStatus(@Valid @RequestBody StatusList statusList, @PathVariable Integer id){
        try {
            StatusList updateStatus = service.updateStatus(id, statusList);
            return ResponseEntity.status(HttpStatus.OK).body(updateStatus);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Status name '" + statusList.getName() + "' already exists, (must be unique)");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusList> deleteStatus(@PathVariable Integer id) {
        try {
            return service.deleteStatus(id);
        } catch (Exception e) {
            throw new BadRequestException("Cannot be delete Status Id : " + id + " (This Id has using!) ");
        }

    }

    @DeleteMapping("/{id}/{newId}")
    public ResponseEntity<TaskAndStatusInt> deleteStatusAndTransfer(@PathVariable Integer id, @PathVariable Integer newId) {
        try {
            TaskAndStatusInt transferTask = tasklistService.transferTasking(id, newId);
            service.deleteStatus(id);
            return ResponseEntity.status(HttpStatus.OK).body(transferTask);
        } catch (DataIntegrityViolationException exception) {
           throw new BadRequestException("destination status for task transfer must be different from current status");
        }
    }

}
