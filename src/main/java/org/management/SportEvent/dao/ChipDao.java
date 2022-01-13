package org.management.SportEvent.dao;

import org.management.SportEvent.model.Chip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChipDao extends JpaRepository<Chip, Integer> {
}
