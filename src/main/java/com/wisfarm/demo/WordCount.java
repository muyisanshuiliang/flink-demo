package com.wisfarm.demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/4 12:07
 */
public class WordCount {
    public static void main(String[] args) throws Exception {
        // 1、创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 2、从文件中读取数据，得到数据源
        String inputPath = "src\\main\\resources\\test_one.txt";
        DataSet<String> inputDataSet = env.readTextFile(inputPath);
        // 3、对数据集进行处理，按空格进行分词展开，转换成（word,1）进行统计
        DataSet<Tuple2<String, Integer>> sum = inputDataSet.flatMap(new MyFlatMapper())
                // 按照二元组数据的第一个位置元素进行分组（位置）
                .groupBy(0)
                // 将二元组的第二个位置上的元素进行求和
                .sum(1);
        sum.print();
    }

    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String, Integer>> {

        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            // 按空格进行分词
            String[] splits = value.split(" ");
            // 遍历所有的数据，包装成二元组
            for (String item : splits) {
                out.collect(new Tuple2<String, Integer>(item, 1));
            }
        }
    }
}
