package com.klpdapp.klpd.Services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klpdapp.klpd.Repository.CartRepo;
import com.klpdapp.klpd.Repository.OrderItemRepository;
import com.klpdapp.klpd.Repository.OrderRepository;
import com.klpdapp.klpd.Repository.ProductRepo;
import com.klpdapp.klpd.Repository.UserRepo;
import com.klpdapp.klpd.Repository.AddressRepo;
import com.klpdapp.klpd.model.Product;
import com.klpdapp.klpd.model.Cart;
import com.klpdapp.klpd.model.Login;
import com.klpdapp.klpd.model.Order;
import com.klpdapp.klpd.model.OrderItem;
import com.klpdapp.klpd.model.Address;

@Service
public class CartService {

    @Autowired
    CartRepo cartRepository;

    @Autowired
    ProductRepo pRepo;

    @Autowired
    UserRepo uRepo;

    @Autowired
    OrderRepository orderrepo;

    @Autowired
    OrderItemRepository orderitemrepo;

    @Autowired
    ProductService productService;

    @Autowired
    AddressRepo addRepo;

    public List<Cart> getCartItems(Login user) {
        return cartRepository.findByUser(user);
    }

    public float calculateSubtotal(List<Cart> cartItems) {
        return cartItems.stream().map(item -> item.getProductTotal()).reduce(0.0f, Float::sum);
    }

    public float calculateTax(float subtotal) {
        return subtotal * 0.10f;
    }

    public int calculateTotal(float subtotal, float tax, float discount) {
        return (int) (subtotal + tax - discount);
    }


    public void deleteCartItem(int cartId) {
        cartRepository.deleteById(cartId);
    }

    public void checkout(Login user, String paymentMode, int AddressId) {
        System.out.println("inside cartservice");
        List<Cart> carts = cartRepository.findByUser(user);
        float subtotal = calculateSubtotal(carts);
        float discount = 0.0f;
        float tax = calculateTax(subtotal);
        int total = calculateTotal(subtotal, tax, discount);
        Address address = addRepo.findById(AddressId).orElse(null);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmt(total);
        order.setOrderDate(LocalDate.now());
        order.setPaymentMode(paymentMode);
        order.setAddress(address);
        orderrepo.save(order);
        System.out.println("order saved");
        System.out.println("order id: " + order.getOrderId());
        for (Cart cart : carts) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProdQuantity(cart.getQuantity());
            orderItem.setProduct(cart.getProduct());
            orderItem.setStatus("Pending");
            orderitemrepo.save(orderItem);
            
            Product product = productService.findById(cart.getProduct().getPid());
            productService.incrementSales(product.getPid());
        }

        cartRepository.deleteByUser(user);
    }   
}