package http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@AllArgsConstructor
@Data

public class ClientHttp {

    URL url;
    String token;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public HttpURLConnection setConfig(String method) throws Exception {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", token);
        } catch (MalformedURLException exception) {
            throw new Exception("Invalid url format ");
        } catch (IOException io) {
            throw new Exception("IOException :"+io.getMessage());
        }
        return conn;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public <T>T executeRequest(HttpURLConnection conn, Class<T> entityClass)throws Exception {
        T object;
        try {
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            ObjectMapper mapper = new ObjectMapper();
            StringBuilder stringBuilder = new StringBuilder();
            while ((output = br.readLine()) != null) {
                stringBuilder.append(output);
            }
            conn.disconnect();
            object = mapper.readValue(stringBuilder.toString(), entityClass);

        } catch (Exception e) {
            throw new Exception("Error executing request:"+e.getMessage());
        }
        return object;
    }
   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
