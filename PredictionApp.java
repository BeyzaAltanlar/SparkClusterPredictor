package org.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PredictionApp {

    public static void main(String[] args) {
        String dataFile = "C:\\Users\\beyza\\Desktop\\training_project\\Raisin_Dataset.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip header line
                if (line.startsWith("Area")) {
                    continue;
                }

                List<Double> features = parseRecord(line);
                String prediction = getPredictionFromPythonService(features);

                if ("Kecimen".equals(prediction)) {
                    // Raise alarm for this record
                    raiseAlarm(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Double> parseRecord(String record) {
        String[] fields = record.split(",");
        // Assuming the features are the first 7 columns and converting them to Double
        return Arrays.asList(Double.parseDouble(fields[0]), Double.parseDouble(fields[1]), Double.parseDouble(fields[2]),
                Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Double.parseDouble(fields[5]),
                Double.parseDouble(fields[6]));
    }

    private static String getPredictionFromPythonService(List<Double> features) {
        String prediction = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            Gson gson = new Gson();
            String json = gson.toJson(new PredictionRequest(features));

            HttpPost post = new HttpPost("http://localhost:5000/predict");
            post.setHeader("Content-type", "application/json");
            post.setEntity(new StringEntity(json));

            CloseableHttpResponse response = httpClient.execute(post);
            String jsonResponse = EntityUtils.toString(response.getEntity());
            prediction = gson.fromJson(jsonResponse, PredictionResponse.class).getPrediction();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prediction;
    }

    private static void raiseAlarm(String record) {
        // Implement this method to raise an alarm for the given record
        System.out.println("Alarm! Record: " + record);
    }

    private static class PredictionRequest {
        private List<Double> features;

        public PredictionRequest(List<Double> features) {
            this.features = features;
        }

        public List<Double> getFeatures() {
            return features;
        }

        public void setFeatures(List<Double> features) {
            this.features = features;
        }
    }

    private static class PredictionResponse {
        private String prediction;

        public String getPrediction() {
            return prediction;
        }

        public void setPrediction(String prediction) {
            this.prediction = prediction;
        }
    }
}
