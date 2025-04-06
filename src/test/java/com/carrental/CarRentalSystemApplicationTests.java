package com.carrental;

import com.carrental.entity.Car;
import com.carrental.repository.BillingRepository;
import com.carrental.repository.CarRentalRepository;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.repository.CarRentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CarRentalSystemApplicationTests {

    @Autowired
    private CarRentalRepository rentalRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void cleanDatabase() {
        rentalRepository.deleteAll();         // Rentals depend on billing
        billingRepository.deleteAll();        // Billing depends on reservations
        reservationRepository.deleteAll();    // Reservations may depend on cars
        carRepository.deleteAll();            // Cars can be deleted last
    }

    @Test
    void contextLoads() {
        assertThat(carRepository).isNotNull();
    }

    @Test
    void testAddAndFetchAvailableCar() {
        Car car = new Car("Toyota", "Corolla", 2023, 750.0, true);
        carRepository.save(car);

        List<Car> availableCars = carRepository.findByAvailableTrue();

        assertThat(availableCars).hasSize(1);
        assertThat(availableCars.get(0).getMake()).isEqualTo("Toyota");
        assertThat(availableCars.get(0).getModel()).isEqualTo("Corolla");
    }

    @Test
    void testAddMultipleAndVerifyOnlyAvailableCarsReturned() {
        Car car1 = new Car("Toyota", "Camry", 2022, 900.0, true);
        Car car2 = new Car("Honda", "Civic", 2021, 850.0, false);
        Car car3 = new Car("Ford", "Focus", 2023, 950.0, true);

        carRepository.saveAll(List.of(car1, car2, car3));

        List<Car> availableCars = carRepository.findByAvailableTrue();

        assertThat(availableCars).hasSize(2);
        assertThat(availableCars).extracting(Car::getMake)
                                 .containsExactlyInAnyOrder("Toyota", "Ford");
    }
}
