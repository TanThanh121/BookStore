# Test Cases - Trần Thanh Tịnh
**Loại kiểm thử:** Kiểm thử tự động (Automation Testing)
**Công cụ:** Selenium WebDriver + JUnit 5
**Số lượng:** 20 Test Cases
**Phạm vi:** Giỏ hàng, Thanh toán, Lịch sử đơn hàng, Tìm kiếm & Lọc sách

---

## Chức năng: Đăng nhập (Tự động)

### Test Case TC_TTT_001
- **Summary:** Tự động đăng nhập thành công với thông tin hợp lệ
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Mở trình duyệt và truy cập `http://localhost:8080/login`.
  - Bước 2: Tìm và điền vào trường `username` giá trị `"testuser"`.
  - Bước 3: Tìm và điền vào trường `password` giá trị `"Test@1234"`.
  - Bước 4: Nhấn nút **Đăng nhập** (`button[type='submit']`).
  - Bước 5: Chờ trang tải xong và lấy URL hiện tại.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** URL chuyển thành `http://localhost:8080/` hoặc trang chủ, không chứa `/login`.

---

### Test Case TC_TTT_002
- **Summary:** Tự động đăng nhập thất bại khi nhập sai mật khẩu
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Mở `http://localhost:8080/login`.
  - Bước 2: Nhập `username = "testuser"`, `password = "WrongPass"`.
  - Bước 3: Nhấn nút **Đăng nhập**.
  - Bước 4: Kiểm tra thông báo lỗi hiển thị trên trang.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trang `/login` hiển thị thông báo lỗi xác thực, URL vẫn là `/login`.

---

## Chức năng: Tìm kiếm & Lọc sách (Tự động)

### Test Case TC_TTT_003
- **Summary:** Tự động tìm kiếm sách với từ khóa hợp lệ
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Truy cập `http://localhost:8080/books`.
  - Bước 2: Tìm ô tìm kiếm và nhập `"Java"`.
  - Bước 3: Nhấn nút tìm kiếm hoặc Enter.
  - Bước 4: Đếm số lượng thẻ sách hiển thị trong kết quả.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trang hiển thị ít nhất 1 kết quả có chứa từ "Java" trong tên sách hoặc tác giả.

---

### Test Case TC_TTT_004
- **Summary:** Tự động lọc sách theo danh mục
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Truy cập `http://localhost:8080/books`.
  - Bước 2: Chọn một danh mục từ dropdown lọc.
  - Bước 3: Chờ trang tải lại.
  - Bước 4: Kiểm tra tất cả thẻ sách hiển thị đều thuộc danh mục đã chọn.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Chỉ hiển thị sách thuộc danh mục đã chọn trong URL `?category={id}`.

---

### Test Case TC_TTT_005
- **Summary:** Tự động sắp xếp sách theo giá tăng dần
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Truy cập `http://localhost:8080/books`.
  - Bước 2: Chọn tùy chọn sắp xếp `"Giá tăng dần"` (`price_asc`).
  - Bước 3: Lấy danh sách giá hiển thị trên trang.
  - Bước 4: Kiểm tra giá từ phần tử đầu ≤ giá phần tử cuối.
- **Độ ưu tiên:** P3 (Medium)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Giá sách được hiển thị theo thứ tự tăng dần.

---

## Chức năng: Giỏ hàng (Tự động)

### Test Case TC_TTT_006
- **Summary:** Tự động thêm sách vào giỏ hàng từ trang danh sách
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập thành công.
  - Bước 2: Truy cập `/books`, nhấn vào sách đầu tiên.
  - Bước 3: Nhấn nút **Thêm vào giỏ hàng**.
  - Bước 4: Truy cập `/cart` và kiểm tra nội dung.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Giỏ hàng hiển thị đúng sản phẩm vừa thêm với số lượng = 1.

---

### Test Case TC_TTT_007
- **Summary:** Tự động cập nhật số lượng sách trong giỏ hàng
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Có ít nhất 1 sách trong giỏ hàng.
  - Bước 2: Truy cập `/cart`.
  - Bước 3: Tìm input số lượng của sản phẩm đầu tiên, thay đổi thành `3`.
  - Bước 4: Nhấn nút cập nhật (hoặc nhấn link `/cart/updateCart/{id}/3`).
  - Bước 5: Kiểm tra tổng tiền trên trang.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Số lượng hiển thị = 3, tổng tiền cập nhật đúng theo công thức `giá × 3`.

