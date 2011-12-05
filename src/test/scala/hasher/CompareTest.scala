package test.scala.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream
import java.io.StringReader

class CompareTest extends Specification {

    // The data being hashed
    val str: String = "test"
    val bytes: Array[Byte] = str.getBytes
    def stream = new ByteArrayInputStream( bytes )
    def reader = new StringReader(str)


    // These values represent the string "test" as various hashes
    val md5ed = "098f6bcd4621d373cade4e832627b4f6"
    val sha1ed = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3"
    val sha256ed =
        "9f86d081884c7d659a2fe" +
        "aa0c55ad015a3bf4f1b2b0" +
        "b822cd15d6c15b0f00a08"
    val sha384ed =
        "768412320f7b0aa5812fce428dc4706b3cae50e02a64caa1" +
        "6a782249bfe8efc4b7ef1ccb126255d196047dfedf17a0a9"
    val sha512ed =
        "ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db2" +
        "7ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff"
    val crc32ed = "d87f7e0c"
    val bcrypted =
        "24326124313024755658345747674b" +
        "524749445643414b5941655a494f41" +
        "4b4f3468543759437053796167356a" +
        "2f5a365261377253334b636b796c32"

    "On a String, the implicit compare methods" should {

        import hasher.Implicits._

        "be comparable to an MD5 Hash" in {
            (str md5sTo md5ed) must beTrue
            (str md5sTo "AHashThatIsWrong") must beFalse
            (str md5sTo "SomeHashThatIsWrong") must beFalse
            (str md5sTo "") must beFalse
        }

        "be comparable to a SHA1 Hash" in {
            (str sha1sTo sha1ed) must beTrue
            (str sha1sTo "AHashThatIsWrong") must beFalse
            (str sha1sTo "SomeHashThatIsWrong") must beFalse
            (str sha1sTo "") must beFalse
        }

        "be comparable to a SHA256 Hash" in {
            (str sha256sTo sha256ed) must beTrue
            (str sha256sTo "AHashThatIsWrong") must beFalse
            (str sha256sTo "SomeHashThatIsWrong") must beFalse
            (str sha256sTo "") must beFalse
        }

        "be comparable to a SHA384 Hash" in {
            (str sha384sTo sha384ed) must beTrue
            (str sha384sTo "AHashThatIsWrong") must beFalse
            (str sha384sTo "SomeHashThatIsWrong") must beFalse
            (str sha384sTo "") must beFalse
        }

        "be comparable to a SHA512 Hash" in {
            (str sha512sTo sha512ed) must beTrue
            (str sha512sTo "AHashThatIsWrong") must beFalse
            (str sha512sTo "SomeHashThatIsWrong") must beFalse
            (str sha512sTo "") must beFalse
        }

        "be comparable to a CRC32 Hash" in {
            (str crc32sTo crc32ed) must beTrue
            (str crc32sTo "AHashThatIsWrong") must beFalse
            (str crc32sTo "SomeHashThatIsWrong") must beFalse
            (str crc32sTo "") must beFalse
        }

        "be comparable to a BCrypt Hash" in {
            (str bcryptsTo bcrypted) must beTrue
            (str bcryptsTo "AHashThatIsWrong") must beFalse
            (str bcryptsTo "SomeHashThatIsWrong") must beFalse
            (str bcryptsTo "") must beFalse
        }
    }

    "On a Byte Array, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (bytes md5sTo md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (bytes sha1sTo sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (bytes sha256sTo sha256ed) must beTrue
        }
        "compare to a SHA384 Hash" in {
            (bytes sha384sTo sha384ed) must beTrue
        }
        "compare to a SHA512 Hash" in {
            (bytes sha512sTo sha512ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (bytes crc32sTo crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (bytes bcryptsTo bcrypted) must beTrue
        }
    }

    "On an Input Stream, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (stream md5sTo md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (stream sha1sTo sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (stream sha256sTo sha256ed) must beTrue
        }
        "compare to a SHA384 Hash" in {
            (stream sha384sTo sha384ed) must beTrue
        }
        "compare to a SHA512 Hash" in {
            (stream sha512sTo sha512ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (stream crc32sTo crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (stream bcryptsTo bcrypted) must beTrue
        }
    }

    "On a Reader, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (reader md5sTo md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (reader sha1sTo sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (reader sha256sTo sha256ed) must beTrue
        }
        "compare to a SHA384 Hash" in {
            (reader sha384sTo sha384ed) must beTrue
        }
        "compare to a SHA512 Hash" in {
            (reader sha512sTo sha512ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (reader crc32sTo crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (reader bcryptsTo bcrypted) must beTrue
        }
    }

}

