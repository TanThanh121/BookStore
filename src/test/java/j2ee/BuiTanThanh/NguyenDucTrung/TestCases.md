# Test Cases - Nguyễn Đức Trung
**Loại kiểm thử:** Hộp Trắng (White Box)
**Số lượng:** 20 Test Cases
**Phạm vi:** BookService, CategoryService, CartService — kiểm thử logic nội bộ, nhánh điều kiện và luồng xử lý

---

## Chức năng: BookService

### Test Case TC_NDT_001
- **Summary:** `getAllBooks` trả về đúng danh sách sách với phân trang (pageNo=0, pageSize=5)
- **Phương thức kiểm thử:** `BookService.getAllBooks(0, 5, "id")`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị dữ liệu có ít nhất 6 sách trong CSDL.
  - Bước 2: Gọi `bookService.getAllBooks(0, 5, "id")`.
  - Bước 3: Kiểm tra kích thước danh sách trả về.
- **Luồng code kiểm tra:** `IBookRepository.findAllBooks(0, 5, "id")` — nhánh phân trang đúng
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về đúng 5 sách, `pageNo=0` tương ứng bản ghi đầu tiên.

---

### Test Case TC_NDT_002
- **Summary:** `getBookById` trả về sách khi ID tồn tại và sách đang active
- **Phương thức kiểm thử:** `BookService.getBookById(id)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị một sách đang `active=true` với id hợp lệ.
  - Bước 2: Gọi `bookService.getBookById(id)`.
  - Bước 3: Kiểm tra `Optional` trả về.
- **Luồng code kiểm tra:** `IBookRepository.findActiveByIdWithCategory(id)` — nhánh tìm thấy
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về `Optional.of(book)` với đúng thông tin sách.

---

### Test Case TC_NDT_003
- **Summary:** `getBookById` trả về `Optional.empty()` khi ID không tồn tại
- **Phương thức kiểm thử:** `BookService.getBookById(9999L)`
- **Các bước thực hiện:**
  - Bước 1: Đảm bảo không có sách nào với id `9999`.
  - Bước 2: Gọi `bookService.getBookById(9999L)`.
  - Bước 3: Kiểm tra kết quả trả về.
- **Luồng code kiểm tra:** `IBookRepository.findActiveByIdWithCategory(9999L)` — nhánh không tìm thấy
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Trả về `Optional.empty()`, không ném ngoại lệ.

---

### Test Case TC_NDT_004
- **Summary:** `addBook` lưu sách mới thành công vào CSDL
- **Phương thức kiểm thử:** `BookService.addBook(book)`
- **Các bước thực hiện:**
  - Bước 1: Tạo đối tượng `Book` mới với `title`, `author`, `price` hợp lệ.
  - Bước 2: Gọi `bookService.addBook(book)`.
  - Bước 3: Truy vấn lại CSDL để kiểm tra sách đã được lưu.
- **Luồng code kiểm tra:** `bookRepository.save(book)` — luồng persist thành công
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sách được lưu với `id` tự sinh, `active=true` theo mặc định.

---

### Test Case TC_NDT_005
- **Summary:** `updateBook` cập nhật đúng `title`, `author`, `price` khi sách tồn tại
- **Phương thức kiểm thử:** `BookService.updateBook(book)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách tồn tại với id hợp lệ.
  - Bước 2: Tạo đối tượng `book` mới với cùng id nhưng title mới `"Updated Title"`.
  - Bước 3: Gọi `bookService.updateBook(book)`.
  - Bước 4: Truy vấn lại và kiểm tra title.
- **Luồng code kiểm tra:** `bookRepository.findById(id)` → cập nhật fields → `bookRepository.save`
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sách trong CSDL có `title = "Updated Title"`.

---

