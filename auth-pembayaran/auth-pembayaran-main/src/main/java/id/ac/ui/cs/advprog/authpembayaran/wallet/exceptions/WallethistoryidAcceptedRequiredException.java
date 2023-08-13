package id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions;

public class WallethistoryidAcceptedRequiredException extends RuntimeException{
    public WallethistoryidAcceptedRequiredException() {
        super("WalletHistoryId and Accepted field is required");
    }

}
