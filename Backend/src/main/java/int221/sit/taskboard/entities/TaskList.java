package int221.sit.taskboard.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "task")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title", nullable = true, length = 100)
    private String Title;
    @Column(name = "description", nullable = false, length = 100)
    private String Description;
    @Column(name = "assignees", nullable = false, length = 100)
    private String Assignees;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "createdOn")
    private ZonedDateTime createdOn;

    @Column(name = "updatedOn")
    private ZonedDateTime updatedOn;
    public enum TaskStatus {
        NO_STATUS,
        TO_DO,
        DOING,
        DONE
    }
}
