package test.roundeights.hasher

import play.api.libs.iteratee.Enumerator
import org.specs2.mutable._

import com.roundeights.hasher.{Foldable, Algo}

import scala.concurrent.Future

class FoldableTest extends Specification {

    "A Foldable hash" should {

        "Allow values to be accumulated over an iterator" in {
            List("three", "blind", "mice")
                .foldLeft( Algo.sha1.foldable ){ (accum, str) => accum(str) }
                .done
                .hex must_== "f7d267bba7ec5b305f71cea8dddaa6dc4389ee22"
        }

        "Calling a foldable more than once should cause an exception" in {
            val foldable = Algo.sha1.foldable
            foldable("should work")
            foldable("wont work work") must throwA[IllegalStateException]
            foldable("wont work work") must throwA[IllegalStateException]
            foldable("wont work work") must throwA[IllegalStateException]
        }

        "Calling 'done' should finalize a foldable" in {
            val foldable = Algo.sha1.foldable("should work")
            foldable.done.hex must_== "7084691b7d2f766fa2e34b97ce0eef26f6bf2698"
            foldable("wont work work") must throwA[IllegalStateException]
            foldable("wont work work") must throwA[IllegalStateException]
            foldable("wont work work") must throwA[IllegalStateException]
        }

        "Produce different hashes" in {
            import play.api.libs.iteratee.Enumerator

            val secret = "secret"

            val xHash = iterate(Enumerator("x".getBytes), Algo.hmac(secret).sha512)
            xHash must be_==("1561e37533789d0537b20d30cb154a781b24886d4af7137ac176f1e4aa4d2018d3863ebc8f8be8ea7c4f9e3df8f0c45c11bb981d88a169b0e0366db5b4c796e5").await()

            val zHash = iterate(Enumerator("z".getBytes), Algo.hmac(secret).sha512)
            zHash must be_==("29ea91923716f40af70053e294b1dbb568ad5b08a496f7fc631a1bf80bdf2bbdd00e3f2424347e85476f36efbe3afbd08da4782c45a3d9ebfa7ecb3e00cee65b").await()

            xHash must be_!=(zHash).await
        }
    }

    def iterate(e : Enumerator[Array[Byte]], algo : Algo) : Future[String] = {
        import play.api.libs.iteratee.Iteratee
        e.run(
            Iteratee.fold[Array[Byte], Foldable](algo.foldable) {
                (state, bytes) => state(bytes)
            }.map(_.done.hex)
        )
    }
}

