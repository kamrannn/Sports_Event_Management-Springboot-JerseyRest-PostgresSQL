package org.management.SportEvent.dao;

import org.management.SportEvent.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultDao extends JpaRepository<Result, Integer> {
    List<Result> findAllByOrderByTotalTimeAsc();

    List<Result> findResultsByEventIDOrderByTotalTimeAsc(Integer eventId);
}
