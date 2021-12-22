package com.concours.komou.app.payoad;

public class PostulantPayload {
   public String prenonm;
   public String nom;
   public String telephone;
   public String password;

    public String getPrenom() {
        return prenonm;
    }

    public void setPrenom(String prenon) {
        this.prenonm = prenon;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
