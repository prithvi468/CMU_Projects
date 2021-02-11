package edu.cmu.andrew.student085; /* prithvipoddar Prithvip */

/**
 * Author: Prithvi Poddar
 * This program uses the MapReduce Paradigm
 * to perform text analytics on an input file.
 * it has six tasks
 */
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Scanner;

public class TempestAnalytics {

    //wordcount
    private static void partTwoTasks(String fileName) {

        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("Project5Task2");

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        // Task 0
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);
        System.out.println("the number of lines in " + fileName +  "is " + inputFile.count());

        // Task 1
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content -> Arrays.asList(content.split(" ")));
        System.out.println("number of Words in "+ fileName +  "is " + wordsFromFile.count());

        //Task2
        System.out.println("number of Words in "+ fileName +  "is " + wordsFromFile.distinct().count());

        //Task3
        JavaPairRDD wordsPairs = wordsFromFile.mapToPair(t -> new Tuple2(t, 1));
        wordsPairs.saveAsTextFile("Project5/Part_2/TheTempestOutputDir1");

        //Task4
        JavaPairRDD wordPairedReduced= wordsPairs.reduceByKey((x, y) -> (int) x + (int) y);
        wordPairedReduced.saveAsTextFile("Project5/Part_2/TheTempestOutputDir2");

        //Task5
        //Take input from the user
        System.out.println("Enter a search word ");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        inputFile.foreach(currLine ->{
            if(currLine.contains(userInput)){
                System.out.println(currLine);
            }
        });

    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No files provided.");
            System.exit(0);
        }

        partTwoTasks(args[0]);
    }
}

