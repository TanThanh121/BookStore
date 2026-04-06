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

| TC ID | Test Case | Priority | Type | Pre-condition | Test Data | Expected Result | Test Script Design |
|-------|-----------|----------|------|---------------|-----------|-----------------|-------------------|
| TC_NHM_001 | Xác minh save mã hóa mật khẩu bằng BCrypt trước khi lưu user mới vào DB | P1 | Positive | None | User{ password="plaintext123" } | Mật khẩu trong DB là chuỗi BCrypt, khác với "plaintext123" | Mock encoder + repo → call `save(user)` → assert password != "plaintext123" & BCryptMatches |
| TC_NHM_002 | Xác minh findByUsername trả về Optional chứa user khi username tồn tại | P1 | Positive | User "admin" tồn tại trong DB | username="admin" | Optional.of(user) với username=="admin" | Mock `findByUsername("admin")` → call `findByUsername("admin")` → assert isPresent() |
| TC_NHM_003 | Xác minh findByUsername trả về Optional.empty() khi username không tồn tại | P1 | Negative | Không tồn tại user "nonexistent" | username="nonexistent" | Optional.empty(), không ném ngoại lệ | Mock return empty → call `findByUsername("nonexistent")` → assert isEmpty() |
| TC_NHM_004 | Xác minh setDefaultRole gán đúng role USER cho user mới khi chưa có role | P1 | Positive | User tồn tại trong DB chưa có role | username của user mới | User được gán role USER trong DB | Mock findByUsername + roleRepo → call `setDefaultRole(username)` → assert role == USER |
| TC_NHM_005 | Xác minh updateProfile cập nhật thông tin thành công khi dữ liệu hợp lệ | P1 | Positive | User tồn tại trong DB | userId, name mới, phone mới, email mới hợp lệ | Thông tin profile được cập nhật đúng trong DB | Mock findById + save → call `updateProfile(...)` → assert fields updated |
| TC_NHM_006 | Xác minh updateProfile ném IllegalArgumentException khi email đã được dùng bởi user khác | P2 | Negative | Email đã tồn tại cho user khác trong DB | userId, email đã tồn tại | Ném `IllegalArgumentException` với thông báo lỗi email trùng | Mock findByEmail return other user → call `updateProfile` → assert throws IllegalArgumentException |
| TC_NHM_007 | Xác minh changePassword cập nhật mật khẩu thành công khi nhập đúng mật khẩu hiện tại | P1 | Positive | User tồn tại, biết mật khẩu hiện tại | currentPassword đúng, newPassword="NewPass@123", confirmPassword="NewPass@123" | Mật khẩu mới được mã hóa và lưu vào DB | Mock encoder.matches=true + save → call `changePassword(...)` → assert new password encoded |
| TC_NHM_008 | Xác minh changePassword ném IllegalArgumentException khi nhập sai mật khẩu hiện tại | P1 | Negative | User tồn tại trong DB | currentPassword sai, newPassword="NewPass@123" | Ném `IllegalArgumentException` "Current password is incorrect" | Mock encoder.matches=false → call `changePassword` → assert throws IllegalArgumentException |
| TC_NHM_009 | Xác minh changePassword ném IllegalArgumentException khi mật khẩu xác nhận không khớp | P1 | Negative | User tồn tại trong DB | currentPassword đúng, newPassword="New@123", confirmPassword="Different@123" | Ném `IllegalArgumentException` do confirm không khớp | Mock encoder.matches=true → call `changePassword` confirm!=new → assert throws IllegalArgumentException |
| TC_NHM_010 | Xác minh countUsers trả về đúng tổng số người dùng trong hệ thống | P3 | Positive | Có N user trong DB | None | Trả về N đúng với số user thực tế | Mock `count()` return N → call `countUsers()` → assert result == N |
| TC_NHM_011 | Xác minh getInvoicesByUsername trả về đúng danh sách đơn hàng khi user đã có đơn | P1 | Positive | User đã có 3 đơn hàng trong DB | username của user có 3 đơn | Danh sách 3 invoice đúng | Mock repo → call `getInvoicesByUsername(user)` → assert list.size() == 3 |
| TC_NHM_012 | Xác minh getInvoicesByUsername trả về danh sách rỗng khi user chưa đặt hàng | P2 | Negative | User mới chưa có đơn hàng nào | username của user mới | Danh sách rỗng, không ném ngoại lệ | Mock return empty list → call `getInvoicesByUsername(newUser)` → assert isEmpty() |
| TC_NHM_013 | Xác minh getInvoiceById trả về Optional chứa invoice khi ID tồn tại | P1 | Positive | Invoice với id hợp lệ tồn tại trong DB | id hợp lệ | Optional.of(invoice) với đúng thông tin đơn hàng | Mock `findById(id)` → call `getInvoiceById(id)` → assert isPresent() |
| TC_NHM_014 | Xác minh getInvoiceById trả về Optional.empty() khi ID không tồn tại | P1 | Negative | Không tồn tại invoice với id=9999 | id=9999L | Optional.empty(), không ném ngoại lệ | Mock return empty → call `getInvoiceById(9999L)` → assert isEmpty() |
| TC_NHM_015 | Xác minh calculateInvoiceTotal tính đúng tổng tiền khi invoice có nhiều item | P1 | Positive | Invoice với 3 item giá 100, 150, 100 | Invoice{ items[ price×qty ] } | Trả về 350.0 | Mock invoice with items → call `calculateInvoiceTotal(inv)` → assert == 350.0 |
| TC_NHM_016 | Xác minh countInvoices trả về đúng tổng số đơn hàng trong hệ thống | P3 | Positive | Có N invoice trong DB | None | Trả về N đúng với số invoice thực tế | Mock `count()` return N → call `countInvoices()` → assert result == N |
| TC_NHM_017 | Xác minh ValidUsernameValidator trả về true khi username chỉ chứa chữ và số | P2 | Positive | None | username="validuser123" | Trả về true, không có lỗi validation | Call `isValid("validuser123", context)` → assert result == true |
| TC_NHM_018 | Xác minh ValidUsernameValidator trả về false khi username chứa ký tự đặc biệt | P2 | Negative | None | username="invalid@!" | Trả về false, vi phạm constraint | Call `isValid("invalid@!", context)` → assert result == false |
| TC_NHM_019 | Xác minh ValidCategoryIdValidator trả về true khi category hợp lệ tồn tại trong DB | P2 | Positive | Danh mục với id hợp lệ tồn tại | category object hợp lệ | Trả về true, category hợp lệ | Mock repo return category → call `isValid(category, context)` → assert result == true |
| TC_NHM_020 | Xác minh ValidCategoryIdValidator trả về false khi category là null | P2 | Negative | None | category=null | Trả về false, không ném NullPointerException | Call `isValid(null, context)` → assert result == false |

