package com.exemple.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.exemple.back.model.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {
    @Query("""
            select f from Film f
            join f.acteurs a
            where a.id = :acteurId
            """)
    List<Film> findFilmsDeActeur(@Param("acteurId") Long acteurId);
}