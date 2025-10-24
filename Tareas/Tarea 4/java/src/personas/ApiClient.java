package personas;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ghost
 */
import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class ApiClient {
    private static String token = null;

    public static String getToken() {
        return token;
    }

    public static boolean login(String email, String password) {
        try {
            URL url = new URL("http://127.0.0.1:8000/api/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("password", password);

            OutputStream os = conn.getOutputStream();
            os.write(data.toString().getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                token = json.getString("token"); // asegúrate de que tu API devuelva “token”
                return true;
            } else {
                System.out.println("Error: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al hacer login: " + e.getMessage());
            return false;
        }
    }

    public static String getPersonas() throws Exception {
        URL url = new URL("http://127.0.0.1:8000/api/personas");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        return response.toString();
    }
}

