package com.concours.komou.app.controller;

import com.concours.komou.app.payoad.ResultatPostulant;
import com.concours.komou.app.service.ResultatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
