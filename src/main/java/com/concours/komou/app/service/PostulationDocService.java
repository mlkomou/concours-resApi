package com.concours.komou.app.service;

import com.concours.komou.app.entity.Postulation;
import com.concours.komou.app.entity.PostulationDoc;
import com.concours.komou.app.entity.Response;
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

    public PostulationDocService(PostulationDocRepository postulationDocRepository, PostulationRepository postulationRepository) {
        this.postulationDocRepository = postulationDocRepository;
        this.postulationRepository = postulationRepository;
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
                    }
                });
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
                    System.err.println("postulationDocAccepted rejet "+postulationDocAccepted.size());
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
