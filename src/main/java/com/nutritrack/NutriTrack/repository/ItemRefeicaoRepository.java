package com.nutritrack.NutriTrack.repository;

import com.nutritrack.NutriTrack.entity.ItemRefeicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRefeicaoRepository extends JpaRepository<ItemRefeicao, UUID> {
}
