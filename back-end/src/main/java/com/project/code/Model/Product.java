package com.project.code.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;


@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = "sku"))
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Name cannot be null")
	private String name;

	@NotNull(message = "Category cannot be null")
	private String category;

	@NotNull(message = "Price cannot be null")
	private Double price;

	@NotNull(message = "SKU cannot be null")
    	@Column(nullable = false, unique = true)
    	private String sku;
	
	@OneToMany(mappedBy = "product")
    	@JsonManagedReference("inventory-product")
   	private List<Inventory> inventories;
	
 	public Long getId() {
        	return id;
    	}

    	public void setId(Long id) {
        	this.id = id;
   	}

    	public String getName() {
        	return name;
    	}

    	public void setName(String name) {
        	this.name = name;
    	}

   	 public String getCategory() {
        	return category;
    	}

    	public void setCategory(String category) {
        	this.category = category;
    	}

    	public Double getPrice() {
        	return price;
    	}

    	public void setPrice(Double price) {
        	this.price = price;
    	}

   	public String getSku() {
        	return sku;
    	}

    	public void setSku(String sku) {
        	this.sku = sku;
    	}


}

