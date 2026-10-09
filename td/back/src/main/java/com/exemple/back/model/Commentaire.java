package com.exemple.back.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String auteur;
    @Column(nullable = false)
    private LocalDateTime dateCreation;
    @Column(nullable = false, length = 2000)
    private String message;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    public Commentaire() { }

    public Long getId() {
        return id;
        }
    public String getAuteur() {
        return auteur;
        }
    public LocalDateTime getDateCreation() {
        return dateCreation;
        }
    public String getMessage() {
        return message;
        }
    public Film getFilm() {
        return film;
        }
    public void setAuteur(String auteur) {
        this.auteur = auteur;
        }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
        }
    public void setMessage(String message) {
        this.message = message;
        }
    public void setFilm(Film film) {
        this.film = film;
        }
}
