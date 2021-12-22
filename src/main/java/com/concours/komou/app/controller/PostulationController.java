package com.concours.komou.app.controller;

import com.concours.komou.app.payoad.UserPostulation;
import com.concours.komou.app.service.PostulationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/postulations/")
public class PostulationController {
    private final PostulationService postulationService;

    public PostulationController(PostulationService postulationService) {
        this.postulationService = postulationService;
    }

    @PostMapping("save")
    public ResponseEntity<Map<String, Object>> saveUserPostulation(@RequestParam("userPostulation") String userPostulationString,
                                                                   @RequestParam("docs") List<MultipartFile> docs) throws JsonProcessingException {
        UserPostulation userPostulation = new ObjectMapper().readValue(userPostulationString, UserPostulation.class);
        return postulationService.savePostulation(userPostulation, docs);
    }
}
