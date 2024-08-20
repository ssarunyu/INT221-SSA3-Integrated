package int221.sit.taskboard.project_management;

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
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 100, message = "description size must be between 0 and 500.")
    @Column(name = "description", nullable = false)
    private String description;

    @Size(max = 30, message = "assignees size must be between 0 and 30.")
    @Column(name = "assignees", nullable = false)
    private String assignees;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="statusId", referencedColumnName = "statusId")
    private StatusList status;

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
