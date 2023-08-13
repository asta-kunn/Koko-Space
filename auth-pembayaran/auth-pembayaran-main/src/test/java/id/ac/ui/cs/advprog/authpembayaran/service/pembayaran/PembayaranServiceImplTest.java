package id.ac.ui.cs.advprog.authpembayaran.service.pembayaran;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.PembayaranResponse;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.*;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.service.CouponServiceImpl;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.service.PembayaranServiceImpl;
import id.ac.ui.cs.advprog.authpembayaran.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PembayaranServiceImplTest {
    private User testUser;
    private Coupon testCoupon;
    private Coupon testCoupon2;

    @Mock
    private UserRepository userRepository;
    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponServiceImpl couponService;

    @InjectMocks
    private PembayaranServiceImpl pembayaranService;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@test.com")
                .password("test")
                .name("test")
                .verified(false)
                .active(true)
                .role(Role.PELANGGAN).saldo(20000.0).id(1)
                .build();

        User testUser2 = User.builder()
                .email("test2@test.com")
                .password("test2")
                .name("test2")
                .verified(false)
                .active(true)
                .role(Role.PELANGGAN).saldo(20000.0).id(2)
                .build();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Add one day to the current date
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        // Get the updated date
        Date nextDay = calendar.getTime();
        testCoupon = Coupon.builder()
                .user(testUser)
                .code("testCoupon")
                .discount(2000.0)
                .startDate(currentDate)
                .endDate(nextDay)
                .id(Long.parseLong("1"))
                .maxUsage(2)
                .minPrice(1000.0)
                .build();

        testCoupon2 = Coupon.builder()
                .user(testUser2)
                .code("testCoupon")
                .discount(2000.0)
                .startDate(currentDate)
                .endDate(nextDay)
                .id(Long.parseLong("2"))
                .maxUsage(2)
                .minPrice(1000.0)
                .build();

    }

    @Test
    void whenPayAmountFieldsIsEmptyShouldThrowException() {
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        var pembayaranReq = new PembayaranRequest(null,"test",null);
        assertThrows(AmountAndRentalNameRequiredException.class,()->pembayaranService.pay(1,pembayaranReq));
    }

    @Test
    void whenRentalNameFieldsIsEmptyShouldThrowException() {
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        var pembayaranReq = new PembayaranRequest(2000.0,null,null);
        assertThrows(AmountAndRentalNameRequiredException.class,()->pembayaranService.pay(1,pembayaranReq));
    }

    @Test
    void whenPayAmountIsNegativeShouldThrowException() {
        var pembayaranReq = new PembayaranRequest(-2000000000.0, "test", null);
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        assertThrows(InvalidAmountException.class, () -> pembayaranService.pay(1, pembayaranReq));
    }

    @Test
    void whenPayInsufficientFundsShouldThrowException() {
        var pembayaranReq = new PembayaranRequest(2000000000.0, "test", null);
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        assertThrows(InsufficientFundsException.class, () -> pembayaranService.pay(1, pembayaranReq));
    }

    @Test
    void whenPayUseCouponButWrongCouponShouldThrowException(){
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        when(couponRepository.findById(any(Integer.class))).thenReturn(Optional.of(testCoupon2));
        var pembayaranReq = new PembayaranRequest(2000.0,"test",2);
        assertThrows(WrongUserCoupon.class,()->pembayaranService.pay(1,pembayaranReq));
    }

    @Test
    void whenPayUseCouponButAmountLessThanMinimalPriceCouponShouldThrowException(){
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        when(couponRepository.findById(any(Integer.class))).thenReturn(Optional.of(testCoupon));
        var pembayaranReq = new PembayaranRequest(200.0,"test",1);
        assertThrows(MinimalAmountForCouponException.class,()->pembayaranService.pay(1,pembayaranReq));

    }

    @Test
    void whenPayUseCouponAndSuccessShouldReturnRightSubstractedAmount(){
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        when(couponRepository.findById(any(Integer.class))).thenReturn(Optional.of(testCoupon));
        when(couponService.useCoupon(1,1)).thenReturn(testCoupon.getDiscount());
        PembayaranRequest pembayaranRequest = new PembayaranRequest(20000.0, "test",1);
        PembayaranResponse pembayaranResponse = pembayaranService.pay(1,pembayaranRequest);
        assertNotNull(pembayaranResponse);
        assertEquals(pembayaranRequest.getAmount()-testCoupon.getDiscount(),pembayaranResponse.getAmount());
    }

    @Test
    void whenPayUseCouponSuccessAmountLessThanDiscountShouldReturnZeroAmount(){
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        when(couponRepository.findById(any(Integer.class))).thenReturn(Optional.of(testCoupon));
        when(couponService.useCoupon(1,1)).thenReturn(testCoupon.getDiscount());
        PembayaranRequest pembayaranRequest = new PembayaranRequest(1500.0, "test",1);
        PembayaranResponse pembayaranResponse = pembayaranService.pay(1,pembayaranRequest);
        assertNotNull(pembayaranResponse);
        assertEquals(0,pembayaranResponse.getAmount());
    }

    @Test
    void whenPaySuccesUserSaldoShouldSubstracted(){
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(testUser));
        PembayaranRequest pembayaranRequest = new PembayaranRequest(20000.0, "test",null);
        PembayaranResponse pembayaranResponse = pembayaranService.pay(1,pembayaranRequest);
        verify(walletRepository,times(1)).save(any());
        assertNotNull(pembayaranResponse);
        assertEquals(pembayaranRequest.getAmount(),pembayaranResponse.getAmount());
    }

}
