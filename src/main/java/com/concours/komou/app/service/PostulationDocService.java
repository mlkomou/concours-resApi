package com.concours.komou.app.service;

import com.concours.komou.app.entity.PostulationDoc;
import com.concours.komou.app.entity.Response;
import com.concours.komou.app.repo.PostulationDocRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostulationDocService {
    private final PostulationDocRepository postulationDocRepository;

    public PostulationDocService(PostulationDocRepository postulationDocRepository) {
        this.postulationDocRepository = postulationDocRepository;
    }
    public ResponseEntity<Map<String, Object>> getDocByPostulantAndConcours(Long postulationId, Long concoursId, Long postulantId) {
        try {
            List<PostulationDoc> postulationDocs = postulationDocRepository.findAllByPostulationIdAndPostulationConcoursIdAndPostulationPostulantId(postulationId,concoursId, postulantId);

            return new ResponseEntity<>(Response.success(postulationDocs, "Liste des dossiers."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur de recupération."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Map<String, Object>> changeState(Long id, boolean accepted) {
        try {
            Optional<PostulationDoc> optionalPostulationDoc = postulationDocRepository.findById(id);
            PostulationDoc postulationDoc = optionalPostulationDoc.get();
            if (optionalPostulationDoc.isPresent()) {
                postulationDoc.setAccepted(accepted);
                PostulationDoc postulationDocSaved = postulationDocRepository.save(postulationDoc);
                return new ResponseEntity<>(Response.success(postulationDocSaved, "Document mis à jour."), HttpStatus.OK);
            }
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Ce document n'existe pas !"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur de la modification."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
