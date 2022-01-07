package com.concours.komou.app.controller;

import com.concours.komou.app.payoad.ResultatPostulant;
import com.concours.komou.app.service.ResultatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/resultats/")
public class ResultatController {

    private final ResultatService resultatService;

    public ResultatController(ResultatService resultatService) {
        this.resultatService = resultatService;
    }

    @PostMapping("save")
    public ResponseEntity<Map<String, Object>> saveResultats(@RequestBody ResultatPostulant resultatPostulant) {
        return resultatService.publishResultat(resultatPostulant);
    }
    @PostMapping("liste")
    public ResponseEntity<Map<String, Object>> getListe(@RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        return resultatService.getResultats(page, size);
    }

    @PostMapping("liste-by-postulant")
    public ResponseEntity<Map<String, Object>> getListeByPostulant(@RequestParam("page") int page,
                                                        @RequestParam("size") int size,
                                                                   @RequestParam("postulantId") Long postulantId) {
        return resultatService.getResultByPostulant(page, size, postulantId);
    }

    @PostMapping("liste-by-resultat")
    public ResponseEntity<Map<String, Object>> getListeByPostulantByResultat(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size,
                                                                   @RequestParam("resultatId") Long resultatId) {
        return resultatService.getPostulantByResultat(page, size, resultatId);
    }

    @PostMapping("change-visibiliy")
    ResponseEntity<Map<String, Object>> changeVisibility(@RequestParam("visibility") Boolean visibility,
                                                         @RequestParam("id") Long id) {
        return resultatService.activeDesactiveResultat(visibility, id);
    }

    @PostMapping("export-excel")
    public void exportToExcel(HttpServletResponse response, @RequestParam("resultatId") Long resultatId) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        resultatService.export(response, resultatId);


//        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);
//
//        excelExporter.export(response);
    }
}
