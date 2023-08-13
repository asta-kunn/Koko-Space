package id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions;

public class WorkspaceDoesNotExistException extends RuntimeException {
    public WorkspaceDoesNotExistException(Integer id) {
        super("Workspace with id " + id + " does not exist");
    }
}
