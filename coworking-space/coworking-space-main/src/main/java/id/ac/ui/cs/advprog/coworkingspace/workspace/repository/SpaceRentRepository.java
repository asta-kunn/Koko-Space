package id.ac.ui.cs.advprog.coworkingspace.workspace.repository;

import id.ac.ui.cs.advprog.coworkingspace.workspace.model.rent.SpaceRent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceRentRepository extends JpaRepository<SpaceRent, Integer> {
    @NonNull
    List<SpaceRent> findAll();
    List<SpaceRent> findAllByUserId(Integer userId);
    List<SpaceRent> findAllByWorkspaceId(Integer workspaceId);
    @NonNull
    Optional<SpaceRent> findById(@NonNull Integer id);
}
