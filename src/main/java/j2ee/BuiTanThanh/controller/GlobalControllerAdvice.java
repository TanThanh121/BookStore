package j2ee.BuiTanThanh.controller;

import j2ee.BuiTanThanh.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import j2ee.BuiTanThanh.entities.Category;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CategoryService categoryService;

    @ModelAttribute("navCategories")
    public List<Category> navCategories() {
        return categoryService.getAllCategories();
    }
}
