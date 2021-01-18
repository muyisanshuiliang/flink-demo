package com.wisfarm.demo;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @description:
 * @Author: yangLang
 * @CreateDate: 2021/1/4 13:30
 */
public class WordCountStream {
    public static void main(String[] args) throws Exception {
        // 1、获取流式处理环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 设置并行度,默认并行度为电脑的核数，如果设置成1，就是没有并行度
//        env.setParallelism(16);
//        env.setParallelism(1);

        // 2.1、从文件中读取数据，得到数据源
//        String inputPath = "src\\main\\resources\\test_one.txt";
//        DataStream<String> inputDataStream = env.readTextFile(inputPath);

        // 2.2、从socket文本流中读取数据
        // （1）、网络参数写死
//        DataStream<String> inputDataStream = env.socketTextStream("192.168.2.147", 9999);

        // （2）、用 parameter tool工具从程序启动参数中提取配置项
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String host = parameterTool.get("host");
        int port = parameterTool.getInt("port");
        DataStream<String> inputDataStream = env.socketTextStream(host, port);

        // 3、基于数据流进行转换计算，对数据集进行处理，按空格进行分词展开，转换成（word,1）进行统计
        DataStream<Tuple2<String, Integer>> sum = inputDataStream.flatMap(new WordCount.MyFlatMapper())
                // 按照二元组数据的第一个位置元素进行分组（位置）
                .keyBy(0)

                // 将二元组的第二个位置上的元素进行求和
                .sum(1).setParallelism(2);
        sum.print();

        // 执行任务
        env.execute();
    }
}
