package com.roundeights.hasher

import scala.language.reflectiveCalls
import scala.annotation.tailrec

import scala.io.Source
import java.io.InputStream
import java.io.Reader

/**
 * Performs some operation using a plain text value
 */
trait WithPlainText[A] {

    /** Executes an operation with the plain text value */
    def apply ( value: PlainText ): A

    /**
     * Constructor for accepting Byte arrays.
     */
    def apply ( value: Array[Byte] ): A = apply( new PlainTextBytes( value ) )

    /**
     * Constructor for accepting strings.
     */
    def apply ( value: String ): A = apply( value.getBytes )

    /**
     * Constructor for accepting StringBuilders.
     */
    def apply ( value: StringBuilder ): A = apply( value.toString )

    /**
     * Constructor for accepting InputStream.
     */
    def apply ( value: InputStream ): A = apply( new PlainTextResource(value) )

    /**
     * Constructor for accepting Readers.
     */
    def apply ( value: Reader ): A = apply( new PlainTextResource(value) )

    /**
     * Constructor for accepting Sources.
     */
    def apply ( value: Source ): A = apply( new PlainTextSource(value) )
}

/**
 * The base class for plain text representations
 */
trait PlainText {

    /**
     * Populates the digest
     */
    protected[hasher] def fill ( digest: MutableDigest ): MutableDigest

    /**
     * Hashes an InputStream according to this algorithm.
     */
    def hash ( digest: MutableDigest ): Hash = fill( digest ).hash
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
    override protected[hasher] def fill (
        digest: MutableDigest
    ): MutableDigest = {
        digest.add( value, value.length )
    }
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
    override protected[hasher] def fill (
        digest: MutableDigest
    ): MutableDigest = {
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
    override protected[hasher] def fill (
        digest: MutableDigest
    ): MutableDigest = {
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
    override protected[hasher] def fill (
        digest: MutableDigest
    ): MutableDigest = {
        digest.add( salt, salt.length )
        inner.fill( digest )
    }
}

