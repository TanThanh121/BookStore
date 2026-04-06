# Test Cases - Nguyễn Hoàng Minh
**Loại kiểm thử:** Hộp Trắng (White Box)
**Số lượng:** 20 Test Cases
**Phạm vi:** UserService, InvoiceService, Validators — kiểm thử logic nội bộ, nhánh điều kiện và xử lý ngoại lệ

---

## Chức năng: UserService

### Test Case TC_NHM_001
- **Summary:** `save` mã hóa mật khẩu trước khi lưu user mới
- **Phương thức kiểm thử:** `UserService.save(user)` — luồng mã hóa mật khẩu
- **Các bước thực hiện:**
  - Bước 1: Tạo đối tượng `User` với password `"plaintext123"`.
  - Bước 2: Gọi `userService.save(user)`.
  - Bước 3: Truy vấn lại `User` từ CSDL và kiểm tra trường `password`.
- **Luồng code kiểm tra:** `passwordEncoder.encode(user.getPassword())` → `userRepository.save(user)`
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Mật khẩu trong CSDL là chuỗi đã được mã hóa BCrypt, khác với `"plaintext123"`.

---

### Test Case TC_NHM_002
- **Summary:** `findByUsername` trả về `Optional<User>` khi username tồn tại
- **Phương thức kiểm thử:** `UserService.findByUsername("admin")`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị user `"admin"` tồn tại trong CSDL.
  - Bước 2: Gọi `userService.findByUsername("admin")`.
  - Bước 3: Kiểm tra giá trị Optional trả về.
- **Luồng code kiểm tra:** `userRepository.findByUsername("admin")` — nhánh tìm thấy
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về `Optional.of(user)` với username = `"admin"`.

---

### Test Case TC_NHM_003
- **Summary:** `findByUsername` trả về `Optional.empty()` khi username không tồn tại
- **Phương thức kiểm thử:** `UserService.findByUsername("nonexistent")`
- **Các bước thực hiện:**
  - Bước 1: Đảm bảo không có user `"nonexistent"`.
  - Bước 2: Gọi `userService.findByUsername("nonexistent")`.
  - Bước 3: Kiểm tra Optional.
- **Luồng code kiểm tra:** `userRepository.findByUsername("nonexistent")` — nhánh không tìm thấy
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trả về `Optional.empty()`, không ném ngoại lệ.

---

### Test Case TC_NHM_004
- **Summary:** `setDefaultRole` gán role `USER` cho tài khoản mới
- **Phương thức kiểm thử:** `UserService.setDefaultRole(username)`
- **Các bước thực hiện:**
  - Bước 1: Lưu user mới chưa có role nào.
  - Bước 2: Gọi `userService.setDefaultRole(user.getUsername())`.
  - Bước 3: Truy vấn lại user và kiểm tra roles.
- **Luồng code kiểm tra:** Tìm role `USER` trong `roleRepository` → gán cho user → lưu
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** User có đúng 1 role là `USER`.

---

### Test Case TC_NHM_005
- **Summary:** `updateProfile` cập nhật thành công email, name, phone hợp lệ
- **Phương thức kiểm thử:** `UserService.updateProfile(username, email, name, phone)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị user tồn tại.
  - Bước 2: Gọi `userService.updateProfile("testuser", "new@email.com", "New Name", "0123456789")`.
  - Bước 3: Truy vấn lại user và kiểm tra các trường.
- **Luồng code kiểm tra:** Tìm user → cập nhật fields → lưu — nhánh không có xung đột
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Thông tin user được cập nhật đúng.

---

### Test Case TC_NHM_006
- **Summary:** `updateProfile` ném `IllegalArgumentException` khi email đã tồn tại ở user khác
- **Phương thức kiểm thử:** `UserService.updateProfile` — nhánh email trùng
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị user A với email `a@test.com` và user B với email `b@test.com`.
  - Bước 2: Gọi `userService.updateProfile("userB", "a@test.com", ...)`.
  - Bước 3: Kiểm tra ngoại lệ được ném.
- **Luồng code kiểm tra:** Kiểm tra email trùng → ném `IllegalArgumentException`
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Ném `IllegalArgumentException` với thông báo lỗi email trùng.

---

### Test Case TC_NHM_007
- **Summary:** `changePassword` đổi mật khẩu thành công khi mật khẩu hiện tại đúng và mật khẩu mới khớp xác nhận
- **Phương thức kiểm thử:** `UserService.changePassword(username, currentPw, newPw, confirmPw)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị user với mật khẩu đã biết.
  - Bước 2: Gọi changePassword với `currentPw` đúng, `newPw = confirmPw = "NewPass@123"`.
  - Bước 3: Kiểm tra không có ngoại lệ và password được cập nhật.
