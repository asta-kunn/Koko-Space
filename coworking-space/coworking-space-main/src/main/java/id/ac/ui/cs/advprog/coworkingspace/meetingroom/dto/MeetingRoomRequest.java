package id.ac.ui.cs.advprog.coworkingspace.meetingroom.dto;

import lombok.*;

import java.util.List;
@lombok.Generated
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRoomRequest {
    private String type;
    private String name;
    private Integer capacity;
    private Integer price;
    private String city;
    private String country;
    private String address;
    private Boolean availability;
    private List<String> images;
}
