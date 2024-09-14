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

    //  New DB
    @Size(max = 100, message = "title size must be between 0 and 100.")
    @Column(name = "taskTitle", nullable = false)
    private String title;

    // Old DB
//    @Size(max = 100, message = "title size must be between 0 and 100.")
//    @Column(name = "title", nullable = false)
//    private String title;

    //  New DB
    @Size(max = 100, message = "description size must be between 0 and 500.")
    @Column(name = "taskDescription", nullable = false)
    private String description;

    //  Old DB
//    @Size(max = 100, message = "description size must be between 0 and 500.")
//    @Column(name = "description", nullable = false)
//    private String description;

    //  New DB
    @Size(max = 30, message = "assignees size must be between 0 and 30.")
    @Column(name = "taskAssignee", nullable = false)
    private String assignees;

    //  Old DB
//    @Size(max = 30, message = "assignees size must be between 0 and 30.")
//    @Column(name = "assignees", nullable = false)
//    private String assignees;

    @ManyToOne
    @JoinColumn(name = "boardId")
    private Boards board;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="statusId", referencedColumnName = "statusId")
    private StatusList status;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "boardId", referencedColumnName = "boardId")
//    private Boards boards;

    @Column(name = "createdOn", insertable = false, updatable = false)
    private ZonedDateTime createdOn;

    @Column(name = "updatedOn", insertable = false, updatable = false)
    private ZonedDateTime updatedOn;

//    public TaskList() {
//        if (this.status == null) {
//            this.status = new StatusList();
//            this.status.setName("No Status");
//        }
//    }

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
