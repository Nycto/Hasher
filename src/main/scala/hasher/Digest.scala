package com.roundeights.hasher

import scala.language.implicitConversions

/**
 * Companion
 */
object Digest {

    /**
     * A helper method for comparing byte arrays for equality. This method
     * is safe from timing attacks
     */
    private[hasher] def compare ( a: Array[Byte], b: Array[Byte] ) = {
        // I opted for writing my own instead of using the MessageDigest
        // function because of this article:
        // http://codahale.com/a-lesson-in-timing-attacks/
        // Yes, the bug was fixed, but this means better support for someone
        // using an old version
        if ( a.length != b.length ) false
        else {
            (0 until a.length).foldLeft(0) {
                (accum, i) => accum | ( a(i) ^ b(i) )
            } == 0
        }
    }

    /** Converts a digest to a hash */
    implicit def digest2hash ( in: Digest ): Hash = in.hash

    /** Converts a digest to a string */
    implicit def digest2string ( in: Digest ): String = in.hex

    /** Converts a digest to a string */
    implicit def digest2byteArray ( in: Digest ): Array[Byte] = in.bytes
}

/**
 * The base class for a hashing algorithm
 */
trait Digest {

    /**
     * Returns the name of this algorithm
     */
    def name: String

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
    def `hash_=` ( vs: String ): Boolean = {
        try {
            hash_=( Hash(vs) )
        } catch {
            case _: NumberFormatException => false
        }
    }

    /**
     * Determines whether the collected bytes compute to a given hash
     */
    def `hash_=` ( vs: Array[Byte] ): Boolean = hash_=( Hash(vs) )

    /**
     * Determines whether the collected bytes compute to a given hash
     */
    def `hash_=` ( vs: Digest ): Boolean = hash_=( vs.hash )

    /**
     * Returns this digest as a hex encoded string
     */
    def hex: String = hash.hex

    /**
     * Returns the raw bytes from the hash this digest generates
     */
    def bytes: Array[Byte] = hash.bytes

    /** {@inheritDoc} */
    override def toString = "Digest(%s, %s)".format( name, hash.hex )
}

/**
 * A digest that allows additional data to be added to it
 */
trait MutableDigest extends Digest {

    /**
     * Adds a list of bytes to this algorithm
     */
    def add ( bytes: Array[Byte], length: Int ): MutableDigest
}

/**
 * The implementation for hashes that use MessageDigest
 */
private class MessageDigest (
    override val name: String
) extends MutableDigest {

    import java.security.{MessageDigest => jMessageDigest}

    /**
     * The digest to collect data into
     */
    private val jDigest = jMessageDigest.getInstance( name )

    /** {@inheritDoc} */
    override def add ( bytes: Array[Byte], length: Int ): MutableDigest = {
        jDigest.update(bytes, 0, length)
        this
    }

    /** {@inheritDoc} */
    override def hash: Hash = {
        // Message digests get reset when they're consumed, so we make
        // a cloen before calculating a digest
        val clone = jDigest.clone.asInstanceOf[jMessageDigest]
        Hash( clone.digest )
    }

    /** {@inheritDoc} */
    override def `hash_=` ( vs: Hash ): Boolean
        = Digest.compare( jDigest.digest, vs.bytes )
}

/**
 * The implementation for hashes that use javax.crypto.Mac
 */
private class HMAC (
    override val name: String,
    key: String
) extends MutableDigest {

    import javax.crypto.Mac
    import javax.crypto.spec.SecretKeySpec

    /**
     * The digest to collect data into
     */
    private val mac = Mac.getInstance( name )

    // initialize the mac with the secret key
    mac.init( new SecretKeySpec(key.getBytes, name) )

    /** {@inheritDoc} */
    override def add ( bytes: Array[Byte], length: Int ): MutableDigest = {
        mac.update(bytes, 0, length)
        this
    }

    /** {@inheritDoc} */
    override def hash: Hash = {
        // Macs get reset when they're consumed, so we make
        // a clone before calculating a digest
        Hash( mac.clone.asInstanceOf[Mac].doFinal )
    }

    /** {@inheritDoc} */
    override def `hash_=` ( vs: Hash ): Boolean
        = Digest.compare( mac.clone.asInstanceOf[Mac].doFinal, vs.bytes )
}

/**
 * The CRC32 hash implementation
 */
private class CRC32Digest extends MutableDigest {

    import java.util.zip.CRC32

    /**
     * The digest to collect data into
     */
    private val digest = new CRC32

    /** {@inheritDoc} */
    override val name = "CRC32"

    /** {@inheritDoc} */
    override def add ( bytes: Array[Byte], length: Int ): MutableDigest = {
        digest.update(bytes, 0, length)
        this
    }

    /** {@inheritDoc} */
    override def hash: Hash = {

        import java.nio.ByteBuffer

        // Convert the int returned by CRC32 into a byte list
        val bytes = ByteBuffer.allocate(8).putLong( digest.getValue ).array

        // Count the number of zero bytes on the left, but leave at
        // least 4 bytes
        val toTrim = bytes.indexWhere( _ != 0 ) match {
            case -1 => 4
            case zeroes => zeroes.min( bytes.length - 4 )
        }

        Hash( bytes.drop( toTrim ).toArray )
    }

    /** {@inheritDoc} */
    override def `hash_=` ( vs: Hash ): Boolean
        = Digest.compare( hash.bytes, vs.bytes )
}

/**
 * The BCrypt hash implementation
 */
private class BCryptDigest (
    private val rounds: Int
) extends MutableDigest {

    import org.mindrot.jbcrypt.{BCrypt => jBCrypt}

    /**
     * The collected value to hash
     */
    private val value = new StringBuilder

    /** {@inheritDoc} */
    override val name = "BCrypt"

    /** {@inheritDoc} */
    override def add ( bytes: Array[Byte], length: Int ): MutableDigest = {
        value.append( new String(bytes, 0, length) )
        this
    }

    /** {@inheritDoc} */
    override def hash: Hash = {
        Hash( jBCrypt.hashpw(
            value.toString,
            jBCrypt.gensalt( rounds )
        ).getBytes )
    }

    /** {@inheritDoc} */
    override def `hash_=` ( vs: Hash ): Boolean = {
        // jBCrypt chokes on empty hashes, so we compensate
        new String(vs.bytes) match {
            case "" => false
            case str => jBCrypt.checkpw( value.toString, str )
        }
    }
}


