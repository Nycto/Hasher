package hasher

import java.io.InputStream


/**
 * Helper methods for generating hashes
 */
object Hasher {

    /**
     * Generates an MD5 hash of a string
     */
    def md5 = new Algo( Digest.md5 )

    /**
     * Generates a sha1 hash of a string
     */
    def sha1 = new Algo( Digest.sha1 )

    /**
     * Generates a sha256 hash of a string
     */
    def sha256 = new Algo( Digest.sha256 )

    /**
     * Generates a sha512 hash of a string
     */
    def sha512 = new Algo( Digest.sha512 )

    /**
     * Generates a crc32 hash of a string
     */
    def crc32 = new Algo( Digest.crc32 )

    /**
     * Generates a bcrypt hash of a string
     */
    def bcrypt = new Algo( Digest.bcrypt )


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
    def md5: Hash = value.hash( Digest.md5 )

    /**
     * Determines whether this value md5s to a given hash
     */
    def md5sTo( hash: String ): Boolean = value.hashesTo( Digest.md5, hash )

    /**
     * Generates a sha1 hash of this string
     */
    def sha1: Hash = value.hash( Digest.sha1 )

    /**
     * Determines whether this value sha1s to a given hash
     */
    def sha1sTo( hash: String ): Boolean = value.hashesTo( Digest.sha1, hash )

    /**
     * Generates a sha256 hash of this string
     */
    def sha256: Hash = value.hash( Digest.sha256 )

    /**
     * Determines whether this value sha256s to a given hash
     */
    def sha256sTo( hash: String ): Boolean
        = value.hashesTo( Digest.sha256, hash )

    /**
     * Generates a sha512 hash of this string
     */
    def sha512: Hash = value.hash( Digest.sha512 )

    /**
     * Determines whether this value sha512s to a given hash
     */
    def sha512sTo( hash: String ): Boolean = value.hashesTo( Digest.sha512, hash )

    /**
     * Generates a crc32 hash of this string
     */
    def crc32: Hash = value.hash( Digest.crc32 )

    /**
     * Determines whether this value crc32s to a given hash
     */
    def crc32sTo( hash: String ): Boolean = value.hashesTo( Digest.crc32, hash )

    /**
     * Generates a bcrypt hash of this string
     */
    def bcrypt: Hash = value.hash( Digest.bcrypt )

    /**
     * Determines whether this value bcrypts to a given hash
     */
    def bcryptsTo( hash: String ): Boolean = value.hashesTo( Digest.bcrypt, hash )

}


