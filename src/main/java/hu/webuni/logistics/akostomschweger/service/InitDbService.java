package hu.webuni.logistics.akostomschweger.service;

import hu.webuni.logistics.akostomschweger.model.Company;
import hu.webuni.logistics.akostomschweger.model.Employee;
import hu.webuni.logistics.akostomschweger.model.PositionDetailsByCompany;
import hu.webuni.logistics.akostomschweger.model.Qualification;
import hu.webuni.logistics.akostomschweger.repository.*;
// import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class InitDbService {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    HolidayRequestRepository holidayRequestRepository;

    @Autowired
    CompanyService companyService;
    //@Autowired
    //EmployeeService employeeService;


    //position entitás miatt hozzáadás
    @Autowired
    PositionRepository positionRepository;

    @Autowired
    PositionDetailsByCompanyRepository positionDetailsByCompanyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public void clearDB() {

        //employeeRepository.truncate();
        //companyRepository.truncate();


        holidayRequestRepository.deleteAll();


        for (PositionDetailsByCompany p : positionDetailsByCompanyRepository.findAll()) {
            positionDetailsByCompanyRepository.delete(p);
        }



        // for (positionRepository r : positionRepository.findAll()) {
        //positionRepository.delete(r);


        //vagy végig iterálva, de lassabb lenne

        for (Employee e : employeeRepository.findAll()) {
            employeeRepository.delete(e);
        }


        for (Company c : companyRepository.findAll()) {
            companyRepository.delete(c);
        }





        /*
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/employees?useUnicode=true");
        dataSource.setUser("root");
        dataSource.setPassword("root");

         */

/*
        String url = "jdbc:postgresql://localhost/test";
        Properties props = new Properties();
        props.setProperty("user","fred");
        props.setProperty("password","secret");
        props.setProperty("ssl","true");
        Connection conn = DriverManager.getConnection(url, props);

        String url = "jdbc:postgresql://localhost/test?user=fred&password=secret&ssl=true";
        Connection conn = DriverManager.getConnection(url);


 */
        /*
                Flyway flyway = Flyway.configure().dataSource(dataSource).load();

        flyway.clean();
        flyway.migrate();
    */
    }


    @Transactional
    public void insertTestData() {

        //position entitás bekapcsolása
        hu.webuni.logistics.akostomschweger.model.Position developer = positionRepository.save(
                new hu.webuni.logistics.akostomschweger.model.Position("fejlesztő", Qualification.UNIVERSITY));
        hu.webuni.logistics.akostomschweger.model.Position tester = positionRepository.save(
                new hu.webuni.logistics.akostomschweger.model.Position("tesztelő", Qualification.HIGH_SCHOOL));


        Employee employeeA = new Employee(
                1001L,
                "Xkos",
                /*"junior java developer",
                position entitás miatt törölve
                 */
                111111,
                LocalDateTime.of(2018, 1, 11, 11, 11));

        Employee employeeB = new Employee(
                1002L,
                "Ykos",
                /*"senior java developer",

                 */
                222222,
                LocalDateTime.of(2019, 2, 22, 22, 22));


        Employee employeeC = new Employee(
                0L,
                "Zkos",
                /*"senior java developer",

                 */
                333333,
                LocalDateTime.of(2020, 2, 22, 22, 22));


        System.out.println("Employees A,B,C:");
        System.out.println(employeeA);
        System.out.println(employeeB);
        System.out.println(employeeC);

        Company companyA = new Company(
                null,
                "88888",
                "X company",
                "Weert",
                new ArrayList<Employee>());

        //new ArrayList<Employee>(List.of(employeeA,employeeB))

        Company companyB = new Company(
                null,
                "99999",
                "Y company",
                "Best",
                new ArrayList<Employee>());


        //employeeA=employeeRepository.save(employeeA);
        employeeA.setPosition(developer);
        employeeA.setUsername("user1");
        employeeA.setPassword(passwordEncoder.encode("pass"));
        long newUserId = employeeRepository.save(employeeA).getId();
        employeeA.setId(newUserId);

        //employeeB=employeeRepository.save(employeeB);
        employeeB.setPosition(tester);
        employeeB.setUsername("user2");
        employeeB.setPassword(passwordEncoder.encode("pass"));
        employeeB.setManager(employeeA);
        long newUserId2 = employeeRepository.save(employeeB).getId();
        employeeB.setId(newUserId2);



        //employeeRepository.save(employeeC);
        employeeC.setPosition(tester);
        employeeC.setUsername("user3");
        employeeC.setPassword(passwordEncoder.encode("pass"));
        employeeC.setManager(employeeA);
        long newUserId3 = employeeRepository.save(employeeC).getId();
        employeeC.setId(newUserId3);

        System.out.println("usernames added, employee A,B,C now:");
        System.out.println(employeeA);
        System.out.println(employeeB);
        System.out.println(employeeC);

        companyRepository.save(companyA);
        //companyA=employeeRepository.save(companyA)
        companyRepository.save(companyB);
        //positionRepository.save(developer);
        //positionRepository.save(tester);
        //companyB=employeeRepository.save(companyB)
        //this.employeeRepository.flush();
        //this.companyRepository.flush();


        //System.out.println(companyA.getId()); -- ha fent null az ID, akkor a valódit kérdezi le, ha fiktív, pl 9991L, akkor ezt a fiktívet adja vissza
        //companyA.addEmployee(employeeA);
        //companyService.addNewEmployee(companyA.getId(),employeeA);


        //employeeRepository.save(employeeA);
        companyA.addEmployee(employeeA);
        //employeeRepository.save(employeeA); ---> ha aktívak, akkor a security nem megy, felülíródik másodszorra
        companyB.addEmployee(employeeA);
        //employeeRepository.save(employeeA);---> ha aktívak, akkor a security nem megy, felülíródik másodszorra
        companyB.addEmployee(employeeB);
        //employeeRepository.save(employeeB);---> ha aktívak, akkor a security nem megy, felülíródik másodszorra


        PositionDetailsByCompany pd = new PositionDetailsByCompany();
        pd.setCompany(companyA);
        pd.setMinSalary(100000);
        pd.setPosition(developer);
        positionDetailsByCompanyRepository.save(pd);

        PositionDetailsByCompany pd2 = new PositionDetailsByCompany();
        pd.setCompany(companyB);
        pd.setMinSalary(50000);
        pd.setPosition(tester);
        positionDetailsByCompanyRepository.save(pd2);


    }


}
