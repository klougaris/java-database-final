
package com.project.code.Service;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.PurchaseProductDTO;
import com.project.code.Model.Customer;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.Store;
import com.project.code.Model.Inventory;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.StoreRepository;
import com.project.code.Repo.OrderDetailsRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.InventoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class OrderService {
    
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;


	@Transactional
	public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {
		
		// 1. Retrieve or Create a new Customer
		Customer customer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail())
				.orElseGet(()-> {
					Customer newCustomer = new Customer();
					newCustomer.setName(placeOrderRequest.getCustomerName());
					newCustomer.setEmail(placeOrderRequest.getCustomerEmail());
					return customerRepository.save(newCustomer);
				});



		// 2. Retrieve the Store
		Store store = storeRepository.findById(placeOrderRequest.getStoreId())
			.orElseThrow(() -> new RuntimeException("Store not found with ID: " + placeOrderRequest.getStoreId()));


		// 3. Create OrderDetails
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setCustomer(customer);
		orderDetails.setStore(store);
		orderDetails.setTotalPrice(placeOrderRequest.getTotalPrice());
		orderDetails.setOrderDate(LocalDateTime.now());
		orderDetails = orderDetailsRepository.save(orderDetails);
		

		// 4. Create and Save OrderItems
		for (PurchaseProductDTO productDTO : placeOrderRequest.getPurchaseProduct()) {
			
			//Fetch inventory
			Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productDTO.getProductId(), store.getId())
				.orElseThrow(() -> new RuntimeException(
					"Inventory not found for product Id: " + productDTO.getProductId() + " in store Id: " + store.getId()));



			
			// Update stock
			if (inventory.getStock() < productDTO.getQuantity()) {
			
				throw new RuntimeException("Insufficient stock for product Id: " + productDTO.getProductId());
			}
		
			inventory.setStock(inventory.getStock() - productDTO.getQuantity());
			inventoryRepository.save(inventory);


			// Create OrderItem
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderDetails(orderDetails);
			orderItem.setProduct(inventory.getProduct());
			orderItem.setQuantity(productDTO.getQuantity());
			orderItem.setPrice(productDTO.getPrice());
			orderItemRepository.save(orderItem);
	
			
		}


		
	}
   
}
