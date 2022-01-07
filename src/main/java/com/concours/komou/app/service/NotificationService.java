package com.concours.komou.app.service;

import com.concours.komou.app.entity.*;
import com.concours.komou.app.payoad.*;
import com.concours.komou.app.repo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class NotificationService {
    private final PostulantRepository postulantRepository;
    private final PostulantResultatRepository postulantResultatRepository;
    private final ConcoursRepository concoursRepository;
    private final PostulationDocRepository postulationDocRepository;
    private final NotificationRepository notificationRepository;

    public NotificationService(PostulantRepository postulantRepository, PostulantResultatRepository postulantResultatRepository, ConcoursRepository concoursRepository, PostulationDocRepository postulationDocRepository, NotificationRepository notificationRepository) {
        this.postulantRepository = postulantRepository;
        this.postulantResultatRepository = postulantResultatRepository;
        this.concoursRepository = concoursRepository;
        this.postulationDocRepository = postulationDocRepository;
        this.notificationRepository = notificationRepository;
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

        switch (notificationPayload.getType()) {
            case "NEW CONCOURS":
                Optional<Concours> concoursOptional = concoursRepository.findById(notificationPayload.getConcoursId());
                Concours concours = concoursOptional.get();
                pushNotification.setData(new PushDataDetail("NEW CONCOURS"));
                pushNotification.setContents(new PushDetail("Un nouvel concours est disponible: " + concours.getName()));
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Basic OTRjNjZkMTgtZWQ0Ni00OTMzLTkxNjMtZGUzYjdkOTA4OWZm");
                HttpEntity<PushNotification> entity = new HttpEntity<>(pushNotification, headers);
                restTemplate.postForObject("https://onesignal.com/api/v1/notifications", entity, ResponsePush.class);
                break;
            case "RESULTAT":
                Optional<PostulantResultat> postulantResultatOptional = postulantResultatRepository.findById(notificationPayload.getPostulantResultatId());
                PostulantResultat postulantResultat = postulantResultatOptional.get();
                singleNotification.setData(new PushDataDetail("RESULTAT"));
                singleNotification.setContents(new PushDetail("Votre résultat pour le concours " + postulantResultat.getResultat().getConcours().getName() + " est disponible."));
                HttpHeaders headerSingle = new HttpHeaders();
                headerSingle.add("Authorization", "Basic OTRjNjZkMTgtZWQ0Ni00OTMzLTkxNjMtZGUzYjdkOTA4OWZm");
                HttpEntity<SingleNotification> entitySingle = new HttpEntity<>(singleNotification, headerSingle);
                restTemplate.postForObject("https://onesignal.com/api/v1/notifications", entitySingle, ResponsePush.class);
                break;
            case "DOCUMENT":
                Optional<PostulationDoc> postulationDocOptional = postulationDocRepository.findById(notificationPayload.getPostulationDocId());
                PostulationDoc postulationDoc = postulationDocOptional.get();
                singleNotification.setData(new PushDataDetail("DOCUMENT"));
                singleNotification.setContents(new PushDetail("Ce document: " + postulationDoc.getName() + " est rejetté !"));
                HttpHeaders headerSingle2 = new HttpHeaders();
                headerSingle2.add("Authorization", "Basic OTRjNjZkMTgtZWQ0Ni00OTMzLTkxNjMtZGUzYjdkOTA4OWZm");
                HttpEntity<SingleNotification> entitySingle2 = new HttpEntity<>(singleNotification, headerSingle2);
                restTemplate.postForObject("https://onesignal.com/api/v1/notifications", entitySingle2, ResponsePush.class);

            default:
                // code block
        }
    }

    public ResponseEntity<Map<String, Object>> getNotifications(int page, int size, Long postulantId) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Notification> notifications = notificationRepository.findAllByPostulantId(paging, postulantId);
            return new ResponseEntity<>(Response.success(notifications, "Liste notifications"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e, "Erreur de recupération."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
