package com.nutritrack.repository;

import com.nutritrack.entity.RegistroAgua;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RegistroAguaRepository extends JpaRepository<RegistroAgua, UUID> {

    List<RegistroAgua> findByUsuarioIdAndDataMedicaoBetween(UUID usuarioId, LocalDate start, LocalDate end);

    @Query("SELECT r FROM RegistroAgua r WHERE r.usuario.id = :usuarioId AND r.dataMedicao = :dataMedicao")
    List<RegistroAgua> findByUsuarioIdAndDataMedicao(UUID usuarioId, LocalDate dataMedicao);

    List<RegistroAgua> findByUsuarioId(UUID usuarioId);
}
