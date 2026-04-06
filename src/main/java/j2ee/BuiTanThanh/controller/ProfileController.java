package j2ee.BuiTanThanh.controller;

import j2ee.BuiTanThanh.entities.Invoice;
import j2ee.BuiTanThanh.services.InvoiceService;
import j2ee.BuiTanThanh.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final InvoiceService invoiceService;

    @GetMapping
    public String showProfile(Model model) {
        String username = getCurrentUsername();
        var user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Invoice> orders = invoiceService.getInvoicesByUsername(username);
        double totalSpent = orders.stream()
                .mapToDouble(o -> o.getPrice() != null ? o.getPrice() : 0).sum();
        int totalBooks = orders.stream()
                .mapToInt(o -> o.getItemInvoices().size()).sum();

        model.addAttribute("user", user);
        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("totalBooks", totalBooks);
        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            RedirectAttributes redirectAttributes) {
        try {
            userService.updateProfile(getCurrentUsername(), email, name, phone);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "New passwords do not match");
            return "redirect:/profile";
        }
        try {
            userService.changePassword(getCurrentUsername(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("passwordSuccess", "Password changed successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("passwordError", e.getMessage());
        }
        return "redirect:/profile";
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}