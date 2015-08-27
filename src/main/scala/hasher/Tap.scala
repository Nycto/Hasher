package com.roundeights.hasher

import scala.io.{Source, Codec}

import java.io.InputStream
import java.io.Reader

/**
 * A tap is a specialized digest that decorates streams
 */
trait Tap extends Digest

/**
 * A tap that buffers the characters before writing to the digest
 */
trait BufferedTap extends Tap {

    import scala.collection.mutable.ArrayBuffer

    /** The digest to write to */
    protected def digest: MutableDigest

    /** The buffered data */
    private val buffer = new ArrayBuffer[Byte]

    /** {@inheritDoc} */
    override def name: String = digest.name

    /** Flushes the buffer to the digest */
    private def flush: Unit = {
        if ( buffer.size > 0 ) {
            val data = buffer.clone.toArray
            digest.add( data, data.size )
            buffer.trimStart( data.size )
        }
    }

    /** Adds a byte to the digest */
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
        digest.hash_=( vs )
    }
}

/**
 * An InputStream that generates a hash
 */
class InputStreamTap (
    override protected val digest: MutableDigest,
    private val stream: InputStream
) extends InputStream with BufferedTap {

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
    protected val digest: MutableDigest,
    private val reader: Reader,
    private val codec: Codec
) extends Reader with Tap {

    import scala.annotation.tailrec

    /** {@inheritDoc} */
    override def name: String = digest.name

    /** {@inheritDoc} */
    override def hash: Hash = digest.hash

    /** {@inheritDoc} */
    override def `hash_=` ( vs: Hash ): Boolean = digest.hash_=( vs )

    /** {@inheritDoc} */
    override def read( cbuf: Array[Char], off: Int, len: Int ): Int = {
        val read = reader.read( cbuf, off, len )

        if ( read > 0 ) {
            // Readers operate on characters, but we need bytes
            val str = new String( cbuf, off, read )
            val bytes = str.getBytes( codec.charSet )
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

    /** Converts this reader to a string */
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

/**
 * Wraps a source and generates a Source as data flows through it
 */
class SourceTap (
    override protected val digest: MutableDigest,
    private val source: Source,
    private val codec: Codec
) extends Source with BufferedTap {

    /** {@inheritDoc} */
    override protected val iter = new Iterator[Char] {

        /** {@inheritDoc} */
        override def hasNext = source.hasNext

        /** {@inheritDoc} */
        override def next: Char = {
            val next = source.next
            val str = Character.toString(next)
            str.getBytes( codec.charSet ).foreach(addByteToDigest)
            next
        }

    }
}


