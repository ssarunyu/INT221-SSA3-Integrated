package int221.sit.taskboard.entities.itbkk_db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.viascom.nanoid.NanoId;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
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

    @Column(name = "created_on", nullable = false)
    private ZonedDateTime createdOn;

    @Column(name = "updated_on", nullable = false)
    private ZonedDateTime updatedOn;

    @Column(name = "owner_id")
    private String ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility boardVisibility;

    public enum Visibility {
        PRIVATE, PUBLIC
    }

    @OneToMany(mappedBy = "board")
    @JsonIgnore
    private List<SharedBoard> sharedBoards;

    @PrePersist
    private void prePersist() {
        if (this.boardId == null) {
            this.boardId = NanoId.generate(10);
        }
    }
}
