package com.exemple.back.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Acteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String prenom;
    @Column(nullable = false, length = 100)
    private String nom;
    @OneToMany(mappedBy = "acteur", cascade = CascadeType.REMOVE)
    private Set<Role> roles = new HashSet<>();
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
    public Set<Role> getRoles() { 
        return roles; 
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