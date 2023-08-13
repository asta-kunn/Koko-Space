package id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions;

public class WorkspaceIsFullException extends RuntimeException {
    public WorkspaceIsFullException(Integer id) {
        super("Workspace with id " + id + " is full");
    }
}
