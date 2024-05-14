package int221.sit.taskboard.services;

import int221.sit.taskboard.entities.StatusList;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.repositories.StatusListRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusListService {
    @Autowired
    private StatusListRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public List<StatusList> getALl() {
        return repository.findAll();
    }

    @Transactional
    public StatusList findById(Integer id) {
        return repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException("Statuses id " + id + " does not exist !!")
        );
    }

    @Transactional
    public StatusList addStatus(StatusList newStatus) {
        if(newStatus.getName() != null) {
            newStatus.trimValues();
            return repository.saveAndFlush(newStatus);
        } else {
            throw new RuntimeException("Status name is required");
        }
    }

    @Transactional
    public StatusList updateStatus(Integer id, StatusList statuslist){
        StatusList statusListUpdated = repository.findById(id).orElse(null);
        statuslist.setId(id);
        statuslist.trimValues();
        if(statusListUpdated != null){
            repository.save(statuslist);
        } else {
            throw new ItemNotFoundException("Status id " + id + " does not exist!");
        }
        return statusListUpdated;
    }

    @Transactional
    public ResponseEntity<StatusList> deleteStatus(Integer id) {
        StatusList statusList = repository.findById(id).orElse(null);
        if(statusList != null){
            repository.delete(statusList);
        } else {
            throw new ItemNotFoundException("Status id " + id + " does not exist!");
        }
        return ResponseEntity.ok(statusList);
    }
}
