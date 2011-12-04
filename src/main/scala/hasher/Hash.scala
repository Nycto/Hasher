package hasher


/**
 * Companion for the Hash class
 */
object Hash {

    /**
     * Constructor...
     */
    def apply ( string: String ) = new Hash(string)

}

/**
 * Represents a hash
 */
case class Hash ( val bytes: Array[Byte] ) {

    /**
     * Constructs a hash from a hex string
     */
    def this ( hex: String ) = this(
        hex.grouped(2).map( Integer.parseInt(_, 16).byteValue ).toArray
    )

    /**
     * Converts this hash to a hex encoded string
     */
    lazy val hex: String = bytes.map( "%02x".format(_) ).mkString("")

    /**
     * Converts this hash to a hex encoded string
     */
    override def toString: String = hex

}


