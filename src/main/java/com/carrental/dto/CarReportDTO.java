package com.carrental.dto;

public class CarReportDTO {
    private Long carId;
    private String make;
    private String model;
    private int year;
    private boolean available;
    private double rentalPrice;

    public CarReportDTO(Long carId, String make, String model, int year, boolean available, double rentalPrice) {
        this.carId = carId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.available = available;
        this.rentalPrice = rentalPrice;
    }

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public double getRentalPrice() {
		return rentalPrice;
	}

	public void setRentalPrice(double rentalPrice) {
		this.rentalPrice = rentalPrice;
	}

    // Getters and Setters
}
