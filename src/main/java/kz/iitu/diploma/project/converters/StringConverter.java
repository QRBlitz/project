package kz.iitu.diploma.project.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return "";
        }
        return String.join(",", strings);
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        if (string == null || string.trim().length() == 0) {
            return new ArrayList<String>();
        }

        String[] data = string.split(",");
        return Arrays.asList(data);
    }

}
