package id.ac.ui.cs.advprog.coworkingspace.workspace.service.workspace;

import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.WorkspaceRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkspaceService {
    List<Workspace> findAll();
    List<Workspace> findAllPersonalSpace();
    List<Workspace> findAllCoworkingSpace();
    Workspace findById(Integer id);
    Workspace create(WorkspaceRequest request);
    Workspace update(Integer id, WorkspaceRequest request);
    void delete(Integer id);
}
