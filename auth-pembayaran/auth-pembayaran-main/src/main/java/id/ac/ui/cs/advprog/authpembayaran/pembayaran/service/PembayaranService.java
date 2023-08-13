package id.ac.ui.cs.advprog.authpembayaran.pembayaran.service;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranResponse;

public interface PembayaranService {

    PembayaranResponse pay(Integer userId, PembayaranRequest request);

}
