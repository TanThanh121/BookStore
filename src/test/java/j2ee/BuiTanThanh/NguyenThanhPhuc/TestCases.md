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

| TC ID | Test Case | Priority | Type | Pre-condition | Test Data | Expected Result | Test Script Design |
|-------|-----------|----------|------|---------------|-----------|-----------------|-------------------|
| TC_NTP_001 | Xác minh đăng ký tài khoản thành công khi nhập đầy đủ thông tin hợp lệ | P1 | Positive | None | username="testuser01", password="Test@1234", email="test01@example.com" | Hệ thống tạo tài khoản và chuyển hướng về /login | Script: mở `/register` → điền form hợp lệ → nhấn Đăng ký → verify URL contains /login |
| TC_NTP_002 | Xác minh đăng ký bị từ chối khi username đã tồn tại trong hệ thống | P1 | Negative | Username "admin" đã tồn tại trong DB | username="admin", password="Test@1234", email="newemail@example.com" | Hiển thị thông báo lỗi trùng username, không tạo tài khoản | Script: mở `/register` → nhập username trùng → nhấn Đăng ký → verify error message displayed |
| TC_NTP_003 | Xác minh đăng ký bị từ chối khi để trống trường password bắt buộc | P1 | Negative | None | username="newuser02", email="new02@example.com", password="" | Hiển thị thông báo lỗi "Password is required", trang đăng ký tải lại | Script: mở `/register` → bỏ trống password → nhấn Đăng ký → verify error about password |
| TC_NTP_004 | Xác minh đăng ký bị từ chối khi email không đúng định dạng | P2 | Negative | None | username="newuser03", password="Test@1234", email="invalidemail" | Hiển thị thông báo lỗi định dạng email không hợp lệ | Script: mở `/register` → nhập email sai định dạng → nhấn Đăng ký → verify email format error |
| TC_NTP_005 | Xác minh đăng nhập thành công khi nhập đúng username và password | P1 | Positive | Tài khoản hợp lệ tồn tại trong hệ thống | username="testuser01", password="Test@1234" | Redirect về trang chủ, không ở lại /login | Script: mở `/login` → điền credentials → nhấn Đăng nhập → verify URL contains home |
| TC_NTP_006 | Xác minh đăng nhập bị từ chối khi nhập sai mật khẩu | P1 | Negative | Tài khoản hợp lệ tồn tại trong hệ thống | username đúng, password="WrongPassword" | Trang /login hiển thị lỗi xác thực, URL vẫn là /login | Script: mở `/login` → nhập sai password → nhấn Đăng nhập → verify error message & URL = /login |
| TC_NTP_007 | Xác minh trang danh sách sách hiển thị đầy đủ sách khi truy cập /books | P1 | Positive | Đã đăng nhập, có sách trong hệ thống | None | Trang hiển thị danh sách sách với đầy đủ thông tin | Script: login → mở `/books` → verify book cards displayed |
| TC_NTP_008 | Xác minh tìm kiếm sách hiển thị kết quả phù hợp khi nhập từ khóa hợp lệ | P2 | Positive | Đã đăng nhập, có sách khớp từ khóa | keyword="Lập trình" | Hiển thị sách chứa từ khóa trong kết quả tìm kiếm | Script: login → `/books` → nhập keyword → search → verify results contain keyword |
| TC_NTP_009 | Xác minh tìm kiếm hiển thị danh sách trống khi từ khóa không tồn tại | P2 | Negative | Đã đăng nhập | keyword="xyznotexist" | Trang hiển thị thông báo không tìm thấy hoặc danh sách trống | Script: login → `/books` → nhập keyword không tồn tại → verify empty result message |
| TC_NTP_010 | Xác minh lọc sách theo danh mục chỉ hiển thị sách đúng danh mục khi chọn filter | P2 | Positive | Đã đăng nhập, có nhiều danh mục và sách | categoryId hợp lệ | Chỉ hiển thị sách thuộc danh mục đã chọn | Script: login → `/books` → click category filter → verify URL contains category= & results show |
| TC_NTP_011 | Xác minh trang chi tiết sách hiển thị đầy đủ thông tin khi truy cập theo ID | P2 | Positive | Đã đăng nhập, sách hợp lệ tồn tại | id sách hợp lệ | Trang hiển thị title, author, price, description của sách | Script: login → `/books` → click tên sách → verify `/books/detail/{id}` & info displayed |
| TC_NTP_012 | Xác minh sắp xếp sách theo giá tăng dần hiển thị đúng thứ tự khi chọn price_asc | P3 | Positive | Đã đăng nhập, có ≥2 sách | sortBy=price_asc | Sách đầu tiên có giá ≤ sách cuối trên trang | Script: login → `/books?sortBy=price_asc` → lấy giá đầu & cuối → assert firstPrice ≤ lastPrice |
| TC_NTP_013 | Xác minh thêm sách vào giỏ hàng thành công khi nhấn nút thêm trên trang sách | P1 | Positive | Đã đăng nhập, có sách trong hệ thống | Sách đầu tiên trong danh sách | Sách xuất hiện trong giỏ hàng tại /cart | Script: login → `/books` → click thêm vào giỏ → `/cart` → verify sách trong danh sách giỏ |
| TC_NTP_014 | Xác minh cập nhật số lượng sách trong giỏ thành công khi thay đổi quantity | P2 | Positive | Đã đăng nhập, có sách trong giỏ hàng | bookId hợp lệ, quantity mới = 3 | Số lượng và tổng tiền được cập nhật đúng | Script: login → addToCart → `/cart` → đổi quantity → verify số lượng & tổng tiền mới |
| TC_NTP_015 | Xác minh xóa một sản phẩm khỏi giỏ hàng thành công khi nhấn nút xóa | P2 | Positive | Đã đăng nhập, có ≥1 sách trong giỏ hàng | None | Sản phẩm bị xóa, số lượng item trong giỏ giảm | Script: login → addToCart → `/cart` → click xóa → verify item removed from cart |
| TC_NTP_016 | Xác minh xóa toàn bộ giỏ hàng hiển thị giỏ trống khi nhấn nút xóa tất cả | P3 | Positive | Đã đăng nhập, có sách trong giỏ hàng | None | Giỏ hàng trống sau khi xóa tất cả | Script: login → addToCart → clearCart → verify "Your cart is empty" |
| TC_NTP_017 | Xác minh thanh toán thành công khi điền đầy đủ thông tin và đồng ý điều khoản | P1 | Positive | Đã đăng nhập, có sách trong giỏ hàng | customerName, email, phone, address hợp lệ, agreeTerms=true | Đơn hàng được tạo, redirect về /orders/detail/{id} | Script: login → addToCart → checkout → điền form + tick terms → submit → verify order created |
| TC_NTP_018 | Xác minh checkout redirect về /cart khi giỏ hàng trống | P1 | Negative | Đã đăng nhập, giỏ hàng trống | None | Redirect về /cart?error=empty, không vào được checkout | Script: login → clearCart → mở `/cart/checkout` → verify redirect to /cart |
| TC_NTP_019 | Xác minh checkout bị từ chối khi không đồng ý điều khoản và điều kiện | P2 | Negative | Đã đăng nhập, có sách trong giỏ hàng | Thông tin hợp lệ nhưng agreeTerms=false | Hiển thị thông báo lỗi về điều khoản, không tạo đơn hàng | Script: login → addToCart → checkout → điền form, không tick terms → submit → verify error |
| TC_NTP_020 | Xác minh trang lịch sử đơn hàng hiển thị đúng danh sách khi user đã có đơn | P2 | Positive | Đã đăng nhập, user đã có ít nhất 1 đơn hàng | None | Trang /orders hiển thị danh sách đơn hàng đúng của user | Script: login → đặt 1 đơn → `/orders` → verify order list displayed for current user |

**Tổng kết:**
- Tổng số test case: **20**
- Positive: **13**
- Negative: **7**
- Pass: ___
- Fail: ___
- Blocked: ___
