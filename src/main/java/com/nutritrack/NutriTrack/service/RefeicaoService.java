package com.nutritrack.NutriTrack.service;

import com.nutritrack.NutriTrack.dto.RefeicaoRequestDTO;
import com.nutritrack.NutriTrack.dto.RefeicaoResponseDTO;
import com.nutritrack.NutriTrack.entity.Alimento;
import com.nutritrack.NutriTrack.entity.ItemRefeicao;
import com.nutritrack.NutriTrack.entity.Refeicao;
import com.nutritrack.NutriTrack.entity.Usuario;
import com.nutritrack.NutriTrack.exception.AccessDeniedException;
import com.nutritrack.NutriTrack.exception.ResourceNotFoundException;
import com.nutritrack.NutriTrack.mapper.RefeicaoMapper;
import com.nutritrack.NutriTrack.repository.AlimentoRepository;
import com.nutritrack.NutriTrack.repository.RefeicaoRepository;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefeicaoService {

    private final RefeicaoRepository refeicaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlimentoRepository alimentoRepository;
    private final RefeicaoMapper refeicaoMapper;
    private final NutrientCalculatorService nutrientCalculatorService;

    @Transactional
    public RefeicaoResponseDTO create(UUID usuarioId, RefeicaoRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + usuarioId));

        Refeicao refeicao = refeicaoMapper.toEntity(requestDTO);
        refeicao.setUsuario(usuario);

        List<ItemRefeicao> itens = requestDTO.itens().stream()
            .map(itemDto -> {
                Alimento alimento = alimentoRepository.findById(itemDto.alimentoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Alimento não encontrado com o ID: " + itemDto.alimentoId()));
                ItemRefeicao item = refeicaoMapper.itemRequestToEntity(itemDto);
                item.setAlimento(alimento);
                item.setRefeicao(refeicao);
                return item;
            }).collect(Collectors.toList());
        refeicao.setItens(itens);

        Refeicao savedRefeicao = refeicaoRepository.save(refeicao);
        RefeicaoResponseDTO responseDTO = refeicaoMapper.toResponseDTO(savedRefeicao);
        return nutrientCalculatorService.calculateNutrients(savedRefeicao, responseDTO);
    }

    @Transactional(readOnly = true)
    public RefeicaoResponseDTO findById(UUID usuarioId, UUID refeicaoId) {
        Refeicao refeicao = refeicaoRepository.findById(refeicaoId)
            .orElseThrow(() -> new ResourceNotFoundException("Refeição não encontrada com o ID: " + refeicaoId));
        if (!refeicao.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para acessar esta refeição.");
        }
        RefeicaoResponseDTO responseDTO = refeicaoMapper.toResponseDTO(refeicao);
        return nutrientCalculatorService.calculateNutrients(refeicao, responseDTO);
    }

    @Transactional(readOnly = true)
    public List<RefeicaoResponseDTO> findByDateRange(UUID usuarioId, LocalDate start, LocalDate end) {
        return refeicaoRepository.findByUsuarioIdAndDateBetween(usuarioId, start, end).stream()
            .map(refeicao -> {
                RefeicaoResponseDTO responseDTO = refeicaoMapper.toResponseDTO(refeicao);
                return nutrientCalculatorService.calculateNutrients(refeicao, responseDTO);
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public RefeicaoResponseDTO update(UUID usuarioId, UUID refeicaoId, RefeicaoRequestDTO requestDTO) {
        Refeicao refeicao = refeicaoRepository.findById(refeicaoId)
            .orElseThrow(() -> new ResourceNotFoundException("Refeição não encontrada com o ID: " + refeicaoId));
        if (!refeicao.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para atualizar esta refeição.");
        }

        refeicaoMapper.updateFromRequest(requestDTO, refeicao);

        List<ItemRefeicao> itens = requestDTO.itens().stream()
            .map(itemDto -> {
                Alimento alimento = alimentoRepository.findById(itemDto.alimentoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Alimento não encontrado com o ID: " + itemDto.alimentoId()));
                ItemRefeicao item = refeicaoMapper.itemRequestToEntity(itemDto);
                item.setAlimento(alimento);
                item.setRefeicao(refeicao);
                return item;
            }).collect(Collectors.toList());
        refeicao.getItens().clear();
        refeicao.getItens().addAll(itens);

        Refeicao savedRefeicao = refeicaoRepository.save(refeicao);
        RefeicaoResponseDTO responseDTO = refeicaoMapper.toResponseDTO(savedRefeicao);
        return nutrientCalculatorService.calculateNutrients(savedRefeicao, responseDTO);
    }

    @Transactional
    public void delete(UUID usuarioId, UUID refeicaoId) {
        Refeicao refeicao = refeicaoRepository.findById(refeicaoId)
            .orElseThrow(() -> new ResourceNotFoundException("Refeição não encontrada com o ID: " + refeicaoId));
        if (!refeicao.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para excluir esta refeição.");
        }
        refeicaoRepository.delete(refeicao);
    }
}