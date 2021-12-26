package com.concours.komou.app.service;

import com.concours.komou.app.constants.AppConstants;
import com.concours.komou.app.entity.*;
import com.concours.komou.app.payoad.UserPostulation;
import com.concours.komou.app.repo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class PostulationService {
    private final PostulationRepository postulationRepository;
    private final UploadImageService uploadImageService;
    private final PostulationDocRepository postulationDocRepository;
    private final PostulantRepository postulantRepository;
    private final ConcoursRepository concoursRepository;
    private final PaiementRepository paiementRepository;

    public PostulationService(PostulationRepository postulationRepository, UploadImageService uploadImageService, PostulationDocRepository postulationDocRepository, PostulantRepository postulantRepository, ConcoursRepository concoursRepository, PaiementRepository paiementRepository) {
        this.postulationRepository = postulationRepository;
        this.uploadImageService = uploadImageService;
        this.postulationDocRepository = postulationDocRepository;
        this.postulantRepository = postulantRepository;
        this.concoursRepository = concoursRepository;
        this.paiementRepository = paiementRepository;
    }

    public ResponseEntity<Map<String, Object>> savePostulation(UserPostulation userPostulation, List<MultipartFile> docs) {
        try {
            Postulation postulation = new Postulation();
            Paiement paiement = new Paiement();
            Optional<Postulant> postulantOptional = postulantRepository.findById(userPostulation.postulantId);
            Optional<Concours> concoursOptional = concoursRepository.findById(userPostulation.getConcoursId());
            Concours concours = concoursOptional.get();
            Postulant postulant = postulantOptional.get();

            if (!postulationRepository.existsByConcoursIdAndPostulantId(concours.getId(), postulant.getId())) {
                postulation.setPostulant(postulant);
                postulation.setConcours(concours);

                ArrayList docArray = new ArrayList();
                Postulation postulationSaved = postulationRepository.save(postulation);

                paiement.setPostulation(postulationSaved);
                paiement.setMoyen(userPostulation.getMoyen());
                paiement.setPourcentage(concours.getFrais() * 5 / 100);
                paiement.setIdentifiant(userPostulation.getIdentifiant());

                paiementRepository.save(paiement);

                docs.forEach(doc -> {
                    PostulationDoc postulationDoc = new PostulationDoc();
                    try {
                        postulationDoc.setPostulation(postulationSaved);
                        postulationDoc.setType(doc.getContentType());
                        postulationDoc.setName(doc.getOriginalFilename());
                        postulationDoc.setPath(uploadImageService.uploadImage(doc, AppConstants.DOCUMENT_UPLOAD_LINK));
                        docArray.add(postulationDoc);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println(e);
                    }
                });
                postulationDocRepository.saveAll(docArray);

                return new ResponseEntity<>(Response.success(postulationSaved, "Postulantion enregistrée"), HttpStatus.OK);
            }
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Vous avez déjà postulé pour ce concours !"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur d'enregistrement"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Map<String, Object>> getByPostulant(Long postulantId, int page, int size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Postulation> postulations = postulationRepository.findByPostulantId(paging, postulantId);
            return new ResponseEntity<>(Response.success(postulations, "Liste de mes concours !"), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("error liste: " + e);
            return new ResponseEntity<>(Response.error(e, "de recuperation !"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> getByConcours(Long concoursId, int page, int size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Postulation> postulations = postulationRepository.findByConcoursId(paging, concoursId);
            System.out.println("postulant postulation: " + postulations.getContent().get(0));
            return new ResponseEntity<>(Response.success(postulations, "Liste des postulants !"), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("error liste: " + e);
            return new ResponseEntity<>(Response.error(e, "Erreur de recuperation !"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
