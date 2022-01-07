package com.concours.komou.app.service;

import com.concours.komou.app.constants.AppConstants;
import com.concours.komou.app.entity.*;
import com.concours.komou.app.payoad.NotificationPayload;
import com.concours.komou.app.repo.ConcoursRepository;
import com.concours.komou.app.repo.DocumentNameRepository;
import com.concours.komou.app.repo.NotificationRepository;
import com.concours.komou.app.repo.PostulantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ConcoursService {
    private final ConcoursRepository concoursRepository;
    private final UploadImageService uploadImageService;
    private final DocumentNameRepository documentNameRepository;
    private final NotificationService notificationService;
    private final PostulantRepository postulantRepository;
    private final NotificationRepository notificationRepository;

    public ConcoursService(ConcoursRepository concoursRepository, UploadImageService uploadImageService, DocumentNameRepository documentNameRepository, NotificationService notificationService, PostulantRepository postulantRepository, NotificationRepository notificationRepository) {
        this.concoursRepository = concoursRepository;
        this.uploadImageService = uploadImageService;
        this.documentNameRepository = documentNameRepository;
        this.notificationService = notificationService;
        this.postulantRepository = postulantRepository;
        this.notificationRepository = notificationRepository;
    }

   public ResponseEntity<Map<String, Object>> saveConcours(Concours concours, MultipartFile photo, String[] documentName) {
        try {
            concours.setPath(uploadImageService.uploadImage(photo, AppConstants.PHOTO_UPLOAD_LINK));
            Concours concoursSaved = concoursRepository.save(concours);
            List<DoumentNamePublication> documentNamePublications = new ArrayList<>();


            for (int i=0; i<documentName.length; i++) {
                DoumentNamePublication documentNamePublication = new DoumentNamePublication();
                documentNamePublication.setConcours(concoursSaved);
                documentNamePublication.setName(documentName[i]);
                documentNamePublications.add(documentNamePublication);
            }
            documentNameRepository.saveAll(documentNamePublications);
            NotificationPayload notificationPayload = new NotificationPayload();
            notificationPayload.setConcoursId(concoursSaved.getId());
            notificationPayload.setDescription("Nouvel concours disponible.");
            notificationPayload.setType("NEW CONCOURS");
            notificationPayload.setTitre("CONCOURS");
            List<String> included_segments = new ArrayList<>();
            included_segments.add("Subscribed Users");

            Notification notification = new Notification();
            notification.setTitre(notificationPayload.getTitre());
            notification.setDescription(notificationPayload.getDescription());
            notification.setType(notificationPayload.getType());
            notification.setConcours(concoursSaved);
            notificationRepository.save(notification);

            notificationService.sendPushNotification(notificationPayload, included_segments);
            return new ResponseEntity<>(Response.success(concoursSaved, "Concours enregistré"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur d'enregistrement"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> getAllConcoursByPage(int page, int size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Concours> concours = concoursRepository.findAll(paging);
            return new ResponseEntity<>(Response.success(concours, "Liste concours"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new Concours(), "erreur de recuperation"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
