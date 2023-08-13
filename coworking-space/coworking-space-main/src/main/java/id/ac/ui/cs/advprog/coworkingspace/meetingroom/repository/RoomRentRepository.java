package id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository;

import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.RoomRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRentRepository extends JpaRepository<RoomRent, Integer> {
    @NonNull
    List<RoomRent> findAll();
    List<RoomRent> findAllByUserId(@NonNull Integer userId);
    @NonNull
    RoomRent findRoomRentById(@NonNull Integer id);
}
