package hu.webuni.logistics.akostomschweger.web;

import hu.webuni.logistics.akostomschweger.dto.EmployeeDto;
import hu.webuni.logistics.akostomschweger.dto.HolidayRequestDto;
import hu.webuni.logistics.akostomschweger.service.HolidayRequestService;
import hu.webuni.logistics.akostomschweger.service.InitDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles({"prod", "test"}) // profile switcher: both profiles on
public class HolidayRequestsIT {

    private static final String BASE_URI = "/api/employees";
    private static final String BASE_URI_HR = "/api/holidayrequests";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    InitDbService initDbService;

    @Autowired
    HolidayRequestService holidayRequestService;

    @Autowired
    HolidayRequestController holidayRequestController;

    @BeforeEach
    public void init() {
        initDbService.clearDB();
        initDbService.insertTestData();
    }

    @Test
    void testCreateHolidayRequest() throws Exception {
        // EmployeeController employeeController = new EmployeeController();


        List<EmployeeDto> employeesBefore = getAllEmployees();
        System.out.println(employeesBefore);

        EmployeeDto employee1 =
                new EmployeeDto(1L, "Akos", "jsj", 100,
                        LocalDateTime.of(2017, Month.JANUARY, 3, 6, 30));
        EmployeeDto employee2 =
                new EmployeeDto(2L, "Bkos", "jxj", 200,
                        LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30));
        // employeeController.createEmployee(employee);
        long employee1id = createEmployee(employee1);
        System.out.println("employee1id: " + employee1id);
        createEmployee(employee2);
        long employee2id = createEmployee(employee2);
        System.out.println("employee2id: " + employee2id);


        List<EmployeeDto> employeesAfter = getAllEmployees();
        System.out.println(employeesAfter);


        List<HolidayRequestDto> holidayRequestsBefore = getAllHRs();
        System.out.println("holidayRequestsBefore: " + holidayRequestsBefore);

        HolidayRequestDto holidayRequest = new HolidayRequestDto(
                1283L, //id
                LocalDateTime.now(), // "createdAt",
                employee1id, // "employeeId"
                employee2id, // "approverId"
                null,// "approved"
                null, // "approvedAt"
                LocalDate.of(2022, 07, 26), // "startDate"
                LocalDate.of(2022, 07, 28) // "endDate"
        );

        long HR_Id = createHR(holidayRequest);
        holidayRequest.setId(HR_Id);
        System.out.println("HR_details: " + holidayRequest);
        System.out.println("HR_Id: " + HR_Id);
        //holidayRequest.setId(HR_Id);

        List<HolidayRequestDto> holidayRequestsAfter = getAllHRs();
        System.out.println("holidayRequestsAfter: " + holidayRequestsAfter);

        assertThat(holidayRequestsAfter.subList(0, holidayRequestsBefore.size()))
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(holidayRequestsBefore);

        assertThat(holidayRequestsAfter
                .get(holidayRequestsAfter.size() - 1))
                .usingRecursiveComparison().ignoringFields("createdAt")
                .isEqualTo(holidayRequest);


