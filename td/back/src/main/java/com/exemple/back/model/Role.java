package com.exemple.back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "role",
    uniqueConstraints = @UniqueConstraint(columnNames = {"film_id", "acteur_id"})
)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "acteur_id", nullable = false)
    private Acteur acteur;
    @Column(length = 200)
    private String personnage;

    public Role() { }

    public Role(Film film, Acteur acteur, String personnage) {
        this.film = film;
        this.acteur = acteur;
        this.personnage = personnage;
    }

    public Long getId() {
        return id;
        }
    public Film getFilm() {
        return film;
        }
    public Acteur getActeur() {
        return acteur;
        }
    public String getPersonnage() {
        return personnage;
        }
    public void setPersonnage(String personnage) {
        this.personnage = personnage;
        }
}
