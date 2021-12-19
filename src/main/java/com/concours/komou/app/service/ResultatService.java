package com.concours.komou.app.service;

import com.concours.komou.app.entity.*;
import com.concours.komou.app.payoad.ResultatPostulant;
import com.concours.komou.app.payoad.ResultatSingle;
import com.concours.komou.app.repo.ConcoursRepository;
import com.concours.komou.app.repo.PostulantRepository;
import com.concours.komou.app.repo.PostulantResultatRepository;
import com.concours.komou.app.repo.ResultatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResultatService {
    private final ResultatRepository resultatRepository;
    private final ConcoursRepository concoursRepository;
    private final PostulantRepository postulantRepository;
    private final PostulantResultatRepository postulantResultatRepository;

    public ResultatService(ResultatRepository resultatRepository, ConcoursRepository concoursRepository, PostulantRepository postulantRepository, PostulantResultatRepository postulantResultatRepository) {
        this.resultatRepository = resultatRepository;
        this.concoursRepository = concoursRepository;
        this.postulantRepository = postulantRepository;
        this.postulantResultatRepository = postulantResultatRepository;
    }
    public ResponseEntity<Map<String, Object>> publishResultat(ResultatPostulant resultatPostulant) {
        try {
            Resultat resultat = new Resultat();
            List<PostulantResultat> postulantResultatList = new ArrayList<>();
            List<PostulantResultat> postulantResultatEchoue = new ArrayList<>();
            List<Postulant> postulans = new ArrayList<>();
            Optional<Concours> concoursOptional = concoursRepository.findById(resultatPostulant.getConcoursId());
            Concours concours = concoursOptional.get();
            resultat.setName(resultatPostulant.getResultatName());
            resultat.setConcours(concours); // set concours to resultat
            resultat.setVisibily(false);
            Resultat resultatSaved = resultatRepository.save(resultat);// save resultat

            List<ResultatSingle> resultatSingles = resultatPostulant.getResultatSingles();
            resultatSingles.forEach(resultatSingle -> {
                Optional<Postulant> postulantOptional = postulantRepository.findById(resultatSingle.getPostulantId());
                Postulant postulant = postulantOptional.get();
                PostulantResultat postulantResultat = new PostulantResultat();

                postulantResultat.setResultat(resultatSaved); // set resultat to postulant_resultat
                postulantResultat.setPostulant(postulant);
                postulantResultat.setStatut("Admis");
                postulantResultatList.add(postulantResultat);
                postulans.add(postulant);
            });

            List<Postulant> postulantList = postulantRepository.findAll();
            postulantList.removeAll(postulans); //difference between admis and echoues
            System.err.println("numbre echoue " + postulantList.size());
            postulantList.forEach(postulant -> {
                PostulantResultat postulantResultat = new PostulantResultat();

                postulantResultat.setResultat(resultatSaved); // set resultat to postulant_resultat
                postulantResultat.setPostulant(postulant);
                postulantResultat.setStatut("Echoue");
                postulantResultatEchoue.add(postulantResultat);
            });

            postulantResultatRepository.saveAll(postulantResultatList); // save resltat for each postulant admis
            postulantResultatRepository.saveAll(postulantResultatEchoue); // save resltat for each postulant echoue


            return new ResponseEntity<>(Response.success(resultatSaved, "Resultat publiée"), HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur d'enregistrement"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
