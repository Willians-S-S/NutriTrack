package com.nutritrack.NutriTrack.repository;

import com.nutritrack.NutriTrack.entity.RegistroPeso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistroPesoRepository extends JpaRepository<RegistroPeso, UUID> {

    List<RegistroPeso> findByUsuarioIdAndDataMedicaoBetween(UUID usuarioId, LocalDate start, LocalDate end);

    Optional<RegistroPeso> findByUsuarioIdAndDataMedicao(UUID usuarioId, LocalDate dataMedicao);

    List<RegistroPeso> findByUsuarioId(UUID usuarioId);
}
