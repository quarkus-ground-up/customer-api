package com.redhat.customer;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class Customer {
    
    private Integer customerId;
    
    @NotEmpty
    private String firstName;
    
    private String middleName;
    
    @NotEmpty
    private String lastName;
    
    private String suffix;
    
    @Email
    private String email;
    
    private String phone;

}