package test.scala.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream
import java.io.StringReader

import scala.io.Source

class CompareTest extends Specification {

    // The data being hashed
    val str: String = "test"
    val builder: StringBuilder = new StringBuilder(str)
    val bytes: Array[Byte] = str.getBytes
    def stream = new ByteArrayInputStream( bytes )
    def reader = new StringReader(str)
    def source = Source.fromBytes( bytes )


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
            (str md5= md5ed) must beTrue
            (str md5= "AHashThatIsWrong") must beFalse
            (str md5= "SomeHashThatIsWrong") must beFalse
            (str md5= "") must beFalse
        }

        "be comparable to a SHA1 Hash" in {
            (str sha1= sha1ed) must beTrue
            (str sha1= "AHashThatIsWrong") must beFalse
            (str sha1= "SomeHashThatIsWrong") must beFalse
            (str sha1= "") must beFalse
        }

        "be comparable to a SHA256 Hash" in {
            (str sha256= sha256ed) must beTrue
            (str sha256= "AHashThatIsWrong") must beFalse
            (str sha256= "SomeHashThatIsWrong") must beFalse
            (str sha256= "") must beFalse
        }

        "be comparable to a SHA384 Hash" in {
            (str sha384= sha384ed) must beTrue
            (str sha384= "AHashThatIsWrong") must beFalse
            (str sha384= "SomeHashThatIsWrong") must beFalse
            (str sha384= "") must beFalse
        }

        "be comparable to a SHA512 Hash" in {
            (str sha512= sha512ed) must beTrue
            (str sha512= "AHashThatIsWrong") must beFalse
            (str sha512= "SomeHashThatIsWrong") must beFalse
            (str sha512= "") must beFalse
        }

        "be comparable to a CRC32 Hash" in {
            (str crc32= crc32ed) must beTrue
            (str crc32= "AHashThatIsWrong") must beFalse
            (str crc32= "SomeHashThatIsWrong") must beFalse
            (str crc32= "") must beFalse
        }

        "be comparable to a BCrypt Hash" in {
            (str bcrypt= bcrypted) must beTrue
            (str bcrypt= "AHashThatIsWrong") must beFalse
            (str bcrypt= "SomeHashThatIsWrong") must beFalse
            (str bcrypt= "") must beFalse
        }
    }

    "On a StringBuilder, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (builder md5= md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (builder sha1= sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (builder sha256= sha256ed) must beTrue
        }
        "compare to a SHA384 Hash" in {
            (builder sha384= sha384ed) must beTrue
        }
        "compare to a SHA512 Hash" in {
            (builder sha512= sha512ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (builder crc32= crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (builder bcrypt= bcrypted) must beTrue
        }
    }

    "On a Byte Array, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (bytes md5= md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (bytes sha1= sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (bytes sha256= sha256ed) must beTrue
        }
        "compare to a SHA384 Hash" in {
            (bytes sha384= sha384ed) must beTrue
        }
        "compare to a SHA512 Hash" in {
            (bytes sha512= sha512ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (bytes crc32= crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (bytes bcrypt= bcrypted) must beTrue
        }
    }

    "On an Input Stream, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (stream md5= md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (stream sha1= sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (stream sha256= sha256ed) must beTrue
        }
        "compare to a SHA384 Hash" in {
            (stream sha384= sha384ed) must beTrue
        }
        "compare to a SHA512 Hash" in {
            (stream sha512= sha512ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (stream crc32= crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (stream bcrypt= bcrypted) must beTrue
        }
    }

    "On a Reader, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (reader md5= md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (reader sha1= sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (reader sha256= sha256ed) must beTrue
        }
        "compare to a SHA384 Hash" in {
            (reader sha384= sha384ed) must beTrue
        }
        "compare to a SHA512 Hash" in {
            (reader sha512= sha512ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (reader crc32= crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (reader bcrypt= bcrypted) must beTrue
        }
    }

    "On a Source, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (source md5= md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (source sha1= sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (source sha256= sha256ed) must beTrue
        }
        "compare to a SHA384 Hash" in {
            (source sha384= sha384ed) must beTrue
        }
        "compare to a SHA512 Hash" in {
            (source sha512= sha512ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (source crc32= crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (source bcrypt= bcrypted) must beTrue
        }
    }

}

