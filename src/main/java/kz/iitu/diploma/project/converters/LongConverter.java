package kz.iitu.diploma.project.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class LongConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> longs) {
        if (longs == null || longs.isEmpty()) {
            return "";
        }
        return String.join(",", longs.toString());
    }

    @Override
    public List<Long> convertToEntityAttribute(String string) {
        if (string == null || string.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String[] data = string.split(",");
        List<Long> list = new ArrayList<>();
        for (String i : data) {
            list.add(Long.valueOf(i));
        }
        return list;
    }

}