---

### Test Case TC_TTT_008
- **Summary:** Tự động xóa một sản phẩm khỏi giỏ hàng
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Có ít nhất 2 sách trong giỏ hàng.
  - Bước 2: Truy cập `/cart`.
  - Bước 3: Đếm số sản phẩm hiện tại.
  - Bước 4: Nhấn nút xóa sản phẩm đầu tiên.
  - Bước 5: Đếm lại số sản phẩm.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Số lượng sản phẩm giảm đi 1, tổng tiền cập nhật lại.

---

### Test Case TC_TTT_009
- **Summary:** Tự động xóa toàn bộ giỏ hàng
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Có sách trong giỏ hàng.
  - Bước 2: Truy cập `/cart`.
  - Bước 3: Nhấn nút **Xóa tất cả** (`/cart/clearCart`).
  - Bước 4: Kiểm tra nội dung trang.
- **Độ ưu tiên:** P3 (Medium)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trang giỏ hàng hiển thị "giỏ hàng trống", tổng tiền = 0.

---

### Test Case TC_TTT_010
- **Summary:** Tự động kiểm tra tổng tiền trong giỏ hàng tính đúng
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Thêm sách A (giá 50.000) với số lượng 2 vào giỏ.
  - Bước 2: Thêm sách B (giá 30.000) với số lượng 1 vào giỏ.
  - Bước 3: Truy cập `/cart` và đọc giá trị tổng tiền.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Tổng tiền hiển thị = `130.000 VNĐ`.

---

## Chức năng: Thanh toán (Tự động)

### Test Case TC_TTT_011
- **Summary:** Tự động truy cập trang checkout khi giỏ hàng có sách
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập & có sách trong giỏ.
  - Bước 2: Truy cập `/cart/checkout`.
  - Bước 3: Kiểm tra form thanh toán hiển thị.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trang checkout hiển thị form nhập thông tin giao hàng và danh sách sản phẩm.

---

### Test Case TC_TTT_012
- **Summary:** Tự động kiểm tra redirect khi truy cập checkout với giỏ hàng trống
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập & giỏ hàng đang trống.
  - Bước 2: Truy cập trực tiếp `http://localhost:8080/cart/checkout`.
  - Bước 3: Kiểm tra URL sau khi trang tải xong.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** URL chuyển thành `/cart?error=empty`.

---

### Test Case TC_TTT_013
- **Summary:** Tự động thanh toán thành công với đầy đủ thông tin
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Có sách trong giỏ, truy cập `/cart/checkout`.
  - Bước 2: Điền `customerName = "Test User"`, `customerEmail = "test@test.com"`, `customerPhone = "0123456789"`, `shippingAddress = "123 Test St"`.
  - Bước 3: Tích chọn ô `agreeTerms`.
  - Bước 4: Nhấn nút **Đặt hàng**.
  - Bước 5: Kiểm tra URL sau khi trang chuyển hướng.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** URL chuyển thành `/orders/detail/{id}`, hiển thị thông báo đặt hàng thành công.

---

### Test Case TC_TTT_014
- **Summary:** Tự động kiểm tra thông báo lỗi khi chưa đồng ý điều khoản
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Có sách trong giỏ, truy cập `/cart/checkout`.
  - Bước 2: Điền đầy đủ thông tin nhưng **không** tích `agreeTerms`.
  - Bước 3: Nhấn **Đặt hàng**.
  - Bước 4: Kiểm tra trang sau khi submit.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trang `/cart/checkout` hiển thị thông báo "Please agree to terms and conditions".

---

## Chức năng: Lịch sử đơn hàng (Tự động)

### Test Case TC_TTT_015
- **Summary:** Tự động xem lịch sử đơn hàng sau khi đặt hàng
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đã đặt ít nhất 1 đơn hàng thành công.
  - Bước 2: Truy cập `http://localhost:8080/orders`.
  - Bước 3: Kiểm tra danh sách đơn hàng.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trang hiển thị ít nhất 1 đơn hàng với thông tin ID, tổng tiền, số lượng sách.

---

### Test Case TC_TTT_016
- **Summary:** Tự động xem chi tiết một đơn hàng cụ thể
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Truy cập `/orders`.
  - Bước 2: Nhấn vào nút **Xem chi tiết** của đơn hàng đầu tiên.
  - Bước 3: Kiểm tra trang chi tiết.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trang `/orders/detail/{id}` hiển thị đầy đủ danh sách sách, số lượng và tổng tiền.

