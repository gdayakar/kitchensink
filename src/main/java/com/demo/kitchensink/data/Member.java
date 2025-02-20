package com.demo.kitchensink.data;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "members") // Maps this class to "members" collection in MongoDB
public class Member {

    @Id
    private String id; // MongoDB ID must be of type String or ObjectId

//    @NotNull
//    @Size(min = 1, max = 25)
//    @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
    private String name;

//    @NotNull
//    @NotEmpty
//    @Email
    private String email;

//    @NotNull
//    @Size(min = 10, max = 12)
//    @Digits(fraction = 0, integer = 12)
    private String phoneNumber;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
