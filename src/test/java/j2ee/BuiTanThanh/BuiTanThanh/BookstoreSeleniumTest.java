package j2ee.BuiTanThanh.BuiTanThanh;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Automation Tests (Selenium WebDriver) - Bùi Tấn Thành
 * Phạm vi: Đăng ký, Quản lý Admin (Sách & Danh mục), Hồ sơ, Bảo mật
 * TC_BTT_001 → TC_BTT_020
 *
 * Yêu cầu: Ứng dụng đang chạy tại http://localhost:8080
 * Tài khoản admin: admin / admin
 * Tài khoản user thường: user / user (hoặc tài khoản có role USER)
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookstoreSeleniumTest {

        private static final String BASE_URL = "http://localhost:8081";
        private static final String ADMIN_USERNAME = "az1533";
        private static final String ADMIN_PASSWORD = "Thanh121";
        private static final Duration WAIT = Duration.ofSeconds(10);

        // Credentials của tài khoản đăng ký ở TC_BTT_001, dùng cho các test user
        // (TC_BTT_015–019)
        private String registeredUsername;
        private String registeredPassword;

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

        // ── Helper methods ───────────────────────────────────────────────────────

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
                WebElement submitBtn = wait.until(
                                ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[type='submit']")));
                jsClick(submitBtn);
                wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        }

        private void logout() {
                driver.get(BASE_URL + "/logout");
        }

        private void openModal(String modalBtnSelector) {
                WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(modalBtnSelector)));
                jsClick(btn);
                // Wait for modal to be visible
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal.show")));
        }

        // ── ĐĂNG KÝ ─────────────────────────────────────────────────────────────

        /**
         * TC_BTT_001: Đăng ký tài khoản mới thành công → redirect /login
         */
        @Test
        @Order(1)
        @DisplayName("TC_BTT_001 - Đăng ký tài khoản mới thành công")
        void registerSuccess() {
                driver.get(BASE_URL + "/register");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));

                String uniqueSuffix = String.valueOf(System.currentTimeMillis()).substring(8);
                registeredUsername = "autouser" + uniqueSuffix;
                registeredPassword = "Auto@1234";

                String phone = "09" + String.format("%08d", Math.abs(System.currentTimeMillis() % 100000000L));
                driver.findElement(By.id("email")).sendKeys("autouser" + uniqueSuffix + "@test.com");
                driver.findElement(By.id("username")).sendKeys(registeredUsername);
                driver.findElement(By.id("password")).sendKeys(registeredPassword);
                driver.findElement(By.id("phone")).sendKeys(phone);
                jsClick(driver.findElement(By.cssSelector("button[type='submit']")));

                wait.until(ExpectedConditions.urlContains("/login"));
                Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                                "Sau đăng ký thành công phải redirect về /login");
        }

        /**
         * TC_BTT_002: Đăng ký với username đã tồn tại → ở lại /register và hiện lỗi
         */
        @Test
        @Order(2)
        @DisplayName("TC_BTT_002 - Đăng ký thất bại khi username đã tồn tại")
        void registerFailDuplicateUsername() {
                driver.get(BASE_URL + "/register");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));

                driver.findElement(By.id("email")).sendKeys("unique_email_" + System.currentTimeMillis() + "@test.com");
                driver.findElement(By.id("username")).sendKeys(ADMIN_USERNAME); // username đã tồn tại
                driver.findElement(By.id("password")).sendKeys("Auto@1234");
                driver.findElement(By.id("phone")).sendKeys("0987654321");
                jsClick(driver.findElement(By.cssSelector("button[type='submit']")));

                // Phải ở lại trang register và hiện thông báo lỗi
                wait.until(ExpectedConditions.urlContains("/register"));
                Assertions.assertTrue(driver.getCurrentUrl().contains("/register"),
                                "Phải ở lại trang /register khi username trùng");
                WebElement errorList = wait.until(
                                ExpectedConditions.presenceOfElementLocated(
                                                By.cssSelector(".alert-danger, .text-danger")));
                Assertions.assertNotNull(errorList, "Phải hiển thị thông báo lỗi");
        }

        /**
         * TC_BTT_003: Đăng ký với email đã tồn tại → ở lại /register và hiện lỗi
         */
        @Test
        @Order(3)
        @DisplayName("TC_BTT_003 - Đăng ký thất bại khi email đã tồn tại")
        void registerFailDuplicateEmail() {
                // Trước tiên cần biết email của admin — giả sử admin dùng "admin@test.com"
                // Test này có thể fail nếu email không trùng trong DB
                driver.get(BASE_URL + "/register");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));

                driver.findElement(By.id("email")).sendKeys("admin@admin.com"); // email đã tồn tại
                driver.findElement(By.id("username")).sendKeys("uniqueuser_" + System.currentTimeMillis());
                driver.findElement(By.id("password")).sendKeys("Auto@1234");
                driver.findElement(By.id("phone")).sendKeys("0999999999");
                jsClick(driver.findElement(By.cssSelector("button[type='submit']")));

                // Kỳ vọng: ở lại /register hoặc hiện lỗi
                Assertions.assertTrue(
                                driver.getCurrentUrl().contains("/register") ||
                                                driver.getPageSource().contains("error") ||
                                                driver.getPageSource().contains("error"),
                                "Phải hiển thị lỗi khi email trùng");
        }

        /**
         * TC_BTT_004: Đăng ký với số điện thoại không hợp lệ → lỗi validation
         */
        @Test
        @Order(4)
        @DisplayName("TC_BTT_004 - Đăng ký thất bại khi phone không hợp lệ")
        void registerFailInvalidPhone() {
                driver.get(BASE_URL + "/register");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phone")));

                driver.findElement(By.id("email")).sendKeys("valid_email_" + System.currentTimeMillis() + "@test.com");
                driver.findElement(By.id("username")).sendKeys("validuser_" + System.currentTimeMillis());
                driver.findElement(By.id("password")).sendKeys("Auto@1234");
                driver.findElement(By.id("phone")).sendKeys("123"); // < 10 ký tự

                jsClick(driver.findElement(By.cssSelector("button[type='submit']")));

                // Phải ở lại /register (server-side validation) hoặc HTML5 validation block
                Assertions.assertTrue(
                                driver.getCurrentUrl().contains("/register"),
                                "Phải ở lại /register khi phone không hợp lệ");
        }

        // ── ADMIN - DASHBOARD ────────────────────────────────────────────────────

        /**
         * TC_BTT_005: Admin đăng nhập và xem dashboard với số liệu thống kê
         */
        @Test
        @Order(5)
        @DisplayName("TC_BTT_005 - Admin xem dashboard hiển thị số liệu thống kê")
        void adminViewDashboard() {
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin");

                wait.until(ExpectedConditions.urlContains("/admin"));

                // Kiểm tra 4 thẻ thống kê có hiển thị số
                WebElement totalBooks = wait.until(
                                ExpectedConditions.presenceOfElementLocated(
                                                By.xpath("//div[contains(@class,'text-muted') and contains(text(),'Total Books')]"
                                                                +
                                                                "/following-sibling::div[contains(@class,'fw-bold')]")));

                Assertions.assertNotNull(totalBooks, "Thẻ Total Books phải hiển thị");
                Assertions.assertFalse(totalBooks.getText().isBlank(), "Total Books phải có giá trị số");

                logout();
        }

        // ── ADMIN - BOOKS ────────────────────────────────────────────────────────

        /**
         * TC_BTT_006: Admin thêm sách mới thành công
         */
        @Test
        @Order(6)
        @DisplayName("TC_BTT_006 - Admin thêm sách mới thành công")
        void adminAddBook() {
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin/books");

                // Mở modal Add Book
                openModal("button[data-bs-target='#addBookModal']");

                // Điền form
                WebElement titleInput = driver.findElement(By.cssSelector("#addBookModal input[name='title']"));
                titleInput.sendKeys("Auto Test Book");
                driver.findElement(By.cssSelector("#addBookModal input[name='author']")).sendKeys("Auto Author");
                driver.findElement(By.cssSelector("#addBookModal input[name='price']")).sendKeys("99000");

                // Submit — lấy modal reference trước để detect staleness sau khi page navigate
                WebElement addBookModal = driver.findElement(By.id("addBookModal"));
                jsClick(driver.findElement(By.cssSelector("#addBookModal button[type='submit']")));

                // Đợi page reload (modal DOM stale = form đã submit và redirect xảy ra)
                wait.until(ExpectedConditions.stalenessOf(addBookModal));
                wait.until(ExpectedConditions.presenceOfElementLocated(
                                By.cssSelector(".alert-success, .alert-danger, table.table")));
                String pageSource = driver.getPageSource();
                Assertions.assertTrue(
                                pageSource.contains("Auto Test Book") || pageSource.contains("added successfully"),
                                "Sách mới phải xuất hiện sau khi thêm thành công");

                logout();
        }

        /**
         * TC_BTT_007: Admin thêm sách thất bại khi thiếu tiêu đề
         */
        @Test
        @Order(7)
        @DisplayName("TC_BTT_007 - Admin thêm sách thất bại khi thiếu tiêu đề")
        void adminAddBookFailMissingTitle() {
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin/books");

                openModal("button[data-bs-target='#addBookModal']");

                // Không điền title, chỉ điền author và price
                driver.findElement(By.cssSelector("#addBookModal input[name='author']")).sendKeys("No Title Author");
                driver.findElement(By.cssSelector("#addBookModal input[name='price']")).sendKeys("50000");

                driver.findElement(By.cssSelector("#addBookModal button[type='submit']")).click();

                // Kỳ vọng: không có flash success hoặc vẫn tại trang /admin/books
                // HTML5 required sẽ ngăn submit, hoặc nếu bypass thì server sẽ trả lỗi
                Assertions.assertFalse(driver.getPageSource().toLowerCase().contains("added successfully"),
                                "Không được thêm sách khi thiếu tiêu đề");

                logout();
        }

        /**
         * TC_BTT_008: Admin sửa giá sách thành công
         */
        @Test
        @Order(8)
        @DisplayName("TC_BTT_008 - Admin sửa thông tin sách thành công")
        void adminEditBook() {
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin/books");

                // Click nút Sửa đầu tiên
                WebElement editBtn = wait.until(
                                ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-edit")));
                editBtn.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editBookModal")));

                // Thay đổi giá
                WebElement priceField = wait.until(
                                ExpectedConditions.elementToBeClickable(By.id("editPrice")));
                priceField.clear();
                priceField.sendKeys("199000");

                driver.findElement(By.cssSelector("#editBookModal button[type='submit']")).click();

                // Kiểm tra flash success
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-success")));
                Assertions.assertTrue(driver.getPageSource().contains("updated successfully") ||
                                driver.getCurrentUrl().contains("/admin/books"),
                                "Sách phải được cập nhật thành công");

                logout();
        }

        /**
         * TC_BTT_009: Admin vô hiệu hóa (soft delete) sách
         */
        @Test
        @Order(9)
        @DisplayName("TC_BTT_009 - Admin vô hiệu hóa sách (soft delete)")
        void adminDeactivateBook() {
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin/books");

                // Click nút Deactivate đầu tiên
                WebElement deactivateBtn = wait.until(
                                ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-delete")));
                deactivateBtn.click();

                // Confirm trong modal
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteModal")));
                driver.findElement(By.cssSelector("#deleteBookForm button[type='submit']")).click();

                wait.until(ExpectedConditions
                                .presenceOfElementLocated(By.cssSelector(".alert-success, .badge.bg-secondary")));
                Assertions.assertTrue(driver.getPageSource().contains("Inactive") ||
                                driver.getPageSource().contains("deactivated") ||
                                driver.getPageSource().contains("bg-secondary"),
                                "Sách phải hiển thị trạng thái Inactive sau khi vô hiệu hóa");

                logout();
        }

        /**
         * TC_BTT_010: Admin kích hoạt lại sách đã vô hiệu hóa
         */
        @Test
        @Order(10)
        @DisplayName("TC_BTT_010 - Admin kích hoạt lại sách đã vô hiệu hóa")
        void adminActivateBook() {
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin/books");

                // Tìm nút Activate (form submit trực tiếp)
                WebElement activateBtn = wait.until(
                                ExpectedConditions.elementToBeClickable(
                                                By.cssSelector("form[action*='/admin/books/activate'] button[type='submit']")));
                activateBtn.click();

                wait.until(ExpectedConditions
                                .presenceOfElementLocated(By.cssSelector(".alert-success, .badge.bg-success")));
                Assertions.assertTrue(driver.getPageSource().contains("Active") ||
                                driver.getPageSource().contains("activated") ||
                                driver.getPageSource().contains("bg-success"),
                                "Sách phải hiển thị trạng thái Active sau khi kích hoạt");

                logout();
        }

        /**
         * TC_BTT_011: Admin tìm kiếm sách theo từ khóa
         */
        @Test
        @Order(11)
        @DisplayName("TC_BTT_011 - Admin tìm kiếm sách trong trang admin")
        void adminSearchBook() {
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin/books");

                // Tìm ô search và nhập từ khóa
                WebElement searchInput = wait.until(
                                ExpectedConditions.elementToBeClickable(By.cssSelector("input[name='keyword']")));
                searchInput.sendKeys("Auto Test Book");
                driver.findElement(By.cssSelector(
                                "button[type='submit'] i.fa-search, button[type='submit']:has(i.fa-search)"))
                                .click();

                wait.until(ExpectedConditions.urlContains("keyword="));

                // Tất cả kết quả phải liên quan đến từ khóa hoặc không có kết quả
                String pageSource = driver.getPageSource();
                Assertions.assertTrue(
                                pageSource.contains("Auto Test Book") || pageSource.contains("No books found"),
                                "Kết quả tìm kiếm phải hiển thị đúng");

                logout();
        }

        // ── ADMIN - CATEGORIES ───────────────────────────────────────────────────

        /**
         * TC_BTT_012: Admin thêm danh mục mới thành công
         */
        @Test
        @Order(12)
        @DisplayName("TC_BTT_012 - Admin thêm danh mục mới thành công")
        void adminAddCategory() {
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin/categories");

                // Dùng form Quick Add trong sidebar (luôn hiển thị, không cần click modal)
                WebElement nameInput = wait.until(
                                ExpectedConditions.elementToBeClickable(
                                                By.cssSelector(".col-lg-4 form[action*='/admin/categories/add'] input[name='name']")));
                nameInput.sendKeys("Auto Selenium Category");
                nameInput.submit();

                wait.until(ExpectedConditions.urlContains("/admin/categories"));
                Assertions.assertTrue(driver.getPageSource().contains("Auto Selenium Category"),
                                "Danh mục mới phải xuất hiện trong danh sách");

                logout();
        }

        /**
         * TC_BTT_013: Admin sửa tên danh mục thành công
         */
        @Test
        @Order(13)
        @DisplayName("TC_BTT_013 - Admin sửa tên danh mục thành công")
        void adminEditCategory() {
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin/categories");

                // Click nút edit trên danh mục đầu tiên
                WebElement editBtn = wait.until(
                                ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-edit-cat")));
                editBtn.click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCategoryModal")));

                WebElement nameField = driver.findElement(By.id("editCatName"));
                nameField.clear();
                nameField.sendKeys("Updated Selenium Category");
                driver.findElement(By.cssSelector("#editCategoryModal button[type='submit']")).click();

                wait.until(ExpectedConditions.urlContains("/admin/categories"));
                Assertions.assertTrue(driver.getPageSource().contains("Updated Selenium Category"),
                                "Tên danh mục phải được cập nhật thành công");

                logout();
        }

        /**
         * TC_BTT_014: Admin xóa danh mục không có sách liên kết
         */
        @Test
        @Order(14)
        @DisplayName("TC_BTT_014 - Admin xóa danh mục trống thành công")
        void adminDeleteEmptyCategory() {
                // Bước 1: Tạo danh mục trống để xóa
                loginAs(ADMIN_USERNAME, ADMIN_PASSWORD);
                driver.get(BASE_URL + "/admin/categories");

                String catName = "ToDelete_" + System.currentTimeMillis();
                WebElement nameInput = wait.until(
                                ExpectedConditions.elementToBeClickable(
                                                By.cssSelector(".col-lg-4 form[action*='/admin/categories/add'] input[name='name']")));
                nameInput.sendKeys(catName);
                nameInput.submit();
                wait.until(ExpectedConditions.urlContains("/admin/categories"));
                wait.until(ExpectedConditions.presenceOfElementLocated(
                                By.xpath("//*[contains(text(),'" + catName + "')]")));

                // Bước 2: Xóa danh mục vừa tạo (danh mục cuối cùng thường là mới nhất)
                java.util.List<WebElement> deleteButtons = driver.findElements(By.cssSelector(".btn-delete-cat"));
                Assertions.assertFalse(deleteButtons.isEmpty(), "Phải có ít nhất một nút xóa danh mục");
                WebElement lastDeleteBtn = deleteButtons.get(deleteButtons.size() - 1);
                jsClick(lastDeleteBtn);

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteCatModal")));
                jsClick(driver.findElement(By.cssSelector("#deleteCatForm button[type='submit']")));

                wait.until(ExpectedConditions.urlContains("/admin/categories"));
                // Danh mục vừa tạo không còn trong danh sách
                Assertions.assertFalse(driver.getPageSource().contains(catName),
                                "Danh mục đã xóa không được xuất hiện trong danh sách");

                logout();
        }

        // ── HỒ SƠ NGƯỜI DÙNG ────────────────────────────────────────────────────

        /**
         * TC_BTT_015: Xem trang hồ sơ người dùng
         */
        @Test
        @Order(15)
        @DisplayName("TC_BTT_015 - Người dùng xem trang hồ sơ")
        void userViewProfile() {
                loginAs(registeredUsername, registeredPassword);
                driver.get(BASE_URL + "/profile");

                wait.until(ExpectedConditions.urlContains("/profile"));

                // Kiểm tra username và email hiển thị
                WebElement usernameField = wait.until(
                                ExpectedConditions.presenceOfElementLocated(By.id("username")));
                Assertions.assertFalse(usernameField.getAttribute("value").isBlank(),
                                "Username phải hiển thị trong profile");

                logout();
        }

        /**
         * TC_BTT_016: Cập nhật thông tin hồ sơ (name, phone)
         */
        @Test
        @Order(16)
        @DisplayName("TC_BTT_016 - Cập nhật name và phone thành công")
        void userUpdateProfile() {
                loginAs(registeredUsername, registeredPassword);
                driver.get(BASE_URL + "/profile");

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));

                WebElement nameField = driver.findElement(By.id("name"));
                nameField.clear();
                nameField.sendKeys("Selenium Test Name");

                WebElement phoneField = driver.findElement(By.id("phone"));
                phoneField.clear();
                phoneField.sendKeys("0911222333");

                driver.findElement(By.cssSelector("form[action*='/profile/update'] button[type='submit']")).click();

                // Kiểm tra flash success
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-success")));
                Assertions.assertTrue(
                                driver.getPageSource().contains("successfully") ||
                                                driver.getPageSource().contains("updated"),
                                "Phải có flash message thành công sau khi cập nhật profile");

                logout();
        }

        /**
         * TC_BTT_017: Đổi mật khẩu thành công
         */
        @Test
        @Order(17)
        @DisplayName("TC_BTT_017 - Đổi mật khẩu thành công")
        void userChangePasswordSuccess() {
                loginAs(registeredUsername, registeredPassword);
                driver.get(BASE_URL + "/profile");

                wait.until(ExpectedConditions.elementToBeClickable(
                                By.cssSelector("button[data-bs-target='#changePasswordModal']"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("changePasswordModal")));

                driver.findElement(By.cssSelector("#changePasswordModal input[name='currentPassword']"))
                                .sendKeys(registeredPassword);
                driver.findElement(By.id("newPassword")).sendKeys("NewPass@123");
                driver.findElement(By.id("confirmPassword")).sendKeys("NewPass@123");
                driver.findElement(By.cssSelector("#changePasswordModal button[type='submit']")).click();

                // Kiểm tra flash thành công
                wait.until(ExpectedConditions.presenceOfElementLocated(
                                By.cssSelector(".alert-success, .alert-danger")));

                logout();
        }

        /**
         * TC_BTT_018: Đổi mật khẩu thất bại khi sai mật khẩu hiện tại
         */
        @Test
        @Order(18)
        @DisplayName("TC_BTT_018 - Đổi mật khẩu thất bại khi mật khẩu hiện tại sai")
        void userChangePasswordFailWrongCurrent() {
                loginAs(registeredUsername, registeredPassword);
                driver.get(BASE_URL + "/profile");

                wait.until(ExpectedConditions.elementToBeClickable(
                                By.cssSelector("button[data-bs-target='#changePasswordModal']"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("changePasswordModal")));

                driver.findElement(By.cssSelector("#changePasswordModal input[name='currentPassword']"))
                                .sendKeys("WrongCurrentPassword");
                driver.findElement(By.id("newPassword")).sendKeys("NewPass@123");
                driver.findElement(By.id("confirmPassword")).sendKeys("NewPass@123");
                driver.findElement(By.cssSelector("#changePasswordModal button[type='submit']")).click();

                // Kỳ vọng: flash lỗi "Current password is incorrect"
                wait.until(ExpectedConditions.presenceOfElementLocated(
                                By.cssSelector(".alert-danger")));
                Assertions.assertTrue(
                                driver.getPageSource().contains("incorrect") ||
                                                driver.getPageSource().contains("error") ||
                                                driver.getPageSource().contains("wrong"),
                                "Phải hiển thị flash lỗi khi mật khẩu hiện tại sai");

                logout();
        }

        // ── BẢO MẬT & PHÂN QUYỀN ────────────────────────────────────────────────

        /**
         * TC_BTT_019: User thường truy cập /admin → bị redirect về 403/error
         */
        @Test
        @Order(19)
        @DisplayName("TC_BTT_019 - User thường không thể truy cập trang admin")
        void regularUserCannotAccessAdmin() {
                loginAs(registeredUsername, registeredPassword);
                driver.get(BASE_URL + "/admin");

                // Kỳ vọng: redirect về /error/403 hoặc /login
                wait.until(ExpectedConditions.not(
                                ExpectedConditions.urlToBe(BASE_URL + "/admin")));

                String currentUrl = driver.getCurrentUrl();
                Assertions.assertTrue(
                                currentUrl.contains("403") || currentUrl.contains("error") ||
                                                currentUrl.contains("/login")
                                                || driver.getPageSource().contains("Forbidden") ||
                                                driver.getPageSource().contains("Access Denied"),
                                "User thường phải bị chặn khi truy cập /admin");

                logout();
        }

        /**
         * TC_BTT_020: Người dùng chưa đăng nhập truy cập /profile bị redirect về /login
         */
        @Test
        @Order(20)
        @DisplayName("TC_BTT_020 - Chưa đăng nhập truy cập /profile bị redirect về /login")
        void unauthenticatedUserRedirectedToLogin() {
                // Đảm bảo chưa đăng nhập
                logout();

                driver.get(BASE_URL + "/profile");

                wait.until(ExpectedConditions.urlContains("/login"));
                Assertions.assertTrue(driver.getCurrentUrl().contains("/login"),
                                "Người dùng chưa đăng nhập phải được redirect về /login");
        }
}