---

### Test Case TC_TTT_017
- **Summary:** Tự động kiểm tra user không thể xem đơn hàng của người khác
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập bằng `userA`.
  - Bước 2: Tìm id của đơn hàng thuộc `userB`.
  - Bước 3: Truy cập `/orders/detail/{id_of_userB_order}`.
  - Bước 4: Kiểm tra phản hồi của hệ thống.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hệ thống từ chối truy cập (500 hoặc redirect), không hiển thị đơn hàng của người khác.

---

### Test Case TC_TTT_018
- **Summary:** Tự động kiểm tra tổng tiền đã chi trên trang lịch sử đơn hàng
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập user đã có đơn hàng.
  - Bước 2: Truy cập `/orders`.
  - Bước 3: Đọc giá trị `totalSpent` hiển thị trên trang.
  - Bước 4: So sánh với tổng tiền từng đơn hàng.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Giá trị `totalSpent` bằng tổng tiền của tất cả các đơn hàng.

---

### Test Case TC_TTT_019
- **Summary:** Tự động kiểm tra đăng xuất thành công
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đang ở trạng thái đã đăng nhập.
  - Bước 2: Nhấn nút **Đăng xuất** trên thanh điều hướng.
  - Bước 3: Kiểm tra URL và trạng thái trang.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** URL chuyển về `/login`, session bị hủy, không thể truy cập trang cần đăng nhập.

---

### Test Case TC_TTT_020
- **Summary:** Tự động kiểm tra phân trang danh sách sách
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Truy cập `http://localhost:8080/books?pageNo=0`.
  - Bước 2: Đếm số sách hiển thị trên trang.
  - Bước 3: Nhấn sang trang 2 (`?pageNo=1`) nếu có.
  - Bước 4: Kiểm tra nội dung trang 2 khác trang 1.
- **Độ ưu tiên:** P3 (Medium)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trang 1 hiển thị tối đa 20 sách; trang 2 hiển thị sách tiếp theo, không trùng với trang 1.

---

## Test Report

