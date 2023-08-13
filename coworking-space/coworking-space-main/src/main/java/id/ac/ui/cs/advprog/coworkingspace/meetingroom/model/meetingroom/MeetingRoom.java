package id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.meetingroom;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@lombok.Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MeetingRoom {
    @Id
    @GeneratedValue
    private Integer id;
    @Enumerated(EnumType.STRING)
    private MeetingRoomType type;

    private String name;

    private Integer price;
    @ElementCollection
    private List<String> images;


    private String city;
    private String country;
    private String address;

    private Integer capacity;
    private Boolean availability;

    public Integer calculateCost(Integer duration) {
        return getPrice()*duration;
    }

}
