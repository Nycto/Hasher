package com.roundeights.hasher

import java.io.InputStream
import java.io.Reader
import scala.io.{Source, Codec}


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
    class HmacBuilder ( val key: Array[Byte] ) {

        /** HMAC-MD5 hashing algorithm */
        def md5 = withAlgo( new Algo( () => new HMAC("HmacMD5", key) ) )

        /** HMAC-SHA1 hashing algorithm */
        def sha1 = withAlgo( new Algo( () => new HMAC("HmacSHA1", key) ) )

        /** HMAC-SHA256 hashing algorithm */
        def sha256 = withAlgo( new Algo( () => new HMAC("HmacSHA256", key) ) )

        /** HMAC-SHA512 hashing algorithm */
        def sha512 = withAlgo( new Algo( () => new HMAC("HmacSHA512", key) ) )
    }

    /** Generates an hmac builder */
    def hmac ( key: Array[Byte] ) = new HmacBuilder( key )

    /** Generates an hmac builder */
    def hmac ( key: String ) = new HmacBuilder( key.getBytes("UTF8") )

    /** Generates a SHA1 based PBKDF2 hash */
    def pbkdf2 ( salt: Array[Byte], iterations: Int, keyLength: Int ): A
        = withAlgo( new Algo( () => new Pbkdf2Digest(
            "PBKDF2WithHmacSHA1", salt, iterations, keyLength
        ) ) )

    /** Generates a SHA1 based PBKDF2 hash */
    def pbkdf2 ( salt: String, iterations: Int, keyLength: Int ): A
        = pbkdf2( salt.getBytes("UTF8"), iterations, keyLength )
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

    /** Generates a new digest for this algorithm */
    def digest: MutableDigest = digestBuilder()

    /** Returns the name of this algorithm */
    def name: String = digest.name

    /** {@inheritDoc} */
    override def toString = "Algo(%s)".format( name )

    /** Generates a hash of a PlainText source */
    override def apply ( value: PlainText ): Digest = value.fill( digest )

    /** Decorates an input stream to generate a hash as data is read */
    def tap ( value: InputStream, codec: Codec ): InputStreamTap =
        new InputStreamTap( digest, value )

    /** Decorates an input stream to generate a hash as data is read */
    def tap ( value: InputStream ): InputStreamTap = tap(value, Codec.UTF8)

    /** Decorates a Reader to generate a hash as data is read */
    def tap ( value: Reader, encoding: Codec ): ReaderTap =
        new ReaderTap( digest, value, encoding )

    /** Decorates a Reader to generate a hash as data is read */
    def tap ( value: Reader ): ReaderTap = tap(value, Codec.UTF8)

    /** Decorates a Source to generate a hash as data is read */
    def tap ( value: Source, encoding: Codec ): SourceTap =
        new SourceTap( digest, value, encoding )

    /** Decorates a Source to generate a hash as data is read */
    def tap ( value: Source ): SourceTap = tap(value, Codec.UTF8)

    /**
     * Creates a foldable object. These are useful for accumulating the content
     * of a hash from content contained in an iterable. Foldables enforce thread
     * safety by only being usable once. Each call to add a value to returns a
     * new instance that can then itself only be used once.
     */
    def foldable = new Foldable( digest )
}

