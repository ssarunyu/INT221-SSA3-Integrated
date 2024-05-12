package int221.sit.taskboard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @Column(name = "title", nullable = true, length = 100)
    private String Title;
    @Column(name = "description", nullable = false, length = 100)
    private String Description;
    @Column(name = "assignees", nullable = false, length = 100)
    private String Assignees;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="statusId")
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
        if (this.Title != null) {
            this.Title = this.Title.trim();
        }
        if (this.Description != null) {
            this.Description = this.Description.trim();
        }
        if (this.Assignees != null) {
            this.Assignees = this.Assignees.trim();
        }
    }
}
