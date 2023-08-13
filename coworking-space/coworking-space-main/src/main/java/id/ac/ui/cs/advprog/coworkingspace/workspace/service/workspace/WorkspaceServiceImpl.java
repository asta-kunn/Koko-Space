package id.ac.ui.cs.advprog.coworkingspace.workspace.service.workspace;

import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.WorkspaceRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions.WorkspaceDoesNotExistException;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.WorkspaceType;
import id.ac.ui.cs.advprog.coworkingspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    @Override
    public List<Workspace> findAll() {
        return workspaceRepository.findAll();
    }

    @Override
    public List<Workspace> findAllPersonalSpace() {
        return workspaceRepository.findAllByType(WorkspaceType.PERSONAL);
    }

    @Override
    public List<Workspace> findAllCoworkingSpace() {
        return workspaceRepository.findAllByType(WorkspaceType.COWORKING);
    }

    @Override
    public Workspace findById(Integer id) {
        var workspace = workspaceRepository.findById(id);
        if (workspace.isEmpty()) throw new WorkspaceDoesNotExistException(id);
        return workspace.get();
    }

    @Override
    public Workspace create(WorkspaceRequest request) {
        Workspace workspace = Workspace.builder()
                .capacity(request.getCapacity())
                .type(WorkspaceType.valueOf(request.getType()))
                .hourlyPrice(request.getHourlyPrice())
                .dailyPrice(request.getDailyPrice())
                .description(request.getDescription())
                .image(request.getImage())
                .filledSeat(0)
                .availability(true)
                .build();

        return workspaceRepository.save(workspace);
    }

    @Override
    public Workspace update(Integer id, WorkspaceRequest request) {
        if (isWorkspaceDoesNotExist(id)) throw new WorkspaceDoesNotExistException(id);

        Workspace workspace = Workspace.builder()
                .id(id)
                .capacity(request.getCapacity())
                .type(WorkspaceType.valueOf(request.getType()))
                .hourlyPrice(request.getHourlyPrice())
                .dailyPrice(request.getDailyPrice())
                .description(request.getDescription())
                .image(request.getImage())
                .filledSeat(request.getFilledSeat())
                .availability(request.getAvailability())
                .build();

        return workspaceRepository.save(workspace);
    }

    @Override
    public void delete(Integer id) {
        if (isWorkspaceDoesNotExist(id)) throw new WorkspaceDoesNotExistException(id);
        else workspaceRepository.deleteById(id);
    }

    private boolean isWorkspaceDoesNotExist(Integer id) {
        return workspaceRepository.findById(id).isEmpty();
    }
}
