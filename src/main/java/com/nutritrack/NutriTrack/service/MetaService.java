package com.nutritrack.NutriTrack.service;

import com.nutritrack.NutriTrack.dto.MetaRequestDTO;
import com.nutritrack.NutriTrack.dto.MetaResponseDTO;
import com.nutritrack.NutriTrack.entity.Meta;
import com.nutritrack.NutriTrack.entity.Usuario;
import com.nutritrack.NutriTrack.mapper.MetaMapper;
import com.nutritrack.NutriTrack.repository.MetaRepository;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaRepository metaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MetaMapper metaMapper;

    public MetaResponseDTO create(UUID usuarioId, MetaRequestDTO metaRequestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + usuarioId));

        Meta meta = metaMapper.toEntity(metaRequestDTO);
        meta.setUsuario(usuario);

        Meta savedMeta = metaRepository.save(meta);
        return metaMapper.toResponseDTO(savedMeta);
    }
}
