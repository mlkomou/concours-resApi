package com.concours.komou.app.controller;

import com.concours.komou.app.constants.AppConstants;
import com.concours.komou.app.service.PostulationDocService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("/postulation-doc/")
public class PostulatioDocController {

    private final PostulationDocService postulationDocService;

    public PostulatioDocController(PostulationDocService postulationDocService) {
        this.postulationDocService = postulationDocService;
    }

    @PostMapping("by-postulant-concours")
    public ResponseEntity<Map<String, Object>> getDossierByPostulantAndConcours(@RequestParam("postulationId") Long postulationId,
                                                                                @RequestParam("concoursId") Long concoursId,
                                                                                @RequestParam("postulantId") Long postulantId) {
        return postulationDocService.getDocByPostulantAndConcours(postulationId, concoursId, postulantId);
    }

    @PostMapping("chane-state")
    public ResponseEntity<Map<String, Object>> changeState(@RequestParam("docId") Long docId,
                                                           @RequestParam("accepted") boolean accepted) {
        return postulationDocService.changeState(docId, accepted);
    }

    @ResponseBody
    @GetMapping("download/{photo}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("photo") String photo) {
        String path = AppConstants.DOCUMENT_UPLOAD_LINK;
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
