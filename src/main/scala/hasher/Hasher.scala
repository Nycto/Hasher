package com.roundeights.hasher

import java.io.InputStream
import java.io.Reader

import scala.io.Source


/**
 * Helper methods for generating hashes
 */
object Hasher extends WithPlainText[Hasher] {

    /** {@inheritDoc} */
    def apply ( value: PlainText ): Hasher = new Hasher(value)
}

/**
 * A helper for generating crypto hashes from a value
 */
class Hasher private (
    private val value: PlainText
) extends WithAlgo[Digest] {

    /**
     * Adds a salt from an array of bytes
     */
    def salt ( saltValue: Array[Byte] ): Hasher
        = Hasher( new PlainTextSalt(value, saltValue) )

    /**
     * Adds a salt from a string
     */
    def salt ( saltValue: String ): Hasher = salt( saltValue.getBytes )

    /** {@inheritDoc} */
    override protected def withAlgo ( algo: Algo ): Digest
        = value.fill( algo.digest )
}