- **Luồng code kiểm tra:** `passwordEncoder.matches(current, stored)` → true → `passwordEncoder.encode(newPw)` → lưu
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Mật khẩu được cập nhật thành công trong CSDL.

---

### Test Case TC_NHM_008
- **Summary:** `changePassword` ném ngoại lệ khi mật khẩu hiện tại sai
- **Phương thức kiểm thử:** `UserService.changePassword` — nhánh mật khẩu cũ sai
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị user.
  - Bước 2: Gọi changePassword với `currentPw = "WrongPassword"`.
  - Bước 3: Kiểm tra ngoại lệ.
- **Luồng code kiểm tra:** `passwordEncoder.matches(wrong, stored)` → false → ném ngoại lệ
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Ném `IllegalArgumentException` với thông báo "Current password is incorrect".

---

### Test Case TC_NHM_009
- **Summary:** `changePassword` ném ngoại lệ khi mật khẩu mới và xác nhận không khớp
- **Phương thức kiểm thử:** `UserService.changePassword` — nhánh mật khẩu xác nhận sai
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị user.
  - Bước 2: Gọi changePassword với `currentPw` đúng, `newPw = "NewPass1"`, `confirmPw = "NewPass2"`.
  - Bước 3: Kiểm tra ngoại lệ.
- **Luồng code kiểm tra:** `newPw.equals(confirmPw)` → false → ném ngoại lệ
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Ném `IllegalArgumentException` với thông báo mật khẩu không khớp.

---

### Test Case TC_NHM_010
- **Summary:** `countUsers` trả về đúng tổng số người dùng
- **Phương thức kiểm thử:** `UserService.countUsers()`
- **Các bước thực hiện:**
  - Bước 1: Biết trước số lượng user trong CSDL.
  - Bước 2: Gọi `userService.countUsers()`.
  - Bước 3: So sánh kết quả.
- **Luồng code kiểm tra:** `userRepository.count()`
- **Độ ưu tiên:** P3 (Medium)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về đúng số bản ghi trong bảng `user`.

---

## Chức năng: InvoiceService

### Test Case TC_NHM_011
- **Summary:** `getInvoicesByUsername` trả về đúng danh sách đơn hàng của user
- **Phương thức kiểm thử:** `InvoiceService.getInvoicesByUsername(username)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị user có 3 đơn hàng.
  - Bước 2: Gọi `invoiceService.getInvoicesByUsername(username)`.
  - Bước 3: Kiểm tra kích thước danh sách.
- **Luồng code kiểm tra:** `invoiceRepository.findByUserUsername(username)`
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về đúng 3 đơn hàng của user.

---

### Test Case TC_NHM_012
- **Summary:** `getInvoicesByUsername` trả về danh sách rỗng khi user chưa có đơn hàng
- **Phương thức kiểm thử:** `InvoiceService.getInvoicesByUsername(username)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị user mới chưa đặt hàng.
  - Bước 2: Gọi `invoiceService.getInvoicesByUsername(username)`.
  - Bước 3: Kiểm tra danh sách.
- **Luồng code kiểm tra:** `invoiceRepository.findByUserUsername` — nhánh empty
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trả về danh sách rỗng, không ném ngoại lệ.

---

