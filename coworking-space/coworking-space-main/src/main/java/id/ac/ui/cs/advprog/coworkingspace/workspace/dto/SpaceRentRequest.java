package id.ac.ui.cs.advprog.coworkingspace.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@lombok.Generated
public class SpaceRentRequest {
    private Integer workspaceId;
    private Integer duration;
}
