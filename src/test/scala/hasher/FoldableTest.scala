package test.roundeights.hasher

import org.specs2.mutable._

import com.roundeights.hasher.Algo

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
    }
}

