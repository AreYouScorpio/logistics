package hu.webuni.logistics.akostomschweger.web;

import hu.webuni.logistics.akostomschweger.dto.CompanyDto;
import hu.webuni.logistics.akostomschweger.dto.EmployeeDto;
import hu.webuni.logistics.akostomschweger.repository.CompanyRepository;
import hu.webuni.logistics.akostomschweger.service.InitDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class CompanyControllerIT_new {


    private static final String BASE_URI = "/api/companies";


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    InitDbService initDbService;

    @Autowired
    CompanyController companyController;

    @Autowired
    CompanyRepository companyRepository;


    @Autowired
    EmployeeController employeeController;


    @BeforeEach
    public void init() {
        initDbService.clearDB();
        initDbService.insertTestData();
    }


    @Test
    void testEmployeeListChanged() throws Exception {


        List<CompanyDto> companyListBefore = getAllCompanies();
        long companyIdForModification = companyListBefore.get(1).getId();


        /*
        for(int i=0; i<companyListBefore.size()-1; i++) {
            long changeTestEmployeeId = getCompanyAndItsEmployeeList(companyIdForModification).getEmployees().get(i).getId();
            EmployeeDto employee = employeeController.getById(changeTestEmployeeId);
            employee.setId(changeTestEmployeeId);
        }
         */

        System.out.println("A c??glista m??rete t??rl??s el??tt: " + companyListBefore.size());
        System.out.println("Az 1. elem neve a c??gek list??j??ban? " + companyListBefore.get(1).getName());
        assertEquals("Y company", companyListBefore.get(1).getName());

        List<EmployeeDto> employeeListBefore =
                getCompanyByIdFull(companyIdForModification).getEmployees();

        System.out.println("A dolgoz??i lista elemeinek sz??ma m??dos??t??s el??tt: " + employeeListBefore.size());
        System.out.println("A dolgoz??i lista utols?? elem??nek neve m??dos??t??s el??tt: " + employeeListBefore.get(employeeListBefore.size() - 1).getName());


        List<EmployeeDto> employeeListForChange =
                new ArrayList<>(List.of(
                        new EmployeeDto("A-Paci", "A-pos", 100,
                                LocalDateTime.of(2001, Month.FEBRUARY, 3, 6, 30)),
                        new EmployeeDto("B-Paci", "B-pos", 200,
                                LocalDateTime.of(2002, Month.FEBRUARY, 3, 6, 30)),
                        new EmployeeDto("C-Paci", "C-pos", 300,
                                LocalDateTime.of(2003, Month.FEBRUARY, 3, 6, 30))
                ));

        replaceEmployees(companyIdForModification, employeeListForChange);

        List<CompanyDto> companyListAfter = getAllCompaniesWithoutEmployees();
        List<EmployeeDto> employeeListAfter =
                getCompanyByIdFull(companyIdForModification).getEmployees();

        System.out.println("A c??glista m??rete t??rl??s ut??n: " + companyListAfter.size());
        System.out.println("A dolgoz??i lista elemeinek sz??ma m??dos??t??s ut??n: " + employeeListAfter.size());
        System.out.println("A dolgoz??i lista utols?? elem??nek neve m??dos??t??s ut??n: " + employeeListAfter.get(employeeListAfter.size() - 1).getName());


        assertThat(employeeListAfter)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrderElementsOf(employeeListForChange);


    }

    @Test
    void testEmployeeDeleted() throws Exception {
        List<CompanyDto> companyListBefore = getAllCompanies();
        System.out.println("A c??glista m??rete t??rl??s el??tt: " + companyListBefore.size());
        System.out.println("A 0. elem neve a c??gek list??j??ban? " + companyListBefore.get(0).getName());
        assertEquals("X company", companyListBefore.get(0).getName());


        long deleteTestCompanyId = companyListBefore.get(0).getId();
        long deleteTestEmployeeId = getCompanyByIdFull(deleteTestCompanyId).getEmployees().get(0).getId();

        System.out.println("A 0. elem?? c??g aktu??lis ID-ja: " + deleteTestCompanyId);
        CompanyDto companyDto = getCompanyByIdFull(companyListBefore.get(0).getId());
        System.out.println("A 0. elem?? c??g (ID: " + deleteTestCompanyId + " )  0. elem?? dolgoz??j??nak neve: " + getCompanyByIdFull(deleteTestCompanyId).getEmployees().get(0).getName());
        System.out.println("A 0. elem?? c??g (ID: " + deleteTestCompanyId + " )  0. elem?? dolgoz??j??nak ID-ja: " + deleteTestEmployeeId);
        System.out.println("A c??glista m??rete t??rl??s el??tt: " + companyListBefore.size());
        System.out.println("A 0. elem?? c??g dolgoz??inak sz??ma t??rl??s el??tt: " + getCompanyByIdFull(deleteTestCompanyId).getEmployees().size());


        //EmployeeDto employee = getEmployeeList(deleteTestCompanyId);
        //employeeController.getById(deleteTestEmployeeId);
        System.out.println("A t??rlend?? c??g ID: " + deleteTestCompanyId + " ??s employee ID: " + deleteTestEmployeeId);


        deleteEmployeeFromCompany(deleteTestCompanyId, deleteTestEmployeeId);


        //System.out.println(deletedId);

        //getEmployeeList(deleteTestCompanyId).setCompany(null);
        //Employee employee = employeeRepository.findById(employee_id).get();
        //mployee.setCompany(null);
        //getCompanyByIdFull(deleteTestCompanyId).getEmployees().remove(deleteID);


        List<CompanyDto> companyListAfter = getAllCompanies();

        System.out.println("A c??glista m??rete t??rl??s ut??n: " + companyListAfter.size());
        System.out.println("A 0. elem?? c??g dolgoz??inak sz??ma t??rl??s ut??n: " + getCompanyByIdFull(deleteTestCompanyId).getEmployees().size());


        assertThat(getCompanyByIdFull(deleteTestCompanyId).getEmployees().size() == 0);
        assertThat(companyListAfter.size() == companyListBefore.size());


        //System.out.println(companyListBefore.get(0).getEmployees().get(0).getName());
        //companyController.deleteCompany(companyListBefore.size()-1);
        /*
        Company company =
                companyRepository
                        .findById((long)(companyListBefore.size()-1)).get();
        System.out.println(company.getName());
           */
        //Employee employee = employeeRepository.findById(employee_id).get();


        //removeIf(e -> e.getId() == employee_id);
        //employee.setCompany(null);
        //company.getEmployees().remove(employee);


    }


    @Test
    void testEmployeeAdded() throws Exception {

        // /addemployeetocompany/{company_id}

        // !! company-t EAGER-re alak??tani, h m??k??dj??n !!


        // companyRepository.deleteAllInBatch();

        //createCompany(companyDto);

        // ez nem dto ad vissza, hanem companyt:
        List<CompanyDto> companyListBefore = getAllCompaniesWithoutEmployees();
        long companyIdForModification = companyListBefore.get(1).getId();
        System.out.println("A m??dos??tand?? company ID (old): " + companyIdForModification);
        //CompanyDto companyWithEmployeesBefore =
        // getCompanyAndItsEmployeeList(companyIdForModification);
        //System.out.println(companyWithEmployeesBefore);
        //System.out.println(companyListBefore);

        System.out.println("OK");

        CompanyDto company = new CompanyDto(
                "666", "HelloCegNev", "Kiskunhalas", new ArrayList<EmployeeDto>());

        EmployeeDto employeeX =
                new EmployeeDto("Akos", "springtanul??", 200,
                        LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30));

        //company.addNewEmployee(employeeX);
        long savedIdForTesting = createCompany(company);

        System.out.println("A m??dos??tand?? company ID (new): " + savedIdForTesting);


        //companyController.addEmployeeToCompany(savedIdForTesting, employeeX);
        //controller helyett webTest h??v??s:

        long employeeSavedId = saveEmployee(employeeX, savedIdForTesting);

        System.out.println("OK, elmentett Employee ID-ja WebTestClientb??l: " + employeeSavedId);

        //System.out.println(getCompanyAndItsEmployeeList(savedIdForTesting));
        System.out.println("Employee felvitele ut??ni teljes c??glista WebTestClientb??l: " + getAllCompanies());
        //System.out.println("Employee felvitele ut??ni teljes c??glista employeekkal WebTestClientb??l: " + getCompanyAndItsEmployeeList(savedIdForTesting));


        //System.out.println("c??glista record companyRepob??l: " + companyRepository.findAllWithEmployees());
        //System.out.println("c??g list??j??b??l a hozz??adott employee neve (Repo): " +
        //        getCompanyAndItsEmployeeList(savedIdForTesting).getEmployees().get(0).getName());
        //companyController.addEmployeeToCompany(1L, employeeX);

        //System.out.println("ez a c??g: " + company.getName());
        System.out.println("a lek??rdezend?? c??g aktu??lis ID-ja: " + savedIdForTesting);

        System.out.println("ez a c??g WebTestClientb??l: " + getCompanyByIdFull(savedIdForTesting));

        System.out.println("c??g eredeti ID: " + company.getId() + "  ??j gener??lt id: " + savedIdForTesting);

        company.setId(savedIdForTesting); // hogy a teszt lefusson, ne null legyen a gener??lt ID

        assertEquals("Akos", getCompanyByIdFull(savedIdForTesting).getEmployees().get(0).getName());
        assertEquals("springtanul??", getCompanyByIdFull(savedIdForTesting).getEmployees().get(0).getPosition());
        assertEquals(200, getCompanyByIdFull(savedIdForTesting).getEmployees().get(0).getSalary());
        assertEquals(LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30),
                getCompanyByIdFull(savedIdForTesting).getEmployees().get(0).getStartDateAtTheCompany());

        /*
        System.out.println("c??g employee lista m??rete: " +
                companyController
                        .getCompaniesAboveEmployeeNumber(0,true)
                        .get(0)
                        .getEmployees()
                        .size());


         */

        System.out.println("EmployeeDto lista a " + savedIdForTesting + ". ID-hoz WebTestClienttel : " + getEmployeeList(savedIdForTesting));

        List<CompanyDto> companyListAfter = getAllCompaniesWithoutEmployees();


        assertThat(companyListAfter.subList(0, companyListBefore.size()))
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(companyListBefore);

        assertThat(companyListAfter
                .get(companyListAfter.size() - 1))
                .usingRecursiveComparison()
                .isEqualTo(company);


    }


    private void deleteEmployeeFromCompany(long companyId, long employeeId) {


        webTestClient
                .delete()
                .uri(BASE_URI + "/" + companyId + "/employees/" + employeeId)
                .exchange()
                .expectStatus()
                .isOk();
        //.expectBody(EmployeeDto.class)
        //.returnResult().getResponseBody().getId();

        //getEmployeeList(companyId).remove(employeeId);

        //Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        //return responseList;


        //return companyController.getById(id, true);
    }

    private long createCompany(CompanyDto company) { //ok


        long Id = webTestClient
                .post()
                .uri(BASE_URI)
                .bodyValue(company)
                .exchange()
                .expectStatus()
                .isOk().expectBody(CompanyDto.class).returnResult().getResponseBody().getId();


        return Id;
        //return companyController.createCompany(company).getId();
    }

    private List<EmployeeDto> get(long id) {


        List<EmployeeDto> responseList = webTestClient
                .get()
                .uri(BASE_URI + "/{id}")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CompanyDto.class)
                .returnResult().getResponseBody().getEmployees();

        //Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        return responseList;


        //return companyController.getById(id, true);
    }


    private CompanyDto getCompanyByIdFull(long id) {


        CompanyDto responseList = webTestClient
                .get()
                .uri(BASE_URI + "/" + id + "?full=true")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CompanyDto.class)
                .returnResult().getResponseBody();

        //Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        return responseList;


        //return companyController.getById(id, true);
    }

    private CompanyDto getCompanyByIdShort(long id) {


        CompanyDto responseList = webTestClient
                .get()
                .uri(BASE_URI + "/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CompanyDto.class)
                .returnResult().getResponseBody();

        //Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        return responseList;


        //return companyController.getById(id, true);
    }

    private long saveEmployee(EmployeeDto newEmployee, long company_id) { //ok
        return webTestClient
                .post()
                .uri(BASE_URI + "/addemployeetocompany/" + company_id)
//				//.headers(headers -> headers.setBasicAuth(username, pass))
                //.headers(headers -> headers.setBearerAuth(jwt))
                .bodyValue(newEmployee)
                .exchange()
                .expectBody(CompanyDto.class)
                .returnResult()
                .getResponseBody()
                .getEmployees().get(0).getId();
    }

    private List<CompanyDto> getAllCompanies() {

        // ??talak??tani, ez m??g a companykat k??rdezi csak le


        List<CompanyDto> responseList = webTestClient
                .get()
                .uri(BASE_URI + "?full=true")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CompanyDto.class)
                .returnResult().getResponseBody();

        Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        return responseList;
    }

    private List<CompanyDto> getAllCompaniesWithoutEmployees() {

        // ??talak??tani, ez m??g a companykat k??rdezi csak le


        List<CompanyDto> responseList = webTestClient
                .get()
                .uri(BASE_URI)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CompanyDto.class)
                .returnResult().getResponseBody();

        Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        return responseList;
    }

    private List<EmployeeDto> getEmployeeList(long company_id) {


        List<EmployeeDto> responseList = webTestClient
                .get()
                .uri(BASE_URI + "/" + company_id + "?full=true")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CompanyDto.class)
                .returnResult().getResponseBody().getEmployees();

        Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        return responseList;


        //return companyController.getById(id, true);
    }

    private List<CompanyDto> replaceEmployees(long companyIdForModification, List<EmployeeDto> employeeListForChange) {

        // ??talak??tani, ez m??g a companykat k??rdezi csak le


        List<CompanyDto> responseList = webTestClient
                .put()
                .uri(BASE_URI + "/" + companyIdForModification + "/employees")
                .bodyValue(employeeListForChange)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CompanyDto.class)
                .returnResult().getResponseBody();

        Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        return responseList;
    }

}

