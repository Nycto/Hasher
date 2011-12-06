package test.scala.hasher

import org.specs2.mutable._

class HasherTest extends Specification {

    val data = TestData.test

    "The static Hasher methods" should {

        import hasher.Hasher

        "md5 hash" in {
            Hasher.md5( data.str ).hex must_== data.md5ed
        }
        "sha1 hash" in {
            Hasher.sha1( data.str ).hex must_== data.sha1ed
        }
        "sha256 hash" in {
            Hasher.sha256( data.str ).hex must_== data.sha256ed
        }
        "sha384 hash" in {
            Hasher.sha384( data.str ).hex must_== data.sha384ed
        }
        "sha256 hash" in {
            Hasher.sha256( data.str ).hex must_== data.sha256ed
        }
        "sha512 hash" in {
            Hasher.sha512( data.str ).hex must_== data.sha512ed
        }
        "crc32 hash" in {
            Hasher.crc32( data.str ).hex must_== data.crc32ed
        }
        "BCrypt hash" in {
            Hasher.bcrypt(data.str).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

}

