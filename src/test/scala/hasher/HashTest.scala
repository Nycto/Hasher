package test.scala.hasher

import org.specs2.mutable._

class HashTest extends Specification {

    // The data being hashed
    val str: String = "test"

    // These values represent the string "test" as various hashes
    val md5ed = "098f6bcd4621d373cade4e832627b4f6"

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

}

