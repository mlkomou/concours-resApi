package com.concours.komou.app.repo;

import com.concours.komou.app.entity.PostulantResultat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostulantResultatRepository extends JpaRepository<PostulantResultat, Long> {
    @Query(value = "select r from PostulantResultat  r where r.resultat.visibily = true and r.postulant.id = :postulantId")
    Page<PostulantResultat> findAllByPostulantId(Pageable pageable, @Param("postulantId") Long postulantId);

    @Query(value = "select r from PostulantResultat  r where r.resultat.visibily = true and r.resultat.id = :resultatId and r.statut = 'Admis'")
    Page<PostulantResultat> findAllByResultatId(Pageable pageable, @Param("resultatId") Long resultatId);
}
