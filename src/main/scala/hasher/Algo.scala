package com.roundeights.hasher

import java.io.InputStream
import java.io.Reader

import scala.io.Source


/**
 * Represents a partially applied hash
 */
class Algo ( private val digest: Digest.Builder ) {

    /** {@inheritDoc} */
    override def toString = digest().name

    /**
     * Generates a hash of a PlainText source
     */
    def apply ( value: PlainText ): Hash = value.hash( digest )

    /**
     * Generates a hash of a string
     */
    def apply ( value: String ): Hash = apply(new PlainTextBytes(value))

    /**
     * Generates a hash of a string
     */
    def apply ( value: StringBuilder ): Hash = apply(new PlainTextBytes(value))

    /**
     * Generates a hash of a byte array
     */
    def apply ( value: Array[Byte] ): Hash = apply(new PlainTextBytes(value))

    /**
     * Generates a hash of an input stream
     */
    def apply ( value: InputStream ): Hash = apply(new PlainTextResource(value))

    /**
     * Generates a hash of a Reader
     */
    def apply ( value: Reader ): Hash = apply(new PlainTextResource(value))

    /**
     * Generates a hash of a Source
     */
    def apply ( value: Source ): Hash = apply(new PlainTextSource(value))


    /**
     * Compares a plain text value to a hex encoding hash string
     */
    def compare ( value: PlainText, hash: String ): Boolean
        = value.hashesTo( digest, hash )

    /**
     * Compares a plain text String to a hex encoding hash string
     */
    def compare ( value: String, hash: String ): Boolean
        = compare( new PlainTextBytes(value), hash )

    /**
     * Compares a plain text StringBuilder to a hex encoding hash string
     */
    def compare ( value: StringBuilder, hash: String ): Boolean
        = compare( new PlainTextBytes(value), hash )

    /**
     * Compares a plain text Array of Bytes to a hex encoding hash string
     */
    def compare ( value: Array[Byte], hash: String ): Boolean
        = compare( new PlainTextBytes(value), hash )

    /**
     * Compares a plain text InputStream to a hex encoding hash string
     */
    def compare ( value: InputStream, hash: String ): Boolean
        = compare( new PlainTextResource(value), hash )

    /**
     * Compares a plain text Reader to a hex encoding hash string
     */
    def compare ( value: Reader, hash: String ): Boolean
        = compare( new PlainTextResource(value), hash )

    /**
     * Compares a plain text Source to a hex encoding hash string
     */
    def compare ( value: Source, hash: String ): Boolean
        = compare( new PlainTextSource(value), hash )

}

