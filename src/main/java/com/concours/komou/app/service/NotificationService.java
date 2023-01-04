package com.concours.komou.app.service;

import com.concours.komou.app.entity.*;
import com.concours.komou.app.payoad.*;
import com.concours.komou.app.repo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class NotificationService {
    private final PostulantResultatRepository postulantResultatRepository;
    private final ConcoursRepository concoursRepository;
    private final PostulationDocRepository postulationDocRepository;
    private final NotificationRepository notificationRepository;
    private final PostulationRepository postulationRepository;

    public NotificationService(PostulantResultatRepository postulantResultatRepository, ConcoursRepository concoursRepository, PostulationDocRepository postulationDocRepository, NotificationRepository notificationRepository, PostulationRepository postulationRepository) {

        this.postulantResultatRepository = postulantResultatRepository;
        this.concoursRepository = concoursRepository;
        this.postulationDocRepository = postulationDocRepository;
        this.notificationRepository = notificationRepository;
        this.postulationRepository = postulationRepository;
    }

    public void sendPushNotification(NotificationPayload notificationPayload, List<String> included_segments) {
        RestTemplate restTemplate = new RestTemplate();

        PushNotification pushNotification = new PushNotification();
        SingleNotification singleNotification = new SingleNotification();

        pushNotification.setApp_id("c57fcda9-a264-4e52-8741-207712809b28");
        pushNotification.setIncluded_segments(included_segments);
        pushNotification.setHeadings(new PushDetail("MonConcours"));

        singleNotification.setApp_id("c57fcda9-a264-4e52-8741-207712809b28");
        singleNotification.setInclude_player_ids(included_segments);
        singleNotification.setHeadings(new PushDetail("MonConcours"));
        Map<String, Object> data = new HashMap<>();

        switch (notificationPayload.getType()) {
            case "NEW CONCOURS":
                Optional<Concours> concoursOptional = concoursRepository.findById(notificationPayload.getConcoursId());
                Concours concours = concoursOptional.get();
                data.put("data", concours);
                data.put("type", "CONCOURS");
                pushNotification.setData(new PushDataDetail(data));
                pushNotification.setContents(new PushDetail("Un nouvel concours est disponible: " + concours.getName()));
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic OTRjNjZkMTgtZWQ0Ni00OTMzLTkxNjMtZGUzYjdkOTA4OWZm");
                HttpEntity<PushNotification> entity = new HttpEntity<>(pushNotification, headers);
                restTemplate.postForObject("https://onesignal.com/api/v1/notifications", entity, ResponsePush.class);
                break;
            case "RESULTAT":
                Optional<PostulantResultat> postulantResultatOptional = postulantResultatRepository.findById(notificationPayload.getPostulantResultatId());
                PostulantResultat postulantResultat = postulantResultatOptional.get();
                data.put("data", postulantResultat);
                data.put("type", "RESULTAT");
                singleNotification.setData(new PushDataDetail(data));
                singleNotification.setContents(new PushDetail("Votre résultat pour le concours " + postulantResultat.getResultat().getConcours().getName() + " est disponible."));
                HttpHeaders headerSingle = new HttpHeaders();
                headerSingle.add("Authorization", "Basic OTRjNjZkMTgtZWQ0Ni00OTMzLTkxNjMtZGUzYjdkOTA4OWZm");
                HttpEntity<SingleNotification> entitySingle = new HttpEntity<>(singleNotification, headerSingle);
                restTemplate.postForObject("https://onesignal.com/api/v1/notifications", entitySingle, ResponsePush.class);
                break;
            case "DOCUMENT":
                Optional<PostulationDoc> postulationDocOptional = postulationDocRepository.findById(notificationPayload.getPostulationDocId());
                PostulationDoc postulationDoc = postulationDocOptional.get();
                data.put("data", postulationDoc);
                data.put("type", "DOCUMENT");
                singleNotification.setData(new PushDataDetail(data));
                singleNotification.setContents(new PushDetail("Ce document: " + postulationDoc.getName() + " est rejetté !"));
                HttpHeaders headerSingle2 = new HttpHeaders();
                headerSingle2.add("Authorization", "Basic OTRjNjZkMTgtZWQ0Ni00OTMzLTkxNjMtZGUzYjdkOTA4OWZm");
                HttpEntity<SingleNotification> entitySingle2 = new HttpEntity<>(singleNotification, headerSingle2);
                restTemplate.postForObject("https://onesignal.com/api/v1/notifications", entitySingle2, ResponsePush.class);
                break;
            case "DOSSIER":
                Optional<Postulation> postulationOptional = postulationRepository.findById(notificationPayload.getPostulationId());
                data.put("data", postulationOptional.get());
                data.put("type", "DOSSIER");
                singleNotification.setData(new PushDataDetail(data));
                singleNotification.setContents(new PushDetail("Votre dossier a été accepté."));
                HttpHeaders headerSingle3 = new HttpHeaders();
                headerSingle3.add("Authorization", "Basic OTRjNjZkMTgtZWQ0Ni00OTMzLTkxNjMtZGUzYjdkOTA4OWZm");
                HttpEntity<SingleNotification> entitySingle3 = new HttpEntity<>(singleNotification, headerSingle3);
                restTemplate.postForObject("https://onesignal.com/api/v1/notifications", entitySingle3, ResponsePush.class);

            default:
                // code block
        }
    }

    public ResponseEntity<Map<String, Object>> getNotifications(int page, int size, Long postulantId) {
        try {
            Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdAt");
            Pageable paging = PageRequest.of(page, size, defaultSort);
            Page<Notification> notifications = notificationRepository.findAllByPostulantId(paging, postulantId);
            return new ResponseEntity<>(Response.success(notifications, "Liste notifications"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e, "Erreur de recupération."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> updateStateNotification(Long id, String state) {
        try {
            Optional<Notification> notification = notificationRepository.findById(id);
            if (notification.isPresent()) {
                notification.get().setLecture(state);
                 Notification notificationUpdated = notificationRepository.save(notification.get());
                 return new ResponseEntity<>(Response.success(notificationUpdated, "Notification lue"), HttpStatus.OK);
            }
            return new ResponseEntity<>(Response.error(new HashMap<>(), "cette notification n'existe pas."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e, "Erreur de recupération."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
