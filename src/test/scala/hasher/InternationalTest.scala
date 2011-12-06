package test.scala.hasher

import org.specs2.mutable._

import java.io.ByteArrayInputStream
import java.io.StringReader

import scala.io.Source

import hasher.Hasher


class InternationalTest extends Specification {

    // test data
    val str = "Iñtërnâtiônàlizætiøn"
    val bytes = str.getBytes
    val builder: StringBuilder = new StringBuilder(str)
    def stream = new ByteArrayInputStream( bytes )
    def reader = new StringReader( str )
    def source = Source.fromBytes( bytes )


    // pre-hashed values
    val md5ed = "e5e628206e73b1ae69b37fc69762a1e1"
    val sha1ed = "4b9c5d2fa4c83f7561787eb4e5f7f06a2cd47425"
    val sha256ed =
        "96dcd7d2a476d7366d71ee7119defbb0" +
        "db845c6f706ed34c1ac4b2c485150636"
    val sha384ed =
        "ca4adc0333351c78f293a97a5b774abf4aa4792bfca6eb2d" +
        "8eb311cdf2ced5a260f50a7b55daa40576762251e7bcc502"
    val sha512ed =
        "0c1d542db80b28e48451417181cedbc98419f029dfbaac68" +
        "6b19b47449208c4e0e6038e159f316ef6cd33c1142cd5012" +
        "eea4004ebf45631544ea7bcf5046543a"
    val crc32ed = "07b5088e"


    "International values in Strings" should {
        "md5 hash" in { Hasher.md5(str).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1(str).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256(str).hex must_== sha256ed }
        "sha384 hash" in { Hasher.sha384(str).hex must_== sha384ed }
        "sha512 hash" in { Hasher.sha512(str).hex must_== sha512ed }
        "crc32 hash" in { Hasher.crc32(str).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(str).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "International values in StringBuilders" should {
        "md5 hash" in { Hasher.md5(builder).hex must_== md5ed }
        "sha1 hash" in { Hasher.sha1(builder).hex must_== sha1ed }
        "sha256 hash" in { Hasher.sha256(builder).hex must_== sha256ed }
        "sha384 hash" in { Hasher.sha384(builder).hex must_== sha384ed }
        "sha512 hash" in { Hasher.sha512(builder).hex must_== sha512ed }
        "crc32 hash" in { Hasher.crc32(builder).hex must_== crc32ed }
        "BCrypt hash" in {
            Hasher.bcrypt(builder).hex must beMatching("^[a-zA-Z0-9]{120}$")
        }
    }

    "International values in Byte Arrays" should {
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

    "International values in InputStreams" should {
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

    "International values in Readers" should {
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

    "International values in Sources" should {
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

