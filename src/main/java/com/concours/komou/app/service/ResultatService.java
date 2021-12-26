package com.concours.komou.app.service;

import com.concours.komou.app.entity.*;
import com.concours.komou.app.payoad.ResultatPostulant;
import com.concours.komou.app.repo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final PostulationRepository postulationRepository;

    public ResultatService(ResultatRepository resultatRepository, ConcoursRepository concoursRepository, PostulantRepository postulantRepository, PostulantResultatRepository postulantResultatRepository, PostulationRepository postulationRepository) {
        this.resultatRepository = resultatRepository;
        this.concoursRepository = concoursRepository;
        this.postulantRepository = postulantRepository;
        this.postulantResultatRepository = postulantResultatRepository;
        this.postulationRepository = postulationRepository;
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

            List<Long> postulantIds = resultatPostulant.getPostulantIds();
            postulantIds.forEach(postulantId -> {
                Optional<Postulant> postulantOptional = postulantRepository.findById(postulantId);
                Postulant postulant = postulantOptional.get();
                PostulantResultat postulantResultat = new PostulantResultat();

                postulantResultat.setResultat(resultatSaved); // set resultat to postulant_resultat
                postulantResultat.setPostulant(postulant);
                postulantResultat.setStatut("Admis");
                postulantResultatList.add(postulantResultat);
                postulans.add(postulant);
            });

//            List<Postulant> postulantList = postulantRepository.findAll();
            List<Postulation> postulantionList = postulationRepository.findAllByConcoursId(resultatPostulant.getConcoursId());
            List<Postulant> postulantList = new ArrayList<>();
            postulantionList.forEach(postulation -> {
                postulantList.add(postulation.getPostulant());
            });
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

    public ResponseEntity<Map<String, Object>> getResultats(int page, int size) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Resultat> resultats = resultatRepository.findAll(paging);
            return new ResponseEntity<>(Response.success(resultats, "Liste des résultats."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur de  recupération."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> activeDesactiveResultat(Boolean visibility, Long id) {
        try {
            Optional<Resultat> resultatOptional = resultatRepository.findById(id);
            if (resultatOptional.isPresent()) {
                Resultat resultat = resultatOptional.get();
                resultat.setVisibily(visibility);
                resultatRepository.save(resultat);
                return new ResponseEntity<>(Response.success(resultat, "Visibilité modifiée avec succès."), HttpStatus.OK);
            }
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Ce résultat n'existe pas !"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur de  recupération."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> getResultByPostulant(int page, int size, Long postulantId) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<PostulantResultat> resultatPage = postulantResultatRepository.findAllByPostulantId(paging, postulantId);
            return new ResponseEntity<>(Response.success(resultatPage, "Liste des résultats !"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur de  recupération."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Map<String, Object>> getPostulantByResultat(int page, int size, Long resultatId) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<PostulantResultat> resultatPage = postulantResultatRepository.findAllByResultatId(paging, resultatId);
            return new ResponseEntity<>(Response.success(resultatPage, "Liste des postulants !"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(new HashMap<>(), "Erreur de  recupération."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
