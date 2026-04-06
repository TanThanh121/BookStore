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

| TC ID | Test Case | Priority | Type | Pre-condition | Test Data | Expected Result | Test Script Design |
|-------|-----------|----------|------|---------------|-----------|-----------------|-------------------|
| TC_BTT_001 | Xác minh đăng ký tài khoản thành công khi nhập thông tin hợp lệ | P1 | Positive | None | username: "autouser01", password: "Auto@1234", email: "autouser01@test.com", phone: "0901234567" | URL chuyển thành `/login`, tài khoản được tạo thành công | Script: mở `/register` → điền form → click Đăng ký → verify URL contains `/login` |
| TC_BTT_002 | Xác minh đăng ký bị từ chối khi username đã tồn tại | P1 | Negative | Tồn tại username "admin" trong hệ thống | username: "admin", password: "Auto@1234", email: "newunique@test.com" | Trang `/register` hiển thị thông báo lỗi trùng username | Script: mở `/register` → điền username trùng → click Đăng ký → verify error message displayed |
| TC_BTT_003 | Xác minh đăng ký bị từ chối khi email đã được sử dụng | P1 | Negative | Tồn tại email trong hệ thống | username: "newunique01", email đã tồn tại, password: "Auto@1234" | Trang hiển thị thông báo lỗi trùng email, form được tải lại | Script: mở `/register` → điền email đã dùng → click Đăng ký → verify error message |
| TC_BTT_004 | Xác minh đăng ký bị từ chối khi số điện thoại không đủ 10 chữ số | P2 | Negative | None | username: "newunique02", phone: "123", password: "Auto@1234", email: "u02@test.com" | Hiển thị lỗi "Phone must be 10 characters" | Script: mở `/register` → điền phone ngắn → click Đăng ký → verify error message |
| TC_BTT_005 | Xác minh dashboard admin hiển thị đúng số liệu thống kê khi đăng nhập admin | P1 | Positive | Tài khoản admin hợp lệ | username: "az1533", password: "Thanh121" | Dashboard hiển thị totalBooks, totalCategories, totalOrders, totalUsers với giá trị số | Script: login admin → truy cập `/admin` → verify stat cards visible |
| TC_BTT_006 | Xác minh thêm sách mới thành công khi điền đầy đủ thông tin hợp lệ | P1 | Positive | Đã đăng nhập admin, tồn tại ít nhất 1 danh mục | title: "Auto Test Book", author: "Auto Author", price: 99000, category: hợp lệ | Flash message "added successfully!", sách xuất hiện trong danh sách | Script: login admin → `/admin/books` → điền form → submit → verify flash & book in list |
| TC_BTT_007 | Xác minh thêm sách bị từ chối khi thiếu trường tiêu đề | P2 | Negative | Đã đăng nhập admin | title: "", author: "Auto Author", price: 99000 | Hiển thị thông báo lỗi, không thêm sách vào danh sách | Script: login admin → `/admin/books` → để trống title → submit → verify error |
| TC_BTT_008 | Xác minh cập nhật thông tin sách thành công khi thay đổi giá | P1 | Positive | Đã đăng nhập admin, tồn tại ít nhất 1 sách | price mới: 150000 | Flash message "updated successfully!", giá mới hiển thị đúng trong danh sách | Script: login admin → `/admin/books` → click Sửa → thay giá → lưu → verify flash & new price |
| TC_BTT_009 | Xác minh vô hiệu hóa sách thành công khi nhấn nút xóa trên sách đang active | P1 | Positive | Đã đăng nhập admin, tồn tại sách đang active | Sách đầu tiên trong danh sách | Sách chuyển trạng thái inactive, nút Kích hoạt xuất hiện | Script: login admin → `/admin/books` → ghi nhận tên sách → click Xóa → verify status inactive |
| TC_BTT_010 | Xác minh kích hoạt lại sách thành công khi sách đang ở trạng thái inactive | P2 | Positive | Đã đăng nhập admin, tồn tại sách inactive | Sách đang inactive | Sách chuyển thành trạng thái active, hiển thị lại trên trang người dùng | Script: login admin → `/admin/books` → tìm sách inactive → click Kích hoạt → verify status active |
| TC_BTT_011 | Xác minh tìm kiếm sách hiển thị đúng kết quả khi nhập từ khóa hợp lệ | P2 | Positive | Đã đăng nhập admin, tồn tại sách trong hệ thống | Từ khóa: "Lập trình" | Chỉ hiển thị sách có tên hoặc tác giả chứa từ khóa | Script: login admin → `/admin/books` → nhập keyword → Enter → verify results match keyword |
| TC_BTT_012 | Xác minh thêm danh mục mới thành công khi điền tên hợp lệ | P1 | Positive | Đã đăng nhập admin | Tên danh mục: "Auto Category" | Danh mục "Auto Category" xuất hiện trong danh sách | Script: login admin → `/admin/categories` → điền tên → click Thêm → verify category in list |
| TC_BTT_013 | Xác minh cập nhật tên danh mục thành công khi nhập tên mới | P2 | Positive | Đã đăng nhập admin, tồn tại ít nhất 1 danh mục | Tên mới: "Updated Category" | Tên danh mục được cập nhật thành "Updated Category" trong danh sách | Script: login admin → `/admin/categories` → click Sửa → thay tên → lưu → verify new name |
| TC_BTT_014 | Xác minh xóa danh mục thành công khi danh mục không có sách liên kết | P2 | Positive | Đã đăng nhập admin, tạo sẵn danh mục trống | Danh mục vừa tạo không có sách | Danh mục biến mất khỏi danh sách | Script: login admin → tạo danh mục mới → click Xóa → xác nhận → verify category removed |
| TC_BTT_015 | Xác minh hồ sơ người dùng hiển thị đúng thông tin khi đăng nhập | P2 | Positive | Tài khoản user hợp lệ đã đăng nhập | Tài khoản user thường | Trang hiển thị đúng username, email, tổng đơn hàng và tổng tiền đã chi | Script: login user → `/profile` → verify username, email, order stats visible |
| TC_BTT_016 | Xác minh cập nhật hồ sơ thành công khi thay đổi tên và số điện thoại | P2 | Positive | Tài khoản user hợp lệ đã đăng nhập | name: "New Name", phone: "0987654321" | Flash message "Profile updated successfully!", thông tin mới hiển thị trên profile | Script: login user → `/profile` → điền name & phone → submit → verify flash & new info |
| TC_BTT_017 | Xác minh đổi mật khẩu thành công khi nhập đúng mật khẩu hiện tại | P1 | Positive | Tài khoản user hợp lệ đã đăng nhập | currentPassword: đúng, newPassword: "NewPass@123", confirmPassword: "NewPass@123" | Flash message "Password changed successfully!", đăng nhập lại được bằng mật khẩu mới | Script: login user → `/profile` → điền form đổi mật khẩu → submit → verify flash → re-login |
| TC_BTT_018 | Xác minh đổi mật khẩu bị từ chối khi nhập sai mật khẩu hiện tại | P1 | Negative | Tài khoản user hợp lệ đã đăng nhập | currentPassword: "WrongCurrentPass", newPassword: "NewPass@123" | Hiển thị lỗi "Current password is incorrect", mật khẩu không thay đổi | Script: login user → `/profile` → điền sai currentPassword → submit → verify error message |
| TC_BTT_019 | Xác minh truy cập admin bị từ chối khi đăng nhập bằng tài khoản không có role ADMIN | P1 | Negative | Tài khoản user thường đã đăng nhập | Tài khoản không có role ADMIN | Hệ thống chuyển hướng về `/error/403` hoặc hiển thị trang Forbidden | Script: login user thường → truy cập `/admin` → verify URL contains `/403` or page shows Forbidden |
| TC_BTT_020 | Xác minh redirect về trang login khi truy cập trang cần xác thực chưa đăng nhập | P1 | Negative | Chưa đăng nhập (session không tồn tại) | None | URL chuyển thành `/login`, nội dung trang bảo mật không hiển thị | Script: đảm bảo chưa đăng nhập → truy cập `/profile` → verify URL contains `/login` |

