package test.scala.hasher

import org.specs2.mutable._

class HasherTest extends Specification {


    // The data being hashed
    val str: String = "test"
    val bytes: Array[Byte] = str.getBytes


    // These values represent the string "test" as various hashes
    val md5ed = "098f6bcd4621d373cade4e832627b4f6"
    val md5Salted = "dc5e57cb22c65a6ab3fc057313f7a2c1"

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

        "be saltable" in {
            str.salt("sweet").md5.hex must_== md5Salted
            ( str.salt("sweet") md5sTo md5Salted ) must beTrue
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

        "be saltable" in { bytes.salt("sweet").md5.hex must_== md5Salted }
    }

    "The static methods" should {

        import hasher.Hasher

        "md5 hash strings" in { Hasher.md5( str ).hex must_== md5ed }
        "sha1 hash strings" in { Hasher.sha1( str ).hex must_== sha1ed }
        "sha256 hash strings" in { Hasher.sha256( str ).hex must_== sha256ed }
        "crc32 hash strings" in { Hasher.crc32( str ).hex must_== crc32ed }
        "BCrypt hash strings" in {
            Hasher.bcrypt(str).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }

        "md5 hash byte arrays" in { Hasher.md5(bytes).hex must_== md5ed }
        "sha1 hash byte arrays" in { Hasher.sha1(bytes).hex must_== sha1ed }
        "sha256 hash byte arrays" in {
            Hasher.sha256(bytes).hex must_== sha256ed
        }
        "crc32 hash byte arrays" in { Hasher.crc32(bytes).hex must_== crc32ed }
        "BCrypt hash byte arrays" in {
            Hasher.bcrypt(bytes).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

}

