package hu.webuni.logistics.akostomschweger.service;

import hu.webuni.logistics.akostomschweger.config.AtConfigurationProperties;
import hu.webuni.logistics.akostomschweger.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmployeeService extends EmployeeSuperClass {

    @Autowired
    AtConfigurationProperties config;


    @Override
    public int getPayRaisePercent(Employee employee) {
        return config.getSalary().getDef().getPercent();
    }


}