### Test Case TC_NDT_006
- **Summary:** `updateBook` không ghi đè ảnh cũ khi ảnh mới rỗng
- **Phương thức kiểm thử:** `BookService.updateBook(book)` — nhánh `image == null || image.isEmpty()`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách đang có `image = "old_image.jpg"`.
  - Bước 2: Tạo đối tượng `book` cập nhật với `image = ""` (rỗng).
  - Bước 3: Gọi `bookService.updateBook(book)`.
  - Bước 4: Kiểm tra giá trị `image` trong CSDL.
- **Luồng code kiểm tra:** `if (book.getImage() != null && !book.getImage().isEmpty())` — nhánh `false`
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** `image` giữ nguyên `"old_image.jpg"`, không bị ghi đè.

---

### Test Case TC_NDT_007
- **Summary:** `deleteBookById` đặt trạng thái `active=false` thay vì xóa vật lý
- **Phương thức kiểm thử:** `BookService.deleteBookById(id)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách tồn tại với `active=true`.
  - Bước 2: Gọi `bookService.deleteBookById(id)`.
  - Bước 3: Truy vấn lại sách qua `bookRepository.findById(id)`.
- **Luồng code kiểm tra:** `book.setActive(false)` → `bookRepository.save(book)` — soft delete
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Sách vẫn còn trong CSDL nhưng `active=false`.

---

### Test Case TC_NDT_008
- **Summary:** `activateBookById` đặt lại `active=true` cho sách đã bị vô hiệu hóa
- **Phương thức kiểm thử:** `BookService.activateBookById(id)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách với `active=false`.
  - Bước 2: Gọi `bookService.activateBookById(id)`.
  - Bước 3: Truy vấn lại và kiểm tra `active`.
- **Luồng code kiểm tra:** `book.setActive(true)` → `bookRepository.save(book)`
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** `active=true` sau khi gọi phương thức.

---

### Test Case TC_NDT_009
- **Summary:** `searchActiveBook` trả về sách phù hợp với từ khóa (chỉ sách active)
- **Phương thức kiểm thử:** `BookService.searchActiveBook("Java")`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị 2 sách active có tên chứa "Java" và 1 sách inactive.
  - Bước 2: Gọi `bookService.searchActiveBook("Java")`.
  - Bước 3: Kiểm tra danh sách kết quả.
- **Luồng code kiểm tra:** `bookRepository.searchActiveBook(keyword)` — lọc theo keyword và active
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về 2 sách active, không trả về sách inactive.

---

### Test Case TC_NDT_010
- **Summary:** `getBooksWithFilters` lọc đúng khi kết hợp cả keyword và category
- **Phương thức kiểm thử:** `BookService.getBooksWithFilters(categoryId, "Java")`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị sách thuộc category X có tên "Java Programming" và sách thuộc category Y có tên "Java".
  - Bước 2: Gọi `bookService.getBooksWithFilters(categoryX.getId(), "Java")`.
  - Bước 3: Kiểm tra danh sách kết quả.
- **Luồng code kiểm tra:** `IBookRepository` — lọc kết hợp category + keyword
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Chỉ trả về sách thuộc category X có từ khóa "Java".

---

### Test Case TC_NDT_011
- **Summary:** `deleteBookById` với ID không tồn tại không ném ngoại lệ
- **Phương thức kiểm thử:** `BookService.deleteBookById(9999L)`
- **Các bước thực hiện:**
  - Bước 1: Đảm bảo không có sách với id `9999`.
  - Bước 2: Gọi `bookService.deleteBookById(9999L)`.
  - Bước 3: Kiểm tra không có exception.
- **Luồng code kiểm tra:** `bookRepository.findById(9999L).ifPresent(...)` — `Optional.empty`, lambda không chạy
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Negative
- **Kết quả mong muốn:** Không có ngoại lệ được ném, không thay đổi dữ liệu.

---

