package com.concours.komou.app.repo;

import com.concours.komou.app.entity.Resultat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ResultatRepository extends JpaRepository<Resultat, Long> {
    Page<Resultat> findAll(Pageable pageable);

    Page<Resultat> findAllByVisibily(Pageable pageable, Boolean visibility);
}
