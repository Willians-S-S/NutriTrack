package com.nutritrack.service;

import com.nutritrack.dto.RefeicaoResponseDTO;
import com.nutritrack.entity.Alimento;
import com.nutritrack.entity.ItemRefeicao;
import com.nutritrack.entity.Refeicao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class NutrientCalculatorService {

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
