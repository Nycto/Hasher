package test.scala.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream
import java.io.StringReader

import scala.io.Source

import hasher.{Algo, Hasher}

class AlgoTest extends Specification {

    // The data being hashed
    val str: String = "test"
    val builder: StringBuilder = new StringBuilder(str)
    val bytes: Array[Byte] = str.getBytes
    def stream = new ByteArrayInputStream( bytes )
    def reader = new StringReader(str)
    def source = Source.fromBytes( bytes )


    // The algo test object
    val algo: Algo = Hasher.md5


    // The hashed test data
    val md5ed = "098f6bcd4621d373cade4e832627b4f6"


    "The apply methods on the Algo object" should {
        "Hash a String" in { algo(str).hex must_== md5ed }
        "Hash a StringBuilder" in { algo(builder).hex must_== md5ed }
        "Hash an Array of Bytes" in { algo(bytes).hex must_== md5ed }
        "Hash a Stream" in { algo(stream).hex must_== md5ed }
        "Hash a Reader" in { algo(reader).hex must_== md5ed }
        "Hash a Source" in { algo(source).hex must_== md5ed }
    }

    "The compare methods on the Algo object" should {
        "match with a String" in { algo.compare(str, md5ed) must beTrue }
        "match with a StringBuilder" in {
            algo.compare(builder, md5ed) must beTrue
        }
        "match with an Array of Bytes" in {
            algo.compare(bytes, md5ed) must beTrue
        }
        "match with a Stream" in { algo.compare(stream, md5ed) must beTrue }
        "match with a Reader" in { algo.compare(reader, md5ed) must beTrue }
        "match with a Source" in { algo.compare(source, md5ed) must beTrue }
    }

}

