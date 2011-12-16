package com.roundeights.hasher

import java.io.InputStream
import java.io.Reader

import scala.io.Source


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
     * Generates a sha384 hash of a string
     */
    def sha384 = new Algo( Digest.sha384 )

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
     * Builds a Hasher from a StringBuilder
     */
    def apply ( from: StringBuilder ): Hasher = new Hasher(from)

    /**
     * Builds a Hasher from an InputStream
     */
    def apply ( from: InputStream ): Hasher = new Hasher(from)

    /**
     * Builds a Hasher from a Reader
     */
    def apply ( from: Reader ): Hasher = new Hasher(from)

    /**
     * Builds a hasher from a Source
     */
    def apply ( from: Source ): Hasher = new Hasher(from)

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
     * Constructor for accepting StringBuilders.
     */
    def this ( value: StringBuilder ) = this( value.toString )

    /**
     * Constructor for accepting InputStream.
     */
    def this ( value: InputStream ) = this( new PlainTextResource(value) )

    /**
     * Constructor for accepting Readers.
     */
    def this ( value: Reader ) = this( new PlainTextResource(value) )

    /**
     * Constructor for accepting Sources.
     */
    def this ( value: Source ) = this( new PlainTextSource(value) )


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
    def md5: Hash = Hasher.md5( value )

    /**
     * Determines whether this value md5s to a given hash
     */
    def `md5_=` ( hash: String ): Boolean = Hasher.md5.compare( value, hash )

    /**
     * Generates a sha1 hash of this string
     */
    def sha1: Hash = Hasher.sha1( value )

    /**
     * Determines whether this value sha1s to a given hash
     */
    def `sha1_=` ( hash: String ): Boolean = Hasher.sha1.compare( value, hash )

    /**
     * Generates a sha256 hash of this string
     */
    def sha256: Hash = Hasher.sha256( value )

    /**
     * Determines whether this value sha256s to a given hash
     */
    def `sha256_=` ( hash: String ): Boolean
        = Hasher.sha256.compare( value, hash )

    /**
     * Generates a sha384 hash of this string
     */
    def sha384: Hash = Hasher.sha384( value )

    /**
     * Determines whether this value sha384s to a given hash
     */
    def `sha384_=` ( hash: String ): Boolean
        = Hasher.sha384.compare( value, hash )

    /**
     * Generates a sha512 hash of this string
     */
    def sha512: Hash = Hasher.sha512( value )

    /**
     * Determines whether this value sha512s to a given hash
     */
    def `sha512_=` ( hash: String ): Boolean
        = Hasher.sha512.compare( value, hash )

    /**
     * Generates a crc32 hash of this string
     */
    def crc32: Hash = Hasher.crc32( value )

    /**
     * Determines whether this value crc32s to a given hash
     */
    def `crc32_=` ( hash: String ): Boolean
        = Hasher.crc32.compare( value, hash )

    /**
     * Generates a bcrypt hash of this string
     */
    def bcrypt: Hash = value.hash( Digest.bcrypt )

    /**
     * Determines whether this value bcrypts to a given hash
     */
    def `bcrypt_=` ( hash: String ): Boolean
        = Hasher.bcrypt.compare( value, hash )

}


