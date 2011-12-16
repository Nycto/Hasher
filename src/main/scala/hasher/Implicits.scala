package com.roundeights.hasher

import java.io.InputStream
import java.io.Reader

import scala.io.Source


/**
 * A list of implicit conversion methods
 */
object Implicits {

    /**
     * Implicitly creates a hasher from a string.
     */
    implicit def stringToHasher ( from: String ): Hasher = Hasher(from)

    /**
     * Implicitly creates a hasher from a StringBuilder.
     */
    implicit def stringBuilderToHasher ( from: StringBuilder ): Hasher
        = Hasher(from)

    /**
     * Implicitly creates a hasher from a byte array.
     */
    implicit def byteArrayToHasher ( from: Array[Byte] ): Hasher = Hasher(from)

    /**
     * Implicitly creates a hasher from an Input Stream
     */
    implicit def streamToHasher ( from: InputStream ): Hasher = Hasher(from)

    /**
     * Implicitly creates a hasher from a Reader
     */
    implicit def readerToHasher ( from: Reader ): Hasher = Hasher(from)

    /**
     * Implicitly creates a hasher from a Source
     */
    implicit def sourceToHasher ( from: Source ): Hasher = Hasher(from)

}

