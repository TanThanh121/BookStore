# Test Cases - Bùi Tấn Thành
**Loại kiểm thử:** Kiểm thử tự động (Automation Testing)
**Công cụ:** Selenium WebDriver + JUnit 5
**Số lượng:** 20 Test Cases
**Phạm vi:** Đăng ký, Quản lý Admin (sách & danh mục), Hồ sơ người dùng, Bảo mật & Phân quyền

---

## Chức năng: Đăng ký tài khoản (Tự động)

### Test Case TC_BTT_001
- **Summary:** Tự động đăng ký tài khoản mới thành công
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Mở trình duyệt và truy cập `http://localhost:8080/register`.
  - Bước 2: Điền `username = "autouser01"`, `password = "Auto@1234"`, `email = "autouser01@test.com"`.
  - Bước 3: Nhấn nút **Đăng ký**.
  - Bước 4: Kiểm tra URL sau khi trang tải xong.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** URL chuyển thành `/login`, tài khoản được tạo thành công.

---

### Test Case TC_BTT_002
- **Summary:** Tự động đăng ký thất bại với username đã tồn tại
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Truy cập `http://localhost:8080/register`.
  - Bước 2: Nhập `username = "admin"` (đã tồn tại), `password = "Auto@1234"`, `email = "newunique@test.com"`.
  - Bước 3: Nhấn nút **Đăng ký**.
  - Bước 4: Kiểm tra thông báo lỗi trên trang.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trang `/register` hiển thị thông báo lỗi trùng username, không tạo tài khoản mới.

---

### Test Case TC_BTT_003
- **Summary:** Tự động đăng ký thất bại khi email đã được dùng
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Truy cập `http://localhost:8080/register`.
  - Bước 2: Nhập `username = "newunique01"`, `email` đã tồn tại trong hệ thống.
  - Bước 3: Nhấn nút **Đăng ký**.
  - Bước 4: Kiểm tra thông báo lỗi.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trang hiển thị thông báo lỗi trùng email, form đăng ký được tải lại.

---

### Test Case TC_BTT_004
- **Summary:** Tự động đăng ký thất bại với số điện thoại không hợp lệ (không phải 10 chữ số)
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Truy cập `http://localhost:8080/register`.
  - Bước 2: Điền thông tin hợp lệ nhưng nhập `phone = "123"` (ít hơn 10 số).
  - Bước 3: Nhấn nút **Đăng ký**.
  - Bước 4: Kiểm tra thông báo lỗi.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hiển thị lỗi "Phone must be 10 characters".

---

## Chức năng: Quản lý sách - Admin (Tự động)

### Test Case TC_BTT_005
- **Summary:** Tự động đăng nhập admin và xem dashboard
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập bằng tài khoản admin.
  - Bước 2: Truy cập `http://localhost:8080/admin`.
  - Bước 3: Kiểm tra các thẻ thống kê trên dashboard.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Dashboard hiển thị `totalBooks`, `totalCategories`, `totalOrders`, `totalUsers` với giá trị số thực tế.

---

### Test Case TC_BTT_006
- **Summary:** Tự động thêm sách mới từ trang admin
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập admin, truy cập `/admin/books`.
  - Bước 2: Điền form thêm sách: `title = "Auto Test Book"`, `author = "Auto Author"`, `price = 99000`.
  - Bước 3: Chọn danh mục hợp lệ.
  - Bước 4: Nhấn nút **Thêm sách**.
  - Bước 5: Kiểm tra thông báo thành công và sách xuất hiện trong danh sách.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Flash message hiển thị `"Book "Auto Test Book" added successfully!"`, sách xuất hiện trong danh sách.

---

### Test Case TC_BTT_007
- **Summary:** Tự động thêm sách thất bại khi thiếu tiêu đề
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập admin, truy cập `/admin/books`.
  - Bước 2: Điền form nhưng bỏ trống trường `title`.
  - Bước 3: Nhấn nút **Thêm sách**.
  - Bước 4: Kiểm tra phản hồi.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hiển thị thông báo lỗi hoặc flash message lỗi, không thêm sách.

---

### Test Case TC_BTT_008
- **Summary:** Tự động sửa thông tin sách từ trang admin
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập admin, truy cập `/admin/books`.
  - Bước 2: Nhấn nút **Sửa** trên sách đầu tiên.
  - Bước 3: Thay đổi `price` thành một giá trị mới.
  - Bước 4: Nhấn **Lưu**.
  - Bước 5: Kiểm tra thông báo và giá mới trong danh sách.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Flash message "Book updated successfully!", giá mới hiển thị đúng trong danh sách.

---

