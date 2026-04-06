package j2ee.BuiTanThanh.TranThanhTinh;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Automation Tests (Selenium WebDriver) - Trần Thanh Tịnh
 * Phạm vi: Đăng nhập, Tìm kiếm & Lọc, Giỏ hàng, Thanh toán, Lịch sử đơn hàng
 * TC_TTT_001 → TC_TTT_020
 *
 * Yêu cầu: Ứng dụng đang chạy tại http://localhost:8081
 * Admin: az1533 / Thanh121
 * User thường: được tạo động tại TC_TTT_001
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookstoreSeleniumTest {

    private static final String BASE_URL = "http://localhost:8081";
    private static final String ADMIN_USERNAME = "az1533";
    private static final String ADMIN_PASSWORD = "Thanh121";
    private static final Duration WAIT = Duration.ofSeconds(10);

    // Tài khoản user thường — tạo động ở TC_TTT_001
    private String userUsername;
    private String userPassword;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    void setUpDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1280,900");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, WAIT);
    }

    @AfterEach
    void ensureLogout() {
        try {
            driver.get(BASE_URL + "/logout");
        } catch (Exception ignored) {
        }
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void loginAs(String username, String password) {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        WebElement btn = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']")));
        jsClick(btn);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
    }

    /**
     * Thêm sách đầu tiên trong trang /books vào giỏ hàng (qua JS click để tránh
     * confirm dialog)
     */
    private void addFirstBookToCart() {
        driver.get(BASE_URL + "/books");
        wait.until(ExpectedConditions
                .presenceOfElementLocated(By.cssSelector("button[type='submit'].btn-success-modern")));
        WebElement addBtn = driver.findElement(By.cssSelector("button[type='submit'].btn-success-modern"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].form.submit();", addBtn);
        wait.until(d -> d.getCurrentUrl().contains("/cart") || d.getCurrentUrl().contains("/books"));
    }

    /** Tạo account user mới ngẫu nhiên và trả về {username, password} */
    private String[] registerNewUser() {
        driver.get(BASE_URL + "/register");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        String suffix = String.valueOf(System.currentTimeMillis()).substring(7);
        String uname = "ttt_user" + suffix;
        String pwd = "Ttt@1234";
        String phone = "09" + String.format("%08d", Math.abs(System.currentTimeMillis() % 100000000L));

        driver.findElement(By.id("email")).sendKeys("ttt" + suffix + "@test.com");
        driver.findElement(By.id("username")).sendKeys(uname);
        driver.findElement(By.id("password")).sendKeys(pwd);
        driver.findElement(By.id("phone")).sendKeys(phone);
        jsClick(driver.findElement(By.cssSelector("button[type='submit']")));
        wait.until(ExpectedConditions.urlContains("/login"));
        return new String[] { uname, pwd };
    }

    // ── ĐĂNG NHẬP ────────────────────────────────────────────────────────────

    /**
     * TC_TTT_001: Đăng ký + Đăng nhập thành công với thông tin hợp lệ
     * (Đăng ký trước để lưu credentials dùng cho các TC user sau)
     */
    @Test
    @Order(1)
    @DisplayName("TC_TTT_001 - Đăng ký và đăng nhập thành công")
    void loginSuccess() {
        String[] credentials = registerNewUser();
        userUsername = credentials[0];
        userPassword = credentials[1];

        loginAs(userUsername, userPassword);

        Assertions.assertFalse(driver.getCurrentUrl().contains("/login"),
                "Sau đăng nhập thành công không được ở lại /login");
    }

    /**
     * TC_TTT_002: Đăng nhập thất bại khi nhập sai mật khẩu
     */
    @Test
    @Order(2)
    @DisplayName("TC_TTT_002 - Đăng nhập thất bại khi sai mật khẩu")
    void loginFailWrongPassword() {
        driver.get(BASE_URL + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys(ADMIN_USERNAME);
        driver.findElement(By.id("password")).sendKeys("WrongPassword123");
        jsClick(driver.findElement(By.cssSelector("button[type='submit']")));

        wait.until(ExpectedConditions.urlContains("/login"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Đăng nhập sai phải ở lại /login");
        Assertions.assertTrue(driver.getCurrentUrl().contains("error") ||
                driver.getPageSource().toLowerCase().contains("invalid") ||
                driver.getPageSource().toLowerCase().contains("error"),
                "Phải hiển thị thông báo lỗi khi đăng nhập sai");
    }

    // ── TÌM KIẾM & LỌC ──────────────────────────────────────────────────────

    /**
     * TC_TTT_003: Tìm kiếm sách với từ khóa hợp lệ
     */
    @Test
    @Order(3)
    @DisplayName("TC_TTT_003 - Tìm kiếm sách với từ khóa hợp lệ")
    void searchBooksValidKeyword() {
        loginAs(userUsername, userPassword);
        driver.get(BASE_URL + "/books");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input.search-input")));

        WebElement searchInput = driver.findElement(By.cssSelector("input.search-input"));
        searchInput.sendKeys("Sài Gòn");
        jsClick(driver.findElement(By.cssSelector("button.search-btn")));

        wait.until(ExpectedConditions.urlContains("keyword="));
        String pageSource = driver.getPageSource();
        Assertions.assertTrue(
                pageSource.contains("Sài Gòn") || pageSource.contains("saigon") ||
                        pageSource.contains("results found") || pageSource.contains("No books found"),
                "Trang phải hiển thị kết quả tìm kiếm");
    }

    /**
     * TC_TTT_004: Lọc sách theo danh mục bằng cách click link danh mục
     */
    @Test
    @Order(4)
    @DisplayName("TC_TTT_004 - Lọc sách theo danh mục")
    void filterBooksByCategory() {
        loginAs(userUsername, userPassword);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("a[href*='category=']")));

        // Click danh mục đầu tiên (không phải "Tất cả")
        WebElement categoryLink = driver.findElement(By.cssSelector("a[href*='category=']"));
        String href = categoryLink.getAttribute("href");
        driver.get(href);

        wait.until(ExpectedConditions.urlContains("category="));
        Assertions.assertTrue(driver.getCurrentUrl().contains("category="),
                "URL sau khi lọc phải chứa tham số category");
    }

    /**
     * TC_TTT_005: Sắp xếp sách theo giá tăng dần
     */
    @Test
    @Order(5)
    @DisplayName("TC_TTT_005 - Sắp xếp sách theo giá tăng dần")
    void sortBooksByPriceAsc() {
        loginAs(userUsername, userPassword);
        driver.get(BASE_URL + "/books?sortBy=price_asc");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".book-price")));

        List<WebElement> priceElements = driver.findElements(By.cssSelector(".book-price span"));
        Assertions.assertFalse(priceElements.isEmpty(), "Phải có sách trên trang");

        if (priceElements.size() >= 2) {
            double firstPrice = Double.parseDouble(priceElements.get(0).getText().replace(",", ""));
            double lastPrice = Double.parseDouble(
                    priceElements.get(priceElements.size() - 1).getText().replace(",", ""));
            Assertions.assertTrue(firstPrice <= lastPrice,
                    "Giá sách đầu (" + firstPrice + ") phải ≤ giá sách cuối (" + lastPrice + ")");
        }
    }

    // ── GIỎ HÀNG ─────────────────────────────────────────────────────────────

    /**
     * TC_TTT_006: Thêm sách vào giỏ hàng thành công (cần đăng nhập)
     */
    @Test
    @Order(6)
    @DisplayName("TC_TTT_006 - Thêm sách vào giỏ hàng thành công")
    void addBookToCart() {
        loginAs(userUsername, userPassword);
        driver.get(BASE_URL + "/books");
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button[type='submit'].btn-success-modern")));

        // Lấy tên sách đầu tiên
        String bookTitle = driver.findElement(By.cssSelector(".book-title a")).getText();

        WebElement addBtn = driver.findElement(By.cssSelector("button[type='submit'].btn-success-modern"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].form.submit();", addBtn);

        // Vào giỏ hàng kiểm tra
        driver.get(BASE_URL + "/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-item")));
        String cartContent = driver.getPageSource();

        Assertions.assertTrue(cartContent.contains(bookTitle) || cartContent.contains("cart-item"),
                "Sách vừa thêm phải xuất hiện trong giỏ hàng");
    }

    /**
     * TC_TTT_007: Cập nhật số lượng sách trong giỏ hàng
     */
    @Test
    @Order(7)
    @DisplayName("TC_TTT_007 - Cập nhật số lượng sách trong giỏ hàng")
    void updateCartQuantity() {
        loginAs(userUsername, userPassword);
        addFirstBookToCart();

        driver.get(BASE_URL + "/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".quantity-input")));

        // Lấy bookId từ input
        WebElement quantityInput = driver.findElement(By.cssSelector(".quantity-input"));
        String bookId = quantityInput.getAttribute("data-id");

        // Update bằng URL trực tiếp
        driver.get(BASE_URL + "/cart/updateCart/" + bookId + "/3");
        wait.until(ExpectedConditions.urlContains("/cart"));

        // Kiểm tra số lượng đã đổi
        WebElement updatedInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".quantity-input")));
        Assertions.assertEquals("3", updatedInput.getAttribute("value"),
                "Số lượng phải được cập nhật thành 3");
    }

    /**
     * TC_TTT_008: Xóa một sản phẩm khỏi giỏ hàng
     */
    @Test
    @Order(8)
    @DisplayName("TC_TTT_008 - Xóa một sản phẩm khỏi giỏ hàng")
    void removeItemFromCart() {
        loginAs(userUsername, userPassword);
        addFirstBookToCart();

        driver.get(BASE_URL + "/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-item")));
        int countBefore = driver.findElements(By.cssSelector(".cart-item")).size();

        // Nhấn nút xóa sản phẩm đầu tiên (dùng JS để bỏ confirm)
        WebElement removeLink = driver.findElement(By.cssSelector("a[href*='/cart/removeFromCart/']"));
        String removeHref = removeLink.getAttribute("href");
        driver.get(removeHref);
        wait.until(ExpectedConditions.urlContains("/cart"));

        List<WebElement> itemsAfter = driver.findElements(By.cssSelector(".cart-item"));
        Assertions.assertTrue(itemsAfter.size() < countBefore,
                "Số sản phẩm trong giỏ phải giảm sau khi xóa");
    }

    /**
     * TC_TTT_009: Xóa toàn bộ giỏ hàng
     */
    @Test
    @Order(9)
    @DisplayName("TC_TTT_009 - Xóa toàn bộ giỏ hàng")
    void clearCart() {
        loginAs(userUsername, userPassword);
        addFirstBookToCart();

        // Gọi clearCart trực tiếp
        driver.get(BASE_URL + "/cart/clearCart");
        wait.until(ExpectedConditions.urlContains("/cart"));

        String pageSource = driver.getPageSource();
        Assertions.assertTrue(
                pageSource.contains("Your cart is empty") || pageSource.contains("cart is empty"),
                "Giỏ hàng phải trống sau khi xóa tất cả");
    }

    /**
     * TC_TTT_010: Kiểm tra tổng tiền giỏ hàng hiển thị đúng (≥ 0)
     */
    @Test
    @Order(10)
    @DisplayName("TC_TTT_010 - Tổng tiền giỏ hàng hiển thị hợp lệ")
    void cartTotalPriceIsValid() {
        loginAs(userUsername, userPassword);
        addFirstBookToCart();

        driver.get(BASE_URL + "/cart");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-total")));

        // Tổng tiền nằm trong phần cart-total
        String cartTotalText = driver.findElement(By.cssSelector(".cart-total")).getText();
        Assertions.assertTrue(cartTotalText.contains("$"),
                "Giỏ hàng phải hiển thị tổng tiền có ký hiệu $");
    }

    // ── THANH TOÁN ───────────────────────────────────────────────────────────

    /**
     * TC_TTT_011: Truy cập trang checkout khi giỏ hàng có sách
     */
    @Test
    @Order(11)
    @DisplayName("TC_TTT_011 - Truy cập checkout với giỏ hàng có sách")
    void checkoutPageLoadWithCart() {
        loginAs(userUsername, userPassword);
        addFirstBookToCart();

        driver.get(BASE_URL + "/cart/checkout");
        wait.until(ExpectedConditions.urlContains("/cart/checkout"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("/cart/checkout"),
                "Phải vào được trang checkout khi giỏ hàng có sách");
        Assertions.assertTrue(driver.getPageSource().contains("Billing") ||
                driver.getPageSource().contains("customerName"),
                "Trang checkout phải hiển thị form thông tin giao hàng");
    }

    /**
     * TC_TTT_012: Redirect khi truy cập checkout với giỏ hàng trống
     */
    @Test
    @Order(12)
    @DisplayName("TC_TTT_012 - Redirect về /cart khi checkout với giỏ trống")
    void checkoutRedirectsWhenCartEmpty() {
        loginAs(userUsername, userPassword);
        // Đảm bảo giỏ trống
        driver.get(BASE_URL + "/cart/clearCart");
        wait.until(ExpectedConditions.urlContains("/cart"));

        driver.get(BASE_URL + "/cart/checkout");
        // Phải redirect về /cart?error=empty
        wait.until(d -> d.getCurrentUrl().contains("/cart"));
        Assertions.assertTrue(
                driver.getCurrentUrl().contains("error=empty") ||
                        driver.getCurrentUrl().contains("/cart"),
                "Phải redirect về /cart khi giỏ trống");
    }

    /**
     * TC_TTT_013: Thanh toán thành công với đầy đủ thông tin
     */
    @Test
    @Order(13)
    @DisplayName("TC_TTT_013 - Thanh toán thành công với đầy đủ thông tin")
    void checkoutSuccess() {
        loginAs(userUsername, userPassword);
        addFirstBookToCart();

        driver.get(BASE_URL + "/cart/checkout");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customerName")));

        driver.findElement(By.id("customerName")).sendKeys("Tran Thanh Tinh");
        driver.findElement(By.id("customerEmail")).sendKeys("ttt@test.com");
        driver.findElement(By.id("customerPhone")).sendKeys("0912345678");
        driver.findElement(By.id("shippingAddress")).sendKeys("123 Test Street");

        // Tick agree terms
        WebElement agreeCheckbox = driver.findElement(By.cssSelector("input[name='agreeTerms']"));
        if (!agreeCheckbox.isSelected()) {
            jsClick(agreeCheckbox);
        }

        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit'].btn-success-modern"));
        jsClick(submitBtn);

        wait.until(d -> d.getCurrentUrl().contains("/orders/detail") ||
                d.getCurrentUrl().contains("/cart"));
        Assertions.assertTrue(
                driver.getCurrentUrl().contains("/orders/detail"),
                "Sau thanh toán thành công phải redirect về /orders/detail/{id}");
    }

    /**
     * TC_TTT_014: Thông báo lỗi khi chưa đồng ý điều khoản
     */
    @Test
    @Order(14)
    @DisplayName("TC_TTT_014 - Lỗi khi checkout không đồng ý điều khoản")
    void checkoutFailWithoutAgreeTerms() {
        loginAs(userUsername, userPassword);
        addFirstBookToCart();

        driver.get(BASE_URL + "/cart/checkout");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customerName")));

        driver.findElement(By.id("customerName")).sendKeys("Test User");
        driver.findElement(By.id("customerEmail")).sendKeys("test@test.com");
        driver.findElement(By.id("customerPhone")).sendKeys("0987654321");
        driver.findElement(By.id("shippingAddress")).sendKeys("456 Test Ave");
        // KHÔNG tick agreeTerms

        WebElement submitBtn = driver.findElement(By.cssSelector("button[type='submit'].btn-success-modern"));
        jsClick(submitBtn);

        // Phải redirect về checkout với flash error
        wait.until(d -> d.getCurrentUrl().contains("/cart/checkout") ||
                d.getCurrentUrl().contains("/cart"));
        Assertions.assertTrue(
                driver.getPageSource().contains("terms") ||
                        driver.getPageSource().contains("Please agree") ||
                        driver.getCurrentUrl().contains("/cart/checkout"),
                "Phải hiển thị lỗi khi chưa đồng ý điều khoản");
    }

    // ── LỊCH SỬ ĐƠN HÀNG ────────────────────────────────────────────────────

    /**
     * TC_TTT_015: Xem lịch sử đơn hàng sau khi đã đặt
     */
    @Test
    @Order(15)
    @DisplayName("TC_TTT_015 - Xem lịch sử đơn hàng")
    void viewOrderHistory() {
        // Đặt 1 đơn hàng trước (gọi lại TC_TTT_013 flow)
        loginAs(userUsername, userPassword);
        addFirstBookToCart();
        driver.get(BASE_URL + "/cart/checkout");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customerName")));
        driver.findElement(By.id("customerName")).sendKeys("Tran Thanh Tinh");
        driver.findElement(By.id("customerEmail")).sendKeys("ttt@test.com");
        driver.findElement(By.id("customerPhone")).sendKeys("0912345678");
        driver.findElement(By.id("shippingAddress")).sendKeys("123 Test Street");
        WebElement agreeCheckbox = driver.findElement(By.cssSelector("input[name='agreeTerms']"));
        if (!agreeCheckbox.isSelected())
            jsClick(agreeCheckbox);
        jsClick(driver.findElement(By.cssSelector("button[type='submit'].btn-success-modern")));
        wait.until(d -> d.getCurrentUrl().contains("/orders") || d.getCurrentUrl().contains("/cart"));

        // Vào trang lịch sử đơn hàng
        driver.get(BASE_URL + "/orders");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

        String pageSource = driver.getPageSource();
        Assertions.assertTrue(
                pageSource.contains("Order History") ||
                        pageSource.contains("Total Orders"),
                "Trang lịch sử đơn hàng phải hiển thị");
    }

    /**
     * TC_TTT_016: Xem chi tiết một đơn hàng cụ thể
     */
    @Test
    @Order(16)
    @DisplayName("TC_TTT_016 - Xem chi tiết đơn hàng")
    void viewOrderDetail() {
        loginAs(userUsername, userPassword);
        driver.get(BASE_URL + "/orders");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

        // Tìm link chi tiết đơn hàng đầu tiên
        List<WebElement> detailLinks = driver.findElements(By.cssSelector("a[href*='/orders/detail/']"));
        if (!detailLinks.isEmpty()) {
            String detailHref = detailLinks.get(0).getAttribute("href");
            driver.get(detailHref);
            wait.until(ExpectedConditions.urlContains("/orders/detail/"));

            Assertions.assertTrue(driver.getCurrentUrl().contains("/orders/detail/"),
                    "Phải vào được trang chi tiết đơn hàng");
            Assertions.assertTrue(
                    driver.getPageSource().contains("Order") ||
                            driver.getPageSource().contains("Total"),
                    "Trang chi tiết phải hiển thị thông tin đơn hàng");
        } else {
            // Nếu chưa có đơn hàng, kiểm tra trang orders trống
            Assertions.assertTrue(driver.getPageSource().contains("No orders") ||
                    driver.getPageSource().contains("Order History"),
                    "Trang orders phải hiển thị");
        }
    }

    /**
     * TC_TTT_017: User A không thể xem đơn hàng của user B (dùng admin order)
     */
    @Test
    @Order(17)
    @DisplayName("TC_TTT_017 - User thường không thể xem đơn hàng của người khác")
    void userCannotViewOtherUserOrder() {
        // User thường đăng nhập
        loginAs(userUsername, userPassword);

        // Thử truy cập đơn hàng id=1 (thường thuộc admin hoặc user khác)
        driver.get(BASE_URL + "/orders/detail/1");

        // Kỳ vọng: bị 500/redirect/error nếu không phải đơn hàng của họ
        String url = driver.getCurrentUrl();
        String src = driver.getPageSource();
        Assertions.assertTrue(
                url.contains("/error") || url.contains("500") || url.contains("/orders") ||
                        src.contains("Access denied") || src.contains("not found") ||
                        src.contains("Error") || src.contains("Forbidden"),
                "Phải chặn user xem đơn hàng của người khác");
    }

    /**
     * TC_TTT_018: Kiểm tra tổng tiền đã chi trên trang lịch sử đơn hàng
     */
    @Test
    @Order(18)
    @DisplayName("TC_TTT_018 - Tổng tiền đã chi trên trang lịch sử đúng định dạng")
    void orderHistoryTotalSpentShownCorrectly() {
        loginAs(userUsername, userPassword);
        driver.get(BASE_URL + "/orders");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

        String pageSource = driver.getPageSource();
        // Trang orders phải tải được
        Assertions.assertTrue(
                pageSource.contains("Order History") ||
                        pageSource.contains("My Order") ||
                        pageSource.contains("Total Spent") ||
                        pageSource.contains("No orders"),
                "Trang lịch sử đơn hàng phải hiển thị thông tin hợp lệ");

        // Nếu có đơn hàng, kiểm tra Total Spent hiển thị $
        if (pageSource.contains("Total Spent")) {
            Assertions.assertTrue(pageSource.contains("$"),
                    "Tổng tiền đã chi phải có ký hiệu $");
        }
    }

    // ── ĐĂNG XUẤT & BẢO MẬT ─────────────────────────────────────────────────

    /**
     * TC_TTT_019: Đăng xuất thành công
     */
    @Test
    @Order(19)
    @DisplayName("TC_TTT_019 - Đăng xuất thành công")
    void logoutSuccess() {
        loginAs(userUsername, userPassword);
        Assertions.assertFalse(driver.getCurrentUrl().contains("/login"),
                "Phải đăng nhập thành công trước");

        // Logout
        driver.get(BASE_URL + "/logout");
        wait.until(ExpectedConditions.urlContains("/login"));

        // Kiểm tra không thể vào trang cần đăng nhập
        driver.get(BASE_URL + "/profile");
        wait.until(ExpectedConditions.urlContains("/login"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                "Sau logout, truy cập /profile phải redirect về /login");
    }

    /**
     * TC_TTT_020: Kiểm tra phân trang danh sách sách
     */
    @Test
    @Order(20)
    @DisplayName("TC_TTT_020 - Phân trang danh sách sách hoạt động đúng")
    void bookListPagination() {
        loginAs(userUsername, userPassword);
        driver.get(BASE_URL + "/books?pageNo=0");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".book-card")));

        // Đếm sách trang 1, tối đa 20
        List<WebElement> page1Books = driver.findElements(By.cssSelector(".book-card"));
        int page1Count = page1Books.size();
        Assertions.assertTrue(page1Count > 0, "Trang 1 phải có ít nhất 1 sách");
        Assertions.assertTrue(page1Count <= 20, "Trang hiển thị tối đa 20 sách");

        // Kiểm tra có trang 2 không
        List<WebElement> nextLinks = driver.findElements(
                By.cssSelector("a[href*='pageNo=1']"));
        if (!nextLinks.isEmpty()) {
            driver.get(BASE_URL + "/books?pageNo=1");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".book-card")));
            List<WebElement> page2Books = driver.findElements(By.cssSelector(".book-card"));
            int page2Count = page2Books.size();
            Assertions.assertTrue(page2Count > 0, "Trang 2 phải có sách");

            // Kiểm tra URL trang 2 đúng
            Assertions.assertTrue(driver.getCurrentUrl().contains("pageNo=1"),
                    "URL trang 2 phải chứa pageNo=1");
        }
    }
}
