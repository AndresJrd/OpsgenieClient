package util;

import entity.AlarmDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final List<String> regex = new ArrayList<>();

    static {
        regex.add("[a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}");
        regex.add("[a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}");
        regex.add("[a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}[-,_,.][a-zA-Z]{2,}");
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String validate(String string) {
        for (String r : regex) {
            Pattern pattern = Pattern.compile(r, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                return matcher.group(0);
            }
        }
        return string;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static List<AlarmDetail> unifyErrors(List<AlarmDetail> response) {
        response.forEach(d -> {
            if (d.getMessage().contains("Error"))
                d.setMessage("Error %");
            else if (d.getMessage().contains("Apdex"))
                d.setMessage("Apdex");
            d.setCreatedAt(d.getCreatedAt().substring(0, 10));
        });
        return response;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
