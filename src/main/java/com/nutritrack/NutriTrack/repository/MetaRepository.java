package com.nutritrack.NutriTrack.repository;

import com.nutritrack.NutriTrack.entity.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetaRepository extends JpaRepository<Meta, UUID> {
}