### Test Case TC_NDT_012
- **Summary:** `countBooks` trả về đúng tổng số sách trong hệ thống
- **Phương thức kiểm thử:** `BookService.countBooks()`
- **Các bước thực hiện:**
  - Bước 1: Biết trước số lượng sách hiện tại trong CSDL (ví dụ: 10).
  - Bước 2: Gọi `bookService.countBooks()`.
  - Bước 3: So sánh kết quả với số lượng thực tế.
- **Luồng code kiểm tra:** `bookRepository.count()`
- **Độ ưu tiên:** P3 (Medium)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về đúng con số bằng số bản ghi trong bảng `book`.

---

## Chức năng: CategoryService

### Test Case TC_NDT_013
- **Summary:** `getAllCategories` trả về toàn bộ danh mục
- **Phương thức kiểm thử:** `CategoryService.getAllCategories()`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị ít nhất 3 category trong CSDL.
  - Bước 2: Gọi `categoryService.getAllCategories()`.
  - Bước 3: Kiểm tra kích thước và nội dung.
- **Luồng code kiểm tra:** `categoryRepository.findAll()`
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về danh sách đầy đủ các category.

---

### Test Case TC_NDT_014
- **Summary:** `addCategory` lưu danh mục mới thành công
- **Phương thức kiểm thử:** `CategoryService.addCategory(category)`
- **Các bước thực hiện:**
  - Bước 1: Tạo đối tượng `Category` với tên hợp lệ.
  - Bước 2: Gọi `categoryService.addCategory(category)`.
  - Bước 3: Truy vấn lại CSDL.
- **Luồng code kiểm tra:** `categoryRepository.save(category)`
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Category được lưu với id tự sinh.

---

### Test Case TC_NDT_015
- **Summary:** `updateCategory` cập nhật tên danh mục thành công
- **Phương thức kiểm thử:** `CategoryService.updateCategory(category)`
- **Các bước thực hiện:**
  - Bước 1: Chuẩn bị category tồn tại.
  - Bước 2: Thay đổi tên rồi gọi `categoryService.updateCategory(category)`.
  - Bước 3: Truy vấn lại và kiểm tra.
- **Luồng code kiểm tra:** `categoryRepository.save(category)` — merge
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Tên category được cập nhật trong CSDL.

---

### Test Case TC_NDT_016
- **Summary:** `deleteCategory` xóa danh mục không có sách liên kết
- **Phương thức kiểm thử:** `CategoryService.deleteCategory(id)`
- **Các bước thực hiện:**
  - Bước 1: Tạo category không có sách nào.
  - Bước 2: Gọi `categoryService.deleteCategory(id)`.
  - Bước 3: Kiểm tra category không còn tồn tại.
- **Luồng code kiểm tra:** `categoryRepository.deleteById(id)`
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Category bị xóa khỏi CSDL.

---

## Chức năng: CartService

### Test Case TC_NDT_017
- **Summary:** `getCart` tạo giỏ hàng mới khi chưa có trong session
- **Phương thức kiểm thử:** `CartService.getCart(session)` — nhánh giỏ hàng chưa tồn tại
- **Các bước thực hiện:**
  - Bước 1: Tạo `HttpSession` mới (chưa có attribute `cart`).
  - Bước 2: Gọi `cartService.getCart(session)`.
  - Bước 3: Kiểm tra đối tượng cart trả về.
- **Luồng code kiểm tra:** `session.getAttribute("cart") == null` → tạo `Cart` mới
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về `Cart` mới rỗng, được lưu vào session.

---

### Test Case TC_NDT_018
- **Summary:** `getCart` trả về giỏ hàng hiện có khi đã tồn tại trong session
- **Phương thức kiểm thử:** `CartService.getCart(session)` — nhánh giỏ hàng đã tồn tại
- **Các bước thực hiện:**
  - Bước 1: Đặt attribute `cart` vào session với 1 sách.
  - Bước 2: Gọi `cartService.getCart(session)`.
  - Bước 3: Kiểm tra cart trả về có đúng là cart đã lưu.
