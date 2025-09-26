package com.project.code.Controller;


import com.project.code.Model.Store;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;

    /**
     * Add Store
     * Endpoint: POST /store
     * Adds a new store and returns a confirmation message with the store ID.
     */
    @PostMapping
    public Map<String, String> addStore(@RequestBody Store store) {
        Map<String, String> response = new HashMap<>();
        try {
            Store savedStore = storeRepository.save(store);
            response.put("message", "Store created successfully with ID: " + savedStore.getId());
        } catch (Exception ex) {
            response.put("message", "Error creating store: " + ex.getMessage());
        }
        return response;
    }

    /**
     * Validate Store
     * Endpoint: GET /store/validate/{storeId}
     * Returns true if the store exists, otherwise false.
     */
    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {
        return storeRepository.existsById(storeId);
    }

    /**
     * Place Order
     * Endpoint: POST /store/placeOrder
     * Places an order and returns a success or error message.
     */
    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO orderRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            orderService.placeOrder(orderRequest);
            response.put("message", "Order placed successfully");
        } catch (Exception ex) {
            response.put("Error", "Failed to place order: " + ex.getMessage());
        }
        return response;
    }
}




