package int221.sit.taskboard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "boards")
public class Boards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardId")
    private long boardId;

    @Column(name = "boardName")
    @Size(max = 120)
    private String boardName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ownerOid")
    @Size(max = 36)
    private Users ownerId;
}
