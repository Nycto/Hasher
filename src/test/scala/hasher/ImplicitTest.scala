package test.scala.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream
import java.io.StringReader

class ImplicitTest extends Specification {


    // The data being hashed
    val str: String = "test"
    val builder: StringBuilder = new StringBuilder(str)
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


    "On a String, the implicit hash methods" should {

        import hasher.Implicits._

        "MD5 hash" in { str.md5.hex must_== md5ed }
        "SHA-1 hash" in { str.sha1.hex must_== sha1ed }
        "SHA-256 hash" in { str.sha256.hex must_== sha256ed }
        "SHA-384 hash" in { str.sha384.hex must_== sha384ed }
        "SHA-512 hash" in { str.sha512.hex must_== sha512ed }
        "CRC32 hash" in { str.crc32.hex must_== crc32ed }
        "BCrypt hash" in {
            str.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "On a StringBuilder, the implicit hash methods" should {

        import hasher.Implicits._

        "MD5 hash" in { builder.md5.hex must_== md5ed }
        "SHA-1 hash" in { builder.sha1.hex must_== sha1ed }
        "SHA-256 hash" in { builder.sha256.hex must_== sha256ed }
        "SHA-384 hash" in { builder.sha384.hex must_== sha384ed }
        "SHA-512 hash" in { builder.sha512.hex must_== sha512ed }
        "CRC32 hash" in { builder.crc32.hex must_== crc32ed }
        "BCrypt hash" in {
            builder.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "On a Byte Array, the implicit hash methods" should {

        import hasher.Implicits._

        "MD5 hash " in { bytes.md5.hex must_== md5ed }
        "SHA-1 hash" in { bytes.sha1.hex must_== sha1ed }
        "SHA-256 hash" in { bytes.sha256.hex must_== sha256ed }
        "SHA-384 hash" in { bytes.sha384.hex must_== sha384ed }
        "SHA-512 hash" in { bytes.sha512.hex must_== sha512ed }
        "CRC32 hash" in { bytes.crc32.hex must_== crc32ed}
        "BCrypt hash" in {
            bytes.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "On an Input Stream, the implicit hash methods" should {

        import hasher.Implicits._

        "MD5 hash " in { stream.md5.hex must_== md5ed }
        "SHA-1 hash" in { stream.sha1.hex must_== sha1ed }
        "SHA-256 hash" in { stream.sha256.hex must_== sha256ed }
        "SHA-384 hash" in { stream.sha384.hex must_== sha384ed }
        "SHA-512 hash" in { stream.sha512.hex must_== sha512ed }
        "CRC32 hash" in { stream.crc32.hex must_== crc32ed }
        "BCrypt hash" in {
            stream.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "On a Reader, the implicit hash methods" should {

        import hasher.Implicits._

        "MD5 hash " in { reader.md5.hex must_== md5ed }
        "SHA-1 hash" in { reader.sha1.hex must_== sha1ed }
        "SHA-256 hash" in { reader.sha256.hex must_== sha256ed }
        "SHA-384 hash" in { reader.sha384.hex must_== sha384ed }
        "SHA-512 hash" in { reader.sha512.hex must_== sha512ed }
        "CRC32 hash" in { reader.crc32.hex must_== crc32ed }
        "BCrypt hash" in {
            reader.bcrypt.hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }


}

