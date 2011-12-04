package hasher

import java.io.InputStream


/**
 * Helper methods for generating hashes
 */
object Hasher {

    /**
     * Generates an MD5 hash of a string
     */
    def md5 ( value: String ): Hash = Hasher(value).md5

    /**
     * Generates an MD5 hash of a byte array
     */
    def md5 ( value: Array[Byte] ): Hash = Hasher(value).md5

    /**
     * Generates an MD5 hash of an input stream
     */
    def md5 ( value: InputStream ): Hash = Hasher(value).md5

    /**
     * Generates a sha1 hash of a string
     */
    def sha1 ( value: String ): Hash = Hasher(value).sha1

    /**
     * Generates a sha1 hash of a byte array
     */
    def sha1 ( value: Array[Byte] ): Hash = Hasher(value).sha1

    /**
     * Generates an SHA1 hash of an input stream
     */
    def sha1 ( value: InputStream ): Hash = Hasher(value).sha1

    /**
     * Generates a sha256 hash of a string
     */
    def sha256 ( value: String ): Hash = Hasher(value).sha256

    /**
     * Generates a sha256 hash of a byte array
     */
    def sha256 ( value: Array[Byte] ): Hash = Hasher(value).sha256

    /**
     * Generates an SHA256 hash of an input stream
     */
    def sha256 ( value: InputStream ): Hash = Hasher(value).sha256

    /**
     * Generates a crc32 hash of a string
     */
    def crc32 ( value: String ): Hash = Hasher(value).crc32

    /**
     * Generates a crc32 hash of a byte array
     */
    def crc32 ( value: Array[Byte] ): Hash = Hasher(value).crc32

    /**
     * Generates an crc32 hash of an input stream
     */
    def crc32 ( value: InputStream ): Hash = Hasher(value).crc32

    /**
     * Generates a bcrypt hash of a string
     */
    def bcrypt ( value: String ): Hash = Hasher(value).bcrypt

    /**
     * Generates a bcrypt hash of a byte array
     */
    def bcrypt ( value: Array[Byte] ): Hash = Hasher(value).bcrypt

    /**
     * Generates a bycrpt hash of an input stream
     */
    def bcrypt ( value: InputStream ): Hash = Hasher(value).bcrypt


    /**
     * Builds a Hasher from an array of bytes
     */
    def apply ( from: Array[Byte] ): Hasher = new Hasher(from)

    /**
     * Builds a Hasher from a string
     */
    def apply ( from: String ): Hasher = new Hasher(from)

    /**
     * Builds a Hasher from an InputStream
     */
    def apply ( from: InputStream ): Hasher = new Hasher(from)

}

/**
 * A helper for generating crypto hashes from a value
 */
case class Hasher private ( private val value: PlainText ) {

    /**
     * Constructor for accepting Byte arrays.
     */
    def this ( value: Array[Byte] ) = this( new PlainTextBytes( value ) )

    /**
     * Constructor for accepting strings.
     */
    def this ( value: String ) = this( value.getBytes )

    /**
     * Constructor for accepting InputStream.
     */
    def this ( value: InputStream ) = this( new PlainTextStream(value) )

    /**
     * Adds a salt from an array of bytes
     */
    def salt ( saltValue: Array[Byte] ): Hasher
        = Hasher( new PlainTextSalt(value, saltValue) )

    /**
     * Adds a salt from a string
     */
    def salt ( saltValue: String ): Hasher = salt( saltValue.getBytes )

    /**
     * Generates an MD5 hash of this string
     */
    def md5: Hash = value.hash( Algo.md5 )

    /**
     * Determines whether this value md5s to a given hash
     */
    def md5sTo( hash: String ): Boolean = value.hashesTo( Algo.md5, hash )

    /**
     * Generates a sha1 hash of this string
     */
    def sha1: Hash = value.hash( Algo.sha1 )

    /**
     * Determines whether this value sha1s to a given hash
     */
    def sha1sTo( hash: String ): Boolean = value.hashesTo( Algo.sha1, hash )

    /**
     * Generates a sha256 hash of this string
     */
    def sha256: Hash = value.hash( Algo.sha256 )

    /**
     * Determines whether this value sha256s to a given hash
     */
    def sha256sTo( hash: String ): Boolean = value.hashesTo( Algo.sha256, hash )

    /**
     * Generates a crc32 hash of this string
     */
    def crc32: Hash = value.hash( Algo.crc32 )

    /**
     * Determines whether this value crc32s to a given hash
     */
    def crc32sTo( hash: String ): Boolean = value.hashesTo( Algo.crc32, hash )

    /**
     * Generates a bcrypt hash of this string
     */
    def bcrypt: Hash = value.hash( Algo.bcrypt )

    /**
     * Determines whether this value bcrypts to a given hash
     */
    def bcryptsTo( hash: String ): Boolean = value.hashesTo( Algo.bcrypt, hash )

}


