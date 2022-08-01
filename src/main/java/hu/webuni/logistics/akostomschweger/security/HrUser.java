package hu.webuni.logistics.akostomschweger.security;

import hu.webuni.logistics.akostomschweger.model.Employee;
//import org.apache.tomcat.jni.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class HrUser extends User {

    private Employee employee;

    public HrUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Employee employee) {
        super(username, password, authorities);
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
