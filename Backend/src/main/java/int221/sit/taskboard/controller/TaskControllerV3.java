package int221.sit.taskboard.controller;

import int221.sit.taskboard.services.BoardService;
import int221.sit.taskboard.services.StatusListService;
import int221.sit.taskboard.services.TaskListService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v3/boards")
@CrossOrigin(origins = {"http://localhost:5173","http://ip23ssa3.sit.kmutt.ac.th"})
public class TaskControllerV3 {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BoardService boardService;

    @Autowired
    private StatusListService statusService;

    @Autowired
    private TaskListService service;

    @Autowired
    private TaskListService taskListService;

}
