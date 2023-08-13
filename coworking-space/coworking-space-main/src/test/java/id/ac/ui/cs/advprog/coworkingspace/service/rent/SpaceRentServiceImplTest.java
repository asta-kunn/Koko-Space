package id.ac.ui.cs.advprog.coworkingspace.service.rent;

import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.SpaceRentResponse;
import id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions.*;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.rent.SpaceRent;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.WorkspaceType;
import id.ac.ui.cs.advprog.coworkingspace.workspace.repository.SpaceRentRepository;
import id.ac.ui.cs.advprog.coworkingspace.workspace.repository.WorkspaceRepository;
import id.ac.ui.cs.advprog.coworkingspace.workspace.service.rent.SpaceRentService;
import id.ac.ui.cs.advprog.coworkingspace.workspace.service.rent.SpaceRentServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpaceRentServiceImplTest {

    @InjectMocks
    private SpaceRentServiceImpl service;
    @Mock
    private SpaceRentRepository spaceRentRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    SpaceRent spaceRent;

    SpaceRent spaceRentPersonal;

    SpaceRentRequest spaceRentRequest;

    Workspace workspace;

    Workspace workspacePersonal;

    @Mock
    Workspace mockWorkspace = new Workspace();

    @BeforeEach
    void setUp() {

        spaceRentRequest = new SpaceRentRequest(1, 1);

        workspace = Workspace.builder()
                .id(1)
                .type(WorkspaceType.COWORKING)
                .capacity(25)
                .hourlyPrice(125000.00)
                .dailyPrice(1200000.00)
                .filledSeat(0)
                .description("Test")
                .image("test")
                .availability(true)
                .build();

        spaceRent = SpaceRent.builder()
                .id(1)
                .userId(1)
                .rentStart(LocalDateTime.now())
                .rentEnd(LocalDateTime.now().plusHours(4))
                .cost(125000.0)
                .workspace(workspace)
                .duration(4L)
                .build();

        workspacePersonal = Workspace.builder()
                .id(2)
                .type(WorkspaceType.PERSONAL)
                .capacity(25)
                .hourlyPrice(125000.00)
                .dailyPrice(1200000.00)
                .filledSeat(0)
                .description("Test")
                .image("test")
                .availability(true)
                .build();

        spaceRentPersonal = SpaceRent.builder()
                .id(2)
                .userId(1)
                .rentStart(LocalDateTime.now())
                .rentEnd(LocalDateTime.now().plusHours(4))
                .cost(125000.0)
                .workspace(workspace)
                .duration(1L)
                .build();

    }

    @Test
    void whenCreateSpaceRentShouldCallSaveOnRepo() {
        when(workspaceRepository.findById(any(Integer.class))).thenReturn(Optional.of(workspace));
        service.create(0, spaceRentRequest);
        verify(spaceRentRepository, atLeastOnce()).save(any(SpaceRent.class));
    }

    @Test
    void whenCreateSpaceRentButWorkspaceNotFoundShouldThrowException() {
        when(workspaceRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(WorkspaceDoesNotExistException.class, () -> {
            service.create(0, spaceRentRequest);
        });
    }

    @Test
    void whenCreateSpaceRentButWorkspaceIsFullShouldThrowException() {
        when(workspaceRepository.findById(any(Integer.class))).thenReturn(Optional.of(workspace));
        workspace.setFilledSeat(25);

        assertThrows(WorkspaceIsFullException.class, () -> {
            service.create(0, spaceRentRequest);
        });
    }

    @Test
    void whenDeleteSpaceRentAndFoundShouldCallDeleteByIdOnRepo() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.of(spaceRent));
        service.delete(0);
        verify(spaceRentRepository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteSpaceRentButNotFoundShouldThrowException() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(SpaceRentDoesNotExistException.class, () -> {
            service.delete(0);
        });
    }

    @Test
    void whenFindAllSpaceRentShouldReturnAllSpaceRent() {
        when(spaceRentRepository.findAll()).thenReturn(Arrays.asList(spaceRent));
        List<SpaceRentResponse> spaceRents = service.findAll();
        assert spaceRents.size() == 1;
        verify(spaceRentRepository, atLeastOnce()).findAll();
    }

    @Test
    void whenGetSpaceRentByIdShouldReturnSpaceRent() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.of(spaceRent));
        SpaceRentResponse spaceRentResponse = service.getSpaceRentById(0);
        assert spaceRentResponse.getWorkspace() == workspace;
        verify(spaceRentRepository, atLeastOnce()).findById(any(Integer.class));
    }

    @Test
    void whenGetSpaceRentByIdAndNotFoundShouldThrowException() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(SpaceRentDoesNotExistException.class, () -> {
            service.getSpaceRentById(0);
        });
    }

    @Test
    void whenFindAllByUserIdShouldReturnAllSpaceRentByUserId() {
        when(spaceRentRepository.findAllByUserId(any(Integer.class))).thenReturn(Collections.singletonList(spaceRent));
        List<SpaceRentResponse> spaceRents = service.findAllByUserId(0);
        assert spaceRents.size() == 1;
        verify(spaceRentRepository, atLeastOnce()).findAllByUserId(any(Integer.class));
    }

    @Test
    void whenUpgradeRentShouldReturnUpgradedRent() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.of(spaceRentPersonal));
        when(spaceRentRepository.getReferenceById(any(Integer.class))).thenReturn(spaceRentPersonal);
        when(workspaceRepository.findById(any(Integer.class))).thenReturn(Optional.of(workspacePersonal));

        SpaceRent result = service.upgrade(spaceRentPersonal.getId(), workspacePersonal.getId());
        assert result.getWorkspace() == workspacePersonal;
        verify(spaceRentRepository, atLeastOnce()).findById(any(Integer.class));
        verify(spaceRentRepository, atLeastOnce()).getReferenceById(any(Integer.class));
        verify(workspaceRepository, atLeastOnce()).findById(any(Integer.class));
    }

    @Test
    void whenExtendRentShouldReturnExtendedRent() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.of(spaceRent));
        when(spaceRentRepository.getReferenceById(any(Integer.class))).thenReturn(spaceRent);

        SpaceRent result = service.extendRent(spaceRent.getId(), 1);
        System.out.println(result.getDuration());
        assert Objects.equals(result.getDuration(), spaceRent.getDuration());
        verify(spaceRentRepository, atLeastOnce()).findById(any(Integer.class));
        verify(spaceRentRepository, atLeastOnce()).getReferenceById(any(Integer.class));
    }

    @Test
    void whenExtendRentAndSpaceRentNotExistShouldThrowException() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(SpaceRentDoesNotExistException.class, () -> {
            service.extendRent(spaceRent.getId(), 1);
        });
    }

    @Test
    void whenCheckRentFinishShouldDecreaseFilledSeat() {
        // Mock data
        mockWorkspace.setId(1);
        mockWorkspace.setFilledSeat(2);

        LocalDateTime now = LocalDateTime.now();

        SpaceRent expiredRent = new SpaceRent();
        expiredRent.setRentEnd(now.minusDays(1));
        expiredRent.setWorkspace(mockWorkspace);

        SpaceRent activeRent = new SpaceRent();
        activeRent.setRentEnd(now.plusDays(1));
        activeRent.setWorkspace(mockWorkspace);

        // Mock repository findAllByWorkspaceId call
        when(spaceRentRepository.findAllByWorkspaceId(mockWorkspace.getId()))
                .thenReturn(List.of(expiredRent, activeRent));

        // Call the method under test
        service.checkRentFinish(mockWorkspace.getId());

        // Verify that workspace was updated only for rentEnd in the past
        verify(expiredRent.getWorkspace()).decrFilledSeat();
    }
    @Test
    void whenExtendRentButDurationExceededShouldThrowException() {
        // Set duration to 12 hours
        spaceRent.setDuration(12L);

        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.of(spaceRent));
        when(spaceRentRepository.getReferenceById(any(Integer.class))).thenReturn(spaceRent);

        // Save id to a variable
        Integer id = spaceRent.getId();

        // Expect InvalidExtendRentException to be thrown
        assertThrows(InvalidExtendRentException.class, () -> {
            // Use the saved id in the service call
            service.extendRent(id, 1);
        });

        verify(spaceRentRepository, atLeastOnce()).findById(any(Integer.class));
        verify(spaceRentRepository, atLeastOnce()).getReferenceById(any(Integer.class));
    }

    @Test
    void whenUpgradeRentButSpaceRentDoesNotExistShouldThrowException() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        var spaceRentId = 1;
        var workspaceId = 2;

        assertThrows(SpaceRentDoesNotExistException.class, () -> {
            service.upgrade(spaceRentId, workspaceId);
        });

        verify(spaceRentRepository, atLeastOnce()).findById(any(Integer.class));
    }
    @Test
    void whenUpgradeRentButInvalidShouldThrowException() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.of(spaceRent));
        when(spaceRentRepository.getReferenceById(any(Integer.class))).thenReturn(spaceRent);

        workspace.setType(WorkspaceType.PERSONAL); // Assuming WorkspaceType.PERSONAL indicates a non-coworking space

        var spaceRentId = spaceRent.getId();
        var workspaceId = workspace.getId();

        assertThrows(InvalidUpgradeException.class, () -> {
            service.upgrade(spaceRentId, workspaceId);
        });

        verify(spaceRentRepository, atLeastOnce()).findById(any(Integer.class));
        verify(spaceRentRepository, atLeastOnce()).getReferenceById(any(Integer.class));;
    }

    @Test
    void whenUpgradeRentButWorkspaceDoesNotExistShouldThrowException() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.of(spaceRent));
        when(spaceRentRepository.getReferenceById(any(Integer.class))).thenReturn(spaceRent);
        when(workspaceRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        var spaceRentId = spaceRent.getId();
        var workspaceId = 2;

        assertThrows(WorkspaceDoesNotExistException.class, () -> {
            service.upgrade(spaceRentId, workspaceId);
        });

        verify(spaceRentRepository, atLeastOnce()).findById(any(Integer.class));
        verify(spaceRentRepository, atLeastOnce()).getReferenceById(any(Integer.class));
        verify(workspaceRepository, atLeastOnce()).findById(any(Integer.class));
    }

    @Test
    void whenUpgradeRentAndSelectedWorkspaceIsCoworkingShouldThrowException() {
        when(spaceRentRepository.findById(any(Integer.class))).thenReturn(Optional.of(spaceRentPersonal));
        when(spaceRentRepository.getReferenceById(any(Integer.class))).thenReturn(spaceRentPersonal);
        when(workspaceRepository.findById(any(Integer.class))).thenReturn(Optional.of(workspace));

        assertThrows(InvalidUpgradeException.class, () -> {
            service.upgrade(spaceRentPersonal.getId(), workspace.getId());
        });

        verify(spaceRentRepository, atLeastOnce()).findById(any(Integer.class));
        verify(spaceRentRepository, atLeastOnce()).getReferenceById(any(Integer.class));
        verify(workspaceRepository, atLeastOnce()).findById(any(Integer.class));
    }


}