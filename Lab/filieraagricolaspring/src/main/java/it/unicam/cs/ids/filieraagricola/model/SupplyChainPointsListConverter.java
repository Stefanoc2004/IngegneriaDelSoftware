package it.unicam.cs.ids.filieraagricola.model;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.AttributeConverter;

/**
 * JPA {@link AttributeConverter} mapping between a list of {@link SupplyChainPoint}
 * and a compact string representation.
 *
 * <p>Format used: each point is serialized as <code>(lat;lng)</code> and points
 * are joined by commas, for example: <code>(43.123;13.456),(43.124;13.457)</code>.</p>
 *
 * <p>Parsing is intentionally lenient for empty segments; values shorter than
 * three characters are ignored.</p>
 */
public class SupplyChainPointsListConverter implements AttributeConverter<List<SupplyChainPoint>, String> {


    @Override
    public String convertToDatabaseColumn(List<SupplyChainPoint> points) {
        String[] strings = new String[points.size()];
        for (int i = 0; i < points.size(); i++) {
            strings[i] = "("+points.get(i).getLat()+";"+points.get(i).getLng()+")";
        }
        return String.join(",", strings);
    }

    @Override
    public List<SupplyChainPoint> convertToEntityAttribute(String s) {
        String[] strings = s.split(",");
        List<SupplyChainPoint> points = new LinkedList<>();
        for (int i = 0; i < strings.length; i++) {
            String s1 = strings[i];
            if (s1.length() > 3) {
                s1 = s1.substring(1, s1.length() - 1);
                String[] parts = s1.split(";");
                double lat = Double.valueOf(parts[0]);
                double lng = Double.valueOf(parts[1]);
                SupplyChainPoint point = new SupplyChainPoint(lat, lng);
                points.add(point);
            }
        }
        return points;
    }




}
