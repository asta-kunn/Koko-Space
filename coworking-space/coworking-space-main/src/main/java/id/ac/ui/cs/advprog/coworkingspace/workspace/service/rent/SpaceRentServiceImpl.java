package id.ac.ui.cs.advprog.coworkingspace.workspace.service.rent;

import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentResponse;
import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions.*;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.rent.SpaceRent;
import id.ac.ui.cs.advprog.coworkingspace.workspace.repository.SpaceRentRepository;
import id.ac.ui.cs.advprog.coworkingspace.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceRentServiceImpl implements SpaceRentService {
    private final SpaceRentRepository spaceRentRepository;
    private final WorkspaceRepository workspaceRepository;

    @Override
    public List<SpaceRentResponse> findAll() {
        return spaceRentRepository.findAll()
                .stream()
                .map(SpaceRentResponse::fromSpaceRent)
                .toList();
    }

    @Override
    public List<SpaceRentResponse> findAllByUserId(Integer userId) {
        return spaceRentRepository.findAllByUserId(userId)
                .stream()
                .map(SpaceRentResponse::fromSpaceRent)
                .toList();
    }

    @Override
    public SpaceRentResponse getSpaceRentById(Integer id) {
        var spaceRent = spaceRentRepository.findById(id);
        if (spaceRent.isEmpty()) throw new SpaceRentDoesNotExistException(id);
        return SpaceRentResponse.fromSpaceRent(spaceRent.get());
    }

    @Override
    public SpaceRent create(Integer userId, SpaceRentRequest spaceRentRequest) {
        var workspace = workspaceRepository.findById(spaceRentRequest.getWorkspaceId());
        if (workspace.isEmpty()) throw new WorkspaceDoesNotExistException(spaceRentRequest.getWorkspaceId());
        if (workspace.get().getFilledSeat() >= workspace.get().getCapacity()) {
            workspace.get().setAvailability(false);
            workspaceRepository.save(workspace.get());
            throw new WorkspaceIsFullException(spaceRentRequest.getWorkspaceId());
        }

        LocalDateTime rentStart = LocalDateTime.now();

        var spaceRent = SpaceRent.builder()
                .userId(userId)
                .rentStart(rentStart)
                .rentEnd(rentStart.plusHours(spaceRentRequest.getDuration()))
                .workspace(workspace.get())
                .duration(spaceRentRequest.getDuration().longValue())
                .cost(workspace.get().calculateCost(spaceRentRequest.getDuration()))
                .build();
        spaceRentRepository.save(spaceRent);

        workspace.get().incrFilledSeat();
        workspaceRepository.save(workspace.get());

        return spaceRent;
    }

    @Override
    public void delete(Integer id) {
        if (isSpaceRentDoesNotExist(id)) throw new SpaceRentDoesNotExistException(id);
        else spaceRentRepository.deleteById(id);

    }

    @Override
    public SpaceRent extendRent(Integer id, Integer addedDuration) {
        if (isSpaceRentDoesNotExist(id)) throw new SpaceRentDoesNotExistException(id);
        SpaceRent spaceRent = spaceRentRepository.getReferenceById(id);
        if (spaceRent.getDuration() >= 12) {
            throw new InvalidExtendRentException(id);
        }
        spaceRent.setDuration(Duration.between(spaceRent.getRentStart(), spaceRent.getRentEnd()).toHours() + addedDuration);
        spaceRent.setRentEnd(spaceRent.getRentEnd().plusHours(addedDuration));
        spaceRent.setCost(spaceRent.getWorkspace().calculateCost(Math.toIntExact(spaceRent.getDuration())));

        spaceRentRepository.save(spaceRent);
        return spaceRent;
    }

    @Override
    public SpaceRent upgrade(Integer id, Integer personalSpaceId) {
        if (isSpaceRentDoesNotExist(id)) throw new SpaceRentDoesNotExistException(id);
        SpaceRent spaceRent = spaceRentRepository.getReferenceById(id);
        if (!spaceRent.getWorkspace().isCoworking()) {
            throw new InvalidUpgradeException(id);
        } else {
            var workspace = workspaceRepository.findById(personalSpaceId);
            if (workspace.isEmpty()) throw new WorkspaceDoesNotExistException(personalSpaceId);
            else {
                if (workspace.get().isCoworking()) throw new InvalidUpgradeException(id);
                else {
                    long remainingDuration = Duration.between(LocalDateTime.now(), spaceRent.getRentEnd()).toHours();
                    var newSpaceRent = SpaceRent.builder()
                            .id(id)
                            .userId(spaceRent.getUserId())
                            .workspace(workspace.get())
                            .duration(remainingDuration)
                            .rentStart(LocalDateTime.now())
                            .rentEnd(LocalDateTime.now().plusHours(remainingDuration))
                            .cost(spaceRent.getCost() + workspace.get().calculateCost(Math.toIntExact(remainingDuration)))
                            .build();
                    spaceRentRepository.save(newSpaceRent);
                    return newSpaceRent;
                }
            }
        }
    }

    @Override
    public void checkRentFinish(Integer workspaceId) {
        for (SpaceRent spaceRent:spaceRentRepository.findAllByWorkspaceId(workspaceId)) {
            if (LocalDateTime.now().isAfter(spaceRent.getRentEnd())) {
                spaceRent.getWorkspace().decrFilledSeat();
            }
        }
    }

    private boolean isSpaceRentDoesNotExist(Integer id) {
        return spaceRentRepository.findById(id).isEmpty();
    }


}
