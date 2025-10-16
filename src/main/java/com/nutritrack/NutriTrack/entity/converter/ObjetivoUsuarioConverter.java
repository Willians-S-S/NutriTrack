package com.nutritrack.NutriTrack.entity.converter;

import com.nutritrack.NutriTrack.entity.ObjetivoUsuario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ObjetivoUsuarioConverter implements AttributeConverter<ObjetivoUsuario, String> {

    @Override
    public String convertToDatabaseColumn(ObjetivoUsuario attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toLowerCase();
    }

    @Override
    public ObjetivoUsuario convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return ObjetivoUsuario.fromString(dbData);
    }
}
