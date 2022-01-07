package com.concours.komou.app.controller;

import com.concours.komou.app.entity.Postulant;
import com.concours.komou.app.payoad.PostulantPayload;
import com.concours.komou.app.service.PostulantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/postulants/")
public class PostulantController {
    private final PostulantService  postulantService;

    public PostulantController(PostulantService postulantService) {
        this.postulantService = postulantService;
    }

    @PostMapping("save")
    public ResponseEntity<Map<String, Object>> subscribePostulant(@RequestBody PostulantPayload postulant) {
        return postulantService.savePostulant(postulant);
    }

    @PostMapping("update-notification-id")
    public ResponseEntity<Map<String, Object>> updateNotificationId(@RequestParam("userId") Long userId, @RequestParam("notificationId") String notificationId) {
        return postulantService.updateOneSignalId(userId, notificationId);
    }
}
