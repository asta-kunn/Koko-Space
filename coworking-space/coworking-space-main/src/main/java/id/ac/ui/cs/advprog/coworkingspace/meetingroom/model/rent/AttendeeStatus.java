package id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent;

import jakarta.persistence.*;
import lombok.*;
@lombok.Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AttendeeStatus {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer rentId;
    private String name;
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

}
