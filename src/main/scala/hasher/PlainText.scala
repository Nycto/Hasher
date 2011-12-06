package hasher

import scala.annotation.tailrec
import scala.io.Source

import java.io.InputStream
import java.io.Reader

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

    /**
     * Creates an instance from a StringBuilder
     */
    def this ( value: StringBuilder ) = this( value.toString )

    /** {@inheritDoc} */
    override protected[hasher] def fill ( digest: Digest ): Digest
        = digest.add( value, value.length )

}

/**
 * A plain text representation of a Reader
 */
private class PlainTextResource (
    private val resource: {
        def read( bytes: Array[Byte] ): Int
        def close: Unit
    }
) extends PlainText {

    /**
     * Constructor for creating a resource from an InputStream
     */
    def this ( stream: InputStream ) = this( new {
        def read( bytes: Array[Byte] ): Int = stream.read(bytes)
        def close: Unit = stream.close
    })

    /**
     * Constructor for creating a resource from a Reader
     */
    def this ( reader: Reader ) = this( new {
        private val buffer = new Array[Char](1024)
        def read( bytes: Array[Byte] ): Int = buffer.synchronized {

            // Readers operate on Chars, but we need bytes. So, we buffer
            // the Char array then convert each value to a byte
            val read = reader.read( buffer )
            ( read  <= 0 ) match {
                case true => -1
                case false => {
                    val readBytes = new String( buffer, 0, read ).getBytes
                    Array.copy( readBytes, 0, bytes, 0, readBytes.length )
                    readBytes.length
                }
            }
        }
        def close: Unit = reader.close
    })

    /** {@inheritDoc} */
    override protected[hasher] def fill ( digest: Digest ): Digest = {
        val buffer = new Array[Byte](8192)

        @tailrec def next: Unit = {
            val read = resource.read(buffer)
            if ( read > 0 ) {
                digest.add( buffer, read )
                next
            }
        }

        try next
        finally resource.close

        digest
    }

}

/**
 * Provides a plain text interface for Source objects
 */
private class PlainTextSource ( private val source: Source ) extends PlainText {

    /** {@inheritDoc} */
    override protected[hasher] def fill ( digest: Digest ): Digest = {
        source.grouped(8192).foreach { group =>
            val bytes = new String( group.toArray ).getBytes
            digest.add( bytes, bytes.length )
        }
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

