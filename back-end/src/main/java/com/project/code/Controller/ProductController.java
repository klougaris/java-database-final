package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceClass serviceClass;

    @Autowired
    private InventoryRepository inventoryRepository;

    // 1️⃣ Add Product
    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
        try {
            // Validate product existence
            serviceClass.validateProduct(product);
            productRepository.save(product);
            response.put("message", "Product added successfully!");
        } catch (DataIntegrityViolationException ex) {
            response.put("message", "Error: Duplicate entry or invalid data.");
        } catch (Exception ex) {
            response.put("message", "Error adding product: " + ex.getMessage());
        }
        return response;
    }

    // 2️⃣ Get Product by ID
    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Product> product = productRepository.findById(id);
        response.put("products", product.orElse(null));
        return response;
    }

    // 3️⃣ Update Product
    @PutMapping
    public Map<String, String> updateProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
        try {
            productRepository.save(product);
            response.put("message", "Product updated successfully!");
        } catch (Exception ex) {
            response.put("message", "Error updating product: " + ex.getMessage());
        }
        return response;
    }

    // 4️⃣ Filter by Category & Name
    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(@PathVariable String name,
                                                       @PathVariable String category) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products;

        if ("null".equalsIgnoreCase(name) && !"null".equalsIgnoreCase(category)) {
            products = productRepository.findByCategory(category);
        } else if (!"null".equalsIgnoreCase(name) && "null".equalsIgnoreCase(category)) {
            products = productRepository.findProductBySubName(name);
        } else if (!"null".equalsIgnoreCase(name) && !"null".equalsIgnoreCase(category)) {
            products = productRepository.findProductBySubNameAndCategory(name, category);
        } else {
            products = productRepository.findAll();
        }

        response.put("products", products);
        return response;
    }

    // 5️⃣ List All Products
    @GetMapping
    public Map<String, Object> listProduct() {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findAll());
        return response;
    }

    // 6️⃣ Filter by Category & StoreId
    @GetMapping("/filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(@PathVariable String category,
                                                              @PathVariable Long storeid) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productRepository.findProductByCategory(category, storeid);
        response.put("product", products);
        return response;
    }

    // 7️⃣ Delete Product
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            serviceClass.ValidateProductId(id);
            // Delete inventory first to maintain FK constraint
            inventoryRepository.deleteByProductId(id);
            productRepository.deleteById(id);
            response.put("message", "Product deleted successfully!");
        } catch (Exception ex) {
            response.put("message", "Error deleting product: " + ex.getMessage());
        }
        return response;
    }

    // 8️⃣ Search Product by Name
    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productRepository.findProductBySubName(name);
        response.put("products", products);
        return response;
    }
}
