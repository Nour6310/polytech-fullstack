package com.exemple.back.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.exemple.back.model.Film;

@Repository
public class FilmRepository {
    private Long compteur=1L;
    // Sans L java considère que c'est un int
    //private List<Film> films = new ArrayList<>();
    //c'est pas pratique car aprés quand on va chercher un film par son id on va devoir parcourir toute la liste pour le trouver avec equals(id) pour trouver e nom du film
    private final Map<Long, Film> films = new HashMap<>();
    //or ici si on a l'id on fait juste films.get(id) va nous retourner le film avec cet id si il existe sinon null
    //c'est comme un dictionnaire 
    public Film save(Film film) {
        // si le film n'a pas d'id, c'est un nouveau film : on lui en donne un
        if (film.getId() == null) {
            film.setId(compteur);
            compteur++;
        }
        // on range le film dans la map, sous son id
        films.put(film.getId(), film);
        return film;
    }
    public Film findById(Long id){
        return films.get(id); //clé

    }
    public boolean existsById( Long id){
        return films.get(id)!=null;
    }
    public void deleteById (Long id){
        films.remove(id);
            }
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
        }
}

