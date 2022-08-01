package hu.webuni.logistics.akostomschweger.repository;

import hu.webuni.logistics.akostomschweger.model.PositionDetailsByCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionDetailsByCompanyRepository extends JpaRepository<PositionDetailsByCompany, Long> {

    Optional<PositionDetailsByCompany> findByPositionNameAndCompanyId(
            String positionName, long companyId);

}
