package id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom.MeetingRoom;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.AttendeeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@lombok.Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RoomRent {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer userId;
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rentStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rentEnd;
    private Integer cost;
    @ManyToOne
    private MeetingRoom meetingRoom;
    private Integer duration;
    @JsonIgnore
    @OneToMany(mappedBy = "rentId", cascade = CascadeType.ALL)
    private List<AttendeeStatus> attendeeStatusList;
}
