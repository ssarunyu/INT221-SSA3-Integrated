package int221.sit.taskboard.entities;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@JsonPropertyOrder({"id", "name", "description"})
@Table(name = "statuses")
public class StatusList {
    @Id
    @Column(name = "statusId")
    private Integer id;
    @Column(name = "statusName")
    private String name;
    @Column(name = "statusDescription")
    private String description;
}
