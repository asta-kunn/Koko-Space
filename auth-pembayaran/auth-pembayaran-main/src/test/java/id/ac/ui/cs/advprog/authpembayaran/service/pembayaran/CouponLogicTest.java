package id.ac.ui.cs.advprog.authpembayaran.service.pembayaran;

import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.UserNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.Role;
import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.component.CouponUseManager;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponCURequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponDeleteRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponUseRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.CouponNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.InvalidCouponInputException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Status;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponUsageRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.service.CouponLogic;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.service.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CouponLogicTest {
    @Mock
    private CouponRepository couponRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private CouponUsageRepository couponUsageRepository;
    
    @Mock
    private CouponServiceImpl couponService;
    
    @Mock
    private CouponUseManager couponUseManager;
    
    @InjectMocks
    private CouponLogic couponLogic;
    
    private Coupon testCoupon;
    private User testUser;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        testCoupon = Coupon.builder()
                .id(1L)
                .code("test")
                .discount(10000.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 100000))
                .minPrice(100000.0)
                .maxUsage(100)
                .status(Status.AVAILABLE)
                .build();
        testUser = User.builder()
                .email("johndoe@example.com")
                .password("test")
                .name("test")
                .verified(false)
                .active(true)
                .role(Role.PELANGGAN)
                .build();
    }
    
    @Test
    void whenCreateCouponButNullFieldShouldThrowException() {
        CouponCURequest request = CouponCURequest.builder()
                .code(null)
                .discount(null)
                .startDate(null)
                .endDate(null)
                .minPrice(null)
                .maxUsage(null)
                .userId(1)
                .build();
        assertThrows(InvalidCouponInputException.class, () -> couponLogic.createCouponLogic(request));
    }
    
    @Test
    void whenCreateCouponButInvalidDiscountFieldShouldThrowException() {
        CouponCURequest request = CouponCURequest.builder()
                .code("asdasd")
                .discount(-1.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 100000))
                .minPrice(100000.0)
                .maxUsage(100)
                .userId(1)
                .build();
        assertThrows(InvalidCouponInputException.class, () -> couponLogic.createCouponLogic(request));
    }
    
    @Test
    void whenCreateCouponButInvalidStartDateShouldThrowException() {
        CouponCURequest request = CouponCURequest.builder()
                .code("asdasd")
                .discount(100.0)
                .startDate(new Date(System.currentTimeMillis() + 100000))
                .endDate(new Date(System.currentTimeMillis() + 10))
                .minPrice(100000.0)
                .maxUsage(100)
                .userId(1)
                .build();
        assertThrows(InvalidCouponInputException.class, () -> couponLogic.createCouponLogic(request));
    }
    
    @Test
    void whenCreateCouponButInvalidEndDateShouldThrowException() {
        CouponCURequest request = CouponCURequest.builder()
                .code("asdasd")
                .discount(100.0)
                .startDate(new Date(System.currentTimeMillis() - 10000000))
                .endDate(new Date(System.currentTimeMillis() - 1000))
                .minPrice(100000.0)
                .maxUsage(100)
                .userId(1)
                .build();
        assertThrows(InvalidCouponInputException.class, () -> couponLogic.createCouponLogic(request));
    }
    
    @Test
    void whenCreateCouponButInvalidMinPriceShouldThrowException() {
        CouponCURequest request = CouponCURequest.builder()
                .code("asdasd")
                .discount(100.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 1000000))
                .minPrice(-1.0)
                .maxUsage(100)
                .userId(1)
                .build();
        assertThrows(InvalidCouponInputException.class, () -> couponLogic.createCouponLogic(request));
    }
    
    @Test
    void whenCreateCouponButInvalidMaxUsageShouldThrowException() {
        CouponCURequest request = CouponCURequest.builder()
                .code("asdasd")
                .discount(100.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 10))
                .minPrice(100000.0)
                .maxUsage(0)
                .userId(1)
                .build();
        InvalidCouponInputException exception = assertThrows(InvalidCouponInputException.class, () -> couponLogic.createCouponLogic(request));
        assertEquals("Maximum usage cannot be less than zero", exception.getMessage());
    }
    
    @Test
    void whenCreateCouponSucceeded() {
        CouponCURequest request = CouponCURequest.builder()
                .code("asdasd")
                .discount(100.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 10000000))
                .minPrice(100000.0)
                .maxUsage(1)
                .userId(1)
                .build();
        assertDoesNotThrow(() -> couponLogic.createCouponLogic(request));
    }
    
    @Test
    void whenDeleteCouponButUserIdInvalidShouldThrowException() {
        CouponDeleteRequest request = CouponDeleteRequest.builder()
                .userId(-1)
                .build();
        assertThrows(UserNotFoundException.class, () -> couponLogic.deleteCouponLogic(request, -1L));
    }
    
    @Test
    void whenDeleteCouponButCouponIdInvalidShouldThrowException() {
        CouponDeleteRequest request = CouponDeleteRequest.builder()
                .userId(1)
                .build();
        assertThrows(InvalidCouponInputException.class, () -> couponLogic.deleteCouponLogic(request, null));
    }
    
    @Test
    void whenDeleteCouponSucceeded() {
        CouponDeleteRequest request = CouponDeleteRequest.builder()
                .userId(1)
                .build();
        assertDoesNotThrow(() -> couponLogic.deleteCouponLogic(request, 1L));
    }
    
    @Test
    void whenUpdateCouponCouponIdInvalidShouldThrowException(){
        CouponCURequest request = CouponCURequest.builder()
                .code("asdasd")
                .discount(100.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 10000000))
                .minPrice(100000.0)
                .maxUsage(1)
                .userId(1)
                .build();
        assertThrows(InvalidCouponInputException.class, () -> couponLogic.updateCouponLogic(request, null));
    }
    
    @Test
    void whenUpdateCouponSucceeded(){
        CouponCURequest request = CouponCURequest.builder()
                .code("asdasd")
                .discount(100.0)
                .startDate(new Date(System.currentTimeMillis()))
                .endDate(new Date(System.currentTimeMillis() + 10000000))
                .minPrice(100000.0)
                .maxUsage(1)
                .userId(1)
                .build();
        assertDoesNotThrow( () -> couponLogic.updateCouponLogic(request, 1L));
    }
    
    @Test
    void whenUseCouponButInvalidUserIdShouldThrowException() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(null)
                .price(100000.0)
                .build();
        assertThrows(UserNotFoundException.class, () -> couponLogic.getPriceAfterDiscountLogic(request, 1L));
    }
    
    @Test
    void whenUseCouponButInvalidPriceShouldThrowException() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(null)
                .build();
        assertThrows(InvalidCouponInputException.class, () -> couponLogic.getPriceAfterDiscountLogic(request, 1L));
    }
    
    @Test
    void whenUseCouponButInvalidCouponIdShouldThrowException() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(100000.0)
                .build();
        assertThrows(CouponNotFoundException.class, () -> couponLogic.getPriceAfterDiscountLogic(request, null));
    }
    
    @Test
    void whenGetPriceSucceeded() {
        CouponUseRequest request = CouponUseRequest.builder()
                .userId(1)
                .price(100000.0)
                .build();
        assertDoesNotThrow( () -> couponLogic.getPriceAfterDiscountLogic(request, 1L));
    }
    
}
