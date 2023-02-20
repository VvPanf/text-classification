package org.example;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainTest {
    @Before
    public void setUp() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.ERROR);
    }

    public UDF1<String, String> porterStemmer() {
        return (value) -> PorterStemmer.stem(
                    value.toLowerCase()
                        .replaceAll("[\\pP\\d\\n\\t\\r$+<>№=]", " ")
                        .replaceAll("  ", " "));
    }

    @Test
    public void main_test() throws IOException {
        SparkSession spark = SparkSession.builder()
                .appName("App1")
                .config("spark.eventLog.enabled", "false")
                .master("local[1]")
                .getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");
        spark.sqlContext().udf().register("porterStemmer", porterStemmer(), DataTypes.StringType);
        // Загрузка тренировочных данных
        Dataset<Row> df = spark.read().option("header", true).csv("src/test/resources/test_data.csv");
        // Преобразуем label к double
        df = df.withColumn("label", new Column("label").cast("double"));
        df.show();
        // Удаление цифр и других лишних знаков
        df = df.withColumn("sentence", functions.callUDF("porterStemmer", df.col("sentence")));
        df.show(false);
        // Разбиение предлодений на слова
        df = new Tokenizer()
                .setInputCol("sentence")
                .setOutputCol("words")
                .transform(df);
        df.show();
        // Выделение токенов слов
        df = new HashingTF()
                .setInputCol("words")
                .setOutputCol("features")
                .setNumFeatures(100)
                .transform(df);
        df.show();
        // Разбиение данныз на тренировочную и тестовую выборки
        Dataset<Row>[] splits = df.randomSplit(new double[]{0.6, 0.4}, 1234L);
        Dataset<Row> train = splits[0];
        Dataset<Row> test = splits[1];
        // Создание наивного байеса
        NaiveBayes nb = new NaiveBayes();
        // Тренировка модели
        NaiveBayesModel model = nb.fit(train);
        // Прогонка модели на тестовых данных
        Dataset<Row> predictions = model.transform(test);
        predictions.show();
        // Вычисление точности на тестовом наборе
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("Test set accuracy = " + accuracy);
        // Сохранение обученной модели
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("data/model.ser")))) {
            oos.writeObject(model);
        }
        // Загрузка модели
        NaiveBayesModel model1;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("data/model.ser")))) {
            model1 = (NaiveBayesModel) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // Прогонка загруженной модели на тестовых данных
        Dataset<Row> predictions1 = model1.transform(test);
        predictions1.show();
        spark.stop();
    }
}