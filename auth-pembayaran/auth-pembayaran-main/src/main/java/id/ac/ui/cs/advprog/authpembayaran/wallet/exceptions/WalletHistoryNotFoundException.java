package id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions;

public class WalletHistoryNotFoundException extends RuntimeException{
    public WalletHistoryNotFoundException() {
        super("Invalid WalletHistory Object. WalletHistory with the id does not exist!");
    }
}
