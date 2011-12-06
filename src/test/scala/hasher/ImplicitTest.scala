package test.scala.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream
import java.io.StringReader

import scala.io.Source

class ImplicitTest extends Specification {

    val data = TestData.test

    "Implicit converstion to a Hasher" should {

        import hasher.Implicits._
        import hasher.Hasher

        "work for Strings" in {
            val value: Hasher = data.str
            ok
        }
        "work for StringBuilders" in {
            val value: Hasher = data.builder
            ok
        }
        "work for Byte Arrays" in {
            val value: Hasher = data.bytes
            ok
        }
        "work for InputStreams" in {
            val value: Hasher = data.stream
            ok
        }
        "work for Readers" in {
            val value: Hasher = data.reader
            ok
        }
        "work for Sources" in {
            val value: Hasher = data.source
            ok
        }
    }

    "The implicit hashing methods" should {

        import hasher.Implicits._

        "MD5 hash" in {
            data.str.md5.hex must_== data.md5ed
        }
        "SHA-1 hash" in {
            data.str.sha1.hex must_== data.sha1ed
        }
        "SHA-256 hash" in {
            data.str.sha256.hex must_== data.sha256ed
        }
        "SHA-384 hash" in {
            data.str.sha384.hex must_== data.sha384ed
        }
        "SHA-512 hash" in {
            data.str.sha512.hex must_== data.sha512ed
        }
        "CRC32 hash" in {
            data.str.crc32.hex must_== data.crc32ed
        }
        "BCrypt hash" in {
            data.str.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "The implicit compare methods" should {

        import hasher.Implicits._

        "be comparable to an MD5 Hash" in {
            (data.str md5= data.md5ed) must beTrue
            (data.str md5= "AHashThatIsWrong") must beFalse
            (data.str md5= "SomeHashThatIsWrong") must beFalse
            (data.str md5= "") must beFalse
        }

        "be comparable to a SHA1 Hash" in {
            (data.str sha1= data.sha1ed) must beTrue
            (data.str sha1= "AHashThatIsWrong") must beFalse
            (data.str sha1= "SomeHashThatIsWrong") must beFalse
            (data.str sha1= "") must beFalse
        }

        "be comparable to a SHA256 Hash" in {
            (data.str sha256= data.sha256ed) must beTrue
            (data.str sha256= "AHashThatIsWrong") must beFalse
            (data.str sha256= "SomeHashThatIsWrong") must beFalse
            (data.str sha256= "") must beFalse
        }

        "be comparable to a SHA384 Hash" in {
            (data.str sha384= data.sha384ed) must beTrue
            (data.str sha384= "AHashThatIsWrong") must beFalse
            (data.str sha384= "SomeHashThatIsWrong") must beFalse
            (data.str sha384= "") must beFalse
        }

        "be comparable to a SHA512 Hash" in {
            (data.str sha512= data.sha512ed) must beTrue
            (data.str sha512= "AHashThatIsWrong") must beFalse
            (data.str sha512= "SomeHashThatIsWrong") must beFalse
            (data.str sha512= "") must beFalse
        }

        "be comparable to a CRC32 Hash" in {
            (data.str crc32= data.crc32ed) must beTrue
            (data.str crc32= "AHashThatIsWrong") must beFalse
            (data.str crc32= "SomeHashThatIsWrong") must beFalse
            (data.str crc32= "") must beFalse
        }

        "be comparable to a BCrypt Hash" in {
            (data.str bcrypt= data.bcrypted) must beTrue
            (data.str bcrypt= "AHashThatIsWrong") must beFalse
            (data.str bcrypt= "SomeHashThatIsWrong") must beFalse
            (data.str bcrypt= "") must beFalse
        }
    }

    "The implicit salt method" should {

        import hasher.Implicits._

        val str: String = "test"
        val md5ed = "d615489ad65aad3f6138728a02221e95"

        "change the hash" in {
            str.salt("one").salt("two").md5.hex must_== md5ed
            (str.salt("one").salt("two") md5= md5ed) must beTrue
        }
    }

}

