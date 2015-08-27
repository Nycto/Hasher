package com.roundeights.hasher

/**
 * A class designed for accumulating the values in a hash using a fold.
 *
 * This class attempts to enforce ordered consumption by rendering each instance
 * unusable the first time you add a new value. Adding a new value then
 * returns a new instance, which you must use for adding more data.
 *
 * Presenting an API like this allows for more efficient code, while still
 * maintaining an outwardly immutable API. It also helps prevent race conditions
 * by ensuring continued forward motion.
 */
class Foldable private[hasher] (
    private val digest: MutableDigest
) extends WithPlainText[Foldable] {

    /** Becomes true after data is added to this foldable */
    private var consumed = false

    /** Validates that this object has not yet been consumed */
    private def checkConsumption = synchronized {
        if ( consumed ) {
            throw new IllegalStateException(
                "Foldable has already been consumed")
        }
        consumed = true
    }

    /** Add a value to this fodable */
    override def apply ( value: PlainText ): Foldable = synchronized {
        checkConsumption
        value.fill(digest)
        new Foldable(digest)
    }

    /** Mark that everything has been added to this foldable. */
    def done: Digest = {
        checkConsumption
        digest
    }
}
