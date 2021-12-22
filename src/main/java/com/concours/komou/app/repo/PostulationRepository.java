package com.concours.komou.app.repo;

import com.concours.komou.app.entity.Postulation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulationRepository extends JpaRepository<Postulation, Long> {
    boolean existsByConcoursIdAndPostulantId(Long concoursId, Long postulantId);
}
