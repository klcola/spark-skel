package com.colorseq.weald

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object SimpleApp {

    /**
     * Spark 示例方法，计算文件 test.txt 中每个单词出现的次数，并将结果保存在 test.count 中。
     *
     */
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("Simple Application")
        val sc = new SparkContext(conf)
        val textFile = sc.textFile("hdfs://localhost:9000/klcola/test.txt")
        val counts = textFile.flatMap(line => line.split(" "))
            .map(word => (word, 1))
            .reduceByKey(_ + _)
        counts.saveAsTextFile("hdfs://localhost:9000/klcola/test.count")
    }

}
