package hu.webuni.logistics.akostomschweger.repository;

import hu.webuni.logistics.akostomschweger.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> ,
        JpaSpecificationExecutor<Employee>

{

    Long countById(Long id);

    List<Employee> findBySalaryGreaterThan(int salary);

    Page<Employee> findBySalaryGreaterThan(int salary, Pageable pageable);

    // position entitás bevezetése miatt ez is megváltozik, már a position name attribútumára keres
    // List<Employee> findByPosition(String position);
    List<Employee> findByPositionName(String position);

    @Query(value = "SELECT e from Employee e where upper(e.name) like concat(upper(:prefix),'%')")
    List<Employee> findByNameStartingWith(@Param("prefix") String prefix);

    //@Query(value = "SELECT e from Employee e where e.start_date_at_the_company between :startDate and :endDate")
    // @Query(value = "SELECT e from Employee e where e.start_date_at_the_company > 2020-02-02")
    //@Query(value = "SELECT e from Employee e")
    //@Query(value = "SELECT * from Employee e where e.start_date_at_the_company < now() ", nativeQuery = true)
    @Query(value = "SELECT * from Employee e where e.start_date_at_the_company between :startDate and :endDate ", nativeQuery = true)
    List<Employee> findByStartDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)


    @Modifying
    @Query(value = "truncate table employee",
            nativeQuery = true)
    void truncate();

    @Modifying
    //update vagy delete query esetén (ha nem selectet írunk) --- így nem getresultlist hanem executeupdate-tel futtatja spring
    @Transactional
    // 1. mego, nem műxik, (hibernate cross joint)
    //@Query("UPDATE Employee e "
    //        + "SET e.salary = :minSalary "
    //        + "WHERE e.position.name= :position "
    //        + "AND e.salary < :minSalary "
    //        + "AND e.company.id = :companyId")
    @Query("UPDATE Employee e "
            + "SET e.salary = :minSalary "
            + "WHERE e.id IN "
            + "(SELECT e2.id "
            + "FROM Employee e2 "
            + "WHERE e2.position.name= :position "
            + "AND e2.salary < :minSalary "
            + "AND e2.company.id = :companyId"
            + ")")
    int updateSalaries(String position, int minSalary, long companyId);

    @EntityGraph(attributePaths = {"managedEmployees"}, type = EntityGraph.EntityGraphType.LOAD) // vagy itt felsoroloma managert is, vagy a típusa LOAD graf legyen
    Optional<Employee> findByUsername(String username);

}
