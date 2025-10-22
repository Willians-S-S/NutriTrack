package com.nutritrack.NutriTrack.repository;

import com.nutritrack.NutriTrack.entity.Meta;
import com.nutritrack.NutriTrack.enums.TipoMeta;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetaRepository extends JpaRepository<Meta, UUID> {
    List<Meta> findByUsuarioIdAndTipo(UUID usuarioId, TipoMeta tipo);

    /**
     * Encontra a meta mais recente que está ativa para um usuário em uma data específica.
     * Uma meta é considerada ativa se a data fornecida está entre a data de início e a data de fim da meta.
     * @param usuarioId ID do usuário.
     * @param tipo O tipo da meta (DIARIA, SEMANAL, MENSAL).
     * @return Um Optional contendo a meta encontrada, se houver.
     */
    @Query(value = "SELECT * FROM metas m WHERE m.usuario_id = ?1 AND m.tipo = ?2  LIMIT 1", nativeQuery = true)
    Optional<Meta> findActiveMetaForDate(UUID usuarioId, String tipo);

    Boolean existsByUsuarioIdAndTipo(UUID usuarioId, TipoMeta tipo);
}
