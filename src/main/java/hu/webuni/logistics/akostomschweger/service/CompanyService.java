package hu.webuni.logistics.akostomschweger.service;

import hu.webuni.logistics.akostomschweger.model.Company;
import hu.webuni.logistics.akostomschweger.model.Employee;
import hu.webuni.logistics.akostomschweger.repository.CompanyRepository;
import hu.webuni.logistics.akostomschweger.repository.EmployeeRepository;
import hu.webuni.logistics.akostomschweger.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    EmployeeService employeeService;
    //@Autowired
    //EmployeeSuperClass employeeSuperClass;
    @Autowired
    PositionRepository positionRepository;

    /*

    private Map<Long, Company> companies = new HashMap<>();

    {
        companies.put(1L, new Company(
                1L,
                "11111",
                "A company",
                "Budapest",
                new ArrayList<EmployeeDto>()

        ));
        companies.put(2L, new Company(
                2L,
                "22222",
                "B company",
                "Amsterdam",
                new ArrayList<EmployeeDto>()

        ));
    }


     */

    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository, EmployeeService employeeService) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    /*
    public CompanyService(CompanyRepository companyRepository,
                          EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }


     */


    @Transactional
    public Company save(Company company) {
        // companies.put(company.getId(), company);
        // return company;
        return companyRepository.save(company);
    }


    public List<Company> findAll() {
        // return new ArrayList<>(companies.values());
        // System.out.println(companyRepository.findAll().stream().findFirst().get().getEmployees().get(0).getName());
        return companyRepository.findAll();
    }

    public Optional<Company> findById(long id) {
        // return companies.get(id);
        return companyRepository.findById(id);
    }

    /*
    public Company update(long id, Company company) {
        companies.put(id, company);
        return company;
    }
     */

    @Transactional
    public Company update(Company company) {
        if (companyRepository.existsById(company.getId()))
            return companyRepository.save(company);
        else throw new NoSuchElementException();
    }

    @Transactional
    public void delete(long id) {
        //companies.remove(id);
        companyRepository.deleteById(id);
    }

    @Transactional
    public Company addNewEmployee(long company_id, Employee employee) {

        Optional<Company> company = companyRepository.findById(company_id);
        if (!company.isPresent()) return null;
        else {
            Company companyToSave = company.get();
            companyToSave.addEmployee(employee);
            //employeeRepository.save(employee); ----->
            employeeService.save(employee);

            // ---
/*
        Position position = employee.getPosition();
        if (position != null) {
            String positionName = position.getName();
            if (!ObjectUtils.isEmpty(positionName)) {
                Position positionInDb = null;
                Optional<Position> foundPosition = positionRepository.findByName(positionName);
                if (foundPosition.isPresent())
                    positionInDb = foundPosition.get();
                else {
                    positionInDb = positionRepository.save(position);
                }
                employee.setPosition(positionInDb);
            } else {
                employee.setPosition(null);
            }

        }

        // ---

 */
            return companyToSave;
        }

    }

    /*
    employeeservice superclass-b??l ??temelve, hogy itt is megval??s??tsam

    @Transactional
    public Employee save(Employee employee) {
        // employees.put(employee.getId(), employee);
        employee.setId(null); // biztos ami biztos ne fel??l??rjon
        Position position = employee.getPosition();
        if (position!=null) {
            String positionName = position.getName();
            if(!ObjectUtils.isEmpty(positionName)) {
                Position positionInDb = null;
                Optional<Position> foundPosition = positionRepository.findByName(positionName);
                if(foundPosition.isPresent())
                    positionInDb = foundPosition.get();
                else {
                    positionInDb = positionRepository.save(position);
                }
                employee.setPosition(positionInDb);
            } else {
                employee.setPosition(null);
            }
        }
        return employeeRepository.save(employee);// employee;
    }



     */


    @Transactional
    public Company deleteEmployeeFromCompany(long company_id, long employee_id) {
        Company company = companyRepository.findById(company_id).get();
        Employee employee = employeeRepository.findById(employee_id).get();
        //removeIf(e -> e.getId() == employee_id);
        employee.setCompany(null);
        company.getEmployees().remove(employee);
        // employeeService.save(employee); ---> a transactional miatt nem kell.. am??gy j?? volt, am??g IT tesztben nem WebTestClienttel pr??b??ltam, de ut??na musz??j volt t??r??lni
        return company;
    }

    @Transactional
    public Company modifyCompany(long company_id, List<Employee> employees) {
        Company company = companyRepository.findById(company_id).get();

        for (Employee employee : company.getEmployees()) {
            employee.setCompany(null);
        }
        company.getEmployees().clear();


        for (Employee employee : company.getEmployees()) {
            company.addEmployee(employee);
            employeeService.save(employee);
        }


        // company.setEmployeeDtoList(employees);

        return company;
    }

    @Transactional
    public Company replaceEmployees(long id, List<Employee> employees) {
        Company company = companyRepository.findById(id).get();
        for (Employee employee : company.getEmployees()) {
            employee.setCompany(null);
        }
        //m??sodik oldalr??l is
        company.getEmployees().clear();
        for (Employee employee : employees) {
            company.addEmployee(employee);
            employeeService.save(employee);
        }
        return company;

    }

    public List<Company> findCompaniesByExample(Company example) {
        System.out.println("Az example company: " + example);
        long id = 0L;
        //if(example.getId()>0) id = example.getId();

        if (example.getId() != null) {
            id = example.getId();
        }
        ;
        System.out.println("Example ID: " + id);

        String name = "";
        if (example.getName() != null) {
            name = example.getName();
        }
        ;

        //name = example.getName();
        System.out.println("Example name: " + name);

        Specification<Company> spec = Specification.where(null); // ??res Specification, ami semmire nem sz??r

        if (id > 0) {
            spec = spec.and(CompanySpecifications.hasId(id));
        }

        if (StringUtils.hasText(name)) // SpringFramework-??s StringUtils
            spec = spec.and(CompanySpecifications.hasCompanyName(name));


        return companyRepository.findAll(spec, Sort.by("id"));
    }
}
