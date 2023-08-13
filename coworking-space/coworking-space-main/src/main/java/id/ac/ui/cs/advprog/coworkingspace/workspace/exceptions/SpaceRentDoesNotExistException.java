package id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions;
public class SpaceRentDoesNotExistException extends RuntimeException {
    public SpaceRentDoesNotExistException(Integer id) {
        super("Space rent with id " + id + " does not exist");
    }
}
