package com.exemple.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exemple.back.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByFilmIdAndActeurId(Long filmId, Long acteurId);
}
