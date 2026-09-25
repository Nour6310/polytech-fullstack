
package com.exemple.back.model;

import java.time.LocalDate;

public class Film {

    private Long id;
    private String titre;
    private String realisateur;
    private LocalDate dateSortie;
    private Genre genre;

    public Film() { }

    public Film(Long id, String titre, String realisateur,
                LocalDate dateSortie, Genre genre) {
        this.id = id;
        this.titre = titre;
        this.realisateur = realisateur;
        this.dateSortie = dateSortie;
        this.genre = genre;
    }
    public Long getId() { 
        return id;
        }
    public String getTitre() { 
        return titre; 
        }
    public String getRealisateur() { 
        return realisateur; 
        }
    public LocalDate getDateSortie() { 
        return dateSortie; 
        }
    public Genre getGenre() { 
        return genre; 
        }
    public void setId(Long id) { 
        this.id = id; 
        }
    public void setTitre(String titre) { 
        this.titre = titre; 
        }
    public void setRealisateur(String realisateur) { 
        this.realisateur = realisateur; 
        }
    
    public void setDateSortie(LocalDate dateSortie) { 
        this.dateSortie = dateSortie; 
        }
   
    public void setGenre(Genre genre) { 
        this.genre = genre; }
}