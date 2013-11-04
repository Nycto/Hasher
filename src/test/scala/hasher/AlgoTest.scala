package test.roundeights.hasher

import org.specs2.mutable._

import com.roundeights.hasher.{Algo, Hasher}

class AlgoTest extends Specification {

    def hashTest ( algo: Algo, data: TestData, hash: String ) = {

        "Using " + algo + ", the apply methods on the Algo object" in {
            "Hash a String" in {
                algo( data.str ).hex must_== hash
            }
            "Hash a StringBuilder" in {
                algo( data.builder ).hex must_== hash
            }
            "Hash an Array of Bytes" in {
                algo( data.bytes ).hex must_== hash
            }
            "Hash a Stream" in {
                algo( data.stream ).hex must_== hash
            }
            "Hash a Reader" in {
                algo( data.reader ).hex must_== hash
            }
            "Hash a Source" in {
                algo( data.source ).hex must_== hash
            }
        }

        "Hashing multiple times with " + algo + " should return the same result" in {
                algo( data.str ).hex must_== hash
                algo( data.str ).hex must_== hash
                algo( data.str ).hex must_== hash
        }

    }

    def compareTest ( algo: Algo, data: TestData, hash: String ) = {

        "Using " + algo + ", the compare methods on the Algo object" in {
            "match with a String" in {
                (algo( data.str ) hash= hash)
                    .aka( "Comparing a String to " + hash ) must beTrue
            }
            "match with a StringBuilder" in {
                (algo( data.builder ) hash= hash)
                    .aka( "Comparing a StringBuilder to " + hash ) must beTrue
            }
            "match with an Array of Bytes" in {
                (algo( data.bytes ) hash= hash)
                    .aka( "Comparing a Byte Array to " + hash ) must beTrue
            }
            "match with a Stream" in {
                (algo( data.stream ) hash= hash )
                    .aka( "Comparing a Stream to " + hash ) must beTrue
            }
            "match with a Reader" in {
                (algo( data.reader ) hash= hash )
                    .aka( "Comparing a Reader to " + hash ) must beTrue
            }
            "match with a Source" in {
                (algo( data.source ) hash= hash )
                    .aka( "Comparing a Source to " + hash ) must beTrue
            }
        }

        "Comparing multiple times with " + algo + " should still match" in {
            "match with a String" in {
                (algo( data.str ) hash= hash )
                    .aka( "Comparing a String to " + hash ) must beTrue
                (algo( data.str ) hash= hash )
                    .aka( "Comparing a String to " + hash ) must beTrue
                (algo( data.str ) hash= hash )
                    .aka( "Comparing a String to " + hash ) must beTrue
            }
        }
    }

    // BCrypt produces salted hashes, so we can only assert against the format
    // of the resulting hashes
    def testBCrypt( data: TestData ) {
        "Using BCrypt, the apply methods on the Algo object" in {
            "Hash a String" in {
                Algo.bcrypt()(data.str).hex must
                    beMatching("^[a-zA-Z0-9]{120}$")
            }
            "Hash a StringBuilder" in {
                Algo.bcrypt()(data.builder).hex must
                    beMatching("^[a-zA-Z0-9]{120}$")
            }
            "Hash an Array of Bytes" in {
                Algo.bcrypt()(data.bytes).hex must
                    beMatching("^[a-zA-Z0-9]{120}$")
            }
            "Hash a Stream" in {
                Algo.bcrypt()(data.stream).hex must
                    beMatching("^[a-zA-Z0-9]{120}$")
            }
            "Hash a Reader" in {
                Algo.bcrypt()(data.reader).hex must
                    beMatching("^[a-zA-Z0-9]{120}$")
            }
            "Hash a Source" in {
                Algo.bcrypt()(data.source).hex must
                    beMatching("^[a-zA-Z0-9]{120}$")
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

