package com.nutritrack.NutriTrack.repository;

import com.nutritrack.NutriTrack.entity.RegistroPeso;
import com.nutritrack.NutriTrack.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegistroPesoRepository extends JpaRepository<RegistroPeso, UUID> {
    Optional<RegistroPeso> findFirstByUsuarioOrderByDataMedicaoDesc(Usuario usuario);

    Optional<RegistroPeso> findByUsuario_IdAndDataMedicao(UUID usuarioId, LocalDate dataMedicao);

    List<RegistroPeso> findByUsuario_IdAndDataMedicaoBetween(UUID usuarioId, LocalDate start, LocalDate end);
}