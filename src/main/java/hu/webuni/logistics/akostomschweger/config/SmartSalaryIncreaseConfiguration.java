package hu.webuni.logistics.akostomschweger.config;

import hu.webuni.logistics.akostomschweger.service.EmployeeService;
import hu.webuni.logistics.akostomschweger.service.SmartEmployeeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("smart")
public class SmartSalaryIncreaseConfiguration {


    @Bean
    public EmployeeService employeeService() {
        return new SmartEmployeeService();
    }
}
