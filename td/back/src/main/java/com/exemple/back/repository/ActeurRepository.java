package com.exemple.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exemple.back.model.Acteur;

public interface ActeurRepository extends JpaRepository<Acteur, Long> {
}