package hasher

import java.io.InputStream
import java.io.Reader

import scala.io.Source


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
     * Generates a hash of a string
     */
    def apply ( value: StringBuilder ): Hash
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
        = new PlainTextResource(value).hash( digest )

    /**
     * Generates a hash of a Reader
     */
    def apply ( value: Reader ): Hash
        = new PlainTextResource(value).hash( digest )

    /**
     * Generates a hash of a Source
     */
    def apply ( value: Source ): Hash
        = new PlainTextSource(value).hash( digest )


}

