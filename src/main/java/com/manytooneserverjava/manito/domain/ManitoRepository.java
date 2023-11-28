package com.manytooneserverjava.manito.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManitoRepository extends JpaRepository<Manito, Long> {
    public Optional<Manito> findByName(String name);
}