### Test Case TC_NHM_013
- **Summary:** `getInvoiceById` trả về `Optional<Invoice>` khi id tồn tại
- **Phương thức kiểm thử:** `InvoiceService.getInvoiceById(id)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị đơn hàng tồn tại với id hợp lệ.
  - Bước 2: Gọi `invoiceService.getInvoiceById(id)`.
  - Bước 3: Kiểm tra Optional.
- **Luồng code kiểm tra:** `invoiceRepository.findById(id)` — nhánh tìm thấy
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về `Optional.of(invoice)` với đúng thông tin đơn hàng.

---

### Test Case TC_NHM_014
- **Summary:** `getInvoiceById` trả về `Optional.empty()` khi id không tồn tại
- **Phương thức kiểm thử:** `InvoiceService.getInvoiceById(9999L)`
- **Các bước thực hiện:**
  - Bước 1: Đảm bảo không có invoice với id `9999`.
  - Bước 2: Gọi `invoiceService.getInvoiceById(9999L)`.
  - Bước 3: Kiểm tra kết quả.
- **Luồng code kiểm tra:** `invoiceRepository.findById(9999L)` — nhánh không tìm thấy
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trả về `Optional.empty()`.

---

### Test Case TC_NHM_015
- **Summary:** `calculateInvoiceTotal` tính đúng tổng tiền từ danh sách `ItemInvoice`
- **Phương thức kiểm thử:** `InvoiceService.calculateInvoiceTotal(invoice)`
- **Các bước thực hiện:**
  - Bước 1: Tạo `Invoice` với 2 `ItemInvoice`: sách A giá 100.000 x2, sách B giá 50.000 x3.
  - Bước 2: Gọi `invoiceService.calculateInvoiceTotal(invoice)`.
  - Bước 3: Kiểm tra kết quả.
- **Luồng code kiểm tra:** Tính tổng `price * quantity` cho từng item
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Tổng = `100.000 * 2 + 50.000 * 3 = 350.000`.

---

### Test Case TC_NHM_016
- **Summary:** `countInvoices` trả về đúng tổng số đơn hàng
- **Phương thức kiểm thử:** `InvoiceService.countInvoices()`
- **Các bước thực hiện:**
  - Bước 1: Biết trước số lượng invoice trong CSDL.
  - Bước 2: Gọi `invoiceService.countInvoices()`.
  - Bước 3: So sánh kết quả.
- **Luồng code kiểm tra:** `invoiceRepository.count()`
- **Độ ưu tiên:** P3 (Medium)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Kết quả bằng tổng số bản ghi trong bảng `invoice`.

---

## Chức năng: Validators

### Test Case TC_NHM_017
- **Summary:** `ValidUsernameValidator` chấp nhận username chỉ chứa chữ cái và chữ số
- **Phương thức kiểm thử:** `ValidUsernameValidator.isValid("validuser123", context)`
- **Các bước thực hiện:**
  - Bước 1: Khởi tạo `ValidUsernameValidator`.
  - Bước 2: Gọi `isValid("validuser123", context)`.
  - Bước 3: Kiểm tra kết quả boolean.
- **Luồng code kiểm tra:** Regex kiểm tra chỉ alpha-numeric — nhánh hợp lệ
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về `true`.

---

### Test Case TC_NHM_018
- **Summary:** `ValidUsernameValidator` từ chối username chứa ký tự đặc biệt
- **Phương thức kiểm thử:** `ValidUsernameValidator.isValid("invalid@user!", context)`
- **Các bước thực hiện:**
  - Bước 1: Khởi tạo `ValidUsernameValidator`.
  - Bước 2: Gọi `isValid("invalid@user!", context)`.
  - Bước 3: Kiểm tra kết quả boolean.
- **Luồng code kiểm tra:** Regex kiểm tra — nhánh không hợp lệ
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trả về `false`.

---

### Test Case TC_NHM_019
- **Summary:** `ValidCategoryIdValidator` chấp nhận sách có category hợp lệ
- **Phương thức kiểm thử:** `ValidCategoryIdValidator.isValid(category, context)`
- **Các bước thực hiện:**
  - Bước 1: Tạo `Category` tồn tại trong CSDL.
  - Bước 2: Gọi `isValid(category, context)`.
  - Bước 3: Kiểm tra kết quả.
- **Luồng code kiểm tra:** `categoryRepository.existsById(id)` — nhánh tìm thấy
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về `true`.

---

### Test Case TC_NHM_020
- **Summary:** `ValidCategoryIdValidator` từ chối category null hoặc không tồn tại
- **Phương thức kiểm thử:** `ValidCategoryIdValidator.isValid(null, context)`
- **Các bước thực hiện:**
  - Bước 1: Khởi tạo `ValidCategoryIdValidator`.
  - Bước 2: Gọi `isValid(null, context)`.
  - Bước 3: Kiểm tra kết quả.
- **Luồng code kiểm tra:** `category == null` → trả về `false` — nhánh null
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trả về `false`.

---

## Test Report

| STT | Test Case ID | Chức năng | Mô tả ngắn | Bản chất | Độ ưu tiên | Phương thức kiểm thử | Kết quả mong muốn | Kết quả thực tế | Trạng thái | Ngày test | Ghi chú |
|-----|-------------|-----------|------------|----------|------------|----------------------|-------------------|-----------------|------------|-----------|---------|
| 1 | TC_NHM_001 | UserService | save mã hóa mật khẩu | Positive | P1 | `save(user)` | Password được BCrypt | | | | |
| 2 | TC_NHM_002 | UserService | findByUsername tìm thấy | Positive | P1 | `findByUsername("admin")` | Optional.of(user) | | | | |
| 3 | TC_NHM_003 | UserService | findByUsername không tìm thấy | Negative | P1 | `findByUsername("nonexistent")` | Optional.empty() | | | | |
| 4 | TC_NHM_004 | UserService | setDefaultRole gán role USER | Positive | P1 | `setDefaultRole(username)` | User có role USER | | | | |
| 5 | TC_NHM_005 | UserService | updateProfile thành công | Positive | P1 | `updateProfile(...)` | Thông tin cập nhật | | | | |
| 6 | TC_NHM_006 | UserService | updateProfile email trùng | Negative | P2 | `updateProfile` email trùng | IllegalArgumentException | | | | |
| 7 | TC_NHM_007 | UserService | changePassword thành công | Positive | P1 | `changePassword(...)` | Password cập nhật | | | | |
| 8 | TC_NHM_008 | UserService | changePassword sai mật khẩu cũ | Negative | P1 | `changePassword` wrong current | IllegalArgumentException | | | | |
| 9 | TC_NHM_009 | UserService | changePassword xác nhận sai | Negative | P1 | `changePassword` confirm != new | IllegalArgumentException | | | | |
| 10 | TC_NHM_010 | UserService | countUsers | Positive | P3 | `countUsers()` | Đúng tổng số | | | | |
| 11 | TC_NHM_011 | InvoiceService | getInvoicesByUsername có đơn | Positive | P1 | `getInvoicesByUsername(user)` | Danh sách 3 đơn | | | | |
| 12 | TC_NHM_012 | InvoiceService | getInvoicesByUsername chưa đặt | Negative | P2 | `getInvoicesByUsername(newUser)` | Danh sách rỗng | | | | |
| 13 | TC_NHM_013 | InvoiceService | getInvoiceById tìm thấy | Positive | P1 | `getInvoiceById(id)` | Optional.of(invoice) | | | | |
| 14 | TC_NHM_014 | InvoiceService | getInvoiceById không tìm thấy | Negative | P1 | `getInvoiceById(9999L)` | Optional.empty() | | | | |
| 15 | TC_NHM_015 | InvoiceService | calculateInvoiceTotal | Positive | P1 | `calculateInvoiceTotal(inv)` | 350.000 | | | | |
| 16 | TC_NHM_016 | InvoiceService | countInvoices | Positive | P3 | `countInvoices()` | Đúng tổng số | | | | |
| 17 | TC_NHM_017 | Validator | ValidUsernameValidator hợp lệ | Positive | P2 | `isValid("validuser123")` | true | | | | |
| 18 | TC_NHM_018 | Validator | ValidUsernameValidator ký tự đặc biệt | Negative | P2 | `isValid("invalid@!")` | false | | | | |
| 19 | TC_NHM_019 | Validator | ValidCategoryIdValidator hợp lệ | Positive | P2 | `isValid(category)` | true | | | | |
| 20 | TC_NHM_020 | Validator | ValidCategoryIdValidator null | Negative | P2 | `isValid(null)` | false | | | | |

**Tổng kết:**
- Tổng số test case: **20**
- Positive: **13**
- Negative: **7**
- Pass: ___
- Fail: ___
- Blocked: ___
