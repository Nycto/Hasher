package com.roundeights.hasher

import java.io.InputStream
import java.io.Reader
import scala.io.Source


/** A helper class for performing some operation with various algorithms */
trait WithAlgo[A] {

    /** The actual operation to perform with an algorithm */
    protected def withAlgo ( algo: Algo ): A

    /** MD5 hashing algorithm */
    def md5 = withAlgo( new Algo( () => new MessageDigest("MD5") ) )

    /** SHA1 hashing algorithm */
    def sha1 = withAlgo( new Algo( () => new MessageDigest("SHA-1") ) )

    /** SHA256 hashing algorithm */
    def sha256 = withAlgo( new Algo( () => new MessageDigest("SHA-256") ) )

    /** SHA384 hashing algorithm */
    def sha384 = withAlgo( new Algo( () => new MessageDigest("SHA-384") ) )

    /** sha512 hashing algorithm */
    def sha512 = withAlgo( new Algo( () => new MessageDigest("SHA-512") ) )

    /** CRC32 algorithm */
    def crc32 = withAlgo( new Algo( () => new CRC32Digest ) )

    /** BCrypt hashing, using 10 rounds */
    def bcrypt = withAlgo( new Algo( () => new BCryptDigest(10) ) )

    /** BCrypt hashing, with a specific number of rounds */
    def bcrypt( rounds: Int = 10 )
        = withAlgo( new Algo( () => new BCryptDigest(rounds) ) )

    /** A fluent interface for generating HMACs */
    class HmacBuilder ( val key: String ) {

        /** HMAC-MD5 hashing algorithm */
        def md5 = withAlgo( new Algo( () => new HMAC("HmacMD5", key) ) )

        /** HMAC-SHA1 hashing algorithm */
        def sha1 = withAlgo( new Algo( () => new HMAC("HmacSHA1", key) ) )

        /** HMAC-SHA256 hashing algorithm */
        def sha256 = withAlgo( new Algo( () => new HMAC("HmacSHA256", key) ) )
    }

    /** Generates an hmac builder */
    def hmac ( key: String ) = new HmacBuilder( key )
}


/**
 * Algo companion
 */
object Algo extends WithAlgo[Algo] {

    /** {@inheritDoc} */
    protected def withAlgo ( algo: Algo ): Algo = algo
}


/**
 * A hashing algorithm
 */
class Algo private[hasher] (
    private val digestBuilder: () => MutableDigest
) extends WithPlainText[Digest] {

    /**
     * Generates a new digest for this algorithm
     */
    def digest: MutableDigest = digestBuilder()


    /**
     * Returns the name of this algorithm
     */
    def name: String = digest.name

    /** {@inheritDoc} */
    override def toString = "Algo(%s)".format( name )


    /**
     * Generates a hash of a PlainText source
     */
    override def apply ( value: PlainText ): Digest = value.fill( digest )


    /**
     * Returns a decorated input stream that will generate a hash as
     * data is read
     */
    def tap ( value: InputStream ): InputStreamTap
        = new InputStreamTap( digest, value )

    /**
     * Returns a decorated Reader that will generate a hash as
     * data is read
     */
    def tap ( value: Reader ): ReaderTap = new ReaderTap( digest, value )

    /**
     * Returns a decorated Source that will generate a hash as
     * data is read
     */
    def tap ( value: Source ): SourceTap = new SourceTap( digest, value )
}

