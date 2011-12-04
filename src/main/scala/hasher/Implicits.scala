package hasher

import java.io.InputStream


/**
 * A list of implicit conversion methods
 */
object Implicits {

    /**
     * Implicitly creates a hasher from a string.
     */
    implicit def stringToHasher ( from: String ): Hasher = Hasher(from)

    /**
     * Implicitly creates a hasher from a byte array.
     */
    implicit def byteArrayToHasher ( from: Array[Byte] ): Hasher = Hasher(from)

    /**
     * Implicitly creates a hasher from an Input Stream
     */
    implicit def streamToHasher ( from: InputStream ): Hasher = Hasher(from)

    /**
     * Implicitly converts from a hash to a string
     */
    implicit def hashToString ( from: Hash ): String = from.hex

    /**
     * Implicitly converts from a hash to a byte array
     */
    implicit def hashToByteArray ( from: Hash ): Array[Byte] = from.bytes

}

