
package com.exemple.back.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Film {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,length=200)
    private String titre;
    private String realisateur;
    private LocalDate dateSortie;
    @Enumerated(EnumType.STRING)
    private Genre genre;

    public Film() { }

    public Film(Long id, String titre, String realisateur,LocalDate dateSortie, Genre genre) {
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