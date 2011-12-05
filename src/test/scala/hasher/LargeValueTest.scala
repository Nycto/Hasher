package test.scala.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream
import java.io.StringReader

import scala.io.Source

class LargeValueTest extends Specification {

    // test data
    val bytes =  Array.fill(20000)(65.byteValue)
    def stream = new ByteArrayInputStream( bytes )
    def reader = new StringReader( new String( bytes ) )
    def source = Source.fromBytes( bytes )

    // pre-hashed values
    val md5ed = "0af181fb57f1eefb62b74081bbddb155"
    val sha1ed = "b9624c14586d4668cba0b2759229a49f1ea355b6"
    val sha256ed =
        "c86f210e0efad769d6ade6f924a85200" +
        "be38917fa99e33b360aa24535716359b"
    val sha384ed =
        "b0d8bf15ba996e77281796e899cc412f0dc2ae6b62a1aabd" +
        "a848eecd7c2fef607167efac88f8a4e57c65e43d9751117a"
    val sha512ed =
        "c107c730231b39a54c16256a6d88511703bf2866bd32f945" +
        "ff8d13590fee60d2924e45a820f052900d7a86ca1951495d" +
        "20fd0d48fa9539b4c72fbd0fc4cd51d8"
    val crc32ed = "d89a101b"


    "For large streams, the static methods for input streams" should {

        import hasher.Hasher

        "md5 hash" in { Hasher.md5(stream).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1(stream).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256(stream).hex must_== sha256ed }
        "sha384 hash" in { Hasher.sha384(stream).hex must_== sha384ed }
        "sha512 hash" in { Hasher.sha512(stream).hex must_== sha512ed }
        "crc32 hash" in { Hasher.crc32(stream).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(stream).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }

    }

    "For large values, the static methods for Readers" should {

        import hasher.Hasher

        "md5 hash" in { Hasher.md5(reader).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1(reader).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256(reader).hex must_== sha256ed }
        "sha384 hash" in { Hasher.sha384(reader).hex must_== sha384ed }
        "sha512 hash" in { Hasher.sha512(reader).hex must_== sha512ed }
        "crc32 hash" in { Hasher.crc32(reader).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(reader).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }

    }

    "For large values, the static methods for Sources" should {

        import hasher.Hasher

        "md5 hash" in { Hasher.md5(source).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1(source).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256(source).hex must_== sha256ed }
        "sha384 hash" in { Hasher.sha384(source).hex must_== sha384ed }
        "sha512 hash" in { Hasher.sha512(source).hex must_== sha512ed }
        "crc32 hash" in { Hasher.crc32(source).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(source).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }

    }

}

