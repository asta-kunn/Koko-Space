package id.ac.ui.cs.advprog.coworkingspace.workspace.repository;

import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.WorkspaceType;
import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {
    @NonNull
    List<Workspace> findAll();
    @NonNull
    Optional<Workspace> findById(@NonNull Integer id);
    List<Workspace> findAllByType(WorkspaceType type);
    void deleteById(@NonNull Integer id);
}
