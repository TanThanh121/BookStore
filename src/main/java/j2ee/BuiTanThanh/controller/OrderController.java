package j2ee.BuiTanThanh.controller;

import j2ee.BuiTanThanh.entities.Invoice;
import j2ee.BuiTanThanh.entities.User;
import j2ee.BuiTanThanh.services.InvoiceService;
import j2ee.BuiTanThanh.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final InvoiceService invoiceService;
    private final UserService userService;

    @GetMapping
    public String orderHistory(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        List<Invoice> orders = invoiceService.getInvoicesByUsername(username);

        double totalSpent = orders.stream()
                .mapToDouble(o -> o.getPrice() != null ? o.getPrice() : 0)
                .sum();
        int totalItems = orders.stream()
                .mapToInt(o -> o.getItemInvoices().size())
                .sum();

        model.addAttribute("orders", orders);
        model.addAttribute("username", username);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("totalItems", totalItems);

        return "order/history";
    }

    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Optional<Invoice> orderOpt = invoiceService.getInvoiceById(id);

        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }

        Invoice order = orderOpt.get();

        // Security check: ensure user can only view their own orders
        if (!order.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("Access denied");
        }

        // Calculate total if not set
        if (order.getPrice() == null || order.getPrice() == 0) {
            Double calculatedTotal = invoiceService.calculateInvoiceTotal(order);
            order.setPrice(calculatedTotal);
        }

        model.addAttribute("order", order);

        return "order/detail";
    }
}