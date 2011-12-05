package test.scala.hasher

import org.specs2.mutable._

class SaltTest extends Specification {

    val str: String = "test"
    val md5ed = "d615489ad65aad3f6138728a02221e95"

    "On a String, the implicit salt method" should {

        import hasher.Implicits._

        "change the hash" in {
            str.salt("one").salt("two").md5.hex must_== md5ed
            (str.salt("one").salt("two") md5= md5ed) must beTrue
        }
    }

}

