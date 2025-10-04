package com.nutritrack.NutriTrack.service;

import com.nutritrack.NutriTrack.dto.RefeicaoResponseDTO;
import com.nutritrack.NutriTrack.entity.Alimento;
import com.nutritrack.NutriTrack.entity.ItemRefeicao;
import com.nutritrack.NutriTrack.entity.Refeicao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Serviço responsável pelo cálculo de nutrientes de uma refeição.
 *
 * Calcula o total de calorias, proteínas, carboidratos e gorduras de uma refeição
 * somando os valores de cada item proporcional à quantidade consumida.
 */
@Service
public class NutrientCalculatorService {

    /**
     * Calcula os nutrientes de uma refeição e retorna um DTO atualizado.
     *
     * Para cada item da refeição, multiplica os valores nutricionais pelo fator de porção
     * (quantidade / 100). Retorna um {@link RefeicaoResponseDTO} com os totais
     * calculados e arredondados para 3 casas decimais.
     *
     * @param refeicao A entidade {@link Refeicao} que contém os itens da refeição
     * @param dto DTO base {@link RefeicaoResponseDTO} que será atualizado com os totais
     * @return {@link RefeicaoResponseDTO} contendo os valores totais de calorias, proteínas,
     * carboidratos e gorduras
     */
    public RefeicaoResponseDTO calculateNutrients(Refeicao refeicao, RefeicaoResponseDTO dto) {
        BigDecimal totalCalorias = BigDecimal.ZERO;
        BigDecimal totalProteinas = BigDecimal.ZERO;
        BigDecimal totalCarboidratos = BigDecimal.ZERO;
        BigDecimal totalGorduras = BigDecimal.ZERO;

        for (ItemRefeicao item : refeicao.getItens()) {
            Alimento alimento = item.getAlimento();
            BigDecimal quantidade = item.getQuantidade();

            // Simplificação: assumindo que a porção base é 100g/100ml e a conversão é 1:1
            // Uma implementação real precisaria de um fator de conversão por unidade
            BigDecimal fator = quantidade.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            totalCalorias = totalCalorias.add(alimento.getCalorias().multiply(fator));
            totalProteinas = totalProteinas.add(alimento.getProteinasG().multiply(fator));
            totalCarboidratos = totalCarboidratos.add(alimento.getCarboidratosG().multiply(fator));
            totalGorduras = totalGorduras.add(alimento.getGordurasG().multiply(fator));
        }

        return new RefeicaoResponseDTO(
            dto.id(),
            dto.usuarioId(),
            dto.tipo(),
            dto.dataHora(),
            dto.observacoes(),
            dto.criadoEm(),
            dto.itens(),
            totalCalorias.setScale(3, RoundingMode.HALF_UP),
            totalProteinas.setScale(3, RoundingMode.HALF_UP),
            totalCarboidratos.setScale(3, RoundingMode.HALF_UP),
            totalGorduras.setScale(3, RoundingMode.HALF_UP)
        );
    }
}
