package id.ac.ui.cs.advprog.coworkingspace.meetingroom.repository;
import id.ac.ui.cs.advprog.coworkingspace.meetingroom.model.rent.AttendeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttendeeStatusRepository extends JpaRepository<AttendeeStatus, Integer> {
    @NonNull
    List<AttendeeStatus> findAll();
    List<AttendeeStatus> findAllByRentId(@NonNull Integer rentId);

}
