package id.ac.ui.cs.advprog.authpembayaran.model.pembayaran;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.CouponUsage;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CouponTest {
    @Test
    void testCreateCoupon() {
        // Create a coupon
        Coupon coupon = Coupon.builder()
                .code("ABC123")
                .discount(0.2)
                .startDate(new Date())
                .endDate(new Date())
                .minPrice(100.0)
                .maxUsage(10)
                .status(Status.AVAILABLE)
                .build();
        
        // Assert coupon properties
//        assertNotNull(coupon.getId());
        assertEquals("ABC123", coupon.getCode());
        assertEquals(0.2, coupon.getDiscount());
        assertNotNull(coupon.getStartDate());
        assertNotNull(coupon.getEndDate());
        assertEquals(100.0, coupon.getMinPrice());
        assertEquals(10, coupon.getMaxUsage());
        assertEquals(Status.AVAILABLE, coupon.getStatus());
        
        // Add a usage
        CouponUsage usage = CouponUsage.builder()
                .coupon(coupon)
                .usageTime(new Date())
                .build();
        coupon.add(usage);
        
        // Assert usage is added
        assertNotNull(coupon.getUsages());
        assertEquals(1, coupon.getUsages().size());
        assertEquals(usage, coupon.getUsages().iterator().next());
        assertEquals(coupon, usage.getCoupon());
        
        // Set a user
        User user = new User();
        coupon.setUser(user);
        
        // Assert user is set
        assertNotNull(coupon.getUser());
        assertEquals(user, coupon.getUser());
    }
    
    @Test
    void testEquals_SameObject_ReturnsTrue() {
        Coupon coupon = Coupon.builder()
                .code("ABC123")
                .discount(0.2)
                .startDate(new Date())
                .endDate(new Date())
                .minPrice(100.0)
                .maxUsage(10)
                .status(Status.AVAILABLE)
                .build();
        
        assertEquals(coupon, coupon);
    }
    
    @Test
    void testEquals_SameProperties_ReturnsTrue() {
        Coupon coupon1 = Coupon.builder()
                .code("ABC123")
                .discount(0.2)
                .startDate(new Date())
                .endDate(new Date())
                .minPrice(100.0)
                .maxUsage(10)
                .status(Status.AVAILABLE)
                .build();
        
        Coupon coupon2 = Coupon.builder()
                .code("ABC123")
                .discount(0.2)
                .startDate(new Date())
                .endDate(new Date())
                .minPrice(100.0)
                .maxUsage(10)
                .status(Status.AVAILABLE)
                .build();
        
        assertEquals(coupon1, coupon2);
        assertEquals(coupon2, coupon1);
    }
    
    @Test
    void testEquals_DifferentProperties_ReturnsFalse() {
        Coupon coupon1 = Coupon.builder()
                .code("ABC123")
                .discount(0.2)
                .startDate(new Date())
                .endDate(new Date())
                .minPrice(100.0)
                .maxUsage(10)
                .status(Status.AVAILABLE)
                .build();
        
        Coupon coupon2 = Coupon.builder()
                .code("DEF456")
                .discount(0.3)
                .startDate(new Date())
                .endDate(new Date())
                .minPrice(150.0)
                .maxUsage(5)
                .status(Status.AVAILABLE)
                .build();
        
        assertNotEquals(coupon1, coupon2);
        assertNotEquals(coupon2, coupon1);
    }
    
    @Test
    void testEquals_NullObject_ReturnsFalse() {
        Coupon coupon = Coupon.builder()
                .code("ABC123")
                .discount(0.2)
                .startDate(new Date())
                .endDate(new Date())
                .minPrice(100.0)
                .maxUsage(10)
                .status(Status.AVAILABLE)
                .build();
        
        assertNotEquals(null, coupon);
    }
}
