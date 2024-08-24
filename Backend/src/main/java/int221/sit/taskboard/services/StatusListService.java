package int221.sit.taskboard.services;

import int221.sit.taskboard.exceptions.BadRequestException;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.entities.*;
import int221.sit.taskboard.repositories.task.StatusListRepository;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@org.springframework.transaction.annotation.Transactional("taskBoardTransactionManager")
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
//            StatusListValidation.validateTaskDataLength(newStatus);
            return repository.saveAndFlush(newStatus);
        } else {
            throw new BadRequestException("Status name must not be null");
        }
    }

    @Transactional
    public StatusList updateStatus(Integer id, StatusList statuslist){
        StatusList existingStatus = repository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Status id " + id + " does not exist!"));

        statuslist.trimValues();

        if (statuslist.getName() == null || statuslist.getName().isEmpty()) {
            throw new BadRequestException("Status name must not be null");
        }

        if (existingStatus.getId() == 1 || "Done".equals(existingStatus.getName())) {
            throw new BadRequestException("Cannot be update Status Id : " + id + ", (No Status, Done)");
        }

        existingStatus.setName(statuslist.getName());
        existingStatus.setDescription(statuslist.getDescription());
//        StatusListValidation.validateTaskDataLength(statuslist);

        return repository.save(existingStatus);
    }

    @Transactional
    public ResponseEntity<StatusList> deleteStatus(Integer id) {
        StatusList statusList = repository.findById(id).orElse(null);

        if(statusList == null){
            throw new ItemNotFoundException("Status id " + id + " does not exist!");
        }

        if (statusList.getId() == 1 || "Done".equals(statusList.getName())){
            throw new BadRequestException("Cannot be delete Status Id : " + id + ", (No Status, Done)");
        }

        try {
            repository.delete(statusList);
        } catch (Exception e){
            throw new BadRequestException("Cannot be delete Status Id : " + id + ", " + e.getMessage());
        }

        return ResponseEntity.ok(statusList);
    }
}
