package test.roundeights.hasher

import org.specs2.mutable._

import com.roundeights.hasher.{Algo, Hasher}
import scala.io.{Codec, Source}

class TapTest extends Specification {

    def hashTest ( algo: Algo, data: TestData, hash: String ) = {
        "Using " + algo + ", the Tap.hash method" in {
            "Hash a Stream" in {
                val tap = algo.tap( data.stream )
                val string = Source.fromInputStream(tap)(Codec.UTF8).mkString
                string must_== data.str
                tap.hash.hex must_== hash
            }
            "Hash a Reader" in {
                val tap = algo.tap( data.reader )
                val string = tap.mkString
                string must_== data.str
                tap.hash.hex must_== hash
            }
            "Hash a Source" in {
                val tap = algo.tap( data.source )
                val string = tap.mkString
                string must_== data.str
                tap.hash.hex must_== hash
            }
        }
    }

    def compareTest ( algo: Algo, data: TestData, hash: String ) = {
        "Using " + algo + ", the Tap.hash= method" in {
            "match a Stream" in {
                val tap = algo.tap( data.stream )
                val string = Source.fromInputStream(tap)(Codec.UTF8).mkString
                string must_== data.str
                ( tap hash= hash ) must_== true
            }
            "match a Reader" in {
                val tap = algo.tap( data.reader )
                val string = tap.mkString
                string must_== data.str
                ( tap hash= hash ) must_== true
            }
            "match a Source" in {
                val tap = algo.tap( data.source )
                val string = tap.mkString
                string must_== data.str
                ( tap hash= hash ) must_== true
            }
        }
    }

    // BCrypt produces salted hashes, so we can only assert against the format
    // of the resulting hashes
    def testBCrypt( data: TestData ) {
        "Using BCrypt, the Tap.hash method" in {
            "Hash a Stream" in {
                val tap = Algo.bcrypt.tap( data.stream )
                val string = Source.fromInputStream(tap)(Codec.UTF8).mkString
                string must_== data.str
                tap.hash.hex must beMatching("^[a-zA-Z0-9]{120}$")
            }
            "Hash a Reader" in {
                val tap = Algo.bcrypt.tap( data.reader )
                val string = tap.mkString
                string must_== data.str
                tap.hash.hex must beMatching("^[a-zA-Z0-9]{120}$")
            }
            "Hash a Source" in {
                val tap = Algo.bcrypt.tap( data.source )
                val string = tap.mkString
                string must_== data.str
                tap.hash.hex must beMatching("^[a-zA-Z0-9]{120}$")

            }
        }
    }

    // A simple test
    "Simple plain text data" should {
        testBCrypt( TestData.test )
        TestData.test.runAgainstUnsalted(hashTest)
        TestData.test.runAgainstAll(compareTest)
    }

    // Test an internationalized, multi-byte string
    "Internationalized plain text values" should {
        testBCrypt( TestData.international )
        TestData.international.runAgainstUnsalted(hashTest)
        TestData.international.runAgainstAll(compareTest)
    }

    // Test a string large enough that it blows out the buffers
    "Large values" should {
        testBCrypt( TestData.large )
        TestData.large.runAgainstUnsalted(hashTest)
        TestData.large.runAgainstAll(compareTest)
    }

    // Test hashing a blank string
    "Blank string" should {
        testBCrypt( TestData.blank )
        TestData.blank.runAgainstUnsalted(hashTest)
        TestData.blank.runAgainstAll(compareTest)
    }

}

