package hasher


/**
 * Companion for the Hash class
 */
object Hash {

    /**
     * Constructor...
     */
    def apply ( algo: Algo, string: String ) = new Hash(algo, string)

    /**
     * Converts a hex string to an array of Bytes
     */
    private def hexToBytes ( str: String ): Array[Byte] = {
        str.grouped(2).map( Integer.parseInt(_, 16).byteValue ).toArray
    }

}

/**
 * Represents a hash
 */
case class Hash ( val algo: Algo, val bytes: Array[Byte] ) {

    /**
     * Constructs a hash from a hex string
     */
    def this ( algo: Algo, hex: String ) = this( algo, Hash.hexToBytes(hex) )

    /**
     * Converts this hash to a hex encoded string
     */
    lazy val hex: String = bytes.map( "%02x".format(_) ).mkString("")

    /**
     * Converts this hash to a hex encoded string
     */
    override def toString: String = hex

}


