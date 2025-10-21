package com.nutritrack.NutriTrack.repository;

import com.nutritrack.NutriTrack.entity.Refeicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RefeicaoRepository extends JpaRepository<Refeicao, UUID> {

    @Query(value = "SELECT * FROM refeicoes r WHERE r.id_usuario = :usuarioId AND CAST(r.data_hora AS DATE) BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Refeicao> findByUsuarioIdAndDateBetween(@Param("usuarioId") UUID usuarioId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Refeicao> findByUsuarioId(UUID usuarioId);

    @Query(value = "SELECT * FROM refeicoes r WHERE r.id_usuario = :usuarioId AND CAST(r.data_hora AS DATE) = CAST(:data AS DATE)", nativeQuery = true)
    List<Refeicao> findByUsuarioIdAndDay(@Param("usuarioId") UUID usuarioId, @Param("data") LocalDate data);
}
