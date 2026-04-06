package j2ee.BuiTanThanh.controller;

import j2ee.BuiTanThanh.entities.Invoice;
import j2ee.BuiTanThanh.services.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public String showCart(HttpSession session,
            Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalPrice",
                cartService.getSumPrice(session));
        model.addAttribute("totalQuantity",
                cartService.getSumQuantity(session));
        return "cart/cart";
    }

    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(HttpSession session,
            @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        return "redirect:/cart";

    }

    @GetMapping("/updateCart/{id}/{quantity}")
    public String updateCart(HttpSession session,
            @PathVariable Long id,
            @PathVariable int quantity) {
        var cart = cartService.getCart(session);
        cart.updateItems(id, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {
        cartService.removeCart(session);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        var cart = cartService.getCart(session);

        if (cart.getCartItems().isEmpty()) {
            return "redirect:/cart?error=empty";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cartService.getSumPrice(session));
        model.addAttribute("totalQuantity", cartService.getSumQuantity(session));

        return "cart/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(HttpSession session,
            @RequestParam String customerName,
            @RequestParam String customerEmail,
            @RequestParam String customerPhone,
            @RequestParam String shippingAddress,
            @RequestParam(defaultValue = "false") boolean agreeTerms,
            RedirectAttributes redirectAttributes) {

        var cart = cartService.getCart(session);
        if (cart.getCartItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }

        if (!agreeTerms) {
            redirectAttributes.addFlashAttribute("error", "Please agree to terms and conditions.");
            redirectAttributes.addFlashAttribute("customerName", customerName);
            redirectAttributes.addFlashAttribute("customerEmail", customerEmail);
            redirectAttributes.addFlashAttribute("customerPhone", customerPhone);
            redirectAttributes.addFlashAttribute("shippingAddress", shippingAddress);
            return "redirect:/cart/checkout";
        }

        try {
            Invoice invoice = cartService.saveCart(session);
            redirectAttributes.addFlashAttribute("success", "Order placed successfully! Order #" + invoice.getId());
            return "redirect:/orders/detail/" + invoice.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to process order: " + e.getMessage());
            return "redirect:/cart/checkout";
        }
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess() {
        return "cart/success";
    }
}