### Test Case TC_BTT_009
- **Summary:** Tự động vô hiệu hóa sách (soft delete) từ trang admin
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập admin, truy cập `/admin/books`.
  - Bước 2: Ghi nhớ tên sách đầu tiên đang `active`.
  - Bước 3: Nhấn nút **Xóa/Vô hiệu hóa** sách đó.
  - Bước 4: Kiểm tra trạng thái sách trong danh sách.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sách hiển thị trạng thái `inactive`, nút **Kích hoạt** xuất hiện.

---

### Test Case TC_BTT_010
- **Summary:** Tự động kích hoạt lại sách đã bị vô hiệu hóa
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập admin, truy cập `/admin/books`.
  - Bước 2: Tìm sách đang `inactive`.
  - Bước 3: Nhấn nút **Kích hoạt**.
  - Bước 4: Kiểm tra trạng thái.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sách chuyển thành trạng thái `active`, hiển thị lại trên trang sách người dùng.

---

### Test Case TC_BTT_011
- **Summary:** Tự động tìm kiếm sách trong trang admin
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập admin, truy cập `/admin/books`.
  - Bước 2: Nhập từ khóa vào ô tìm kiếm và nhấn Enter.
  - Bước 3: Kiểm tra danh sách kết quả.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Chỉ hiển thị sách có tên hoặc tác giả chứa từ khóa đã nhập.

---

## Chức năng: Quản lý danh mục - Admin (Tự động)

### Test Case TC_BTT_012
- **Summary:** Tự động thêm danh mục mới từ trang admin
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập admin, truy cập `/admin/categories`.
  - Bước 2: Điền tên danh mục mới `"Auto Category"`.
  - Bước 3: Nhấn nút **Thêm**.
  - Bước 4: Kiểm tra danh sách.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Danh mục `"Auto Category"` xuất hiện trong danh sách.

---

### Test Case TC_BTT_013
- **Summary:** Tự động sửa tên danh mục từ trang admin
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập admin, truy cập `/admin/categories`.
  - Bước 2: Nhấn nút **Sửa** trên danh mục đầu tiên.
  - Bước 3: Thay đổi tên thành `"Updated Category"`.
  - Bước 4: Lưu và kiểm tra.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Tên danh mục được cập nhật thành `"Updated Category"` trong danh sách.

---

### Test Case TC_BTT_014
- **Summary:** Tự động xóa danh mục không có sách liên kết
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập admin, tạo mới một danh mục trống.
  - Bước 2: Nhấn nút **Xóa** trên danh mục vừa tạo.
  - Bước 3: Xác nhận xóa.
  - Bước 4: Kiểm tra danh sách.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Danh mục biến mất khỏi danh sách.

---

## Chức năng: Hồ sơ người dùng (Tự động)

### Test Case TC_BTT_015
- **Summary:** Tự động xem trang hồ sơ người dùng
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập người dùng.
  - Bước 2: Truy cập `http://localhost:8080/profile`.
  - Bước 3: Kiểm tra thông tin hiển thị.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trang hiển thị đúng username, email, tổng số đơn hàng và tổng tiền đã chi.

---

### Test Case TC_BTT_016
- **Summary:** Tự động cập nhật thông tin hồ sơ (name, phone)
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập và truy cập `/profile`.
  - Bước 2: Tìm form cập nhật thông tin, điền `name = "New Name"`, `phone = "0987654321"`.
  - Bước 3: Nhấn nút **Cập nhật**.
  - Bước 4: Kiểm tra thông báo và thông tin mới.
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Flash message "Profile updated successfully!", profile hiển thị tên và số điện thoại mới.

---

### Test Case TC_BTT_017
- **Summary:** Tự động đổi mật khẩu thành công
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập và truy cập `/profile`.
  - Bước 2: Tìm form đổi mật khẩu, điền `currentPassword` đúng, `newPassword = "NewPass@123"`, `confirmPassword = "NewPass@123"`.
  - Bước 3: Nhấn nút **Đổi mật khẩu**.
  - Bước 4: Kiểm tra thông báo.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Flash message "Password changed successfully!", có thể đăng nhập lại bằng mật khẩu mới.

---

### Test Case TC_BTT_018
- **Summary:** Tự động đổi mật khẩu thất bại khi nhập sai mật khẩu hiện tại
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập và truy cập `/profile`.
  - Bước 2: Điền form đổi mật khẩu với `currentPassword = "WrongCurrentPass"`.
  - Bước 3: Nhấn nút **Đổi mật khẩu**.
  - Bước 4: Kiểm tra thông báo lỗi.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Flash message lỗi hiển thị "Current password is incorrect", mật khẩu không thay đổi.

---

## Chức năng: Bảo mật & Phân quyền (Tự động)

