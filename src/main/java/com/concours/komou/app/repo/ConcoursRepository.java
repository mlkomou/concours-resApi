package com.concours.komou.app.repo;

import com.concours.komou.app.entity.Concours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcoursRepository extends JpaRepository<Concours, Long> {

    Page<Concours> findAll(Pageable pageable);
}
