package com.concours.komou.app.controller;

import com.concours.komou.app.entity.Postulant;
import com.concours.komou.app.service.PostulantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/postulants/")
public class PostulantController {
    private final PostulantService  postulantService;

    public PostulantController(PostulantService postulantService) {
        this.postulantService = postulantService;
    }

    @PostMapping("save")
    public ResponseEntity<Map<String, Object>> subscribePostulant(@RequestBody Postulant postulant) {
        return postulantService.savePostulant(postulant);
    }
}
