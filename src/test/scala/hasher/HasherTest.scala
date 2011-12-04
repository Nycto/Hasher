package test.scala.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream

class HasherTest extends Specification {


    // The data being hashed
    val str: String = "test"
    val bytes: Array[Byte] = str.getBytes
    def stream = new ByteArrayInputStream( bytes )


    // These values represent the string "test" as various hashes
    val md5ed = "098f6bcd4621d373cade4e832627b4f6"

    val sha1ed = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3"

    val sha256ed =
        "9f86d081884c7d659a2fe" +
        "aa0c55ad015a3bf4f1b2b0" +
        "b822cd15d6c15b0f00a08"

    val crc32ed = "d87f7e0c"

    val bcrypted =
        "24326124313024755658345747674b" +
        "524749445643414b5941655a494f41" +
        "4b4f3468543759437053796167356a" +
        "2f5a365261377253334b636b796c32"


    "A Hash" should {

        import hasher.Implicits._

        "convert to a string implicitly" in {
            val hash: String = str.md5
            hash must_== md5ed
        }

        "convert to a byte array implicitly" in {
            val hash: Array[Byte] = str.sha1
            ok
        }
    }

    "On a String, the implicit hash methods" should {

        import hasher.Implicits._

        "MD5 hash" in { str.md5.hex must_== md5ed }
        "SHA-1 hash" in { str.sha1.hex must_== sha1ed }
        "SHA-256 hash" in { str.sha256.hex must_== sha256ed }
        "CRC32 hash" in { str.crc32.hex must_== crc32ed }
        "BCrypt hash" in {
            str.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

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

    "On a String, the implicit salt method" should {

        import hasher.Implicits._

        val saltedMd5 = "d615489ad65aad3f6138728a02221e95"

        "change the hash" in {
            str.salt("one").salt("two").md5.hex must_== saltedMd5
            (str.salt("one").salt("two") md5sTo saltedMd5) must beTrue
        }
    }

    "On a Byte Array, the implicit hash methods" should {

        import hasher.Implicits._

        "MD5 hash " in { bytes.md5.hex must_== md5ed }
        "SHA-1 hash" in { bytes.sha1.hex must_== sha1ed }
        "SHA-256 hash" in { bytes.sha256.hex must_== sha256ed }
        "BCrypt hash" in {
            bytes.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "On a Byte Array, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (bytes md5sTo md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (bytes sha1sTo sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (bytes sha256sTo sha256ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (bytes crc32sTo crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (bytes bcryptsTo bcrypted) must beTrue
        }
    }

    "On an Input Stream, the implicit hash methods" should {

        import hasher.Implicits._

        "MD5 hash " in { stream.md5.hex must_== md5ed }
        "SHA-1 hash" in { stream.sha1.hex must_== sha1ed }
        "SHA-256 hash" in { stream.sha256.hex must_== sha256ed }
        "BCrypt hash" in {
            stream.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "On an Input Stream, the implicit compare methods" should {

        import hasher.Implicits._

        "compare to an MD5 Hash" in { (stream md5sTo md5ed) must beTrue }
        "compare to a SHA1 Hash" in { (stream sha1sTo sha1ed) must beTrue }
        "compare to a SHA256 Hash" in {
            (stream sha256sTo sha256ed) must beTrue
        }
        "compare to a CRC32 Hash" in {
            (stream crc32sTo crc32ed) must beTrue
        }
        "compare to a BCrypt Hash" in {
            (stream bcryptsTo bcrypted) must beTrue
        }
    }

    "The static methods for strings" should {

        import hasher.Hasher

        "md5 hash" in { Hasher.md5( str ).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1( str ).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256( str ).hex must_== sha256ed }
        "crc32 hash" in { Hasher.crc32( str ).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(str).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "The static methods for byte arrays" should {

        import hasher.Hasher

        "md5 hash" in { Hasher.md5(bytes).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1(bytes).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256(bytes).hex must_== sha256ed }
        "crc32 hash" in { Hasher.crc32(bytes).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(bytes).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "The static methods for input streams" should {

        import hasher.Hasher

        "md5 hash" in { Hasher.md5(stream).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1(stream).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256(stream).hex must_== sha256ed }
        "crc32 hash" in { Hasher.crc32(stream).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(stream).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "For large streams, the static methods for input streams" should {

        import hasher.Hasher

        def large = new ByteArrayInputStream( Array.fill(20000)(65.byteValue) )

        "md5 hash" in {
            Hasher.md5(large).hex must_== "0af181fb57f1eefb62b74081bbddb155"
        }

        "sha1 hash" in {
            Hasher.sha1(large).hex must_==
                "b9624c14586d4668cba0b2759229a49f1ea355b6"
        }

        "sha256 hash" in {
            Hasher.sha256(large).hex must_==
                "c86f210e0efad769d6ade6f924a85200" +
                "be38917fa99e33b360aa24535716359b"
        }

        "crc32 hash" in { Hasher.crc32(large).hex must_== "d89a101b" }

        "BCrypt hash" in {
            Hasher.bcrypt(large).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

}

