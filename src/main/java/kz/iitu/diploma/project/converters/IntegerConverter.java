package kz.iitu.diploma.project.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class IntegerConverter implements AttributeConverter<List<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(List<Integer> integers) {
        if (integers == null || integers.isEmpty()) {
            return "";
        }
        return String.join(",", integers.toString());
    }

    @Override
    public List<Integer> convertToEntityAttribute(String string) {
        if (string == null || string.trim().length() == 0) {
            return new ArrayList<Integer>();
        }

        String[] data = string.split(",");
        List<Integer> list = new ArrayList<>();
        for (String i : data) {
            list.add(Integer.valueOf(i));
        }
        return list;
    }

}
