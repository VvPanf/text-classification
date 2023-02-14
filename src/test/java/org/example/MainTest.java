package org.example;

import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.junit.Test;


public class MainTest {
    public UDF1<String, String> porterStemmer() {
        return (value) -> PorterStemmer.stem(
                    value.toLowerCase()
                        .replaceAll("[\\pP\\d\\n\\t\\r$+<>№=]", " ")
                        .replaceAll("  ", " "));
    }

    @Test
    public void main_test() {
        SparkSession spark = SparkSession.builder()
                .appName("App1")
                .config("spark.eventLog.enabled", "false")
                .master("local[1]")
                .getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");
        spark.sqlContext().udf().register("porterStemmer", porterStemmer(), DataTypes.StringType);
        // Загрузка тренировочных данных
        Dataset<Row> df = spark.read().option("header", true).csv("src/main/resources/test_data.csv");
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
        spark.stop();
    }
}