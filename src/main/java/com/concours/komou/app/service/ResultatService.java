package com.concours.komou.app.service;

import com.concours.komou.app.entity.*;
import com.concours.komou.app.payoad.NotificationPayload;
import com.concours.komou.app.payoad.ResultatPostulant;
import com.concours.komou.app.repo.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

//import com.sun.org.slf4j.internal.Logger;
//import com.sun.org.slf4j.internal.LoggerFactory;

@Service
public class ResultatService {
    private final ResultatRepository resultatRepository;
    private final ConcoursRepository concoursRepository;
    private final PostulantRepository postulantRepository;
    private final PostulantResultatRepository postulantResultatRepository;
    private final PostulationRepository postulationRepository;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
//    Logger logger = LoggerFactory.getLogger(LoggingController.class);


    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ResultatService(ResultatRepository resultatRepository, ConcoursRepository concoursRepository, PostulantRepository postulantRepository, PostulantResultatRepository postulantResultatRepository, PostulationRepository postulationRepository, NotificationService notificationService, NotificationRepository notificationRepository) {
        this.resultatRepository = resultatRepository;
        this.concoursRepository = concoursRepository;
        this.postulantRepository = postulantRepository;
        this.postulantResultatRepository = postulantResultatRepository;
        this.postulationRepository = postulationRepository;
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        workbook = new XSSFWorkbook();
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

           List<PostulantResultat> postulantResultatsAdmis = postulantResultatRepository.saveAll(postulantResultatList); // save resltat for each postulant admis
            List<PostulantResultat> postulantResultatsEchoues = postulantResultatRepository.saveAll(postulantResultatEchoue); // save resltat for each postulant echoue



            postulantResultatsAdmis.forEach(postulantResultat -> {
                System.err.println("admis "+ postulantResultat.getPostulant().getNom());
                NotificationPayload  notificationPayload = new NotificationPayload();
                notificationPayload.setType("RESULTAT");
                notificationPayload.setPostulantResultatId(postulantResultat.getId());
                notificationPayload.setPostulantId(postulantResultat.getPostulant().getId());
                List<String> included_segments = new ArrayList<>();
                included_segments.add(postulantResultat.getPostulant().getNotificationId());
                System.err.println("start notification " + notificationPayload.getPostulantResultatId());
                System.err.println("start resultat " + postulantResultat.getPostulant().getNotificationId());

                Notification notification = new Notification();
                notification.setPostulant(postulantResultat.getPostulant());
                notification.setTitre("RESULTAT");
                notification.setDescription("Votre résultat pour le concours " + postulantResultat.getResultat().getConcours().getName() + " est disponible.");
                notification.setType(notificationPayload.getType());
                notification.setPostulantResultat(postulantResultat);
                notificationRepository.save(notification);

                notificationService.sendPushNotification(notificationPayload, included_segments);
            });

            postulantResultatsEchoues.forEach(postulantResultat -> {
                NotificationPayload notificationPayload = new NotificationPayload();
                notificationPayload.setType("RESULTAT");
                notificationPayload.setDescription("Votre résultat pour le concours " + postulantResultat.getResultat().getConcours().getName() + " est disponible.");
                notificationPayload.setPostulantResultatId(postulantResultat.getId());
                notificationPayload.setTitre("RESULTAT");
                notificationPayload.setPostulantId(postulantResultat.getPostulant().getId());
                List<String> included_segments = new ArrayList<>();
                included_segments.add(postulantResultat.getPostulant().getNotificationId());

                Notification notification = new Notification();
                notification.setPostulant(postulantResultat.getPostulant());
                notification.setTitre("RESULTAT");
                notification.setDescription("Votre résultat pour le concours " + postulantResultat.getResultat().getConcours().getName() + " est disponible.");
                notification.setType(notificationPayload.getType());
                notification.setPostulantResultat(postulantResultat);
                notificationRepository.save(notification);


//                notificationService.saveNotification(notificationPayload);
                notificationService.sendPushNotification(notificationPayload, included_segments);
            });

            return new ResponseEntity<>(Response.success(resultatSaved, "Resultat publiée"), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e);
            return new ResponseEntity<>(Response.error(e, "Erreur d'enregistrement"), HttpStatus.INTERNAL_SERVER_ERROR);
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
            Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdAt");
            Pageable paging = PageRequest.of(page, size, defaultSort);
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


    //export to excel
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Résultat");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Prenom", style);
        createCell(row, 1, "Nom", style);
        createCell(row, 2, "Telephone", style);
        createCell(row, 3, "Situation", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines(Long resultatId) {
        int rowCount = 1;
        List<PostulantResultat> postulantResultats = postulantResultatRepository.getPostulantAdmisForExcel(resultatId);


        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (PostulantResultat postulantResultat : postulantResultats) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, postulantResultat.getPostulant().getPrenom(), style);
            createCell(row, columnCount++, postulantResultat.getPostulant().getNom(), style);
            createCell(row, columnCount++, postulantResultat.getPostulant().getTelephone(), style);
            createCell(row, columnCount++, postulantResultat.getStatut(), style);
//            createCell(row, columnCount++, user.isEnabled(), style);

        }
    }

    public void export(HttpServletResponse response, Long resultatId) throws IOException {
        writeHeaderLine();
        writeDataLines(resultatId);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