| TC ID | Test Case | Priority | Type | Pre-condition | Test Data | Expected Result | Test Script Design |
|-------|-----------|----------|------|---------------|-----------|-----------------|-------------------|
| TC_TTT_001 | Xác minh đăng nhập thành công khi nhập đúng username và password hợp lệ | P1 | Positive | Tài khoản user tồn tại trong hệ thống | username và password hợp lệ | URL không chứa /login, chuyển về trang chủ | Script: mở `/login` → điền credentials → click đăng nhập → verify URL != /login |
| TC_TTT_002 | Xác minh đăng nhập bị từ chối khi nhập sai mật khẩu | P1 | Negative | Tài khoản user tồn tại trong hệ thống | username đúng, password sai | Trang /login hiển thị thông báo lỗi xác thực | Script: mở `/login` → nhập sai password → click đăng nhập → verify URL contains /login & error |
| TC_TTT_003 | Xác minh tìm kiếm sách hiển thị kết quả phù hợp khi nhập từ khóa hợp lệ | P2 | Positive | Đã đăng nhập, có sách trong hệ thống | keyword="Sài Gòn" | Trang hiển thị kết quả tìm kiếm hoặc thông báo không tìm thấy | Script: login → `/books` → nhập keyword → click search → verify URL contains keyword= |
| TC_TTT_004 | Xác minh lọc sách theo danh mục hiển thị đúng URL khi click link danh mục | P2 | Positive | Đã đăng nhập, có ít nhất 1 danh mục với sách | Danh mục đầu tiên trong danh sách | URL chứa tham số category= sau khi lọc | Script: login → `/books` → click link category → verify URL contains category= |
| TC_TTT_005 | Xác minh sắp xếp sách theo giá tăng dần hiển thị đúng thứ tự khi chọn price_asc | P3 | Positive | Đã đăng nhập, có ≥2 sách trong hệ thống | sortBy=price_asc | Giá sách đầu tiên ≤ giá sách cuối cùng trên trang | Script: login → `/books?sortBy=price_asc` → lấy giá đầu & cuối → assert firstPrice ≤ lastPrice |
| TC_TTT_006 | Xác minh thêm sách vào giỏ hàng thành công khi nhấn nút thêm | P1 | Positive | Đã đăng nhập, có sách trong hệ thống | Sách đầu tiên trong danh sách | Sách xuất hiện trong giỏ hàng tại /cart | Script: login → `/books` → click thêm vào giỏ (form.submit) → `/cart` → verify .cart-item |
| TC_TTT_007 | Xác minh cập nhật số lượng sách trong giỏ thành công khi gọi updateCart | P2 | Positive | Đã đăng nhập, có sách trong giỏ hàng | bookId hợp lệ, quantity=3 | Số lượng sách trong giỏ được cập nhật thành 3 | Script: login → addToCart → `/cart/updateCart/{id}/3` → verify quantity input == 3 |
| TC_TTT_008 | Xác minh xóa một sản phẩm khỏi giỏ thành công khi nhấn nút xóa | P2 | Positive | Đã đăng nhập, có ≥1 sách trong giỏ hàng | bookId cần xóa | Số lượng item trong giỏ giảm đi 1 | Script: login → addToCart → đếm items → removeFromCart/{id} → assert items.size() giảm |
| TC_TTT_009 | Xác minh xóa toàn bộ giỏ hàng thành công khi gọi clearCart | P3 | Positive | Đã đăng nhập, có sách trong giỏ hàng | None | Giỏ hàng trống, hiển thị "Your cart is empty" | Script: login → addToCart → `/cart/clearCart` → verify page contains "cart is empty" |
| TC_TTT_010 | Xác minh tổng tiền giỏ hàng hiển thị đúng định dạng khi có sách trong giỏ | P1 | Positive | Đã đăng nhập, có sách trong giỏ hàng | Sách có giá đã biết trong giỏ | Tổng tiền hiển thị có ký hiệu $ | Script: login → addToCart → `/cart` → verify .cart-total contains "$" |
| TC_TTT_011 | Xác minh trang checkout hiển thị form billing khi giỏ hàng có sách | P1 | Positive | Đã đăng nhập, có sách trong giỏ hàng | None | URL là /cart/checkout, form Billing & Shipping hiển thị | Script: login → addToCart → `/cart/checkout` → verify URL & page contains "Billing" |
| TC_TTT_012 | Xác minh checkout redirect về /cart khi giỏ hàng trống | P1 | Negative | Đã đăng nhập, giỏ hàng trống | None | Redirect về /cart?error=empty | Script: login → clearCart → `/cart/checkout` → verify URL contains /cart |
| TC_TTT_013 | Xác minh đặt hàng thành công khi điền đầy đủ thông tin và đồng ý điều khoản | P1 | Positive | Đã đăng nhập, có sách trong giỏ hàng | customerName, email, phone, address hợp lệ, agreeTerms=true | Redirect về /orders/detail/{id} | Script: login → addToCart → checkout → điền form + tick terms → submit → verify URL contains /orders/detail |
| TC_TTT_014 | Xác minh checkout bị từ chối khi không đồng ý điều khoản và điều kiện | P2 | Negative | Đã đăng nhập, có sách trong giỏ hàng | customerName, email, phone, address hợp lệ, agreeTerms=false | Trang hiển thị lỗi về điều khoản, không tạo đơn hàng | Script: login → addToCart → checkout → điền form, không tick terms → submit → verify error shown |
| TC_TTT_015 | Xác minh trang lịch sử đơn hàng hiển thị đúng khi user đã đặt hàng | P2 | Positive | Đã đăng nhập, user đã có ít nhất 1 đơn hàng | None | Trang /orders hiển thị "Order History" với danh sách đơn | Script: login → đặt 1 đơn → `/orders` → verify page contains "Order History" |
| TC_TTT_016 | Xác minh xem chi tiết đơn hàng thành công khi click link từ lịch sử đơn | P2 | Positive | Đã đăng nhập, user đã có ít nhất 1 đơn hàng | id đơn hàng hợp lệ của user | URL chứa /orders/detail/, trang hiển thị thông tin đơn hàng | Script: login → `/orders` → click link detail → verify URL contains /orders/detail/ |
| TC_TTT_017 | Xác minh truy cập đơn hàng của người khác bị từ chối khi không phải chủ đơn | P1 | Negative | Đã đăng nhập bằng user thường, tồn tại đơn hàng id=1 của user khác | /orders/detail/1 (thuộc user khác) | Hệ thống từ chối, hiển thị trang lỗi hoặc redirect | Script: login user thường → `/orders/detail/1` → verify page contains error hoặc URL redirected |
| TC_TTT_018 | Xác minh tổng tiền đã chi hiển thị đúng định dạng trên trang lịch sử đơn hàng | P2 | Positive | Đã đăng nhập, user đã có đơn hàng | None | Trang /orders hiển thị thông tin hợp lệ (có $ nếu có đơn) | Script: login → `/orders` → verify page contains "Order History" & "$" nếu totalSpent > 0 |
| TC_TTT_019 | Xác minh đăng xuất thành công khi truy cập /logout và redirect về /login | P2 | Positive | Đã đăng nhập thành công | None | URL chứa /login sau logout, /profile redirect về /login | Script: login → `/logout` → verify URL contains /login → `/profile` → verify redirect /login |
| TC_TTT_020 | Xác minh phân trang danh sách sách hiển thị đúng số lượng khi có nhiều sách | P3 | Positive | Đã đăng nhập, có sách trong hệ thống | pageNo=0, pageNo=1 (nếu có trang 2) | Trang 1 có tối đa 20 sách, trang 2 có sách nếu tồn tại | Script: login → `/books?pageNo=0` → đếm .book-card → assert ≤20; nếu có trang 2 → verify books > 0 |

