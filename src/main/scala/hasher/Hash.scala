package hasher

import javax.xml.bind.DatatypeConverter


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