---

## Test Cases Summary

| TC ID | Module | Test Case Title | Priority | Type | Status | Assigned To | Created Date | Due Date |
|-------|--------|-----------------|----------|------|--------|-------------|--------------|----------|
| TC_BTT_001 | Registration | Xác minh đăng ký tài khoản thành công khi nhập thông tin hợp lệ | P1 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_002 | Registration | Xác minh đăng ký bị từ chối khi username đã tồn tại | P1 | Negative | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_003 | Registration | Xác minh đăng ký bị từ chối khi email đã được sử dụng | P1 | Negative | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_004 | Registration | Xác minh đăng ký bị từ chối khi số điện thoại không đủ 10 chữ số | P2 | Negative | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_005 | Book Management | Xác minh dashboard admin hiển thị đúng số liệu thống kê khi đăng nhập admin | P1 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_006 | Book Management | Xác minh thêm sách mới thành công khi điền đầy đủ thông tin hợp lệ | P1 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_007 | Book Management | Xác minh thêm sách bị từ chối khi thiếu trường tiêu đề | P2 | Negative | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_008 | Book Management | Xác minh cập nhật thông tin sách thành công khi thay đổi giá | P1 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_009 | Book Management | Xác minh vô hiệu hóa sách thành công khi nhấn nút xóa trên sách đang active | P1 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_010 | Book Management | Xác minh kích hoạt lại sách thành công khi sách đang ở trạng thái inactive | P2 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_011 | Book Management | Xác minh tìm kiếm sách hiển thị đúng kết quả khi nhập từ khóa hợp lệ | P2 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_012 | Category Management | Xác minh thêm danh mục mới thành công khi điền tên hợp lệ | P1 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_013 | Category Management | Xác minh cập nhật tên danh mục thành công khi nhập tên mới | P2 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_014 | Category Management | Xác minh xóa danh mục thành công khi danh mục không có sách liên kết | P2 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_015 | User Profile | Xác minh hồ sơ người dùng hiển thị đúng thông tin khi đăng nhập | P2 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_016 | User Profile | Xác minh cập nhật hồ sơ thành công khi thay đổi tên và số điện thoại | P2 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_017 | User Profile | Xác minh đổi mật khẩu thành công khi nhập đúng mật khẩu hiện tại | P1 | Positive | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_018 | User Profile | Xác minh đổi mật khẩu bị từ chối khi nhập sai mật khẩu hiện tại | P1 | Negative | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_019 | Security | Xác minh truy cập admin bị từ chối khi đăng nhập bằng tài khoản không có role ADMIN | P1 | Negative | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
| TC_BTT_020 | Security | Xác minh redirect về trang login khi truy cập trang cần xác thực chưa đăng nhập | P1 | Negative | | Bùi Tấn Thành | 2026-03-24 | 2026-04-07 |