        // Employee employeeTest = employeeService.findById(21L);
        //assertThat(employeeTest.getSalary()).isEqualTo(200);


    }


    @Test
    void testGetHR_ById() throws Exception {
        // EmployeeController employeeController = new EmployeeController();


        List<EmployeeDto> employeesBefore = getAllEmployees();
        System.out.println(employeesBefore);

        EmployeeDto employee1 =
                new EmployeeDto(1L, "Akos", "jsj", 100,
                        LocalDateTime.of(2017, Month.JANUARY, 3, 6, 30));
        EmployeeDto employee2 =
                new EmployeeDto(2L, "Bkos", "jxj", 200,
                        LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30));
        // employeeController.createEmployee(employee);
        long employee1id = createEmployee(employee1);
        System.out.println("employee1id: " + employee1id);
        createEmployee(employee2);
        long employee2id = createEmployee(employee2);
        System.out.println("employee2id: " + employee2id);


        List<EmployeeDto> employeesAfter = getAllEmployees();
        System.out.println(employeesAfter);


        List<HolidayRequestDto> holidayRequestsBefore = getAllHRs();
        System.out.println("holidayRequestsBefore: " + holidayRequestsBefore);

        HolidayRequestDto holidayRequest = new HolidayRequestDto(
                1283L, //id
                LocalDateTime.now(), // "createdAt",
                employee1id, // "employeeId"
                employee2id, // "approverId"
                null,// "approved"
                null, // "approvedAt"
                LocalDate.of(2022, 07, 26), // "startDate"
                LocalDate.of(2022, 07, 28) // "endDate"
        );

        long HR_Id = createHR(holidayRequest);
        holidayRequest.setId(HR_Id);
        System.out.println("HR_details: " + holidayRequest);
        System.out.println("HR_Id: " + HR_Id);
        //holidayRequest.setId(HR_Id);

        System.out.println("WebClient response ID: " + getHR_ById(HR_Id).getId());
        assertEquals(HR_Id, getHR_ById(HR_Id).getId());


    }


    @Test
    void testHR_Deleted() throws Exception {


        List<EmployeeDto> employeesBefore = getAllEmployees();
        System.out.println(employeesBefore);

        EmployeeDto employee1 =
                new EmployeeDto(1L, "Akos", "jsj", 100,
                        LocalDateTime.of(2017, Month.JANUARY, 3, 6, 30));
        EmployeeDto employee2 =
                new EmployeeDto(2L, "Bkos", "jxj", 200,
                        LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30));
        // employeeController.createEmployee(employee);
        long employee1id = createEmployee(employee1);
        System.out.println("employee1id: " + employee1id);
        createEmployee(employee2);
        long employee2id = createEmployee(employee2);
        System.out.println("employee2id: " + employee2id);


        List<EmployeeDto> employeesAfter = getAllEmployees();
        System.out.println(employeesAfter);


        List<HolidayRequestDto> holidayRequestsBefore = getAllHRs();
        System.out.println("holidayRequestsBefore: " + holidayRequestsBefore);

        HolidayRequestDto holidayRequest = new HolidayRequestDto(
                1283L, //id
                LocalDateTime.now(), // "createdAt",
                employee1id, // "employeeId"
                employee2id, // "approverId"
                null,// "approved"
                null, // "approvedAt"
                LocalDate.of(2022, 07, 26), // "startDate"
                LocalDate.of(2022, 07, 28) // "endDate"
        );

        long HR_Id = createHR(holidayRequest);
        holidayRequest.setId(HR_Id);
        System.out.println("HR_details: " + holidayRequest);
        System.out.println("HR_Id: " + HR_Id);
        //holidayRequest.setId(HR_Id);

        System.out.println("WebClient response ID: " + getHR_ById(HR_Id).getId());

        List<HolidayRequestDto> holidayRequestsBeforeDelete = getAllHRs();
        System.out.println("Size before delete: " + holidayRequestsBeforeDelete.size());

        System.out.println("HR to be deleted: " + getHR_ById(HR_Id));

        deleteHR(HR_Id);


        List<HolidayRequestDto> holidayRequestsAfterDelete = getAllHRs();
        System.out.println("Size after delete: " + holidayRequestsAfterDelete.size());


        assertEquals(holidayRequestsAfterDelete.size(), holidayRequestsBeforeDelete.size()-1);
