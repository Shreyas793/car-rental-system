package com.carrental.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private int year;
    
    @Column(name = "rental_price")
    private double rentalPrice;

    private boolean available;

    public Car() {}

    public Car(String make, String model, int year, double rentalPrice, boolean available) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.rentalPrice = rentalPrice;
        this.available = available;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getRentalPrice() { return rentalPrice; }
    public void setRentalPrice(double rentalPrice) { this.rentalPrice = rentalPrice; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
