package hasher

import java.security.MessageDigest

/**
 * Helper methods for generating hashes
 */
object Hasher {

    /**
     * Implicit builder from a string
     */
    implicit def apply ( from: String ) = new Hasher(from)

}

/**
 * A helper for generating crypto hashes from a string
 */
class Hasher ( private val string: String ) {

    /**
     * Puts this string through a specific message digest and hex
     * encodes the result
     */
    private def digest ( algorithm: String ) = {
        val hash = MessageDigest.getInstance( algorithm )
        hash.reset()
        hash.update( string.getBytes )
        hash.digest().foldLeft("") { (accum, digit) =>
            accum + "%02x".format( 0xFF & digit )
        }
    }

    /**
     * Generates an MD5 hash of this string
     */
    def md5: String = digest("MD5")

    /**
     * Generates a sha1 hash of this string
     */
    def sha1: String = digest("SHA-1")

    /**
     * Generates a sha256 hash of this string
     */
    def sha256: String = digest("SHA-256")

}

