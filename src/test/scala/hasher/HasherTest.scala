package test.scala.hasher

import org.specs2.mutable._

import hasher.Hasher._

class HasherTest extends Specification {

    "A String" should {

        "be MD5 hashable " in {
            "test".md5 must_== "098f6bcd4621d373cade4e832627b4f6"
        }

        "be SHA-1 hashable " in {
            "test".sha1 must_== "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3"
        }

        "be SHA-256 hashable " in {
            "test".sha256 must_==
                "9f86d081884c7d659a2fe" +
                "aa0c55ad015a3bf4f1b2b0" +
                "b822cd15d6c15b0f00a08"
        }

    }

}

