package test.roundeights.hasher

import org.specs2.mutable._

class HasherTest extends Specification {

    val data = TestData.test

    "The static Hasher methods" should {

        import com.roundeights.hasher.Hasher

        "md5 hash" in {
            Hasher( data.str ).md5.hex must_== data.md5ed
        }
        "sha1 hash" in {
            Hasher( data.str ).sha1.hex must_== data.sha1ed
        }
        "sha256 hash" in {
            Hasher( data.str ).sha256.hex must_== data.sha256ed
        }
        "sha384 hash" in {
            Hasher( data.str ).sha384.hex must_== data.sha384ed
        }
        "sha256 hash" in {
            Hasher( data.str ).sha256.hex must_== data.sha256ed
        }
        "sha512 hash" in {
            Hasher( data.str ).sha512.hex must_== data.sha512ed
        }
        "hmacMd5 hash" in {
            Hasher( data.str ).hmac("secret").md5.hex must_== data.hmacMd5ed
        }
        "hmacSha1 hash" in {
            Hasher( data.str ).hmac("secret").sha1.hex must_== data.hmacSha1ed
        }
        "hmacSha256 hash" in {
            Hasher( data.str ).hmac("secret").sha256.hex must_==
                data.hmacSha256ed
        }
        "crc32 hash" in {
            Hasher( data.str ).crc32.hex must_== data.crc32ed
        }
        "pbkdf2 hash" in {
            Hasher( data.str ).pbkdf2("secret", 1000, 128).hex must_==
                data.pbkdf2ed.get
        }
        "BCrypt hash" in {
            Hasher(data.str).bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
        "BCrypt hash with round count" in {
            Hasher(data.str).bcrypt(12).hex must
                beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

}

