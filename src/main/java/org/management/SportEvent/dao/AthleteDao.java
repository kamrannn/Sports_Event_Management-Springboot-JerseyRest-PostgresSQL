package org.management.SportEvent.dao;

import org.management.SportEvent.model.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AthleteDao extends JpaRepository<Athlete, Integer> {
    Optional<Athlete> findByEmail(String email);
}
