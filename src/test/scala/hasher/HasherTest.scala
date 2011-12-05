package test.scala.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream
import java.io.StringReader

import scala.io.Source

class HasherTest extends Specification {


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


    "The static methods for strings" should {

        import hasher.Hasher

        "md5 hash" in { Hasher.md5( str ).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1( str ).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256( str ).hex must_== sha256ed }
        "sha384 hash" in { Hasher.sha384( str ).hex must_== sha384ed }
        "sha256 hash" in { Hasher.sha256( str ).hex must_== sha256ed }
        "sha512 hash" in { Hasher.sha512( str ).hex must_== sha512ed }
        "crc32 hash" in { Hasher.crc32( str ).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(str).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "The static methods for StringBuilders" should {

        import hasher.Hasher

        "md5 hash" in { Hasher.md5( builder ).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1( builder ).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256( builder ).hex must_== sha256ed }
        "sha384 hash" in { Hasher.sha384( builder ).hex must_== sha384ed }
        "sha256 hash" in { Hasher.sha256( builder ).hex must_== sha256ed }
        "sha512 hash" in { Hasher.sha512( builder ).hex must_== sha512ed }
        "crc32 hash" in { Hasher.crc32( builder ).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(builder).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "The static methods for byte arrays" should {

        import hasher.Hasher

        "md5 hash" in { Hasher.md5(bytes).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1(bytes).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256(bytes).hex must_== sha256ed }
        "sha384 hash" in { Hasher.sha384(bytes).hex must_== sha384ed }
        "sha512 hash" in { Hasher.sha512(bytes).hex must_== sha512ed }
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
        "sha384 hash" in { Hasher.sha384(stream).hex must_== sha384ed }
        "sha512 hash" in { Hasher.sha512(stream).hex must_== sha512ed }
        "crc32 hash" in { Hasher.crc32(stream).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(stream).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "The static methods for Readers" should {

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

    "The static methods for Sources" should {

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

