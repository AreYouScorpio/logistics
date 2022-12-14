package hu.webuni.logistics.akostomschweger.web;

import hu.webuni.logistics.akostomschweger.dto.HolidayRequestDto;
import hu.webuni.logistics.akostomschweger.dto.HolidayRequestFilterDto;
import hu.webuni.logistics.akostomschweger.mapper.EmployeeMapper;
import hu.webuni.logistics.akostomschweger.mapper.HolidayRequestMapper;
import hu.webuni.logistics.akostomschweger.model.HolidayRequest;
import hu.webuni.logistics.akostomschweger.service.HolidayRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/holidayrequests")
public class HolidayRequestController {

    @Autowired
    HolidayRequestService holidayRequestService;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    HolidayRequestMapper holidayRequestMapper;

    @GetMapping
    public List<HolidayRequestDto> getAll() {
        return holidayRequestMapper.holidayRequestsToDtos(holidayRequestService.findAll());
    }

    @GetMapping("/{id}")
    public HolidayRequestDto getById(@PathVariable long id) {
        HolidayRequest holidayRequest = holidayRequestService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return holidayRequestMapper.holidayRequestToDto(holidayRequest);
    }

    @PostMapping("/search")
    public List<HolidayRequestDto> findByExample(@RequestBody HolidayRequestFilterDto example,
                                                 Pageable pageable) {
        Page<HolidayRequest> page = holidayRequestService.findHolidayRequestByExample(example, pageable);
        return holidayRequestMapper.holidayRequestsToDtos(page.getContent());
    }


    @PostMapping
    @PreAuthorize("#newHolidayRequest.employeeId == authentication.principal.employee.id") // els?? fele a holidayrequest employeeId-ja (employeeId), a m??sodik pedig a visszaadott HrUser (Employee) id-ja (sima id)
    public HolidayRequestDto addHolidayRequest(@RequestBody @Valid HolidayRequestDto newHolidayRequest) {
        HolidayRequest holidayRequest;
        System.out.println("EmployeeID for HolidayRequest: " + newHolidayRequest.getEmployeeId());
        System.out.println("ApproverID for HolidayRequest: " + newHolidayRequest.getApproverId());
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            holidayRequest = holidayRequestService.addHolidayRequest(holidayRequestMapper.dtoToHolidayRequest(newHolidayRequest), newHolidayRequest.getEmployeeId());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return holidayRequestMapper.holidayRequestToDto(holidayRequest);
    }



    @PutMapping("/{id}")
    public HolidayRequestDto modifyHolidayRequest(@PathVariable long id, @RequestBody @Valid HolidayRequestDto modifiedHolidayRequest) {
        modifiedHolidayRequest.setEmployeeId(id);
        HolidayRequest holidayRequest;
        try {
            holidayRequest = holidayRequestService.modifyHolidayRequest(id, holidayRequestMapper.dtoToHolidayRequest(modifiedHolidayRequest));
        } catch (InvalidParameterException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return holidayRequestMapper.holidayRequestToDto(holidayRequest);
    }

    // !!!! fontos @PreAuthorize-hoz:
    @DeleteMapping("/{id}") // a delete-hez csak id ker??l felk??ld??sre, emiatt PreAuthorize-t nem tudok ??rni, mint a post-n??l id checkre, emiatt a holidayrequestservice deleteholidayrequestj??ben kell meg??rni !!!
    public void deleteHolidayRequest(@PathVariable long id) {
        try {
            holidayRequestService.deleteHolidayRequest(id);
        } catch (InvalidParameterException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //old , before JWT token check, hogy a felettese-e aki approve-ol:
    /*
    @PutMapping(value = "/{id}/approval", params = {"status", "approverId"})
    public HolidayRequestDto approveHolidayRequest(@PathVariable long id, @RequestParam long approverId, @RequestParam boolean status) {
        HolidayRequest holidayRequest;
        try {
            holidayRequest = holidayRequestService.approveHolidayRequest(id, approverId, status);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return holidayRequestMapper.holidayRequestToDto(holidayRequest);
    }


     */
// felettes check JWT tokennel:
// holidayRequestId ??s approverId ker??l felk??ld??sre
    @PutMapping(value = "/{id}/approval", params = {"status"}) // t??r??lve az approverId felk??ld??s
    public HolidayRequestDto approveHolidayRequest(@PathVariable long id, @RequestParam boolean status) { //detto
        HolidayRequest holidayRequest;
        try {
            holidayRequest = holidayRequestService.approveHolidayRequest(id, status); //detto .. ehelyett nyerj??k ki a met??dusban
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return holidayRequestMapper.holidayRequestToDto(holidayRequest);
    }

}
