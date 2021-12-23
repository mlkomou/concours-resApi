package com.concours.komou.app.repo;

import com.concours.komou.app.entity.Postulation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostulationRepository extends JpaRepository<Postulation, Long> {
    boolean existsByConcoursIdAndPostulantId(Long concoursId, Long postulantId);
    Page<Postulation> findByPostulantId(Pageable pageable, Long postulantId);
    Page<Postulation> findByConcoursId(Pageable pageable, Long concoursId);
    List<Postulation> findAllByConcoursId(Long concoursId);
}
