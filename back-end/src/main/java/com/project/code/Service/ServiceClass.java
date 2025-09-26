package com.project.code.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;


@Service
public class ServiceClass {

	@Autowired
	private final InventoryRepository invetoryRepository;

	@Autowired
	private final ProductRepository productReposity;
	
	// Checks whether an inventory record exists for a given product and store combination.
	public boolean validateInventory(Inventory inventory) {

		Inventory existingInventory = inventoryRepository
                .findByProductIdAndStoreId(inventory.getProduct().getId(), inventory.getStore().getId());
        	return existingInventory == null; // true if not exists, false if exists
	}


	// Checks whether a product exists by its name.

	public boolean validateProduct(Product product) {
        	Product existingProduct = productRepository.findByName(product.getName());
        	return existingProduct == null; // true if not exists, false if exists
    	}


	// Validates whether a product exists by its ID.
	
	public boolean validateProductId(long id) {

        	Product product = productRepository.findById(id).orElse(null);
        	return product != null; // true if exists, false if not
    	}
	
	
	// Fetches the inventory record for a given product and store combination.
	
	public Inventory getInventoryId(Inventory inventory) {
        	return inventoryRepository
                	.findByProductIdAndStoreId(inventory.getProduct().getId(), inventory.getStore().getId());
    	}


}
