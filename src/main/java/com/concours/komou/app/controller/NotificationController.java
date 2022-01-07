package com.concours.komou.app.controller;

import com.concours.komou.app.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notifications/")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("liste-pagination")
    public ResponseEntity<Map<String, Object>> getNotificationsByPage(@RequestParam("page") int page,
                                                                 @RequestParam("size") int size,
                                                                 @RequestParam("postulantId") Long posulantId) {
        return notificationService.getNotifications(page, size, posulantId);
    }
}
