Hasher
======

Creating a hash should be a simple function call. But it always somehow seems
to be more complicated than that. Hasher is a small Scala library to make
generating hashes a breeze.

Supported Hashing Algorithms
----------------------------

* MD5
* SHA1
* SHA256
* BCrypt
* CRC32

Usage
-----

Once you have added the Hasher package as a dependency, using it is simple.
To get access to the implicit helper methods that attach themselves to strings,
import the hasher.Implicits methods, like this:


    package org.example.hasher

    import hasher.Implicits._

    object Main {
        def main(args: Array[String]) {

            val hashMe = "Some String"

            val md5 = hashMe.md5
            val sha1 = hashMe.sha1
            val sha256 = hashMe.sha256
            val crc32 = hashMe.crc32
            val bcrypt = hashMe.bcrypt

            println("MD5: " + md5)
            println("SHA1: " + sha1)
            println("SHA256: " + sha256)
            println("CRC32: " + crc32)
            println("BCrypt: " + bcrypt)

            println("MD5 Matches: " + (hashMe md5sTo md5) )
            println("SHA1 Matches: " + (hashMe sha1sTo sha1) )
            println("SHA256 Matches: " + (hashMe sha256sTo sha256) )
            println("CRC32 Matches: " + (hashMe crc32sTo crc32) )
            println("BCrypt Matches: " + (hashMe bcryptsTo bcrypt) )
        }
    }

BCrypt
------

If you are interested in using the BCrypt hash (Which you should, by the way,
for hashing passwords), you will need to add the jBCrypt package a dependency.
If you are using SBT, it would look like this:

    libraryDependencies ++= Seq(
        "org.mindrot" % "jbcrypt" % "0.3m"
    )

