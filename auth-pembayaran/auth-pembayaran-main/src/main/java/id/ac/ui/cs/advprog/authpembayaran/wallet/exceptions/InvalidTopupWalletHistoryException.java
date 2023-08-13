package id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions;

public class InvalidTopupWalletHistoryException extends RuntimeException{
    public InvalidTopupWalletHistoryException(){
        super("Invalid WalletHistory Object. The object already verified or not a top up");
        }
}
