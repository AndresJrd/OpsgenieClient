package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import helper.Tuple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlarmDetail {
    private String seen;
    private String id;
    private Integer tinyId;
    private String alias;
    private String message;
    private String status;
    private String acknowledged;//boolean
    private String isSeen;//boolean
    private String snoozed;//boolean
    private String lastOccurredAt;
    private String createdAt;
    private String updatedAt;
    private String source;
    private String owner;
    private String priority;
    private Report report;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<Tuple> getFieldValue() {
        Field[] fields = AlarmDetail.class.getDeclaredFields();
        List<Tuple> tuples = new ArrayList<>();
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            try {
                tuples.add(new Tuple(field.getName(), field.get(this).toString()));
            } catch (final IllegalAccessException e) {
                tuples.add(new Tuple(field.getName(), e.getClass().getSimpleName()));
            }
        });
        return tuples;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<String> getFields() {
        Field[] fields = AlarmDetail.class.getDeclaredFields();
        List<String> lines = new ArrayList<>();
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            lines.add(field.getName());
        });
        return lines;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
