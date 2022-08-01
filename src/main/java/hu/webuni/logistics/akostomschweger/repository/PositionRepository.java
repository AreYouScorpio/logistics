package hu.webuni.logistics.akostomschweger.repository;

import hu.webuni.logistics.akostomschweger.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Integer> {

    public Optional<Position> findByName(String name);
}

