package com.concours.komou.app.service;

import com.concours.komou.app.entity.Postulant;
import com.concours.komou.app.entity.Response;
import com.concours.komou.app.payoad.PostulantPayload;
import com.concours.komou.app.repo.PostulantRepository;

import com.concours.komou.app.verification.AllMethode;
import com.concours.komou.entity.ApplicationUser;
import com.concours.komou.repo.ApplicationUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PostulantService {
    private final PostulantRepository postulantRepository;
    private final ApplicationUserRepository applicationUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AllMethode allMethode;

    public PostulantService(PostulantRepository postulantRepository, ApplicationUserRepository applicationUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AllMethode allMethode) {
        this.postulantRepository = postulantRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.allMethode = allMethode;
    }

    public ResponseEntity<Map<String, Object>> savePostulant(PostulantPayload postulantPayload) {
        try {
            if (!allMethode.checkUniquePostulant(postulantPayload.getTelephone())) {
                ApplicationUser user = new ApplicationUser();
                Postulant postulant = new Postulant();
                postulant.setNom(postulantPayload.getNom());
                postulant.setPrenom(postulantPayload.getPrenom());
                postulant.setTelephone(postulantPayload.getTelephone());
                user.setUsername(postulant.getTelephone());
                user.setPassword(bCryptPasswordEncoder.encode(postulantPayload.getPassword()));
                ApplicationUser userSaved = applicationUserRepository.save(user);
                postulant.setUser(userSaved);

                Postulant postulant1 = postulantRepository.save(postulant);
                return new ResponseEntity<>(Response.success(postulant1, "Postulant enregistré"), HttpStatus.OK);
            };
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Ce postulant existe déjà. Veuillez choisir un autre numéro de téléphone."), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur d'enregistrement"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
