package test.roundeights.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream
import java.io.StringReader
import scala.io.Source

class ImplicitTest extends Specification {

    val data = TestData.test

    "Implicit converstion to a Hasher" should {

        import com.roundeights.hasher.Implicits._
        import com.roundeights.hasher.Hasher

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

        import com.roundeights.hasher.Implicits._

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
        "HMAC-MD5 hash" in {
            data.str.hmac("secret").md5.hex must_== data.hmacMd5ed
        }
        "HMAC-SHA1 hash" in {
            data.str.hmac("secret").sha1.hex must_== data.hmacSha1ed
        }
        "HMAC-SHA256 hash" in {
            data.str.hmac("secret").sha256.hex must_== data.hmacSha256ed
        }
        "CRC32 hash" in {
            data.str.crc32.hex must_== data.crc32ed
        }
        "BCrypt hash" in {
            data.str.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
        "BCrypt hash with a round count" in {
            data.str.bcrypt(12).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "The implicit compare methods" should {

        import com.roundeights.hasher.Implicits._

        "be comparable to an MD5 Hash" in {
            (data.str.md5 hash= data.md5ed) must beTrue
            (data.str.md5 hash= "AHashThatIsWrong") must beFalse
            (data.str.md5 hash= "SomeHashThatIsWrong") must beFalse
            (data.str.md5 hash= "") must beFalse
            (data.str.md5 hash= ("other".md5)) must beFalse
        }

        "be comparable to a SHA1 Hash" in {
            (data.str.sha1 hash= data.sha1ed) must beTrue
            (data.str.sha1 hash= "AHashThatIsWrong") must beFalse
            (data.str.sha1 hash= "SomeHashThatIsWrong") must beFalse
            (data.str.sha1 hash= "") must beFalse
            (data.str.sha1 hash= ("other".sha1)) must beFalse
        }

        "be comparable to a SHA256 Hash" in {
            (data.str.sha256 hash= data.sha256ed) must beTrue
            (data.str.sha256 hash= "AHashThatIsWrong") must beFalse
            (data.str.sha256 hash= "SomeHashThatIsWrong") must beFalse
            (data.str.sha256 hash= "") must beFalse
            (data.str.sha256 hash= ("other".sha256)) must beFalse
        }

        "be comparable to a SHA384 Hash" in {
            (data.str.sha384 hash= data.sha384ed) must beTrue
            (data.str.sha384 hash= "AHashThatIsWrong") must beFalse
            (data.str.sha384 hash= "SomeHashThatIsWrong") must beFalse
            (data.str.sha384 hash= "") must beFalse
            (data.str.sha384 hash= ("other".sha384)) must beFalse
        }

        "be comparable to a SHA512 Hash" in {
            (data.str.sha512 hash= data.sha512ed) must beTrue
            (data.str.sha512 hash= "AHashThatIsWrong") must beFalse
            (data.str.sha512 hash= "SomeHashThatIsWrong") must beFalse
            (data.str.sha512 hash= "") must beFalse
            (data.str.sha512 hash= ("other".sha512)) must beFalse
        }

        "be comparable to a CRC32 Hash" in {
            (data.str.crc32 hash= data.crc32ed) must beTrue
            (data.str.crc32 hash= "AHashThatIsWrong") must beFalse
            (data.str.crc32 hash= "SomeHashThatIsWrong") must beFalse
            (data.str.crc32 hash= "") must beFalse
            (data.str.crc32 hash= ("other".crc32)) must beFalse
        }

        "be comparable to a BCrypt Hash" in {
            (data.str.bcrypt hash= data.bcrypted) must beTrue
            (data.str.bcrypt hash= "AHashThatIsWrong") must beFalse
            (data.str.bcrypt hash= "SomeHashThatIsWrong") must beFalse
            (data.str.bcrypt hash= "") must beFalse
            (data.str.bcrypt hash= ("other".bcrypt)) must beFalse
        }

        "be comparable to a BCrypt Hash with a round count" in {
            (data.str.bcrypt(12) hash= data.bcrypted12) must beTrue
            (data.str.bcrypt(12) hash= "AHashThatIsWrong") must beFalse
            (data.str.bcrypt(12) hash= "SomeHashThatIsWrong") must beFalse
            (data.str.bcrypt(12) hash= "") must beFalse
            (data.str.bcrypt(12) hash= ("other".bcrypt(12))) must beFalse
        }
    }

    "The implicit salt method" should {

        import com.roundeights.hasher.Implicits._

        val str: String = "test"
        val md5ed = "d615489ad65aad3f6138728a02221e95"

        "change the hash" in {
            str.salt("one").salt("two").md5.hex must_== md5ed
            (str.salt("one").salt("two").md5 hash= md5ed) must beTrue
        }
    }

}

