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
            Hasher( data.str ).hmacMd5("secret").hex must_== data.hmacMd5ed
        }
        "hmacSha1 hash" in {
            Hasher( data.str ).hmacSha1("secret").hex must_== data.hmacSha1ed
        }
        "hmacSha256 hash" in {
            Hasher( data.str ).hmacSha256("secret").hex must_==
                data.hmacSha256ed
        }
        "crc32 hash" in {
            Hasher( data.str ).crc32.hex must_== data.crc32ed
        }
        "BCrypt hash" in {
            Hasher(data.str).bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

}

