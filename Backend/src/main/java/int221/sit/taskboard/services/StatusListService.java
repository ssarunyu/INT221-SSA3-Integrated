package int221.sit.taskboard.services;

import int221.sit.taskboard.entities.StatusList;
import int221.sit.taskboard.exceptions.ItemNotFoundException;
import int221.sit.taskboard.repositories.StatusListRepository;
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

    @Autowired
    private ListMapper listMapper;

    public List<StatusList> getALl() {
        return repository.findAll();
    }

    public StatusList findById(Integer id) {
        return repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException("Statuses id " + id + " does not exist !!")
        );
    }

    public StatusList addStatus(StatusList newStatus) {
        if(newStatus.getName() == null) {
            throw new ItemNotFoundException("NOT FOUND");
        } else {
            return repository.saveAndFlush(newStatus);
        }
    }

}
