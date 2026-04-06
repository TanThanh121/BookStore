package j2ee.BuiTanThanh.NguyenDucTrung;

import j2ee.BuiTanThanh.daos.Cart;
import j2ee.BuiTanThanh.daos.Item;
import j2ee.BuiTanThanh.repositories.IBookRepository;
import j2ee.BuiTanThanh.repositories.IInvoiceRepository;
import j2ee.BuiTanThanh.repositories.IItemInvoiceRepository;
import j2ee.BuiTanThanh.repositories.IUserRepository;
import j2ee.BuiTanThanh.services.CartService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * White Box Tests - Nguyễn Đức Trung
 * Phạm vi: CartService (TC_NDT_017 → TC_NDT_020)
 * Công cụ: JUnit 5 + Mockito
 */
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    private static final String CART_SESSION_KEY = "cart";

    @Mock
    private IInvoiceRepository invoiceRepository;

    @Mock
    private IItemInvoiceRepository itemInvoiceRepository;

    @Mock
    private IBookRepository bookRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartService cartService;

    // =========================================================
    // TC_NDT_017: getCart tạo Cart mới khi chưa có trong session
    // Luồng code: session.getAttribute("cart") == null → tạo Cart mới →
    // setAttribute
    // =========================================================
    @Test
    @DisplayName("TC_NDT_017 - getCart tạo Cart mới khi session chưa có giỏ hàng")
    void getCart_shouldCreateNewCart_whenSessionIsEmpty() {
        // Nhánh: attribute "cart" = null → Optional.empty → tạo mới
        when(session.getAttribute(CART_SESSION_KEY)).thenReturn(null);

        Cart result = cartService.getCart(session);

        assertThat(result).isNotNull();
        assertThat(result.getCartItems()).isEmpty();
        // Verify cart mới được lưu vào session
        verify(session).setAttribute(eq(CART_SESSION_KEY), any(Cart.class));
    }

    // =========================================================
    // TC_NDT_018: getCart trả về Cart đã có khi session tồn tại
    // Luồng code: session.getAttribute("cart") != null → trả về cart cũ
    // =========================================================
    @Test
    @DisplayName("TC_NDT_018 - getCart trả về Cart cũ khi session đã có giỏ hàng")
    void getCart_shouldReturnExistingCart_whenSessionHasCart() {
        Cart existingCart = new Cart();
        existingCart.addItems(new Item(1L, "Java Book", 50000.0, 2));
        // Nhánh: attribute "cart" != null → Optional.of(cart) → trả về cart cũ
        when(session.getAttribute(CART_SESSION_KEY)).thenReturn(existingCart);

        Cart result = cartService.getCart(session);

        assertThat(result).isSameAs(existingCart);
        assertThat(result.getCartItems()).hasSize(1);
        // Không tạo cart mới → setAttribute KHÔNG được gọi thêm
        verify(session, never()).setAttribute(any(), any());
    }

    // =========================================================
    // TC_NDT_019: getSumPrice tính đúng tổng tiền
    // Sách A: 50.000 x2 = 100.000 | Sách B: 30.000 x1 = 30.000 → Tổng = 130.000
    // =========================================================
    @Test
    @DisplayName("TC_NDT_019 - getSumPrice tính đúng tổng tiền = 130.000")
    void getSumPrice_shouldReturnCorrectTotalPrice() {
        Cart cart = new Cart();
        cart.addItems(new Item(1L, "Book A", 50000.0, 2)); // 50.000 * 2 = 100.000
        cart.addItems(new Item(2L, "Book B", 30000.0, 1)); // 30.000 * 1 = 30.000
        when(session.getAttribute(CART_SESSION_KEY)).thenReturn(cart);

        double totalPrice = cartService.getSumPrice(session);

        // 100.000 + 30.000 = 130.000
        assertThat(totalPrice).isEqualTo(130000.0);
    }

    // =========================================================
    // TC_NDT_020: getSumQuantity tính đúng tổng số lượng
    // Sách A: 3 | Sách B: 2 → Tổng = 5
    // =========================================================
    @Test
    @DisplayName("TC_NDT_020 - getSumQuantity tính đúng tổng số lượng = 5")
    void getSumQuantity_shouldReturnCorrectTotalQuantity() {
        Cart cart = new Cart();
        cart.addItems(new Item(1L, "Book A", 50000.0, 3)); // quantity = 3
        cart.addItems(new Item(2L, "Book B", 30000.0, 2)); // quantity = 2
        when(session.getAttribute(CART_SESSION_KEY)).thenReturn(cart);

        int totalQuantity = cartService.getSumQuantity(session);

        // 3 + 2 = 5
        assertThat(totalQuantity).isEqualTo(5);
    }
}
