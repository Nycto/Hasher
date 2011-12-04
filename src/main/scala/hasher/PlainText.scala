package hasher

/**
 * The base class for plain text representations
 */
trait PlainText {

    /**
     * Populates the digest
     */
    protected def fill ( algo: Algo ): Algo

    /**
     * Hashes an InputStream according to this algorithm.
     */
    def hash ( algo: Algo.Builder ): Hash = fill( algo() ).hash

    /**
     * Determines whether this value computes to a given hash
     */
    def hashesTo ( algo: Algo.Builder, vs: Hash ): Boolean
        = fill( algo() ).hashesTo( vs )

    /**
     * Determines whether this value computes to a given hash string
     */
    def hashesTo ( algo: Algo.Builder, vs: String ): Boolean = {
        try { hashesTo( algo, Hash(vs) ) }
        catch { case _:IllegalArgumentException => false }
    }

}

/**
 * A plain text representation of a Byte Array
 */
private case class PlainTextBytes (
    private val value: Array[Byte]
) extends PlainText {

    /**
     * Creates an instance from a string
     */
    def this ( value: String ) = this( value.getBytes )

    /** {@inheritDoc} */
    override protected def fill ( algo: Algo ): Algo
        = algo.add( value, value.length )

}


