package int221.sit.taskboard.entities.itbkk_db;

import io.viascom.nanoid.NanoId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "board")
    private List<SharedBoard> sharedBoards;

    @PrePersist
    private void prePersist() {
        if (this.boardId == null) {
            this.boardId = NanoId.generate(10);
        }
    }
}