**Tổng kết:**
- Tổng số test case: **20**
- Positive: **13**
- Negative: **7**
- Pass: ___
- Fail: ___
- Blocked: ___

---

## Test Cases Summary

| TC ID | Module | Test Case Title | Priority | Type | Status | Assigned To | Created Date | Due Date |
|-------|--------|-----------------|----------|------|--------|-------------|--------------|----------|
| TC_NHM_001 | UserService | Xác minh save mã hóa mật khẩu bằng BCrypt trước khi lưu user mới vào DB | P1 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_002 | UserService | Xác minh findByUsername trả về Optional chứa user khi username tồn tại | P1 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_003 | UserService | Xác minh findByUsername trả về Optional.empty() khi username không tồn tại | P1 | Negative | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_004 | UserService | Xác minh setDefaultRole gán đúng role USER cho user mới khi chưa có role | P1 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_005 | UserService | Xác minh updateProfile cập nhật thông tin thành công khi dữ liệu hợp lệ | P1 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_006 | UserService | Xác minh updateProfile ném IllegalArgumentException khi email đã được dùng bởi user khác | P2 | Negative | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_007 | UserService | Xác minh changePassword cập nhật mật khẩu thành công khi nhập đúng mật khẩu hiện tại | P1 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_008 | UserService | Xác minh changePassword ném IllegalArgumentException khi nhập sai mật khẩu hiện tại | P1 | Negative | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_009 | UserService | Xác minh changePassword ném IllegalArgumentException khi mật khẩu xác nhận không khớp | P1 | Negative | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_010 | UserService | Xác minh countUsers trả về đúng tổng số người dùng trong hệ thống | P3 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_011 | InvoiceService | Xác minh getInvoicesByUsername trả về đúng danh sách đơn hàng khi user đã có đơn | P1 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_012 | InvoiceService | Xác minh getInvoicesByUsername trả về danh sách rỗng khi user chưa đặt hàng | P2 | Negative | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_013 | InvoiceService | Xác minh getInvoiceById trả về Optional chứa invoice khi ID tồn tại | P1 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_014 | InvoiceService | Xác minh getInvoiceById trả về Optional.empty() khi ID không tồn tại | P1 | Negative | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_015 | InvoiceService | Xác minh calculateInvoiceTotal tính đúng tổng tiền khi invoice có nhiều item | P1 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_016 | InvoiceService | Xác minh countInvoices trả về đúng tổng số đơn hàng trong hệ thống | P3 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_017 | Validator | Xác minh ValidUsernameValidator trả về true khi username chỉ chứa chữ và số | P2 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_018 | Validator | Xác minh ValidUsernameValidator trả về false khi username chứa ký tự đặc biệt | P2 | Negative | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_019 | Validator | Xác minh ValidCategoryIdValidator trả về true khi category hợp lệ tồn tại trong DB | P2 | Positive | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
| TC_NHM_020 | Validator | Xác minh ValidCategoryIdValidator trả về false khi category là null | P2 | Negative | | Nguyễn Hoàng Minh | 2026-03-24 | 2026-04-07 |