### Test Case TC_BTT_019
- **Summary:** Tự động kiểm tra user thường không thể truy cập trang admin
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đăng nhập bằng tài khoản không có role `ADMIN`.
  - Bước 2: Truy cập `http://localhost:8080/admin`.
  - Bước 3: Kiểm tra URL và nội dung trang phản hồi.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Hệ thống chuyển hướng về trang `/error/403` hoặc hiển thị trang lỗi Forbidden.

---

### Test Case TC_BTT_020
- **Summary:** Tự động kiểm tra người dùng chưa đăng nhập bị redirect về trang login khi truy cập trang cần xác thực
- **Công cụ:** Selenium WebDriver
- **Các bước thực hiện:**
  - Bước 1: Đảm bảo chưa đăng nhập (hoặc đã đăng xuất).
  - Bước 2: Truy cập trực tiếp `http://localhost:8080/profile`.
  - Bước 3: Kiểm tra URL sau khi trang tải.
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** URL chuyển thành `/login`, trang yêu cầu xác thực không hiển thị.

---

## Test Report

| STT | Test Case ID | Chức năng | Mô tả ngắn | Bản chất | Độ ưu tiên | Công cụ | Kết quả mong muốn | Kết quả thực tế | Trạng thái | Ngày test | Ghi chú |
|-----|-------------|-----------|------------|----------|------------|---------|-------------------|-----------------|------------|-----------|---------|
| 1 | TC_BTT_001 | Đăng ký | Đăng ký thành công | Positive | P1 | Selenium | Redirect /login | | | | |
| 2 | TC_BTT_002 | Đăng ký | Username đã tồn tại | Negative | P1 | Selenium | Thông báo lỗi trùng username | | | | |
| 3 | TC_BTT_003 | Đăng ký | Email đã tồn tại | Negative | P1 | Selenium | Thông báo lỗi trùng email | | | | |
| 4 | TC_BTT_004 | Đăng ký | Phone không hợp lệ | Negative | P2 | Selenium | Lỗi "Phone must be 10 characters" | | | | |
| 5 | TC_BTT_005 | Admin | Xem dashboard admin | Positive | P1 | Selenium | Hiển thị số liệu thống kê | | | | |
| 6 | TC_BTT_006 | Admin | Thêm sách mới | Positive | P1 | Selenium | Flash success, sách trong danh sách | | | | |
| 7 | TC_BTT_007 | Admin | Thêm sách thiếu tiêu đề | Negative | P2 | Selenium | Thông báo lỗi | | | | |
| 8 | TC_BTT_008 | Admin | Sửa thông tin sách | Positive | P1 | Selenium | Flash success, giá mới đúng | | | | |
| 9 | TC_BTT_009 | Admin | Vô hiệu hóa sách | Positive | P1 | Selenium | Sách inactive | | | | |
| 10 | TC_BTT_010 | Admin | Kích hoạt lại sách | Positive | P2 | Selenium | Sách active trở lại | | | | |
| 11 | TC_BTT_011 | Admin | Tìm kiếm sách admin | Positive | P2 | Selenium | Kết quả đúng từ khóa | | | | |
| 12 | TC_BTT_012 | Admin | Thêm danh mục | Positive | P1 | Selenium | Danh mục xuất hiện trong danh sách | | | | |
| 13 | TC_BTT_013 | Admin | Sửa danh mục | Positive | P2 | Selenium | Tên danh mục được cập nhật | | | | |
| 14 | TC_BTT_014 | Admin | Xóa danh mục trống | Positive | P2 | Selenium | Danh mục biến mất | | | | |
| 15 | TC_BTT_015 | Hồ sơ | Xem hồ sơ người dùng | Positive | P2 | Selenium | Thông tin hiển thị đúng | | | | |
| 16 | TC_BTT_016 | Hồ sơ | Cập nhật name/phone | Positive | P2 | Selenium | Flash success, thông tin mới | | | | |
| 17 | TC_BTT_017 | Hồ sơ | Đổi mật khẩu thành công | Positive | P1 | Selenium | Flash success, đăng nhập được | | | | |
| 18 | TC_BTT_018 | Hồ sơ | Đổi mật khẩu sai mật khẩu cũ | Negative | P1 | Selenium | Flash lỗi mật khẩu sai | | | | |
| 19 | TC_BTT_019 | Bảo mật | User thường vào /admin | Negative | P1 | Selenium | Trang 403 Forbidden | | | | |
| 20 | TC_BTT_020 | Bảo mật | Chưa đăng nhập vào /profile | Negative | P1 | Selenium | Redirect /login | | | | |

**Tổng kết:**
- Tổng số test case: **20**
- Positive: **13**
- Negative: **7**
- Pass: ___
- Fail: ___
- Blocked: ___
