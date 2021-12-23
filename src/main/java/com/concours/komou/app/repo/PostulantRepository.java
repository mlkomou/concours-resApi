package com.concours.komou.app.repo;

import com.concours.komou.app.entity.Postulant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostulantRepository extends JpaRepository<Postulant, Long> {
    boolean existsByTelephone(String phone);
}
