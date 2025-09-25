package com.nutritrack.service;

import com.nutritrack.dto.RefeicaoRequestDTO;
import com.nutritrack.dto.RefeicaoResponseDTO;
import com.nutritrack.entity.Alimento;
import com.nutritrack.entity.ItemRefeicao;
import com.nutritrack.entity.Refeicao;
import com.nutritrack.entity.Usuario;
import com.nutritrack.exception.AccessDeniedException;
import com.nutritrack.exception.ResourceNotFoundException;
import com.nutritrack.mapper.RefeicaoMapper;
import com.nutritrack.repository.AlimentoRepository;
import com.nutritrack.repository.RefeicaoRepository;
import com.nutritrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas operações de CRUD e consultas de {@link Refeicao}.
 *
 * Este serviço gerencia a criação, leitura, listagem por período e exclusão de refeições,
 * garantindo permissões de acesso e calculando os nutrientes totais através do
 * {@link NutrientCalculatorService}.
 */
@Service
@RequiredArgsConstructor
public class RefeicaoService {

    private final RefeicaoRepository refeicaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlimentoRepository alimentoRepository;
    private final RefeicaoMapper refeicaoMapper;
    private final NutrientCalculatorService nutrientCalculatorService;

    /**
     * Cria uma nova refeição para o usuário especificado.
     *
     * @param usuarioId ID do usuário que está registrando a refeição
     * @param requestDTO DTO contendo os dados da refeição a ser criada
     * @return {@link RefeicaoResponseDTO} com os dados da refeição criada e totais de nutrientes calculados
     * @throws ResourceNotFoundException se o usuário ou algum alimento não existir
     */
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

    /**
     * Busca uma refeição específica de um usuário.
     *
     * @param usuarioId ID do usuário dono da refeição
     * @param refeicaoId ID da refeição a ser buscada
     * @return {@link RefeicaoResponseDTO} com os dados da refeição e totais de nutrientes calculados
     * @throws ResourceNotFoundException se a refeição não existir
     * @throws AccessDeniedException se o usuário não for dono da refeição
     */
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

    /**
     * Lista todas as refeições de um usuário em um intervalo de datas.
     *
     * @param usuarioId ID do usuário
     * @param start Data inicial do intervalo
     * @param end Data final do intervalo
     * @return Lista de {@link RefeicaoResponseDTO} com os dados das refeições e nutrientes calculados
     */
    @Transactional(readOnly = true)
    public List<RefeicaoResponseDTO> findByDateRange(UUID usuarioId, OffsetDateTime start, OffsetDateTime end) {
        return refeicaoRepository.findByUsuarioIdAndDataHoraBetween(usuarioId, start, end).stream()
            .map(refeicao -> {
                RefeicaoResponseDTO responseDTO = refeicaoMapper.toResponseDTO(refeicao);
                return nutrientCalculatorService.calculateNutrients(refeicao, responseDTO);
            })
            .collect(Collectors.toList());
    }

    /**
     * Exclui uma refeição de um usuário.
     *
     * @param usuarioId ID do usuário dono da refeição
     * @param refeicaoId ID da refeição a ser excluída
     * @throws ResourceNotFoundException se a refeição não existir
     * @throws AccessDeniedException se o usuário não for dono da refeição
     */
    @Transactional
    public void delete(UUID usuarioId, UUID refeicaoId) {
        Refeicao refeicao = refeicaoRepository.findById(refeicaoId)
            .orElseThrow(() -> new ResourceNotFoundException("Refeição não encontrada com o ID: " + refeicaoId));
        if (!refeicao.getUsuario().getId().equals(usuarioId)) {
            throw new AccessDeniedException("Você não tem permissão para excluir esta refeição.");
        }
        refeicaoRepository.delete(refeicao);
    }

    // Outros métodos como update, addItem, removeItem seriam implementados aqui
}
