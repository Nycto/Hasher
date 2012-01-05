package com.roundeights.hasher

import java.io.InputStream
import java.io.Reader

/**
 * A Tap is a decorator that wraps a stream of some sort, generating
 * a hash as the data passes through it
 */
trait Tap {

    /**
     * Calculates the hash of the collected bytes so far
     */
    def hash: Hash

    /**
     * Determines whether the collected bytes compute to a given hash
     */
    def `hash_=` ( vs: Hash ): Boolean

    /**
     * Determines whether the collected bytes compute to a given hash
     */
    def `hash_=` ( vs: String ): Boolean = hash_=( Hash(vs) )

}

/**
 * A tap that buffers the characters before writing to the digest
 */
trait BufferedTap extends Tap {

    import scala.collection.mutable.ArrayBuffer

    /**
     * The digest to write to
     */
    protected def digest: Digest

    /**
     * The buffered data
     */
    private val buffer = new ArrayBuffer[Byte]

    /**
     * Flushes the buffer to the digest
     */
    private def flush: Unit = {
        if ( buffer.size > 0 ) {
            val data = buffer.clone.toArray
            digest.add( data, data.size )
            buffer.trimStart( data.size )
        }
    }

    /**
     * Adds a byte to the digest
     */
    protected def addByteToDigest( byte: Byte ): Byte = {
        buffer += byte
        if ( buffer.size >= 1024 ) flush
        byte
    }

    /** {@inheritDoc} */
    override def hash: Hash = {
        flush
        digest.hash
    }

    /** {@inheritDoc} */
    override def `hash_=` ( vs: Hash ): Boolean = {
        flush
        digest.hashesTo( vs )
    }

}

/**
 * An InputStream that generates a hash
 */
class InputStreamTap (
    protected val digest: Digest,
    private val stream: InputStream
) extends InputStream with BufferedTap with Tap {

    /** {@inheritDoc} */
    override def read: Int = {
        val byte = stream.read
        if ( byte >= 0 ) addByteToDigest( byte.toByte )
        byte
    }

    /** {@inheritDoc} */
    override def available = stream.available

    /** {@inheritDoc} */
    override def close = stream.close

    /** {@inheritDoc} */
    override def markSupported = false

    /** {@inheritDoc} */
    override def mark(readlimit: Int) = throw new UnsupportedOperationException

    /** {@inheritDoc} */
    override def reset = throw new UnsupportedOperationException

}

/**
 * A Reader that generates a hash
 */
class ReaderTap (
    private val digest: Digest,
    private val reader: Reader
) extends Reader with Tap {

    import scala.annotation.tailrec

    /** {@inheritDoc} */
    def hash: Hash = digest.hash

    /** {@inheritDoc} */
    def `hash_=` ( vs: Hash ): Boolean = digest.hashesTo( vs )

    /** {@inheritDoc} */
    override def read( cbuf: Array[Char], off: Int, len: Int ): Int = {
        val read = reader.read( cbuf, off, len )

        if ( read > 0 ) {
            // Readers operate on characters, but we need bytes
            val bytes = new String( cbuf, off, read ).getBytes
            digest.add( bytes, bytes.length )
        }

        read
    }

    /** {@inheritDoc} */
    override def ready = reader.ready

    /** {@inheritDoc} */
    override def close = reader.close

    /** {@inheritDoc} */
    override def markSupported = false

    /** {@inheritDoc} */
    override def mark(readlimit: Int) = throw new UnsupportedOperationException

    /** {@inheritDoc} */
    override def reset = throw new UnsupportedOperationException

    /**
     * Converts this reader to a string
     */
    def mkString: String = {

        val result = new StringBuilder
        val buffer = new Array[Char](1024)

        @tailrec def build: Unit = {
            val count = read( buffer, 0, 1024 )
            if ( count == -1 ) {
                close
            }
            else {
                result.appendAll( buffer, 0, count )
                build
            }
        }

        build

        result.toString()
    }
}


