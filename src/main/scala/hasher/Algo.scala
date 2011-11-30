package hasher

import java.security.MessageDigest
import java.util.zip.CRC32
import java.nio.ByteBuffer

import org.mindrot.jbcrypt.{BCrypt => jBCrypt}


/**
 * The base class for the list of available algorithms
 */
trait Algo {

    /**
     * The name of this algorithm
     */
    def name: String

    /**
     * Hashes a value according to this algorithm.
     */
    def hash ( value: Array[Byte] ): Hash

    /**
     * Hashes a string according to this algorithm.
     */
    def hash ( value: String ): Hash = hash( value.getBytes )

    /**
     * Determines whether a plain text computes to a given hash
     */
    def hashesTo ( plain: Array[Byte], vs: Hash ): Boolean
        = MessageDigest.isEqual( hash(plain).bytes, vs.bytes )

    /**
     * Determines whether a plain text computes to a given hash string
     */
    def hashesTo ( plain: Array[Byte], vs: String ): Boolean = {
        try { hashesTo( plain, Hash(this, vs) ) }
        catch { case _:IllegalArgumentException => false }
    }

}

/**
 * A list of the supported algorithms
 */
object Algo extends Enumeration {

    /**
     * A base class for algorithms based on Java's build in MessageDigest
     */
    trait MessageDigestAlgo extends Algo {

        /** {@inheritDoc} */
        override def hash ( value: Array[Byte] ): Hash = {
            val hash = MessageDigest.getInstance( name )
            hash.reset()
            hash.update( value )
            Hash( this, hash.digest() )
        }

    }

    /**
     * Implements BCrypt hashing
     */
    trait BCryptAlgo extends Algo {

        /** {@inheritDoc} */
        override def hash ( value: Array[Byte] ): Hash = {
            val str = new String(value)
            Hash( this, jBCrypt.hashpw(str, jBCrypt.gensalt()).getBytes )
        }

        /** {@inheritDoc} */
        override def hashesTo ( plain: Array[Byte], vs: Hash ): Boolean = {
            // jBCrypt chokes on empty hashes, so we compensate
            new String(vs.bytes) match {
                case "" => false
                case str => jBCrypt.checkpw( new String(plain), str )
            }
        }

    }

    /**
     * Implements CRC32 checksums
     */
    trait CRC32Algo extends Algo {

        /** {@inheritDoc} */
        override def hash ( value: Array[Byte] ): Hash = {
            val hash = new CRC32
            hash.reset()
            hash.update( value )

            val result = hash.getValue()

            // Convert the int returned by CRC32 into a byte list
            val bytes = ByteBuffer.allocate(8).putLong( result ).array

            // Trim any leading zeroes off of the byte list
            val trimmed = bytes.dropWhile( _ == 0 ).toArray

            Hash( this, trimmed )
        }

    }

    /**
     * The base class to unite the enum values with the Algo trait
     */
    sealed case class AlgoType ( val name: String ) extends Val


    val MD5 = new AlgoType("MD5") with MessageDigestAlgo
    val SHA1 = new AlgoType("SHA-1") with MessageDigestAlgo
    val SHA256 = new AlgoType("SHA-256") with MessageDigestAlgo
    val BCrypt = new AlgoType("BCrypt") with BCryptAlgo
    val CRC32 = new AlgoType("CRC32") with CRC32Algo
}