- **Luồng code kiểm tra:** `session.getAttribute("cart") != null` → trả về cart cũ
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Trả về đúng giỏ hàng cũ với sách đã có.

---

### Test Case TC_NDT_019
- **Summary:** `getSumPrice` tính đúng tổng tiền của các sản phẩm trong giỏ hàng
- **Phương thức kiểm thử:** `CartService.getSumPrice(session)`
- **Các bước thực hiện:**
  - Bước 1: Thêm 2 sách vào giỏ: sách A giá 50.000 x2, sách B giá 30.000 x1.
  - Bước 2: Gọi `cartService.getSumPrice(session)`.
  - Bước 3: So sánh kết quả với 130.000.
- **Luồng code kiểm tra:** Tính tổng `price * quantity` cho mỗi item
- **Độ ưu tiên:** P1 (Critical)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Tổng tiền = `50.000 * 2 + 30.000 * 1 = 130.000`.

---

### Test Case TC_NDT_020
- **Summary:** `getSumQuantity` tính đúng tổng số lượng sách trong giỏ hàng
- **Phương thức kiểm thử:** `CartService.getSumQuantity(session)`
- **Các bước thực hiện:**
  - Bước 1: Thêm sách A số lượng 3 và sách B số lượng 2 vào giỏ.
  - Bước 2: Gọi `cartService.getSumQuantity(session)`.
  - Bước 3: Kiểm tra kết quả trả về.
- **Luồng code kiểm tra:** Tính tổng `quantity` của mỗi item trong cart
- **Độ ưu tiên:** P2 (High)
- **Bản chất:** Positive
- **Kết quả mong muốn:** Tổng số lượng = `3 + 2 = 5`.

---

## Test Report

