package org.example;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.Transformer;
import org.apache.spark.ml.classification.*;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.StopWordsRemover;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.example.utils.PorterStemmer;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


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
                        .replaceAll(" +", " "));
    }

    private SparkSession createSparkSession() {
        SparkSession spark = SparkSession.builder()
                .appName("App1")
                .config("spark.eventLog.enabled", "false")
                .master("local[1]")
                .getOrCreate();
        spark.sparkContext().setLogLevel("ERROR");
        spark.sqlContext().udf().register("porterStemmer", porterStemmer(), DataTypes.StringType);
        return spark;
    }

    @Test
    public void main_test() throws IOException {
        SparkSession spark = createSparkSession();
        // Загрузка тренировочных данных
        Dataset<Row> df = spark.read().option("header", true).csv("src/test/resources/test_data.csv");
        // Преобразуем label к double
        df = df.withColumn("label", new Column("label").cast("double"));
        df.show();
        // Удаление цифр и других лишних знаков
        df = df.withColumn("sentence", functions.callUDF("porterStemmer", df.col("sentence")));
        df.show(false);
        // Разбиение предложений на слова
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

    @Test
    public void test_bbc() throws IOException {
        SparkSession spark = createSparkSession();
        // Загрузка тренировочных данных
        Dataset<Row> df = spark.read().option("header", true).option("delimiter", ";").csv("src/test/resources/text-data.csv");
        df = df.withColumn("text", functions.callUDF("porterStemmer", df.col("text")));
//        df.show(5);
        // Разбиение данныз на тренировочную и тестовую выборки
        Dataset<Row>[] splits = df.randomSplit(new double[]{0.7, 0.3}, 1234L);
        Dataset<Row> train = splits[0];
        Dataset<Row> test = splits[1];

        // ПОДГОТОВКА ШАГОВ ПО ОБРАБОТКЕ ДАННЫХ
        // Преобразование метки (строки) в целое число
        StringIndexer stringIndexer = new StringIndexer()
                .setInputCol("category")
                .setOutputCol("label");
        // Разбиение предложений на слова
        Tokenizer tokenizer = new Tokenizer()
                .setInputCol("text")
                .setOutputCol("words");
        // Удаление стоп-слов
        StopWordsRemover stopWordsRemover = new StopWordsRemover()
                .setInputCol("words")
                .setOutputCol("cleanTokens");
        // Выделение токенов слов
        HashingTF hashingTF = new HashingTF()
                .setInputCol("cleanTokens")
                .setOutputCol("features")
                .setNumFeatures(1000);
        // Создание наивного байеса
        NaiveBayes naiveBayes = new NaiveBayes();
        // Создание регресии
        LogisticRegression logisticRegression = new LogisticRegression()
                .setMaxIter(10)
                .setRegParam(0.3)
                .setElasticNetParam(0);
//        // Преобразование индекса к метке класса
//        IndexToString indexToStringExp = new IndexToString()
//                .setInputCol("label")
//                .setOutputCol("expected");
        // Конвеер по обработке данных
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{
                        stringIndexer,
                        tokenizer,
                        stopWordsRemover,
                        hashingTF,
                        naiveBayes,
//                        logisticRegression,
//                        indexToStringExp
                });
        PipelineModel model = pipeline.fit(train);
        Dataset<Row> predictions = model.transform(test);
        predictions.show(200);
        // Вычисление точности на тестовом наборе
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("Test set accuracy = " + accuracy);
        // Сохранение обученной модели только с необходимыми стадиями
//        model.save("data/model");
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get("data/bayes_model.ser")))) {
            List<Transformer> stages = Arrays.stream(model.stages()).skip(1).collect(Collectors.toList());
            PipelineModel modelToSave = new PipelineModel(UUID.randomUUID().toString(), stages);
            oos.writeObject(modelToSave);
        }
        spark.stop();
    }

    @Test
    public void test_prediction() {
        SparkSession spark = createSparkSession();
        // Загрузка обученной модели из файла
//        PipelineModel model = PipelineModel.load("data/model");
        PipelineModel model;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get("data/bayes_model.ser")))) {
            model = (PipelineModel) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        // Массив с категориями
        String[] categories = {"Новая Афина", "EGAR E4", "Террасофт CRM", "ЦФТ-БАНК"};
        // Подготовка датасета
        final String textToClassify = "Просто какая-то фигня написана, которую не надо классифицировать";
        List<Row> rows = Collections.singletonList(RowFactory.create(textToClassify));
        StructType schema = new StructType(new StructField[]{
                new StructField("text", DataTypes.StringType, false, Metadata.empty())
        });
        Dataset<Row> df = spark.createDataFrame(rows, schema);
        df = df.withColumn("text", functions.callUDF("porterStemmer", df.col("text")));
        Dataset<Row> predictions = model.transform(df);
        predictions.show();
        int categoryIndex = (int) predictions.select("prediction").collectAsList().get(0).getDouble(0);
        System.out.println("Вероятности категорий: " + predictions.select("probability").collectAsList().get(0).get(0));
        System.out.println("Определена категория: " + categories[categoryIndex]);
        spark.stop();
    }

    @Test
    public void most_best_model() {
        // Количество прогонов
        int numAttempts = 3;
        Random rand = new Random();
        SparkSession spark = createSparkSession();
        Map<String, Double> results = new LinkedHashMap<>();
        List<Classifier<?,?,?>> classifierList = new ArrayList<>();
        // Наивный Байес
        classifierList.add(new NaiveBayes());
        // Логистическая регрессия
        classifierList.add(new LogisticRegression()
                            .setMaxIter(10)
                            .setRegParam(0.3)
                            .setElasticNetParam(0));
        // Случайный лес
        classifierList.add(new RandomForestClassifier()
                .setNumTrees(10));
        // Дерево решений
        classifierList.add(new DecisionTreeClassifier());
        // Прогоняем обучение по каждому классификатору
        for (Classifier<?,?,?> classifier : classifierList) {
            String classifierName = classifier.getClass().toString().substring(classifier.getClass().toString().lastIndexOf(".") + 1);
            System.out.println("Обучаем " + classifierName);
            for (int i=0; i<numAttempts; i++) {
                double accuracy = learn_and_evaluate(spark, classifier, rand.nextLong());
                results.put(i + " " + classifierName, accuracy);
            }
        }
        // Выводим таблицу с результатами
        for (Map.Entry<String, Double> entry : results.entrySet()) {
            System.out.println(entry.toString());
        }
        spark.stop();
    }

    private double learn_and_evaluate(SparkSession spark, Classifier<?,?,?> classifier, long randomKey) {
        Dataset<Row> df = spark.read().option("header", true).option("delimiter", ";").csv("src/test/resources/text-data.csv");
        df = df.withColumn("text", functions.callUDF("porterStemmer", df.col("text")));
        Dataset<Row>[] splits = df.randomSplit(new double[]{0.7, 0.3}, randomKey);
        Dataset<Row> train = splits[0];
        Dataset<Row> test = splits[1];

        // Шаги по обработке данных
        StringIndexer stringIndexer = new StringIndexer()
                .setInputCol("category")
                .setOutputCol("label");
        Tokenizer tokenizer = new Tokenizer()
                .setInputCol("text")
                .setOutputCol("words");
        StopWordsRemover stopWordsRemover = new StopWordsRemover()
                .setInputCol("words")
                .setOutputCol("cleanTokens");
        HashingTF hashingTF = new HashingTF()
                .setInputCol("cleanTokens")
                .setOutputCol("features")
                .setNumFeatures(1000);
        // Обучаем модель
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{
                        stringIndexer,
                        tokenizer,
                        stopWordsRemover,
                        hashingTF,
                        classifier
                });
        PipelineModel model = pipeline.fit(train);
        Dataset<Row> predictions = model.transform(test);
        // Вычисление точности на тестовом наборе
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        // Возвращаем точность модели
        return evaluator.evaluate(predictions);
    }
}