package org.example.service;

import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ClassificationService {
    private final String[] categories = {"Новая Афина", "EGAR E4", "Террасофт CRM", "ЦФТ-БАНК"};
    @Autowired
    private SparkSession spark;
    @Autowired
    private PipelineModel model;

    public String predict(String text) {
        List<Row> rows = Collections.singletonList(RowFactory.create(text));
        StructType schema = new StructType(new StructField[]{
                new StructField("text", DataTypes.StringType, false, Metadata.empty())
        });
        Dataset<Row> df = spark.createDataFrame(rows, schema);
        df = df.withColumn("text", functions.callUDF("porterStemmer", df.col("text")));
        Dataset<Row> predictions = model.transform(df);
        int categoryIndex = (int) predictions.select("prediction").collectAsList().get(0).getDouble(0);
        return categories[categoryIndex];
    }

    public List<String> getCategories() {
        return Arrays.asList(categories);
    }
}