**Tổng kết:**
- Tổng số test case: **20**
- Positive: **16**
- Negative: **4**
- Pass: ___
- Fail: ___
- Blocked: ___

---

## Test Cases Summary

| TC ID | Module | Test Case Title | Priority | Type | Status | Assigned To | Created Date | Due Date |
|-------|--------|-----------------|----------|------|--------|-------------|--------------|----------|
| TC_TTT_001 | Authentication | Xác minh đăng nhập thành công khi nhập đúng username và password hợp lệ | P1 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_002 | Authentication | Xác minh đăng nhập bị từ chối khi nhập sai mật khẩu | P1 | Negative | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_003 | Search & Filter | Xác minh tìm kiếm sách hiển thị kết quả phù hợp khi nhập từ khóa hợp lệ | P2 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_004 | Search & Filter | Xác minh lọc sách theo danh mục hiển thị đúng URL khi click link danh mục | P2 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_005 | Search & Filter | Xác minh sắp xếp sách theo giá tăng dần hiển thị đúng thứ tự khi chọn price_asc | P3 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_006 | Cart | Xác minh thêm sách vào giỏ hàng thành công khi nhấn nút thêm | P1 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_007 | Cart | Xác minh cập nhật số lượng sách trong giỏ thành công khi gọi updateCart | P2 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_008 | Cart | Xác minh xóa một sản phẩm khỏi giỏ thành công khi nhấn nút xóa | P2 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_009 | Cart | Xác minh xóa toàn bộ giỏ hàng thành công khi gọi clearCart | P3 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_010 | Cart | Xác minh tổng tiền giỏ hàng hiển thị đúng định dạng khi có sách trong giỏ | P1 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_011 | Checkout | Xác minh trang checkout hiển thị form billing khi giỏ hàng có sách | P1 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_012 | Checkout | Xác minh checkout redirect về /cart khi giỏ hàng trống | P1 | Negative | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_013 | Checkout | Xác minh đặt hàng thành công khi điền đầy đủ thông tin và đồng ý điều khoản | P1 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_014 | Checkout | Xác minh checkout bị từ chối khi không đồng ý điều khoản và điều kiện | P2 | Negative | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_015 | Order History | Xác minh trang lịch sử đơn hàng hiển thị đúng khi user đã đặt hàng | P2 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_016 | Order History | Xác minh xem chi tiết đơn hàng thành công khi click link từ lịch sử đơn | P2 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_017 | Order History | Xác minh truy cập đơn hàng của người khác bị từ chối khi không phải chủ đơn | P1 | Negative | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_018 | Order History | Xác minh tổng tiền đã chi hiển thị đúng định dạng trên trang lịch sử đơn hàng | P2 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_019 | Authentication | Xác minh đăng xuất thành công khi truy cập /logout và redirect về /login | P2 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
| TC_TTT_020 | Book Browsing | Xác minh phân trang danh sách sách hiển thị đúng số lượng khi có nhiều sách | P3 | Positive | | Trần Thanh Tịnh | 2026-03-24 | 2026-04-07 |
