# Test Cases - Nguyễn Thành Phúc
**Loại kiểm thử:** Hộp Đen (Black Box)
**Số lượng:** 20 Test Cases
**Phạm vi:** Đăng ký, Đăng nhập, Duyệt sách, Giỏ hàng, Thanh toán, Lịch sử đơn hàng

---

## Chức năng: Đăng ký tài khoản

### Test Case TC_NTP_001
- **Summary:** Đăng ký thành công với đầy đủ thông tin hợp lệ
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/register`.
  - Bước 2: Nhập username `testuser01`, password `Test@1234`, email `test01@example.com`.
  - Bước 3: Nhấn nút **Đăng ký**.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Hệ thống tạo tài khoản thành công và chuyển hướng về trang `/login`.

---

### Test Case TC_NTP_002
- **Summary:** Đăng ký thất bại khi username đã tồn tại trong hệ thống
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/register`.
  - Bước 2: Nhập username đã tồn tại `admin`, password `Test@1234`, email `newemail@example.com`.
  - Bước 3: Nhấn nút **Đăng ký**.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hiển thị thông báo lỗi trùng username, không tạo tài khoản mới.

---

### Test Case TC_NTP_003
- **Summary:** Đăng ký thất bại khi để trống trường bắt buộc (password)
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/register`.
  - Bước 2: Nhập username `newuser02`, email `new02@example.com`, bỏ trống password.
  - Bước 3: Nhấn nút **Đăng ký**.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hiển thị thông báo lỗi "Password is required", trang đăng ký được tải lại.

---

### Test Case TC_NTP_004
- **Summary:** Đăng ký thất bại khi email không đúng định dạng
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/register`.
  - Bước 2: Nhập username `newuser03`, password `Test@1234`, email `invalidemail`.
  - Bước 3: Nhấn nút **Đăng ký**.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hiển thị thông báo lỗi định dạng email không hợp lệ.

---

## Chức năng: Đăng nhập

### Test Case TC_NTP_005
- **Summary:** Đăng nhập thành công với thông tin hợp lệ
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/login`.
  - Bước 2: Nhập username `testuser01` và password `Test@1234`.
  - Bước 3: Nhấn nút **Đăng nhập**.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Đăng nhập thành công, chuyển hướng về trang chủ `/`.

---

### Test Case TC_NTP_006
- **Summary:** Đăng nhập thất bại khi nhập sai mật khẩu
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/login`.
  - Bước 2: Nhập username `testuser01` và password `WrongPass`.
  - Bước 3: Nhấn nút **Đăng nhập**.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hiển thị thông báo lỗi xác thực, không cho phép vào hệ thống.

---

## Chức năng: Duyệt & Tìm kiếm sách

### Test Case TC_NTP_007
- **Summary:** Hiển thị danh sách sách ở trang chủ sách
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/books`.
  - Bước 2: Kiểm tra danh sách sách hiển thị.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Danh sách sách hiển thị đầy đủ tên sách, tác giả, giá và ảnh bìa.

---

### Test Case TC_NTP_008
- **Summary:** Tìm kiếm sách theo từ khóa hợp lệ
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/books`.
  - Bước 2: Nhập từ khóa vào ô tìm kiếm, ví dụ `Java`.
  - Bước 3: Nhấn nút tìm kiếm.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Hiển thị danh sách sách có tên hoặc tác giả chứa từ khóa `Java`.

---

### Test Case TC_NTP_009
- **Summary:** Tìm kiếm sách với từ khóa không tồn tại
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/books`.
  - Bước 2: Nhập từ khóa không tồn tại `xyzxyzxyz`.
  - Bước 3: Nhấn nút tìm kiếm.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hiển thị thông báo không tìm thấy kết quả, danh sách sách trống.

---

### Test Case TC_NTP_010
- **Summary:** Lọc sách theo danh mục cụ thể
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/books`.
  - Bước 2: Chọn một danh mục từ bộ lọc danh mục.
  - Bước 3: Kiểm tra kết quả hiển thị.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Chỉ hiển thị các sách thuộc danh mục đã chọn.

---

