package com.concours.komou.app.repo;

import com.concours.komou.app.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "select n from Notification n where n.postulant.id = :postulantId or n.type = 'NEW CONCOURS'")
    Page<Notification> findAllByPostulantId(Pageable pageable, @Param("postulantId") Long postulantId);

}