| TC ID | Test Case | Priority | Type | Pre-condition | Test Data | Expected Result | Test Script Design |
|-------|-----------|----------|------|---------------|-----------|-----------------|-------------------|
| TC_NDT_001 | Xác minh getAllBooks trả về đúng số lượng sách khi áp dụng phân trang hợp lệ | P1 | Positive | Có ≥6 sách trong DB | pageNo=0, pageSize=5, sortBy="id" | Trả về đúng 5 sách, bắt đầu từ bản ghi đầu tiên | Mock repository → call `getAllBooks(0,5,"id")` → assert size == 5 |
| TC_NDT_002 | Xác minh getBookById trả về Optional chứa sách khi ID tồn tại và sách đang active | P1 | Positive | Sách active với id hợp lệ tồn tại trong DB | id hợp lệ (active=true) | Optional.of(book) với đầy đủ thông tin sách | Mock `findActiveByIdWithCategory(id)` → call `getBookById(id)` → assert isPresent() |
| TC_NDT_003 | Xác minh getBookById trả về Optional.empty() khi ID không tồn tại trong DB | P1 | Negative | Không tồn tại sách với id=9999 | id=9999L | Optional.empty(), không ném ngoại lệ | Mock return empty → call `getBookById(9999L)` → assert isEmpty() |
| TC_NDT_004 | Xác minh addBook lưu sách mới thành công khi dữ liệu đầu vào hợp lệ | P1 | Positive | Danh mục hợp lệ tồn tại trong DB | Book{ title, author, price, category } hợp lệ | Sách được lưu vào DB, `save()` được gọi đúng 1 lần | Mock `save(book)` → call `addBook(book)` → verify save called once |
| TC_NDT_005 | Xác minh updateBook cập nhật đúng thông tin sách khi truyền dữ liệu mới | P1 | Positive | Sách với id tồn tại trong DB | Book{ id, newTitle, newPrice } | Title và price được cập nhật đúng trong DB | Mock findById + save → call `updateBook(book)` → assert title updated |
| TC_NDT_006 | Xác minh updateBook giữ nguyên ảnh cũ khi trường image truyền vào là rỗng | P2 | Positive | Sách đã có ảnh tồn tại trong DB | Book{ id, image="" } | Trường image trong DB không bị thay đổi | Mock findById với image cũ → call `updateBook` image rỗng → assert image unchanged |
| TC_NDT_007 | Xác minh deleteBookById chuyển sách sang inactive khi ID hợp lệ (soft delete) | P1 | Positive | Sách active với id tồn tại trong DB | id hợp lệ của sách active | Sách có active=false sau khi gọi | Mock findById + save → call `deleteBookById(id)` → assert active==false |
| TC_NDT_008 | Xác minh activateBookById chuyển sách sang active thành công khi sách đang inactive | P2 | Positive | Sách inactive với id tồn tại trong DB | id hợp lệ của sách inactive | Sách có active=true sau khi gọi | Mock findById + save → call `activateBookById(id)` → assert active==true |
| TC_NDT_009 | Xác minh searchActiveBook trả về đúng kết quả khi tìm theo từ khóa hợp lệ | P2 | Positive | Có sách active chứa từ khóa "Java" trong DB | keyword="Java" | Chỉ trả về sách active có title/author chứa "Java" | Mock `searchActive("Java")` → call `searchActiveBook("Java")` → assert results match |
| TC_NDT_010 | Xác minh getBooksWithFilters lọc đúng sách khi kết hợp category và từ khóa | P2 | Positive | Dữ liệu nhiều sách, nhiều danh mục tồn tại | categoryId hợp lệ, keyword="Java" | Chỉ trả về sách khớp cả danh mục lẫn từ khóa | Mock repository filter → call `getBooksWithFilters(catId, kw)` → assert filtered list |
| TC_NDT_011 | Xác minh deleteBookById không ném ngoại lệ khi ID không tồn tại trong DB | P2 | Negative | Không tồn tại sách với id=9999 | id=9999L | Không ném exception, DB không thay đổi | Mock findById return empty → call `deleteBookById(9999L)` → assert no exception thrown |
| TC_NDT_012 | Xác minh countBooks trả về đúng tổng số sách hiện có trong hệ thống | P3 | Positive | Có N sách trong DB | None | Trả về N đúng với số sách thực tế | Mock `count()` return N → call `countBooks()` → assert result == N |
| TC_NDT_013 | Xác minh getAllCategories trả về đầy đủ danh sách danh mục khi DB có dữ liệu | P1 | Positive | Có ≥1 danh mục trong DB | None | Danh sách đầy đủ tất cả danh mục, không rỗng | Mock `findAll()` → call `getAllCategories()` → assert list not empty |
| TC_NDT_014 | Xác minh addCategory lưu danh mục mới thành công khi tên hợp lệ | P1 | Positive | None | Category{ name="Test Cat" } | Danh mục được lưu, `save()` được gọi đúng 1 lần | Mock `save(cat)` → call `addCategory(cat)` → verify save called once |
| TC_NDT_015 | Xác minh updateCategory cập nhật tên danh mục thành công khi truyền tên mới | P2 | Positive | Danh mục với id tồn tại trong DB | Category{ id, name="Updated" } | Tên danh mục được cập nhật đúng trong DB | Mock save → call `updateCategory(cat)` → assert name == "Updated" |
| TC_NDT_016 | Xác minh deleteCategory xóa danh mục thành công khi không có sách liên kết | P2 | Positive | Danh mục không có sách liên kết | id hợp lệ | Danh mục bị xóa khỏi DB, `deleteById()` được gọi | Mock `deleteById` → call `deleteCategory(id)` → verify deleteById called once |
| TC_NDT_017 | Xác minh getCart tạo cart mới rỗng khi session chưa có cart | P1 | Positive | Session mới, chưa có cart | HttpSession mới không có cart | Cart mới rỗng được tạo và lưu vào session | Create new mock session → call `getCart(session)` → assert cart.isEmpty() |
| TC_NDT_018 | Xác minh getCart trả về cart cũ khi session đã tồn tại cart | P1 | Positive | Session đã chứa cart với 2 sách | HttpSession có cart sẵn | Cart cũ với đúng items được trả về | Mock session with existing cart → call `getCart(session)` → assert same cart returned |
| TC_NDT_019 | Xác minh getSumPrice tính tổng tiền đúng khi giỏ hàng có 2 sách | P1 | Positive | Cart có 2 sách giá 100 và 30 | Session có cart{ item(100), item(30) } | Trả về 130.0 | Mock session with cart → call `getSumPrice(session)` → assert == 130.0 |
| TC_NDT_020 | Xác minh getSumQuantity tính tổng số lượng đúng khi giỏ hàng có nhiều mục | P2 | Positive | Cart có 2 sách qty 3 và 2 | Session có cart{ item(qty=3), item(qty=2) } | Trả về 5 | Mock session with cart → call `getSumQuantity(session)` → assert == 5 |

