package int221.sit.taskboard.entities.itbkk_db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskId")
    private Integer id;

    @Size(max = 100, message = "title size must be between 0 and 100.")
    @Column(name = "taskTitle", nullable = false)
    private String title;

    @Size(max = 100, message = "description size must be between 0 and 500.")
    @Column(name = "taskDescription", nullable = false)
    private String description;

    @Size(max = 30, message = "assignees size must be between 0 and 30.")
    @Column(name = "taskAssignee", nullable = false)
    private String assignees;

    @ManyToOne
    @JoinColumn(name = "boardId")
    @JsonIgnore
    private Boards board;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="statusId", referencedColumnName = "statusId")
    private StatusList status;

    @Column(name = "createdOn", nullable = false)
    private ZonedDateTime createdOn;

    @Column(name = "updatedOn", nullable = false)
    private ZonedDateTime updatedOn;

    public void trimValues() {
        if (this.title != null) {
            this.title = this.title.trim();
        }
        if (this.description != null) {
            this.description = this.description.trim();
        }
        if (this.assignees != null) {
            this.assignees = this.assignees.trim();
        }
    }
}
