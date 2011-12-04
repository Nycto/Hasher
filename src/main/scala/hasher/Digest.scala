package hasher

import java.io.InputStream


/**
 * The base class for a hashing algorithm
 */
trait Digest {

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

    private[hasher] val crc32 = Builder( (build) => new CRC32Digest )

    private[hasher] val bcrypt = Builder( (build) => new BCryptDigest )

}

/**
 * The implementation for hashes that use MessageDigest
 */
private class MessageDigest (
    private val digest: Digest.Builder, name: String
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
    override def hash: Hash = Hash( jDigest.digest )

    /** {@inheritDoc} */
    override def hashesTo ( vs: Hash ): Boolean
        = jMessageDigest.isEqual( jDigest.digest, vs.bytes )

}

/**
 * The CRC32 hash implementation
 */
private class CRC32Digest extends Digest {

    import java.util.zip.CRC32
    import java.security.MessageDigest

    /**
     * The digest to collect data into
     */
    private val digest = new CRC32

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

        // Trim any leading zeroes off of the byte list
        val trimmed = bytes.dropWhile( _ == 0 ).toArray

        Hash( trimmed )
    }

    /** {@inheritDoc} */
    override def hashesTo ( vs: Hash ): Boolean
        = MessageDigest.isEqual( hash.bytes, vs.bytes )

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


