package com.project.code.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Customer {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long ID;

@NotNull(message = "Name cannot be null")
private String name;

@NotNull(message = "Email cannot be null")
private String email;

@NotNull(message = "Phone cannot be null")
private String phone;

@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
@JsonManagedReference
private List<OrderItem> orders;
// 6. Getters and Setters:
//    - For each field ('id', 'name', 'email', 'phone'), add getter and setter methods.
//    - Example: public Long getId(), public void setId(Long id)
//    - Example: public String getName(), public void setName(String name)
//    - Add getters and setters for 'email' and 'phone' fields as well.
public Customer() {}

public Customer(String name, String email, String phone) {

	this.name = name;
	this.email = email;
	this.phone = phone;

}

public Long getId() {
        
	return id;

}

public void setID(Long ID) {

	this.ID = ID;

}

public String getName() {
        
	return name;

}

public void setName(String name) {

	this.name = name;

}

public String getEmail() {
        
	return email;

}

public void setEmail(String email) {

	this.email = email;

}

public String getPhone() {
        
	return phone;

}

public void setPhone(String phone) {

	this.phone = phone;

}


}



