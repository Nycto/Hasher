package hasher


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
     * Generates a crc32 hash of a string
     */
    def crc32 ( value: String ): Hash = Algo.CRC32.hash( value )

    /**
     * Generates a crc32 hash of a byte array
     */
    def crc32 ( value: Array[Byte] ): Hash = Algo.CRC32.hash( value )

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
     * Adds in a salt to this value
     */
    def salt ( salt: String ) = Hasher( value ++ salt.getBytes )

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
     * Generates a crc32 hash of this string
     */
    def crc32: Hash = Hasher.crc32(value)

    /**
     * Determines whether this value crc32s to a given hash
     */
    def crc32sTo( hash: String ): Boolean = Algo.CRC32.hashesTo(value, hash)

    /**
     * Generates a bcrypt hash of this string
     */
    def bcrypt: Hash = Hasher.bcrypt(value)

    /**
     * Determines whether this value bcrypts to a given hash
     */
    def bcryptsTo( hash: String ): Boolean = Algo.BCrypt.hashesTo(value, hash)

}


