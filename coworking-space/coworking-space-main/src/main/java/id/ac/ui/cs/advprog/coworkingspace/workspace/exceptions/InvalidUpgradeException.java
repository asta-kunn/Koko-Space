package id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions;

public class InvalidUpgradeException extends RuntimeException {
    public InvalidUpgradeException(Integer id) {
        super("Space rent with id " + id + " cannot be upgraded");
    }
}