//      assertEquals(getHR_ById(HR_Id), null);


    }


    @Test
    void testHR_Approved() throws Exception {


        List<EmployeeDto> employeesBefore = getAllEmployees();
        System.out.println(employeesBefore);

        EmployeeDto employee1 =
                new EmployeeDto(1L, "Akos", "jsj", 100,
                        LocalDateTime.of(2017, Month.JANUARY, 3, 6, 30));
        EmployeeDto employee2 =
                new EmployeeDto(2L, "Bkos", "jxj", 200,
                        LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30));
        // employeeController.createEmployee(employee);
        long employee1id = createEmployee(employee1);
        System.out.println("employee1id: " + employee1id);
        createEmployee(employee2);
        long employee2id = createEmployee(employee2);
        System.out.println("employee2id: " + employee2id);


        List<EmployeeDto> employeesAfter = getAllEmployees();
        System.out.println(employeesAfter);


        List<HolidayRequestDto> holidayRequestsBefore = getAllHRs();
        System.out.println("holidayRequestsBefore: " + holidayRequestsBefore);

        HolidayRequestDto holidayRequest = new HolidayRequestDto(
                1283L, //id
                LocalDateTime.now(), // "createdAt",
                employee1id, // "employeeId"
                employee2id, // "approverId"
                null,// "approved"
                null, // "approvedAt"
                LocalDate.of(2022, 07, 26), // "startDate"
                LocalDate.of(2022, 07, 28) // "endDate"
        );

        long HR_Id = createHR(holidayRequest);
        holidayRequest.setId(HR_Id);
        System.out.println("HR_details: " + holidayRequest);
        System.out.println("HR_Id: " + HR_Id);
        //holidayRequest.setId(HR_Id);

        System.out.println("WebClient response ID: " + getHR_ById(HR_Id).getId());
        long approverId = getHR_ById(HR_Id).getApproverId();
        System.out.println("ApproverID: " + approverId);
        List<HolidayRequestDto> holidayRequestsBeforeApproved = getAllHRs();
        boolean statusBeforeApproved;
        if (getHR_ById(HR_Id).getApproved() == null)
            statusBeforeApproved = false;
        else
            statusBeforeApproved = getHR_ById(HR_Id).getApproved();
        System.out.println("Status before approved: "
                + statusBeforeApproved);

        approveHR(HR_Id, approverId);


        List<HolidayRequestDto> holidayRequestsAfterApproved = getAllHRs();
        boolean statusAfterApproved;
        if (getHR_ById(HR_Id).getApproved() == null)
            statusAfterApproved = false;
        else
            statusAfterApproved = getHR_ById(HR_Id).getApproved();

        System.out.println("Status after approved: "
                + statusAfterApproved);


        assertEquals(false, statusBeforeApproved);
        assertEquals(true, statusAfterApproved);


    }


    @Test
    void testModifyHolidayRequest() throws Exception {
        // EmployeeController employeeController = new EmployeeController();


        List<EmployeeDto> employeesBefore = getAllEmployees();
        System.out.println(employeesBefore);

        EmployeeDto employee1 =
                new EmployeeDto(1L, "Akos", "jsj", 100,
                        LocalDateTime.of(2017, Month.JANUARY, 3, 6, 30));
        EmployeeDto employee2 =
                new EmployeeDto(2L, "Bkos", "jxj", 200,
                        LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30));
        // employeeController.createEmployee(employee);
        long employee1id = createEmployee(employee1);
        System.out.println("employee1id: " + employee1id);
        createEmployee(employee2);

        long employee2id = createEmployee(employee2);
        System.out.println("employee2id: " + employee2id);


        List<EmployeeDto> employeesAfter = getAllEmployees();
        System.out.println(employeesAfter);


        List<HolidayRequestDto> holidayRequestsBefore = getAllHRs();
        System.out.println("holidayRequestsBefore: " + holidayRequestsBefore);

        HolidayRequestDto holidayRequest = new HolidayRequestDto(
                1283L, //id
                LocalDateTime.now(), // "createdAt",
                employee1id, // "employeeId"
                employee2id, // "approverId"
                null,// "approved"
                null, // "approvedAt"
                LocalDate.of(2022, 07, 26), // "startDate"
                LocalDate.of(2022, 07, 28) // "endDate"
        );


        long HR_Id = createHR(holidayRequest);
        long employeeIdOld = holidayRequest.getEmployeeId();
        System.out.println("employeeIdOld: " + employeeIdOld);
        long approverIdOld = holidayRequest.getApproverId();
        System.out.println("approverIdOld: " + approverIdOld);

        holidayRequest.setId(HR_Id);
        System.out.println("HR_details: " + holidayRequest);
        System.out.println("HR_Id: " + HR_Id);
        //holidayRequest.setId(HR_Id);

        HolidayRequestDto holidayRequest2 = new HolidayRequestDto(
                HR_Id, //id
                LocalDateTime.now(), // "createdAt",
                employee1id, // "employeeId"
                employee2id, // "approverId"
                null,// "approved"
                null, // "approvedAt"
                LocalDate.of(2022, 07, 27), // "startDate"
                LocalDate.of(2022, 07, 29) // "endDate"
        );

        System.out.println("most j??n a m??dos??t??s.. " + HR_Id + "id-ra , ??s erre a HR-re:" + holidayRequest2);
        modifyHR(HR_Id, holidayRequest2);


        List<HolidayRequestDto> holidayRequestsAfter = getAllHRs();
        System.out.println("holidayRequestsAfter: " + holidayRequestsAfter);

        assertEquals(27, getHR_ById(HR_Id).getStartDate().getDayOfMonth());
        assertNotEquals(26, getHR_ById(HR_Id).getStartDate().getDayOfMonth());
        assertEquals(29, getHR_ById(HR_Id).getEndDate().getDayOfMonth());
        assertNotEquals(28, getHR_ById(HR_Id).getEndDate().getDayOfMonth());


        // Employee employeeTest = employeeService.findById(21L);
        //assertThat(employeeTest.getSalary()).isEqualTo(200);


    }


    @Test
    void testSearchHolidayRequest() throws Exception {
        // EmployeeController employeeController = new EmployeeController();


        List<EmployeeDto> employeesBefore = getAllEmployees();
        System.out.println(employeesBefore);

        EmployeeDto employee1 =
                new EmployeeDto(1L, "Akos", "jsj", 100,
                        LocalDateTime.of(2017, Month.JANUARY, 3, 6, 30));
        EmployeeDto employee2 =
                new EmployeeDto(2L, "Bkos", "jxj", 200,
                        LocalDateTime.of(2017, Month.FEBRUARY, 3, 6, 30));
        // employeeController.createEmployee(employee);
        long employee1id = createEmployee(employee1);
        System.out.println("employee1id: " + employee1id);
        createEmployee(employee2);

        long employee2id = createEmployee(employee2);
        System.out.println("employee2id: " + employee2id);


        List<EmployeeDto> employeesAfter = getAllEmployees();
        System.out.println(employeesAfter);


        List<HolidayRequestDto> holidayRequestsBefore = getAllHRs();
        System.out.println("holidayRequestsBefore: " + holidayRequestsBefore);

        HolidayRequestDto holidayRequest = new HolidayRequestDto(
                1283L, //id
                LocalDateTime.now(), // "createdAt",
                employee1id, // "employeeId"
                employee2id, // "approverId"
                null,// "approved"
                null, // "approvedAt"
                LocalDate.of(2022, 07, 26), // "startDate"
                LocalDate.of(2022, 07, 28) // "endDate"
        );

        HolidayRequestDto holidayRequest2 = new HolidayRequestDto(
                1284L, //id
                LocalDateTime.now(), // "createdAt",
                employee2id, // "employeeId"
                employee1id, // "approverId"
                true,// "approved"
                LocalDateTime.of(2022,07,22,10,10,01,111), // "approvedAt"
                LocalDate.of(2022, 07, 27), // "startDate"
                LocalDate.of(2022, 07, 29) // "endDate"
        );


        long HR_Id = createHR(holidayRequest);
        long HR_Id2 = createHR(holidayRequest2);

        long approverId1 = holidayRequest.getApproverId();
        System.out.println("approverId1: " + approverId1);

        long approverId2 = holidayRequest2.getApproverId();
        System.out.println("approverId2: " + approverId2);

        holidayRequest.setId(HR_Id);
        holidayRequest2.setId(HR_Id2);
        System.out.println("HR_details_1: " + holidayRequest);
        System.out.println("HR_Id1: " + HR_Id);
        System.out.println("HR_details_2: " + holidayRequest2);
        System.out.println("HR_Id2: " + HR_Id2);

        HolidayRequestDto metamodel = new HolidayRequestDto();
        metamodel.setId(0L);
        metamodel.setCreatedAt(null);
        metamodel.setEmployeeId(null);
        metamodel.setApproverId(null);
        metamodel.setApproved(true);
        metamodel.setApprovedAt(null);
        metamodel.setStartDate(null);
        metamodel.setEndDate(null);
        System.out.println("metamodel: " + metamodel);

        List<HolidayRequestDto> result = searchHR(metamodel);

        System.out.println("A tal??lat HR lista " + result);


        //List<HolidayRequestDto> holidayRequestsAfter = getAllHRs();
        //System.out.println("holidayRequestsAfter: " + holidayRequestsAfter);

        assertEquals(true, result.get(0).getApproved());
        //assertNotEquals(26, getHR_ById(HR_Id).getStartDate().getDayOfMonth());
        //assertEquals(29, getHR_ById(HR_Id).getEndDate().getDayOfMonth());
        //assertNotEquals(28, getHR_ById(HR_Id).getEndDate().getDayOfMonth());


        // Employee employeeTest = employeeService.findById(21L);
        //assertThat(employeeTest.getSalary()).isEqualTo(200);


    }


    private long createHR(HolidayRequestDto holidayRequestDto) {
        long id = webTestClient
                .post()
                .uri(BASE_URI_HR)
                .bodyValue(holidayRequestDto)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(HolidayRequestDto.class).returnResult().getResponseBody().getId();

        return id;
    }

    private long createEmployee(EmployeeDto employee) {
        long id = webTestClient
                .post()
                .uri(BASE_URI)
                .bodyValue(employee)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(EmployeeDto.class).returnResult().getResponseBody().getId();
        ;

        return id;
    }

    private List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> responseList =
                webTestClient
                        .get()
                        .uri(BASE_URI)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(EmployeeDto.class)
                        .returnResult()
                        .getResponseBody();

        Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));
        return responseList;

    }

    private void deleteHR(long id) {
        webTestClient
                .delete()
                .uri(BASE_URI_HR + "/" + id)
                .exchange()
                .expectStatus()
                .isOk()
        ;
    }

    private List<HolidayRequestDto> getAllHRs() {
        List<HolidayRequestDto> responseList =
                webTestClient
                        .get()
                        .uri(BASE_URI_HR)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(HolidayRequestDto.class)
                        .returnResult()
                        .getResponseBody();

        Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));
        return responseList;

    }


    private HolidayRequestDto getHR_ById(long id) {
        HolidayRequestDto response =
                webTestClient
                        .get()
                        .uri(BASE_URI_HR + "/" + id)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(HolidayRequestDto.class)
                        .returnResult()
                        .getResponseBody().get(0);


        return response;


    }


    private void approveHR(long HR_Id, long approverId) {
        //HolidayRequestDto response =
        webTestClient
                .put()
                .uri(BASE_URI_HR + "/" + HR_Id + "/" + "approval?status=true&approverId=" + approverId)
                //.contentType(APPLICATION_JSON)
                //.syncBody(holidayRequestDtoNew)
                //.accept(MediaType.APPLICATION_JSON)
                //.contentType(APPLICATION_JSON)
                //.bodyValue(holidayRequestDtoNew)
                .exchange()
                .expectStatus()
                .isOk();
        //.expectBody(HolidayRequestDto.class)
        //.returnResult().getResponseBody();
//return response;





            /*
        webTestClient
                .put()
                .uri(BASE_URI_HR + "/" + id)
                .bodyValue(holidayRequestDtoNew)
                .exchange()
                .expectStatus()
                .isOk();


             */


    }

    private void modifyHR(long id, HolidayRequestDto holidayRequestDtoNew) {
        //HolidayRequestDto response =
        webTestClient
                .put()
                .uri(BASE_URI_HR + "/" + id)
                //.contentType(APPLICATION_JSON)
                //.syncBody(holidayRequestDtoNew)
                //.accept(MediaType.APPLICATION_JSON)
                //.contentType(APPLICATION_JSON)
                .bodyValue(holidayRequestDtoNew)
                .exchange()
                .expectStatus()
                .isOk();
        //.expectBody(HolidayRequestDto.class)
        //.returnResult().getResponseBody();
//return response;





            /*
        webTestClient
                .put()
                .uri(BASE_URI_HR + "/" + id)
                .bodyValue(holidayRequestDtoNew)
                .exchange()
                .expectStatus()
                .isOk();


             */


    }


    private List<HolidayRequestDto> searchHR(HolidayRequestDto metamodel) {
        List<HolidayRequestDto> responseList =
                webTestClient
                        .post()
                        .uri(BASE_URI_HR + "/search")
                        //.contentType(APPLICATION_JSON)
                        //.syncBody(holidayRequestDtoNew)
                        //.accept(MediaType.APPLICATION_JSON)
                        //.contentType(APPLICATION_JSON)
                        .bodyValue(metamodel)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBodyList(HolidayRequestDto.class)
                        .returnResult().getResponseBody();

        Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));

        return responseList;





            /*
        webTestClient
                .put()
                .uri(BASE_URI_HR + "/" + id)
                .bodyValue(holidayRequestDtoNew)
                .exchange()
                .expectStatus()
                .isOk();


             */


    }


}




