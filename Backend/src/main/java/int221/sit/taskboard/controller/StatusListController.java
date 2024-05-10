package int221.sit.taskboard.controller;

import int221.sit.taskboard.entities.StatusList;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.services.StatusListService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/statuses")
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th"})
public class StatusListController {

    @Autowired
    private StatusListService service;

    @GetMapping("")
    public List<StatusList> getAllStatusList() {
        return service.getALl();
    }

    @GetMapping("/{id}")
    public StatusList getStatusListById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping("")
    public ResponseEntity<StatusList> addNewStatus(@RequestBody StatusList newStatus) {
        StatusList createNewStatus = service.addStatus(newStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(createNewStatus);
    }
}
