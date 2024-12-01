package ma.ensa.www.assistdoc.entities;



public class Medicament {

    public String id;
    private String nom;
    private String dosage;
    private String frequence;
    private String heurePris;


    public Medicament() {
    }
    public Medicament(String nom, String dosage, String frequence, String heurePris) {
        this.nom = nom;
        this.dosage = dosage;
        this.frequence = frequence;
        this.heurePris = heurePris;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequence() {
        return frequence;
    }

    public void setFrequence(String frequence) {
        this.frequence = frequence;
    }

    public String getHeurePris() {
        return heurePris;
    }

    public void setHeurePris(String heurePris) {
        this.heurePris = heurePris;
    }
}
