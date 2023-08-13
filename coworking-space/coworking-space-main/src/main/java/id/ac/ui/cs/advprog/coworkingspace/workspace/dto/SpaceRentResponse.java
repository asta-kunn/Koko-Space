package id.ac.ui.cs.advprog.coworkingspace.workspace.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.rent.SpaceRent;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@lombok.Generated
public class SpaceRentResponse {
    private Integer spaceRentId;
    private Integer userId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime rentStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime rentEnd;
    private Double cost;
    private Workspace workspace;
    private Integer duration;

    public static SpaceRentResponse fromSpaceRent(SpaceRent spaceRent) {
        return SpaceRentResponse.builder()
                .userId(spaceRent.getUserId())
                .spaceRentId(spaceRent.getId())
                .rentStart(spaceRent.getRentStart())
                .rentEnd(spaceRent.getRentEnd())
                .cost(spaceRent.getCost())
                .workspace(spaceRent.getWorkspace())
                .duration(Math.toIntExact(spaceRent.getDuration()))
                .build();
    }


}
