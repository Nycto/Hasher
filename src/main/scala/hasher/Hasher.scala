package hasher

import java.security.MessageDigest

import javax.xml.bind.DatatypeConverter

import org.mindrot.jbcrypt.{BCrypt => jBCrypt}

/**
 * A list of implicit conversion methods
 */
object Implicits {

    /**
     * Implicitly creates a hasher from a string.
     */
    implicit def stringToHasher ( from: String ): Hasher = Hasher(from)

    /**
     * Implicitly creates a hasher from a byte array.
     */
    implicit def byteArrayToHasher ( from: Array[Byte] ): Hasher = Hasher(from)

    /**
     * Implicitly converts from a hash to a string
     */
    implicit def hashToString ( from: Hash ): String = from.hex

    /**
     * Implicitly converts from a hash to a byte array
     */
    implicit def hashToByteArray ( from: Hash ): Array[Byte] = from.bytes

}


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
     * The base class to unite the enum values with the Algo trait
     */
    sealed case class AlgoType ( val name: String ) extends Val

    val MD5 = new AlgoType("MD5") with MessageDigestAlgo
    val SHA1 = new AlgoType("SHA-1") with MessageDigestAlgo
    val SHA256 = new AlgoType("SHA-256") with MessageDigestAlgo
    val BCrypt = new AlgoType("BCrypt") with BCryptAlgo
}


/**
 * Companion for the Hash class
 */
object Hash {

    /**
     * Constructor...
     */
    def apply ( algo: Algo, string: String ) = new Hash(algo, string)

}

/**
 * Represents a hash
 */
case class Hash ( val algo: Algo, val bytes: Array[Byte] ) {

    /**
     * Constructs a hash from a hex string
     */
    def this ( algo: Algo, hex: String )
        = this( algo, DatatypeConverter.parseHexBinary( hex ) )

    /**
     * Converts this hash to a hex encoded string
     */
    lazy val hex: String = {
        bytes.foldLeft("") {
            (accum, digit) => accum + "%02x".format( 0xFF & digit )
        }
    }

    /**
     * Converts this hash to a hex encoded string
     */
    override def toString: String = hex

}


/**
 * Helper methods for generating hashes
 */
object Hasher {

    /**
     * Generates an MD5 hash of a string
     */
    def md5 ( value: String ): Hash = Algo.MD5.hash( value )

    /**
     * Generates an MD5 hash of a byte array
     */
    def md5 ( value: Array[Byte] ): Hash = Algo.MD5.hash( value )

    /**
     * Generates a sha1 hash of a string
     */
    def sha1 ( value: String ): Hash = Algo.SHA1.hash( value )

    /**
     * Generates a sha1 hash of a byte array
     */
    def sha1 ( value: Array[Byte] ): Hash = Algo.SHA1.hash( value )

    /**
     * Generates a sha256 hash of a string
     */
    def sha256 ( value: String ): Hash = Algo.SHA256.hash( value )

    /**
     * Generates a sha256 hash of a byte array
     */
    def sha256 ( value: Array[Byte] ): Hash = Algo.SHA256.hash( value )

    /**
     * Generates a bcrypt hash of a string
     */
    def bcrypt ( value: String ): Hash = Algo.BCrypt.hash( value )

    /**
     * Generates a bcrypt hash of a byte array
     */
    def bcrypt ( value: Array[Byte] ): Hash = Algo.BCrypt.hash( value )

    /**
     * Builds a Hasher from a string
     */
    def apply ( from: String ): Hasher = new Hasher(from)

}

/**
 * A helper for generating crypto hashes from a value
 */
case class Hasher ( private val value: Array[Byte] ) {

    /**
     * An alternate constructor for accepting strings.
     */
    def this ( value: String ) = this( value.getBytes )

    /**
     * Generates an MD5 hash of this string
     */
    def md5: Hash = Hasher.md5(value)

    /**
     * Determines whether this value md5s to a given hash
     */
    def md5sTo( hash: String ): Boolean = Algo.MD5.hashesTo(value, hash)

    /**
     * Generates a sha1 hash of this string
     */
    def sha1: Hash = Hasher.sha1(value)

    /**
     * Determines whether this value sha1s to a given hash
     */
    def sha1sTo( hash: String ): Boolean = Algo.SHA1.hashesTo(value, hash)

    /**
     * Generates a sha256 hash of this string
     */
    def sha256: Hash = Hasher.sha256(value)

    /**
     * Determines whether this value sha256s to a given hash
     */
    def sha256sTo( hash: String ): Boolean = Algo.SHA256.hashesTo(value, hash)

    /**
     * Generates a bcrypt hash of this string
     */
    def bcrypt: Hash = Hasher.bcrypt(value)

    /**
     * Determines whether this value bcrypts to a given hash
     */
    def bcryptsTo( hash: String ): Boolean = Algo.BCrypt.hashesTo(value, hash)

}

