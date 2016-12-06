import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.feature.Word2Vec
import org.apache.spark.rdd.RDD
import spark.jobserver._
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.api.java.JavaRDD
import org.scalactic._

import scala.util.Try
/**
  * Created by d799383 on 06.12.2016.
  */

class Init {

  def InitSparkContext: SparkContext = {
    val conf = new SparkConf().setAppName("Word2VecExample")
      .setMaster("local")
    SparkContext.getOrCreate(conf)
  }

}

class Word2VecTest {

  def getInput = {
    val sc = new Init().InitSparkContext
    sc.textFile("../resources/testData.tsv", 1).map(line => line.split(" "))
    //sc.textFile("/resources/testData.tsv").map(line => line.split(" ")).toJavaRDD()
  }

  /*def getModel: Unit = {
    val input = getInput
    val w2v = new Word2Vec
    w2v.fit(input.asInstanceOf[RDD[Array[String]]])
  }*/

}

object Word2VecJob extends SparkJob with NamedRddSupport {

  def validate(sc: SparkContext, config: Config): SparkJobValidation = {
    Try(config.getString("input.string"))
      .map(x => SparkJobValid)
      .getOrElse(SparkJobInvalid("No input.string config param"))
  }

  def runJob(sc: SparkContext, config: Config): Any = {
    //sc.parallelize(config.getString("input.string").split(" ").toSeq).countByValue
    val input = getInput(sc)
    var w2v = new Word2Vec
    w2v.fit(input)
  }

  def getInput(sc: SparkContext): JavaRDD[Array[String]] = {
    sc.textFile("../resources/testData.tsv").map(line => line.split(" ")).toJavaRDD()
  }

  def main(args: Array[String]) {
    val sc = new Init().InitSparkContext


    val config = ConfigFactory.parseString("")
    val results = runJob(sc, config)
    println("Result is " + results)
  }


}
