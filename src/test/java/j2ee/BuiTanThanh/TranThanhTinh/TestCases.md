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

| STT | Test Case ID | Chức năng | Mô tả ngắn | Bản chất | Độ ưu tiên | Công cụ | Kết quả mong muốn | Kết quả thực tế | Trạng thái | Ngày test | Ghi chú |
|-----|-------------|-----------|------------|----------|------------|---------|-------------------|-----------------|------------|-----------|---------|
| 1 | TC_TTT_001 | Đăng nhập | Đăng nhập thành công | Positive | P1 | Selenium | Redirect trang chủ | | | | |
| 2 | TC_TTT_002 | Đăng nhập | Sai mật khẩu | Negative | P1 | Selenium | Hiển thị lỗi xác thực | | | | |
| 3 | TC_TTT_003 | Tìm kiếm | Tìm kiếm từ khóa hợp lệ | Positive | P2 | Selenium | Có kết quả phù hợp | | | | |
| 4 | TC_TTT_004 | Lọc sách | Lọc theo danh mục | Positive | P2 | Selenium | Chỉ hiện đúng danh mục | | | | |
| 5 | TC_TTT_005 | Sắp xếp | Giá tăng dần | Positive | P3 | Selenium | Thứ tự giá đúng | | | | |
| 6 | TC_TTT_006 | Giỏ hàng | Thêm sách vào giỏ | Positive | P1 | Selenium | Sách trong giỏ | | | | |
| 7 | TC_TTT_007 | Giỏ hàng | Cập nhật số lượng | Positive | P2 | Selenium | Số lượng & tổng tiền đúng | | | | |
| 8 | TC_TTT_008 | Giỏ hàng | Xóa một sản phẩm | Positive | P2 | Selenium | Số lượng sản phẩm giảm 1 | | | | |
| 9 | TC_TTT_009 | Giỏ hàng | Xóa toàn bộ giỏ | Positive | P3 | Selenium | Giỏ hàng trống | | | | |
| 10 | TC_TTT_010 | Giỏ hàng | Tổng tiền tính đúng | Positive | P1 | Selenium | 130.000 VNĐ | | | | |
| 11 | TC_TTT_011 | Thanh toán | Truy cập checkout có hàng | Positive | P1 | Selenium | Form checkout hiển thị | | | | |
| 12 | TC_TTT_012 | Thanh toán | Checkout giỏ hàng trống | Negative | P1 | Selenium | Redirect /cart?error=empty | | | | |
| 13 | TC_TTT_013 | Thanh toán | Đặt hàng thành công | Positive | P1 | Selenium | Redirect /orders/detail/{id} | | | | |
| 14 | TC_TTT_014 | Thanh toán | Chưa đồng ý điều khoản | Negative | P2 | Selenium | Thông báo lỗi điều khoản | | | | |
| 15 | TC_TTT_015 | Lịch sử đơn | Xem danh sách đơn hàng | Positive | P2 | Selenium | Hiển thị đơn hàng | | | | |
| 16 | TC_TTT_016 | Lịch sử đơn | Xem chi tiết đơn hàng | Positive | P2 | Selenium | Hiển thị đầy đủ chi tiết | | | | |
| 17 | TC_TTT_017 | Bảo mật | Xem đơn hàng người khác | Negative | P1 | Selenium | Hệ thống từ chối | | | | |
| 18 | TC_TTT_018 | Lịch sử đơn | Tổng tiền đã chi | Positive | P2 | Selenium | Tổng tiền đúng | | | | |
| 19 | TC_TTT_019 | Đăng xuất | Đăng xuất thành công | Positive | P2 | Selenium | Redirect /login | | | | |
| 20 | TC_TTT_020 | Phân trang | Phân trang danh sách sách | Positive | P3 | Selenium | Trang 2 khác trang 1 | | | | |

**Tổng kết:**
- Tổng số test case: **20**
- Positive: **16**
- Negative: **4**
- Pass: ___
- Fail: ___
- Blocked: ___
