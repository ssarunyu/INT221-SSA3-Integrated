package int221.sit.taskboard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.viascom.nanoid.NanoId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "boards")
public class Boards {
    @Id
    @Column(name = "boardId")
    private String boardId;

    @Column(name = "boardName")
    @Size(max = 120)
    private String boardName;

    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    private Users owner;

    @PrePersist
    private void prePersist() {
        if (this.boardId == null) {
            this.boardId = NanoId.generate(10);
        }
    }
}
