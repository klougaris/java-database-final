package com.project.code.Controller;

import com.project.code.Model.Review;
import com.project.code.Model.Customer;
import com.project.code.Repo.ReviewRepository;
import com.project.code.Repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Fetch reviews for a specific product in a store.
     * @param storeId   ID of the store
     * @param productId ID of the product
     * @return Map containing filtered reviews
     */
    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable Long storeId,
                                          @PathVariable Long productId) {
        Map<String, Object> response = new HashMap<>();

        // Fetch all reviews for the given store and product
        List<Review> reviews = reviewRepository.findByStoreIdAndProductId(storeId, productId);

        // Prepare a list of filtered review objects
        List<Map<String, Object>> filteredReviews = new ArrayList<>();

        for (Review review : reviews) {
            Map<String, Object> reviewData = new HashMap<>();
            reviewData.put("comment", review.getComment());
            reviewData.put("rating", review.getRating());

            // Fetch customer name using customerId
            Optional<Customer> customerOpt = customerRepository.findById(review.getCustomerId());
            String customerName = customerOpt.map(Customer::getName).orElse("Unknown");
            reviewData.put("customerName", customerName);

            filteredReviews.add(reviewData);
        }

        response.put("reviews", filteredReviews);
        return response;
    }
}
