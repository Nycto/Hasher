package test.roundeights.hasher

import org.specs2.specification.Example

import com.roundeights.hasher.{Algo, Hasher}

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
        hmacMd5ed = "63d6baf65df6bdee8f32b332e0930669",
        hmacSha1ed = "1aa349585ed7ecbd3b9c486a30067e395ca4b356",
        hmacSha256ed =
            "0329a06b62cd16b33eb6792be8c60b15" +
            "8d89a2ee3a876fce9a881ebb488c0914",
        crc32ed = "d87f7e0c",
        bcrypted =
            "24326124313024755658345747674b" +
            "524749445643414b5941655a494f41" +
            "4b4f3468543759437053796167356a" +
            "2f5a365261377253334b636b796c32",
        bcrypted12 =
            "243261243132246138486731305057" +
            "4d6d35592f325044743631466d4f52" +
            "384b36616975594f46383179676430" +
            "44574c356a6634616844424648344b"
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
        hmacMd5ed = "28ea3cc0afc317bc391a2fae1d4a83e9",
        hmacSha1ed = "d73ba311a36f9b9697ed3099ac40733748d57107",
        hmacSha256ed =
            "73b9f218b093078018a79922ef73054b" +
            "13fb363a424c4fb66d0a695bddfe7889",
        crc32ed = "d89a101b",
        bcrypted =
            "2432612431302432776f7a74417658" +
            "5066554b36737655724e556d537534" +
            "7a784d5045474148526835726c5352" +
            "624f6e346f44682f506c704e446f75",
        bcrypted12 =
            "24326124313224376e562f2e566373" +
            "346e62472e5074556c6c4f4e554f38" +
            "446371734b4f515442447a59486c76" +
            "32346636574767455a555a7062586d"
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
        hmacMd5ed = "6bca4a37218091bf0b0e12925ae02e27",
        hmacSha1ed = "4555fa5b1a9d42305e59e168a14ae4fc23435444",
        hmacSha256ed =
            "49e926020a71960a3c02a0db62c103a0" +
            "0359dad83cd850687cae344e1004a697",
        crc32ed = "07b5088e",
        bcrypted =
            "2432612431302469307035536d6549" +
            "6c622f6c336b43777541556a486541" +
            "766439686e6f336753463634546650" +
            "3333736f64615464477a6b42684857",
        bcrypted12 =
            "243261243132245a4433437158694f" +
            "6734474a506a664959537977474f4d" +
            "3744575932455245554f73706c7543" +
            "427870616b4d4a45334e62544a4e69"
    )

    // Empty data
    val blank = TestData(
        bytes = "".getBytes,
        md5ed = "d41d8cd98f00b204e9800998ecf8427e",
        sha1ed = "da39a3ee5e6b4b0d3255bfef95601890afd80709",
        sha256ed =
            "e3b0c44298fc1c149afbf4c8996fb924" +
            "27ae41e4649b934ca495991b7852b855",
        sha384ed =
            "38b060a751ac96384cd9327eb1b1e36a21fdb71114be0743" +
            "4c0cc7bf63f6e1da274edebfe76f65fbd51ad2f14898b95b",
        sha512ed =
            "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc" +
            "83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f" +
            "63b931bd47417a81a538327af927da3e",
        hmacMd5ed = "5c8db03f04cec0f43bcb060023914190",
        hmacSha1ed = "25af6174a0fcecc4d346680a72b7ce644b9a88e8",
        hmacSha256ed =
            "f9e66e179b6747ae54108f82f8ade8b3" +
            "c25d76fd30afde6c395822c530196169",
        crc32ed = "00000000",
        bcrypted =
            "24326124313024646665575364666f" +
            "72464b75684b61456e746c31567541" +
            "6c39494c783830722e33537a554438" +
            "6b315558317956646777614d525347",
        bcrypted12 =
            "243261243132244172674374753475" +
            "366d547676497276414b6e32687557" +
            "467a79553338467a796b4550497966" +
            "7451694f4a4b6b3434784c3752456d"
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
    val hmacMd5ed: String,
    val hmacSha1ed: String,
    val hmacSha256ed: String,
    val crc32ed: String,
    val bcrypted: String,
    val bcrypted12: String
) {

    def str: String = new String( bytes )
    def builder: StringBuilder = new StringBuilder(str)
    def stream = new ByteArrayInputStream( bytes )
    def reader = new StringReader(str)
    def source = Source.fromBytes( bytes )

    def length = str.length

    def runAgainstUnsalted (run: (Algo, TestData, String) => Unit) = {
        run( Algo.md5, this, md5ed )
        run( Algo.sha1, this, sha1ed )
        run( Algo.sha256, this, sha256ed )
        run( Algo.sha384, this, sha384ed )
        run( Algo.sha512, this, sha512ed )
        run( Algo.hmac("secret").md5, this, hmacMd5ed )
        run( Algo.hmac("secret").sha1, this, hmacSha1ed )
        run( Algo.hmac("secret").sha256, this, hmacSha256ed )
        run( Algo.crc32, this, crc32ed )
    }

    def runAgainstAll (run: (Algo, TestData, String) => Unit) = {
        runAgainstUnsalted( run )
        run( Algo.bcrypt, this, bcrypted )
        run( Algo.bcrypt(12), this, bcrypted12 )
    }

}

