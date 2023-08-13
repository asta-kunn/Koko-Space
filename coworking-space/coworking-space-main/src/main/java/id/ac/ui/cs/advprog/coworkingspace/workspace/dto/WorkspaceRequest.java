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
public class WorkspaceRequest {
    private String type;
    private Integer capacity;
    private Double hourlyPrice;
    private Double dailyPrice;
    private Integer filledSeat;
    private String description;
    private String image;
    private Boolean availability;
}
