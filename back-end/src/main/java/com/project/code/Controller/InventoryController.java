package com.project.code.Controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.project.code.Model.Product;
import com.project.code.Model.Inventory;
import com.project.code.Model.CombinedRequest;

import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ServiceClass inventoryService;

   
    @Autowired
    private ProductRepository productRepository;

    
    @Autowired
    private InventoryRepository inventoryRepository;


 // Update inventory and product
    @PutMapping("/update")
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {
        Map<String, String> response = new HashMap<>();
        
        try {
            Product product = request.getProduct();
            Inventory inventory = request.getInventory();

            // Validate product ID
            boolean productExists = inventoryService.validateProductId(product.getId());
            if (!productExists) {
                response.put("message", "Product with given ID does not exist");
                return response;
            }

            // Check if inventory exists for this product-store combination
            Inventory existingInventory = inventoryService.getInventoryId(inventory);
            if (existingInventory != null) {
                // Update product fields
                Product existingProduct = productRepository.findById(product.getId()).orElse(null);
                if (existingProduct != null) {
                    existingProduct.setName(product.getName());

                    // Add other fields if needed
                    productRepository.save(existingProduct);
                }

                // Update inventory fields
                existingInventory.setQuantity(inventory.getQuantity());
                existingInventory.setPrice(inventory.getPrice());
                // Add other fields if needed
                inventoryRepository.save(existingInventory);

                response.put("message", "Successfully updated product");
            } else {
                response.put("message", "No data available");
            }

        } catch (DataIntegrityViolationException ex) {
            response.put("message", "Data integrity violation: " + ex.getMessage());
        } catch (Exception ex) {
            response.put("message", "An error occurred: " + ex.getMessage());
        }

        return response;
    }


 @PostMapping("/save")
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> response = new HashMap<>();

        try {
            // Check if the inventory already exists
            boolean isValid = inventoryService.validateInventory(inventory);
            if (!isValid) {
                response.put("message", "Inventory data is already present");
                return response;
            }

            // Save the inventory
            inventoryRepository.save(inventory);
            response.put("message", "Inventory data saved successfully");

        } catch (DataIntegrityViolationException ex) {
            response.put("message", "Data integrity violation: " + ex.getMessage());
        } catch (Exception ex) {
            response.put("message", "An error occurred: " + ex.getMessage());
        }

        return response;
    }

@GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(@PathVariable("storeid") long storeId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Fetch products for the given store ID
            List<Product> products = productRepository.findProductsByStoreId(storeId);
            response.put("products", products);
        } catch (Exception ex) {
            response.put("message", "An error occurred: " + ex.getMessage());
        }
        return response;
    }


 @GetMapping("filter/{category}/{name}/{storeid}")
    public Map<String, Object> getProductName(
            @PathVariable("category") String category,
            @PathVariable("name") String name,
            @PathVariable("storeid") long storeId) {

        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> filteredProducts;

            boolean categoryIsNull = category.equalsIgnoreCase("null");
            boolean nameIsNull = name.equalsIgnoreCase("null");

            if (categoryIsNull && !nameIsNull) {
                // Filter by name only
                filteredProducts = productRepository.findByNameLike(storeId, name);
            } else if (!categoryIsNull && nameIsNull) {
                // Filter by category only
                filteredProducts = productRepository.findByCategoryAndStoreId(category, storeId);
            } else if (!categoryIsNull && !nameIsNull) {
                // Filter by both name and category
                filteredProducts = productRepository.findByNameAndCategory(name, category, storeId);
            } else {
                // Both name and category are "null" → return all products for store
                filteredProducts = productRepository.findProductsByStoreId(storeId);
            }

            response.put("product", filteredProducts);

        } catch (Exception ex) {
            response.put("message", "An error occurred: " + ex.getMessage());
        }

        return response;
    }

@GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(
            @PathVariable("name") String name,
            @PathVariable("storeId") long storeId) {

        Map<String, Object> response = new HashMap<>();
        try {
            // Search for products matching the name in the specified store
            List<Product> products = productRepository.findByNameLike(storeId, name);
            response.put("product", products);
        } catch (Exception ex) {
            response.put("message", "An error occurred: " + ex.getMessage());
        }

        return response;
    }

@DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable("id") long id) {
        Map<String, String> response = new HashMap<>();

        try {
            // Validate if product exists
            boolean productExists = inventoryService.validateProductId(id);
            if (!productExists) {
                response.put("message", "Product not present in database");
                return response;
            }

            // Delete inventory entries for the product
            inventoryRepository.deleteByProductId(id);

            // Delete the product itself
            productRepository.deleteById(id);

            response.put("message", "Product deleted successfully");
        } catch (Exception ex) {
            response.put("message", "An error occurred: " + ex.getMessage());
        }

        return response;
    }

@GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(
            @PathVariable("quantity") int quantity,
            @PathVariable("storeId") long storeId,
            @PathVariable("productId") long productId) {

        try {
            Inventory inventory = inventoryRepository.findByProductIdAndStoreId(productId, storeId);
            if (inventory != null && inventory.getQuantity() >= quantity) {
                return true;
            }
        } catch (Exception ex) {
            // Log the exception if needed
            System.out.println("Error validating quantity: " + ex.getMessage());
        }

        return false;
    }
}
