package int221.sit.taskboard.entities.itbkk_db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import lombok.Getter;

import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Embeddable
@JsonPropertyOrder({"id", "name", "description"})
@Table(name = "statuses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"boardId", "statusName"}))
public class StatusList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statusId")
    private Integer id;

    @Size(max = 50, message = "name size must be between 0 and 50.")
    @Column(name = "statusName", nullable = false)
    private String name;

    @Size(max = 200, message = "description size must be between 0 and 200.")
    @Column(name = "statusDescription")
    private String description;

    @Column(name = "created_on")
    private ZonedDateTime createdOn;

    @Column(name = "updated_on")
    private ZonedDateTime updatedOn;

    @Column(name = "statusColor")
    private String statusColor;

    @ManyToOne
    @JoinColumn(name = "boardId")
    @JsonIgnore
    private Boards board;

    public void trimValues() {
        if (this.name != null) {
            this.name = this.name.trim();
        }
        if (this.description != null) {
            this.description = this.description.trim();
        }
    }
}
