package org.example.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.example.utils.PorterStemmer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class SparkConfig {
    @Value("${spark.app.name}")
    private String appName;
    @Value("${spark.master}")
    private String masterUri;
    @Value("${spark.model-path}")
    private String modelPath;

    @Bean
    public SparkConf conf() {
        return new SparkConf()
                .setAppName(appName)
                .setMaster(masterUri)
                .set("spark.eventLog.enabled", "false");
    }

    @Bean
    public JavaSparkContext sparkContext() {
        return new JavaSparkContext(conf());
    }

    @Bean
    public SparkSession sparkSession() {
        SparkSession spark = SparkSession
                .builder()
                .sparkContext(sparkContext().sc())
                .appName(appName)
                .getOrCreate();
        spark.sqlContext().udf().register("porterStemmer", porterStemmer(), DataTypes.StringType);
        return spark;
    }

    public UDF1<String, String> porterStemmer() {
        return (value) -> PorterStemmer.stem(
                value.toLowerCase()
                        .replaceAll("[\\pP\\d\\n\\t\\r$+<>№=]", " ")
                        .replaceAll(" +", " "));
    }

    @Bean
    public PipelineModel model() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(modelPath)))) {
            PipelineModel model = (PipelineModel) ois.readObject();
            return model;
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
