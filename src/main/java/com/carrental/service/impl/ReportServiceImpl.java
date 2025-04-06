package com.carrental.service.impl;

import com.carrental.dto.CarReportDTO;
import com.carrental.repository.CarRepository;
import com.carrental.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final CarRepository carRepository;

    public ReportServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<CarReportDTO> getAvailableCarsList() {
        return carRepository.findAvailableCars();
    }

    @Override
    public List<CarReportDTO> getRentedCarsList() {
        return carRepository.findRentedCars();
    }

    @Override
    public Page<CarReportDTO> getAvailableCarsPaged(Pageable pageable) {
        return carRepository.findAvailableCars(pageable);
    }

    @Override
    public Page<CarReportDTO> getRentedCarsPaged(Pageable pageable) {
        return carRepository.findRentedCarsPaged(pageable);
    }
}
