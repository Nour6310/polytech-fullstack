package com.exemple.back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Acteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String prenom;
    @Column(nullable = false, length = 100)
    private String nom;
    public Acteur() { }

    public Long getId() { 
        return id; 
        }
    public String getPrenom() { 
        return prenom; 
        }
    public String getNom() { 
        return nom; 
        }
    public void setId(Long id) { 
        this.id = id; 
        }
    public void setPrenom(String prenom) { 
        this.prenom = prenom; 
        }
    public void setNom(String nom) { 
        this.nom = nom; 
        }
}