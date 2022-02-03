package com.redhat.customer;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class Customer {
    
    private Integer customerId;
    
    @NotEmpty(message = "{Customer.firstName.required}")
    private String firstName;
    
    private String middleName;

    @NotEmpty(message = "{Customer.lastName.required}")
    private String lastName;
    
    private String suffix;

    @Email(message = "{Customer.email.invalid}")
    private String email;
    
    private String phone;

}