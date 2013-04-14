package com.roundeights.hasher


/**
 * The base class for a hashing algorithm
 */
trait Digest {

    /**
     * Returns the name of this digest algorithm.
     */
    def name: String

    /**
     * Adds a list of bytes to this algorithm
     */
    def add ( bytes: Array[Byte], length: Int ): Digest

    /**
     * Calculates the hash of the collected bytes so far
     */
    def hash: Hash

    /**
     * Determines whether the collected bytes compute to a given hash
     */
    def hashesTo ( vs: Hash ): Boolean

}

/**
 * Companion
 */
object Digest {

    /**
     * Builds a new algorithm
     */
    private[hasher] case class Builder (
        private val callback: (Builder) => Digest
    ) {
        /**
         * Builds a new digest object
         */
        def apply (): Digest = callback(this)
    }

    private[hasher] val md5
        = Builder( (build) => new MessageDigest(build, "MD5") )

    private[hasher] val sha1
        = Builder( (build) => new MessageDigest(build, "SHA-1") )

    private[hasher] val sha256
        = Builder( (build) => new MessageDigest(build, "SHA-256") )

    private[hasher] val sha384
        = Builder( (build) => new MessageDigest(build, "SHA-384") )

    private[hasher] val sha512
        = Builder( (build) => new MessageDigest(build, "SHA-512") )

    private[hasher] def hmacMd5 ( key: String )
        = Builder( (build) => new HMAC(build, "HmacMD5", key) )

    private[hasher] def hmacSha1 ( key: String )
        = Builder( (build) => new HMAC(build, "HmacSHA1", key) )

    private[hasher] def hmacSha256 ( key: String )
        = Builder( (build) => new HMAC(build, "HmacSHA256", key) )

    private[hasher] val crc32 = Builder( (build) => new CRC32Digest )

    private[hasher] val bcrypt = Builder( (build) => new BCryptDigest )

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

}

/**
 * The implementation for hashes that use MessageDigest
 */
private class MessageDigest (
    private val digest: Digest.Builder, override val name: String
) extends Digest {

    import java.security.{MessageDigest => jMessageDigest}

    /**
     * The digest to collect data into
     */
    private val jDigest = jMessageDigest.getInstance( name )

    /** {@inheritDoc} */
    override def add ( bytes: Array[Byte], length: Int ): Digest = {
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
    override def hashesTo ( vs: Hash ): Boolean
        = Digest.compare( jDigest.digest, vs.bytes )

}

/**
 * The implementation for hashes that use javax.crypto.Mac
 */
private class HMAC (
    private val digest: Digest.Builder,
    override val name: String,
    key: String
) extends Digest {

    import javax.crypto.Mac
    import javax.crypto.spec.SecretKeySpec

    /**
     * The digest to collect data into
     */
    private val mac = Mac.getInstance( name )

    // initialize the mac with the secret key
    mac.init( new SecretKeySpec(key.getBytes, name) )

    /** {@inheritDoc} */
    override def add ( bytes: Array[Byte], length: Int ): Digest = {
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
    override def hashesTo ( vs: Hash ): Boolean
        = Digest.compare( mac.clone.asInstanceOf[Mac].doFinal, vs.bytes )

}

/**
 * The CRC32 hash implementation
 */
private class CRC32Digest extends Digest {

    import java.util.zip.CRC32

    /**
     * The digest to collect data into
     */
    private val digest = new CRC32

    /** {@inheritDoc} */
    override val name = "CRC32"

    /** {@inheritDoc} */
    override def add ( bytes: Array[Byte], length: Int ): Digest = {
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
    override def hashesTo ( vs: Hash ): Boolean
        = Digest.compare( hash.bytes, vs.bytes )

}

/**
 * The BCrypt hash implementation
 */
private class BCryptDigest extends Digest {

    import org.mindrot.jbcrypt.{BCrypt => jBCrypt}

    /**
     * The collected value to hash
     */
    private val value = new StringBuilder

    /** {@inheritDoc} */
    override val name = "BCrypt"

    /** {@inheritDoc} */
    override def add ( bytes: Array[Byte], length: Int ): Digest = {
        value.append( new String(bytes, 0, length) )
        this
    }

    /** {@inheritDoc} */
    override def hash: Hash = {
        Hash( jBCrypt.hashpw(
            value.toString,
            jBCrypt.gensalt()
        ).getBytes )
    }

    /** {@inheritDoc} */
    override def hashesTo ( vs: Hash ): Boolean = {
        // jBCrypt chokes on empty hashes, so we compensate
        new String(vs.bytes) match {
            case "" => false
            case str => jBCrypt.checkpw( value.toString, str )
        }
    }

}


