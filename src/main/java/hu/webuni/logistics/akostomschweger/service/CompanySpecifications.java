package hu.webuni.logistics.akostomschweger.service;

import hu.webuni.logistics.akostomschweger.model.Company;
import hu.webuni.logistics.akostomschweger.model.Company_;
import org.springframework.data.jpa.domain.Specification;

public class CompanySpecifications {

    public static Specification<Company> hasId(long id) {
        return (root, cq, cb) -> cb.equal(root.get(Company_.id), id);
    }


    public static Specification<Company> hasCompanyName(String companyName) {
        String newName = companyName.toLowerCase();
        return (root, cq, cb) -> cb.like(cb.lower(root.get(Company_.name)), newName + "%");

    }


}
