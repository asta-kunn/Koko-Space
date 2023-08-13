package id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions;

public class InvalidExtendRentException extends RuntimeException {
    public InvalidExtendRentException(Integer id) {
        super("Space rent with id " + id + " cannot be extended due to daily rent");
    }
}