### Test Case TC_NTP_011
- **Summary:** Xem chi tiết một cuốn sách
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/books`.
  - Bước 2: Nhấn vào tên hoặc ảnh của một cuốn sách.
  - Bước 3: Kiểm tra trang chi tiết.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Hiển thị đầy đủ tên sách, tác giả, giá, mô tả và ảnh bìa.

---

### Test Case TC_NTP_012
- **Summary:** Sắp xếp danh sách sách theo giá tăng dần
- **Các bước thực hiện:**
  - Bước 1: Truy cập trang `/books`.
  - Bước 2: Chọn tùy chọn sắp xếp `price_asc`.
  - Bước 3: Kiểm tra thứ tự hiển thị.
- **Độ ưu tiên:** P3 (Medium)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Danh sách sách được sắp xếp từ giá thấp đến cao.

---

## Chức năng: Giỏ hàng

### Test Case TC_NTP_013
- **Summary:** Thêm sách vào giỏ hàng thành công
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập vào hệ thống.
  - Bước 2: Truy cập trang chi tiết sách.
  - Bước 3: Nhấn nút **Thêm vào giỏ hàng**.
  - Bước 4: Truy cập trang `/cart` để kiểm tra.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sách xuất hiện trong giỏ hàng với số lượng và giá đúng.

---

### Test Case TC_NTP_014
- **Summary:** Cập nhật số lượng sách trong giỏ hàng
- **Các bước thực hiện:**
  - Bước 1: Có ít nhất 1 sách trong giỏ hàng.
  - Bước 2: Truy cập trang `/cart`.
  - Bước 3: Thay đổi số lượng sách rồi nhấn cập nhật.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Số lượng và tổng tiền được cập nhật đúng.

---

### Test Case TC_NTP_015
- **Summary:** Xóa một sản phẩm khỏi giỏ hàng
- **Các bước thực hiện:**
  - Bước 1: Có ít nhất 1 sách trong giỏ hàng.
  - Bước 2: Truy cập trang `/cart`.
  - Bước 3: Nhấn nút xóa bên cạnh sản phẩm.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sản phẩm bị xóa khỏi giỏ hàng, tổng tiền cập nhật lại.

---

### Test Case TC_NTP_016
- **Summary:** Xóa toàn bộ giỏ hàng
- **Các bước thực hiện:**
  - Bước 1: Có ít nhất 1 sách trong giỏ hàng.
  - Bước 2: Truy cập trang `/cart`.
  - Bước 3: Nhấn nút **Xóa tất cả**.
- **Độ ưu tiên:** P3 (Medium)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Giỏ hàng trống hoàn toàn, tổng tiền về 0.

---

## Chức năng: Thanh toán (Checkout)

### Test Case TC_NTP_017
- **Summary:** Thanh toán thành công với đầy đủ thông tin
- **Các bước thực hiện:**
  - Bước 1: Có sách trong giỏ hàng, truy cập `/cart/checkout`.
  - Bước 2: Điền đầy đủ tên, email, số điện thoại, địa chỉ giao hàng.
  - Bước 3: Tích vào ô đồng ý điều khoản.
  - Bước 4: Nhấn nút **Đặt hàng**.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Đơn hàng được tạo thành công, chuyển hướng tới trang chi tiết đơn hàng.

---

### Test Case TC_NTP_018
- **Summary:** Không thể thanh toán khi giỏ hàng trống
- **Các bước thực hiện:**
  - Bước 1: Đảm bảo giỏ hàng đang trống.
  - Bước 2: Truy cập trực tiếp `/cart/checkout`.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hệ thống chuyển hướng về `/cart?error=empty`.

---

### Test Case TC_NTP_019
- **Summary:** Thanh toán thất bại khi chưa đồng ý điều khoản
- **Các bước thực hiện:**
  - Bước 1: Có sách trong giỏ hàng, truy cập `/cart/checkout`.
  - Bước 2: Điền đầy đủ thông tin nhưng **không** tích ô đồng ý điều khoản.
  - Bước 3: Nhấn nút **Đặt hàng**.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hiển thị thông báo "Please agree to terms and conditions", không tạo đơn hàng.

---

## Chức năng: Lịch sử đơn hàng

### Test Case TC_NTP_020
- **Summary:** Xem lịch sử đơn hàng của người dùng đã đặt
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập vào tài khoản đã có đơn hàng.
  - Bước 2: Truy cập trang `/orders`.
  - Bước 3: Kiểm tra danh sách đơn hàng hiển thị.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Hiển thị danh sách đơn hàng của người dùng với tổng tiền đã chi và số lượng sách đã mua.

---

## Test Report

| STT | Test Case ID | Chức năng | Mô tả ngắn | Bản chất | Độ ưu tiên | Kết quả mong muốn | Kết quả thực tế | Trạng thái | Ngày test | Ghi chú |
|-----|-------------|-----------|------------|----------|------------|-------------------|-----------------|------------|-----------|---------|
| 1 | TC_NTP_001 | Đăng ký | Đăng ký thành công | Positive | P1 | Tạo tài khoản & redirect /login | | | | |
| 2 | TC_NTP_002 | Đăng ký | Username đã tồn tại | Negative | P1 | Hiển thị lỗi trùng username | | | | |
| 3 | TC_NTP_003 | Đăng ký | Bỏ trống password | Negative | P1 | Hiển thị lỗi "Password is required" | | | | |
| 4 | TC_NTP_004 | Đăng ký | Email sai định dạng | Negative | P2 | Hiển thị lỗi định dạng email | | | | |
| 5 | TC_NTP_005 | Đăng nhập | Đăng nhập thành công | Positive | P1 | Redirect về trang chủ | | | | |
| 6 | TC_NTP_006 | Đăng nhập | Sai mật khẩu | Negative | P1 | Hiển thị lỗi xác thực | | | | |
| 7 | TC_NTP_007 | Duyệt sách | Hiển thị danh sách sách | Positive | P1 | Hiển thị đầy đủ sách | | | | |
| 8 | TC_NTP_008 | Tìm kiếm | Tìm kiếm với từ khóa hợp lệ | Positive | P2 | Hiển thị kết quả phù hợp | | | | |
| 9 | TC_NTP_009 | Tìm kiếm | Từ khóa không tồn tại | Negative | P2 | Danh sách trống | | | | |
| 10 | TC_NTP_010 | Lọc sách | Lọc theo danh mục | Positive | P2 | Chỉ hiện sách đúng danh mục | | | | |
| 11 | TC_NTP_011 | Chi tiết sách | Xem chi tiết sách | Positive | P2 | Hiển thị đầy đủ thông tin | | | | |
| 12 | TC_NTP_012 | Sắp xếp | Sắp xếp giá tăng dần | Positive | P3 | Danh sách đúng thứ tự giá | | | | |
| 13 | TC_NTP_013 | Giỏ hàng | Thêm sách vào giỏ | Positive | P1 | Sách xuất hiện trong giỏ | | | | |
| 14 | TC_NTP_014 | Giỏ hàng | Cập nhật số lượng | Positive | P2 | Số lượng và tổng tiền cập nhật | | | | |
| 15 | TC_NTP_015 | Giỏ hàng | Xóa một sản phẩm | Positive | P2 | Sản phẩm bị xóa khỏi giỏ | | | | |
| 16 | TC_NTP_016 | Giỏ hàng | Xóa toàn bộ giỏ | Positive | P3 | Giỏ hàng trống | | | | |
| 17 | TC_NTP_017 | Thanh toán | Thanh toán thành công | Positive | P1 | Đơn hàng được tạo thành công | | | | |
| 18 | TC_NTP_018 | Thanh toán | Giỏ hàng trống | Negative | P1 | Redirect /cart?error=empty | | | | |
| 19 | TC_NTP_019 | Thanh toán | Không đồng ý điều khoản | Negative | P2 | Hiển thị lỗi điều khoản | | | | |
| 20 | TC_NTP_020 | Lịch sử đơn hàng | Xem danh sách đơn hàng | Positive | P2 | Hiển thị đúng danh sách đơn hàng | | | | |

**Tổng kết:**
- Tổng số test case: **20**
- Positive: **13**
- Negative: **7**
- Pass: ___
- Fail: ___
- Blocked: ___
