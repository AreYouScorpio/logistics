package hu.webuni.logistics.akostomschweger.service;

import org.springframework.stereotype.Service;

// this service is not in use but done in abstract service class .. this would be an alternative solution

@Service
public class PositionService {
    /*
    @Autowired
    PositionRepository positionRepository;

    public void setPositionForEmployee(Employee employee) {
        Position position = employee.getPosition();
        if (position != null) {
            String positionName = position.getName();
            if (positionName != null) {
                //Optional<Position> positionsByName = positionRepository.findByName(positionName);
                List<Position> positionsByName = positionRepository.findByName(positionName);
                if (positionsByName.isEmpty()) {
                    position = positionRepository.save(new Position(positionName, null));
                } else {
                    position = positionsByName.get(0);
                }
            }
        }
        employee.setPosition(position);
    }

     */
}
