package com.nutritrack.NutriTrack.repository;

import com.nutritrack.NutriTrack.entity.Alimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, UUID> {
    Page<Alimento> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
