package test.roundeights.hasher

import org.specs2.mutable._

import com.roundeights.hasher.Implicits._
import com.roundeights.hasher.Hash

class HashTest extends Specification {

    // The data being hashed
    val str: String = "test"

    // These values represent the string "test" as various hashes
    val md5ed = "098f6bcd4621d373cade4e832627b4f6"

    "A Hash" should {

        "convert to a string implicitly" in {
            val hash: String = str.md5.hash
            hash must_== md5ed
        }

        "convert to a byte array implicitly" in {
            val hash: Array[Byte] = str.sha1.hash
            ok
        }

        "Support equality" in {
            Hash(md5ed).equals(md5ed) must beTrue
            Hash(md5ed).equals("ABC123") must beFalse

            Hash(md5ed).equals( Hash(md5ed) ) must beTrue
            Hash(md5ed).equals( Hash("ABC123") ) must beFalse

            Hash(md5ed).equals( Hash(md5ed).bytes ) must beTrue
            Hash(md5ed).equals( Hash("ABC123").bytes ) must beFalse
        }

        "initialize from a hex string of even length" in {
            Hash("98f6bcd4621d373cade4e832627b4f6").bytes mustEqual Hash(md5ed).bytes
        }
    }

}

