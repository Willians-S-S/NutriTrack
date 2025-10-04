package com.nutritrack.NutriTrack.repository;

import com.nutritrack.NutriTrack.entity.Refeicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RefeicaoRepository extends JpaRepository<Refeicao, UUID> {

    List<Refeicao> findByUsuarioIdAndDataHoraBetween(UUID usuarioId, OffsetDateTime start, OffsetDateTime end);

    List<Refeicao> findByUsuarioId(UUID usuarioId);

    @Query("SELECT r FROM Refeicao r WHERE r.usuario.id = :usuarioId AND date_trunc('day', r.dataHora) = date_trunc('day', :data)")
    List<Refeicao> findByUsuarioIdAndDay(UUID usuarioId, OffsetDateTime data);
}
