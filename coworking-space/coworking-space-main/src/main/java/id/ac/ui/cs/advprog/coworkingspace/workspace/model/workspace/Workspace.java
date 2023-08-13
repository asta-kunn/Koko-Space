package id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Workspace {
    @Id
    @GeneratedValue
    private Integer id;
    @Enumerated(EnumType.STRING)
    private WorkspaceType type;
    private Integer capacity;
    private Double hourlyPrice;
    private Double dailyPrice;
    private Integer filledSeat;
    private String description;
    private String image;
    private Boolean availability;

    public void incrFilledSeat() {
        filledSeat++;
    }

    public void decrFilledSeat() {
        filledSeat--;
    }

    public Double calculateCost(Integer duration) {
        if (duration < 12) {
            double cost = duration * getHourlyPrice();
            if (duration <= 3) return cost;
            else if (duration <= 6) return 0.9 * cost;
            else return 0.8 * cost;
        } else {
            return getDailyPrice();
        }
    }

    public boolean isCoworking() {
        return this.type == WorkspaceType.COWORKING;
    }
}
