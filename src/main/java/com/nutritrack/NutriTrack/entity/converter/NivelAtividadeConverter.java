package com.nutritrack.NutriTrack.entity.converter;

import com.nutritrack.NutriTrack.entity.NivelAtividade;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NivelAtividadeConverter implements AttributeConverter<NivelAtividade, String> {

    @Override
    public String convertToDatabaseColumn(NivelAtividade attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase();
    }

    @Override
    public NivelAtividade convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return NivelAtividade.fromString(dbData);
    }
}
