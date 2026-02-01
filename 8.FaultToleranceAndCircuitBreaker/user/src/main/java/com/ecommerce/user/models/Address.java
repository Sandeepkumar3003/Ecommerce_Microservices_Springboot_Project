package com.ecommerce.user.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @Column(length = 36)
    private String id;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;

    @PrePersist
    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }
}
