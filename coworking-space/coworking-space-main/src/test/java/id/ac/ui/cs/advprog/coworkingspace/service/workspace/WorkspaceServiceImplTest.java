package id.ac.ui.cs.advprog.coworkingspace.service.workspace;

import id.ac.ui.cs.advprog.coworkingspace.workspace.dto.WorkspaceRequest;
import id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions.WorkspaceDoesNotExistException;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.Workspace;
import id.ac.ui.cs.advprog.coworkingspace.workspace.model.workspace.WorkspaceType;
import id.ac.ui.cs.advprog.coworkingspace.workspace.repository.WorkspaceRepository;
import id.ac.ui.cs.advprog.coworkingspace.workspace.service.workspace.WorkspaceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceImplTest {
    @InjectMocks
    private WorkspaceServiceImpl service;

    @Mock
    private WorkspaceRepository repository;

    Workspace workspace;
    Workspace newWorkspace;
    WorkspaceRequest createRequest;
    WorkspaceRequest updateRequest;

    @BeforeEach
    void setUp() {
        createRequest = WorkspaceRequest.builder()
                .type("COWORKING")
                .capacity(25)
                .hourlyPrice(125000.00)
                .dailyPrice(1200000.00)
                .filledSeat(0)
                .description("test")
                .image("An image Link")
                .availability(true)
                .build();

        updateRequest = WorkspaceRequest.builder()
                .type("PERSONAL")
                .capacity(10)
                .hourlyPrice(150000.00)
                .dailyPrice(1500000.00)
                .filledSeat(0)
                .description("test")
                .image("An image Link")
                .availability(true)
                .build();

        workspace = Workspace.builder()
                .id(1)
                .type(WorkspaceType.COWORKING)
                .capacity(25)
                .hourlyPrice(125000.00)
                .dailyPrice(1200000.00)
                .filledSeat(0)
                .description("test")
                .image("An image Link")
                .availability(true)
                .build();

        newWorkspace = Workspace.builder()
                .id(1)
                .type(WorkspaceType.PERSONAL)
                .capacity(10)
                .hourlyPrice(150000.00)
                .dailyPrice(1500000.00)
                .filledSeat(0)
                .description("test")
                .image("An image Link")
                .availability(true)
                .build();
    }

    @Test
    void whenFindAllWorkspaceShouldReturnListOfWorkspace() {
        List<Workspace> allWorkspace = List.of(workspace);

        when(repository.findAll()).thenReturn(allWorkspace);

        List<Workspace> result = service.findAll();
        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(allWorkspace, result);
    }

    @Test
    void whenFindByIdAndFoundShouldReturnWorkspace() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(workspace));

        Workspace result = service.findById(0);
        verify(repository, atLeastOnce()).findById(any(Integer.class));
        Assertions.assertEquals(workspace, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(WorkspaceDoesNotExistException.class, () -> {
            service.findById(0);
        });
    }

    @Test
    void whenCreateWorkspaceShouldReturnTheCreatedWorkspace() {
        when(repository.save(any(Workspace.class))).thenAnswer(invocation -> {
            var workspace = invocation.getArgument(0, Workspace.class);
            workspace.setId(1);
            return workspace;
        });

        Workspace result = service.create(createRequest);
        verify(repository, atLeastOnce()).save(any(Workspace.class));
        Assertions.assertEquals(workspace, result);
    }

    @Test
    void whenUpdateWorkspaceAndFoundShouldReturnTheUpdatedWorkspace() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(workspace));
        when(repository.save(any(Workspace.class))).thenAnswer(invocation ->
                invocation.getArgument(0, Workspace.class));

        Workspace result = service.update(1, updateRequest);
        verify(repository, atLeastOnce()).save(any(Workspace.class));
        Assertions.assertEquals(newWorkspace, result);
    }

    @Test
    void whenUpdateWorkspaceAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(WorkspaceDoesNotExistException.class, () -> {
            service.update(0, createRequest);
        });
    }

    @Test
    void whenDeleteWorkspaceAndFoundShouldCallDeleteByIdOnRepo() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(workspace));

        service.delete(0);
        verify(repository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void whenDeleteWorkspaceAndNotFoundShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Assertions.assertThrows(WorkspaceDoesNotExistException.class, () -> {
            service.delete(0);
        });
    }

    @Test
    void whenFindAllCoworkingSpaceShouldReturnListOfCoworkingSpace() {
        List<Workspace> allWorkspace = List.of(workspace);

        when(repository.findAllByType(WorkspaceType.COWORKING)).thenReturn(allWorkspace);

        List<Workspace> result = service.findAllCoworkingSpace();
        verify(repository, atLeastOnce()).findAllByType(WorkspaceType.COWORKING);
        Assertions.assertEquals(WorkspaceType.COWORKING, result.get(0).getType());
    }

    @Test
    void whenFindAllPersonalgSpaceShouldReturnListOfPersonalSpace() {
        List<Workspace> allWorkspace = List.of(newWorkspace);

        when(repository.findAllByType(WorkspaceType.PERSONAL)).thenReturn(allWorkspace);

        List<Workspace> result = service.findAllPersonalSpace();
        verify(repository, atLeastOnce()).findAllByType(WorkspaceType.PERSONAL);
        Assertions.assertEquals(WorkspaceType.PERSONAL, result.get(0).getType());
    }
}
