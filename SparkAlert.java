package org.example;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;

public class SparkAlert {
    public static void main(String[] args) {
        // Spark Konfigürasyonu ve SparkContext oluşturma
        SparkConf conf = new SparkConf().setAppName("Spark Alert Example").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Veri setinin yolu
        String datasetPath = "C:\\Users\\beyza\\Desktop\\training_project\\Data.csv"; // Dosya yolunu kontrol edin

        // Veri setini satır satır okuma
        JavaRDD<String> lines = sc.textFile(datasetPath);

        // İlk satırı başlık olarak atlayın
        JavaRDD<String> dataLines = lines.filter(line -> !line.startsWith("Airline"));

        // Belirtilen parametreyi arama ve konsola "alert" yazısı bastırma
        String searchParam = "IndiGo"; // Aramak istediğiniz parametre
        dataLines.filter(line -> line.contains(searchParam))
                .foreach(line -> System.out.println("alert: " + line));

        // SparkContext'i kapatma
        sc.close();
    }
}
