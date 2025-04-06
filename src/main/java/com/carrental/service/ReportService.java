package com.carrental.service;

import com.carrental.dto.CarReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ReportService {
    List<CarReportDTO> getAvailableCarsList();
    List<CarReportDTO> getRentedCarsList();
    Page<CarReportDTO> getAvailableCarsPaged(Pageable pageable);
    Page<CarReportDTO> getRentedCarsPaged(Pageable pageable);
}
