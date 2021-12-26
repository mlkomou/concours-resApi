package com.concours.komou.app.repo;

import com.concours.komou.app.entity.PostulationDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostulationDocRepository extends JpaRepository<PostulationDoc, Long> {
    List<PostulationDoc> findAllByPostulationIdAndPostulationConcoursIdAndPostulationPostulantId(Long postulationId, Long concoursId, Long postulantId);
}
