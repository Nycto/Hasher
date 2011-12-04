package hasher

import java.io.InputStream


/**
 * Represents a partially applied hash
 */
class Algo ( private val digest: Digest.Builder ) {

    /**
     * Generates a hash of a string
     */
    def apply ( value: String ): Hash
        = new PlainTextBytes(value).hash( digest )

    /**
     * Generates a hash of a byte array
     */
    def apply ( value: Array[Byte] ): Hash
        = new PlainTextBytes(value).hash( digest )

    /**
     * Generates a hash of an input stream
     */
    def apply ( value: InputStream ): Hash
        = new PlainTextStream(value).hash( digest )

}