**Tổng kết:**
- Tổng số test case: **20**
- Positive: **18**
- Negative: **2**
- Pass: ___
- Fail: ___
- Blocked: ___

---

## Test Cases Summary

| TC ID | Module | Test Case Title | Priority | Type | Status | Assigned To | Created Date | Due Date |
|-------|--------|-----------------|----------|------|--------|-------------|--------------|----------|
| TC_NDT_001 | BookService | Xác minh getAllBooks trả về đúng số lượng sách khi áp dụng phân trang hợp lệ | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_002 | BookService | Xác minh getBookById trả về Optional chứa sách khi ID tồn tại và sách đang active | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_003 | BookService | Xác minh getBookById trả về Optional.empty() khi ID không tồn tại trong DB | P1 | Negative | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_004 | BookService | Xác minh addBook lưu sách mới thành công khi dữ liệu đầu vào hợp lệ | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_005 | BookService | Xác minh updateBook cập nhật đúng thông tin sách khi truyền dữ liệu mới | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_006 | BookService | Xác minh updateBook giữ nguyên ảnh cũ khi trường image truyền vào là rỗng | P2 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_007 | BookService | Xác minh deleteBookById chuyển sách sang inactive khi ID hợp lệ (soft delete) | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_008 | BookService | Xác minh activateBookById chuyển sách sang active thành công khi sách đang inactive | P2 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_009 | BookService | Xác minh searchActiveBook trả về đúng kết quả khi tìm theo từ khóa hợp lệ | P2 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_010 | BookService | Xác minh getBooksWithFilters lọc đúng sách khi kết hợp category và từ khóa | P2 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_011 | BookService | Xác minh deleteBookById không ném ngoại lệ khi ID không tồn tại trong DB | P2 | Negative | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_012 | BookService | Xác minh countBooks trả về đúng tổng số sách hiện có trong hệ thống | P3 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_013 | CategoryService | Xác minh getAllCategories trả về đầy đủ danh sách danh mục khi DB có dữ liệu | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_014 | CategoryService | Xác minh addCategory lưu danh mục mới thành công khi tên hợp lệ | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_015 | CategoryService | Xác minh updateCategory cập nhật tên danh mục thành công khi truyền tên mới | P2 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_016 | CategoryService | Xác minh deleteCategory xóa danh mục thành công khi không có sách liên kết | P2 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_017 | CartService | Xác minh getCart tạo cart mới rỗng khi session chưa có cart | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_018 | CartService | Xác minh getCart trả về cart cũ khi session đã tồn tại cart | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_019 | CartService | Xác minh getSumPrice tính tổng tiền đúng khi giỏ hàng có 2 sách | P1 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
| TC_NDT_020 | CartService | Xác minh getSumQuantity tính tổng số lượng đúng khi giỏ hàng có nhiều mục | P2 | Positive | | Nguyễn Đức Trung | 2026-03-24 | 2026-04-07 |
