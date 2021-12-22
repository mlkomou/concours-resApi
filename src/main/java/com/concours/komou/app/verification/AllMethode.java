package com.concours.komou.app.verification;

import com.concours.komou.app.repo.PostulantRepository;
import org.springframework.stereotype.Service;

@Service
public class AllMethode {
    private final PostulantRepository postulantRepository;

    public AllMethode(PostulantRepository postulantRepository) {
        this.postulantRepository = postulantRepository;
    }

    public boolean checkUniquePostulant(String phone) {
        return postulantRepository.existsByTelephone(phone);
    }
}
