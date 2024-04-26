package int221.sit.taskborad.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    @Column(name = "assignee", nullable = false, length = 100)
    private String Assignee;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "createOn")
    private ZonedDateTime createdOn;

    @Column(name = "updateOn")
    private ZonedDateTime updatedOn;
    public enum TaskStatus {
        NO_STATUS,
        TO_DO,
        DOING,
        DONE
    }
}
