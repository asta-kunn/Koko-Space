package id.ac.ui.cs.advprog.coworkingspace.workspace.model.rent;

import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@lombok.Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SpaceRent {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime rentStart;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime rentEnd;

    private Double cost;

    @ManyToOne
    @JoinColumn(name = "workspace_id", referencedColumnName = "id")
    private Workspace workspace;

    private Long duration;
}
