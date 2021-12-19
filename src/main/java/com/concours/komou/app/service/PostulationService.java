package com.concours.komou.app.service;

import com.concours.komou.app.entity.Postulant;
import com.concours.komou.app.entity.Postulation;
import com.concours.komou.app.entity.PostulationDoc;
import com.concours.komou.app.entity.Response;
import com.concours.komou.app.payoad.UserPostulation;
import com.concours.komou.app.repo.PostulantRepository;
import com.concours.komou.app.repo.PostulationDocRepository;
import com.concours.komou.app.repo.PostulationRepository;
import com.concours.komou.auth.constants.SecurityConstants;
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

    public PostulationService(PostulationRepository postulationRepository, UploadImageService uploadImageService, PostulationDocRepository postulationDocRepository, PostulantRepository postulantRepository) {
        this.postulationRepository = postulationRepository;
        this.uploadImageService = uploadImageService;
        this.postulationDocRepository = postulationDocRepository;
        this.postulantRepository = postulantRepository;
    }

    public ResponseEntity<Map<String, Object>> savePostulation(UserPostulation userPostulation) {
        try {
            Postulation postulation = userPostulation.getPostulation();
            Optional<Postulant> postulant = postulantRepository.findById(userPostulation.postulantId);
            List<MultipartFile> docs = userPostulation.getDocs();
            ArrayList docArray = new ArrayList();

            postulation.setPostulant(postulant.get());
            Postulation postulationSaved = postulationRepository.save(userPostulation.getPostulation());

            docs.forEach(doc -> {
                PostulationDoc postulationDoc = new PostulationDoc();
                try {
                    postulationDoc.setPostulation(postulationSaved);
                    postulationDoc.setType(doc.getContentType());
                    postulationDoc.setPath(uploadImageService.uploadImage(doc, SecurityConstants.DOCUMENT_UPLOAD_LINK));
                    docArray.add(postulationDoc);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println(e);
                }
            });
            postulationDocRepository.saveAll(docArray);

            return new ResponseEntity<>(Response.success(postulationSaved, "Postulantion enregistrée"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur d'enregistrement"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
