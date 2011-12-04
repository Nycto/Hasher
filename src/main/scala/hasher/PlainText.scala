package hasher

import java.io.InputStream

/**
 * The base class for plain text representations
 */
trait PlainText {

    /**
     * Populates the digest
     */
    protected[hasher] def fill ( digest: Digest ): Digest

    /**
     * Hashes an InputStream according to this algorithm.
     */
    def hash ( digest: Digest.Builder ): Hash = fill( digest() ).hash

    /**
     * Determines whether this value computes to a given hash
     */
    def hashesTo ( digest: Digest.Builder, vs: Hash ): Boolean
        = fill( digest() ).hashesTo( vs )

    /**
     * Determines whether this value computes to a given hash string
     */
    def hashesTo ( digest: Digest.Builder, vs: String ): Boolean = {
        try { hashesTo( digest, Hash(vs) ) }
        catch { case _:IllegalArgumentException => false }
    }

}

/**
 * A plain text representation of a Byte Array
 */
private class PlainTextBytes (
    private val value: Array[Byte]
) extends PlainText {

    /**
     * Creates an instance from a string
     */
    def this ( value: String ) = this( value.getBytes )

    /** {@inheritDoc} */
    override protected[hasher] def fill ( digest: Digest ): Digest
        = digest.add( value, value.length )

}

/**
 * A plain text representation of an Input Stream
 */
private class PlainTextStream (
    private val stream: InputStream
) extends PlainText {

    /** {@inheritDoc} */
    override protected[hasher] def fill ( digest: Digest ): Digest = {
        val buffer = new Array[Byte](8192)

        def next: Unit = {
            val read = stream.read(buffer)
            if ( read > 0 ) {
                digest.add( buffer, read )
                next
            }
        }

        try next
        finally stream.close

        digest
    }

}

/**
 * A PlainText wrapper that adds a salt
 */
private class PlainTextSalt (
    private val inner: PlainText, private val salt: Array[Byte]
) extends PlainText {

    /** {@inheritDoc} */
    override protected[hasher] def fill ( digest: Digest ): Digest = {
        digest.add( salt, salt.length )
        inner.fill( digest )
    }

}

