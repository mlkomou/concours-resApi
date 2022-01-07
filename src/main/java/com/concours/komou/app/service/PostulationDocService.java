package com.concours.komou.app.service;

import com.concours.komou.app.entity.Notification;
import com.concours.komou.app.entity.Postulation;
import com.concours.komou.app.entity.PostulationDoc;
import com.concours.komou.app.entity.Response;
import com.concours.komou.app.payoad.NotificationPayload;
import com.concours.komou.app.repo.NotificationRepository;
import com.concours.komou.app.repo.PostulationDocRepository;
import com.concours.komou.app.repo.PostulationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostulationDocService {
    private final PostulationDocRepository postulationDocRepository;
    private final PostulationRepository postulationRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    public PostulationDocService(PostulationDocRepository postulationDocRepository, PostulationRepository postulationRepository, NotificationService notificationService, NotificationRepository notificationRepository) {
        this.postulationDocRepository = postulationDocRepository;
        this.postulationRepository = postulationRepository;
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }
    public ResponseEntity<Map<String, Object>> getDocByPostulantAndConcours(Long postulationId, Long concoursId, Long postulantId) {
        try {
            List<PostulationDoc> postulationDocs = postulationDocRepository.findAllByPostulationIdAndPostulationConcoursIdAndPostulationPostulantId(postulationId,concoursId, postulantId);

            return new ResponseEntity<>(Response.success(postulationDocs, "Liste des dossiers."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur de recupération."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Map<String, Object>> changeState(Long id, String accepted, Long postulationId) {
        try {
            Optional<PostulationDoc> optionalPostulationDoc = postulationDocRepository.findById(id);
            PostulationDoc postulationDoc = optionalPostulationDoc.get();
            List<PostulationDoc> postulationDocAccepted = new ArrayList<>();
            List<PostulationDoc> postulationDocrejected = new ArrayList<>();
            if (optionalPostulationDoc.isPresent()) {
                postulationDoc.setAccepted(accepted);
                PostulationDoc postulationDocSaved = postulationDocRepository.save(postulationDoc);
                Optional<Postulation> postulation = postulationRepository.findById(postulationId);
                List<PostulationDoc> postulationDocs = postulationDocRepository.findAllByPostulationId(postulationId);
                System.err.println("docs to accept "+ postulationDocs);
                postulationDocs.forEach(postulationDoc1 -> {
                    System.err.println("doc state "+postulationDoc1.getAccepted());
                    if (postulationDoc1.getAccepted().equals("ACCEPTÉ")) {
                        System.err.println("docs acepted "+ postulationDoc1.toString());
                        postulationDocAccepted.add(postulationDoc1);
                    } else {
                        postulationDocrejected.add(postulationDoc1);

                        NotificationPayload notificationPayload = new NotificationPayload();
                    notificationPayload.setPostulationDocId(postulationDoc1.getId());
                    notificationPayload.setType("DOCUMENT");
                    notificationPayload.setDescription("Ce document: " + postulationDoc1.getName() + " est rejetté !");
                    notificationPayload.setTitre("DOCUMENT");
                    notificationPayload.setPostulantId(postulationDoc1.getPostulation().getPostulant().getId());
                    List<String> included_segments = new ArrayList<>();
                    included_segments.add(postulationDoc1.getPostulation().getPostulant().getNotificationId());

                        Notification notification = new Notification();
                        notification.setPostulationDoc(postulationDoc);
                        notification.setPostulant(postulationDoc.getPostulation().getPostulant());
                        notification.setTitre("DOCUMENT");
                        notification.setDescription("Ce document: " + postulationDoc1.getName() + " est rejetté !");
                        notification.setType("DOCUMENT");
                        notificationRepository.save(notification);

                    notificationService.sendPushNotification(notificationPayload, included_segments);
                    }

                });
                System.err.println("refuse size "+ postulationDocrejected.size());

//                postulationDocrejected.forEach(postulationDoc2 -> {
//                    NotificationPayload notificationPayload = new NotificationPayload();
//                    notificationPayload.setPostulationDocId(postulationDoc2.getId());
//                    notificationPayload.setType("DOCUMENT");
//                    notificationPayload.setDescription("Ce document: " + postulationDoc2.getName() + " est rejetté !");
//                    notificationPayload.setTitre("DOCUMENT");
//                    notificationPayload.setPostulantId(postulationDoc2.getPostulation().getPostulant().getId());
//                    List<String> included_segments = new ArrayList<>();
//                    included_segments.add(postulationDoc2.getPostulation().getPostulant().getNotificationId());
////                    notificationService.saveNotification(notificationPayload);
//                    notificationService.sendPushNotification(notificationPayload, included_segments);
//                });


                if (postulationDocs.size() == postulationDocAccepted.size()) {
                    System.err.println("postulationDocs "+postulationDocs.size());
                    System.err.println("postulationDocAccepted "+postulationDocAccepted.size());
                    if (postulation.isPresent()) {
                        Postulation postulation1 = postulation.get();
                        postulation1.setValidation("VALIDÉ");
                        Postulation postulationUpdated = postulationRepository.save(postulation1);
                        System.err.println("postulation accepted "+ postulation1.toString());
                        return new ResponseEntity<>(Response.success(postulationUpdated, "Document mis à jour."), HttpStatus.OK);
                    }
                } else {
                    System.err.println("postulationDocs rejet "+postulationDocs.size());
                    System.err.println("postulationDocAccepted "+postulationDocAccepted.size());

                    if (postulation.isPresent()) {
                        Postulation postulation1 = postulation.get();
                        postulation1.setValidation("REJETTÉ");
                        Postulation postulationUpdated = postulationRepository.save(postulation1);
                        System.err.println("postulation rejected "+ postulation1.toString());


                        return new ResponseEntity<>(Response.success(postulationUpdated, "Document mis à jour."), HttpStatus.OK);
                    }
                }

                return new ResponseEntity<>(Response.success(postulationDocSaved, "Document mis à jour."), HttpStatus.OK);
            }
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Ce document n'existe pas !"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur de la modification."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
