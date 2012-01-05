package com.roundeights.hasher

import java.io.InputStream

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
 * An InputStream that generates a hash
 */
class InputStreamTap (
    private val digest: Digest,
    private val stream: InputStream
) extends InputStream with Tap {

    import scala.collection.mutable.ArrayBuffer

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

    /** {@inheritDoc} */
    override def read: Int = {
        val byte = stream.read
        if ( byte >= 0 ) buffer += byte.toByte
        if ( buffer.size >= 1024 ) flush
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


