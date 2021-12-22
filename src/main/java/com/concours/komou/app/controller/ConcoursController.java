package com.concours.komou.app.controller;

import com.concours.komou.app.constants.AppConstants;
import com.concours.komou.app.entity.Concours;
import com.concours.komou.app.service.ConcoursService;
import com.concours.komou.constants.SecurityConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/concours/")
public class ConcoursController {
    private final ConcoursService concoursService;

    public ConcoursController(ConcoursService concoursService) {
        this.concoursService = concoursService;
    }

    @PostMapping("save")
    public ResponseEntity<Map<String, Object>> saveConcours(@RequestParam("concours") String concoursString,
                                                            @RequestParam("documentNames") String docNameString,
                                                            @RequestParam("photo") MultipartFile photo) throws JsonProcessingException {
        Concours concours = new ObjectMapper().readValue(concoursString, Concours.class);
        String[] docNames = new ObjectMapper().readValue(docNameString, String[].class);
        return concoursService.saveConcours(concours, photo, docNames);
    }

    @PostMapping("liste-pagination")
    public ResponseEntity<Map<String, Object>> getConcoursByPage(@RequestParam("page") int page,
                                                                 @RequestParam("size") int size) {
        return concoursService.getAllConcoursByPage(page, size);
    }

    @ResponseBody
    @GetMapping("download/{photo}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("photo") String photo) {
        String path = AppConstants.PHOTO_UPLOAD_LINK;
        try {
            Path fileName = Paths.get(path, photo);
            byte[] buffer = Files.readAllBytes(fileName);
            ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
            return ResponseEntity.ok()
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("image/png"))
                    .body(byteArrayResource);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return ResponseEntity.badRequest().build();
    }
}
