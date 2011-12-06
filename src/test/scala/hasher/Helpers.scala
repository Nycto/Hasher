package test.scala.hasher

import org.specs2.specification.Example

import hasher.{Algo, Hasher}

import java.io.ByteArrayInputStream
import java.io.StringReader

import scala.io.Source

/**
 * Basic test data
 */
object TestData {

    // Simple test data
    val test = TestData(
        bytes = "test".getBytes,
        md5ed = "098f6bcd4621d373cade4e832627b4f6",
        sha1ed = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3",
        sha256ed =
            "9f86d081884c7d659a2fe" +
            "aa0c55ad015a3bf4f1b2b0" +
            "b822cd15d6c15b0f00a08",
        sha384ed =
            "768412320f7b0aa5812fce428dc4706b3cae50e02a64caa1" +
            "6a782249bfe8efc4b7ef1ccb126255d196047dfedf17a0a9",
        sha512ed =
            "ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db2" +
            "7ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff",
        crc32ed = "d87f7e0c",
        bcrypted =
            "24326124313024755658345747674b" +
            "524749445643414b5941655a494f41" +
            "4b4f3468543759437053796167356a" +
            "2f5a365261377253334b636b796c32"
    )

    // Buffer busting test data
    val large = TestData(
        bytes = Array.fill(20000)(65.byteValue),
        md5ed = "0af181fb57f1eefb62b74081bbddb155",
        sha1ed = "b9624c14586d4668cba0b2759229a49f1ea355b6",
        sha256ed =
            "c86f210e0efad769d6ade6f924a85200" +
            "be38917fa99e33b360aa24535716359b",
        sha384ed =
            "b0d8bf15ba996e77281796e899cc412f0dc2ae6b62a1aabd" +
            "a848eecd7c2fef607167efac88f8a4e57c65e43d9751117a",
        sha512ed =
            "c107c730231b39a54c16256a6d88511703bf2866bd32f945" +
            "ff8d13590fee60d2924e45a820f052900d7a86ca1951495d" +
            "20fd0d48fa9539b4c72fbd0fc4cd51d8",
        crc32ed = "d89a101b",
        bcrypted =
            "2432612431302432776f7a74417658" +
            "5066554b36737655724e556d537534" +
            "7a784d5045474148526835726c5352" +
            "624f6e346f44682f506c704e446f75"
    )

    // Multi-byte test data
    val international = TestData(
        bytes = "Iñtërnâtiônàlizætiøn".getBytes,
        md5ed = "e5e628206e73b1ae69b37fc69762a1e1",
        sha1ed = "4b9c5d2fa4c83f7561787eb4e5f7f06a2cd47425",
        sha256ed =
            "96dcd7d2a476d7366d71ee7119defbb0" +
            "db845c6f706ed34c1ac4b2c485150636",
        sha384ed =
            "ca4adc0333351c78f293a97a5b774abf4aa4792bfca6eb2d" +
            "8eb311cdf2ced5a260f50a7b55daa40576762251e7bcc502",
        sha512ed =
            "0c1d542db80b28e48451417181cedbc98419f029dfbaac68" +
            "6b19b47449208c4e0e6038e159f316ef6cd33c1142cd5012" +
            "eea4004ebf45631544ea7bcf5046543a",
        crc32ed = "07b5088e",
        bcrypted =
            "2432612431302469307035536d6549" +
            "6c622f6c336b43777541556a486541" +
            "766439686e6f336753463634546650" +
            "3333736f64615464477a6b42684857"
    )

}

/**
 * Represents a set of test data
 */
case class TestData (
    val bytes: Array[Byte],
    val md5ed: String,
    val sha1ed: String,
    val sha256ed: String,
    val sha384ed: String,
    val sha512ed: String,
    val crc32ed: String,
    val bcrypted: String
) {

    def str: String = new String( bytes )
    def builder: StringBuilder = new StringBuilder(str)
    def stream = new ByteArrayInputStream( bytes )
    def reader = new StringReader(str)
    def source = Source.fromBytes( bytes )

    def runAgainstUnsalted (run: (Algo, TestData, String) => Example) = {
        run( Hasher.md5, this, md5ed )
        run( Hasher.sha1, this, sha1ed )
        run( Hasher.sha256, this, sha256ed )
        run( Hasher.sha384, this, sha384ed )
        run( Hasher.sha512, this, sha512ed )
        run( Hasher.crc32, this, crc32ed )
    }

    def runAgainstAll (run: (Algo, TestData, String) => Example) = {
        runAgainstUnsalted( run )
        run( Hasher.bcrypt, this, bcrypted )
    }

}

