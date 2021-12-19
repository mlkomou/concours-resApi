package com.concours.komou.app.service;

import com.concours.komou.app.entity.Postulant;
import com.concours.komou.app.entity.Response;
import com.concours.komou.app.repo.PostulantRepository;
import com.concours.komou.auth.entity.ApplicationUser;
import com.concours.komou.auth.repo.ApplicationUserRepository;
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

    public PostulantService(PostulantRepository postulantRepository, ApplicationUserRepository applicationUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.postulantRepository = postulantRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<Map<String, Object>> savePostulant(Postulant postulant) {
        try {
            ApplicationUser user = new ApplicationUser();
            user.setUsername(postulant.getTelephone());
            user.setPassword(bCryptPasswordEncoder.encode(postulant.getPrenom()));
            ApplicationUser userSaved = applicationUserRepository.save(user);
            postulant.setUser(userSaved);

            Postulant postulant1 = postulantRepository.save(postulant);
            return new ResponseEntity<>(Response.success(postulant1, "Postulant enregistré"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur d'enregistrement"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
