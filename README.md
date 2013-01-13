Hasher
======

Creating a hash should be a simple function call. But it always somehow seems
to be more complicated than that. Hasher is a small Scala library to make
generating hashes a breeze.

[![Build Status](https://secure.travis-ci.org/Nycto/Hasher.png?branch=master)](http://travis-ci.org/Nycto/Hasher)


Supported Hashing Algorithms
----------------------------

* `MD5`
* `SHA1`
* `SHA256`
* `SHA384`
* `SHA512`
* `BCrypt`
* `CRC32`


Adding it to your Project
-------------------------

Hasher is not currently hosted in any publicly available maven repositories.
However, you can still add it to your project by publishing it to your local 
repository.

Run the following commands:

```
git clone https://github.com/Nycto/Hasher.git /tmp/Hasher;
cd /tmp/Hasher;
sbt publish-local;
```

Then, just add this to your `build.sbt` file and recompile:

```
libraryDependencies ++= Seq(
    "com.roundeights" %% "hasher" % "0.3" 
)
```


Basic Usage
-----------

Once you have added the Hasher package as a dependency, the easiest way to
use it is by importing the implicit conversion methods. Once you have them
in scope, you get a bunch of new methods attached to existing types. For each
supported algorithm, you get:

* A method to generate a hash
* A method to compare a plain text value to an existing hash

Here is a sample app showing how to use these various methods:

```scala
package org.example.hasher

import com.roundeights.hasher.Implicits._

object Main {
    def main(args: Array[String]) = {

        val hashMe = "Some String"

        // Generate set of hashes using various algorithms
        val hashes = Map(
            "MD5" -> hashMe.md5,
            "SHA1" -> hashMe.sha1,
            "SHA256" -> hashMe.sha256,
            "SHA384" -> hashMe.sha384,
            "SHA512" -> hashMe.sha512,
            "CRC32" -> hashMe.crc32,
            "BCrypt" -> hashMe.bcrypt
        )

        // Print each of the calculated hashes
        hashes.foreach( pair => println( pair._1 + ": " + pair._2 ) )

        // Now compare the original value to each hashed value
        // and print the boolean result
        println("MD5 Matches: " + (hashMe md5= hashes("MD5")) )
        println("SHA1 Matches: " + (hashMe sha1= hashes("SHA1")) )
        println("SHA256 Matches: " + (hashMe sha256= hashes("SHA256")) )
        println("SHA384 Matches: " + (hashMe sha384= hashes("SHA384")) )
        println("SHA512 Matches: " + (hashMe sha512= hashes("SHA512")) )
        println("CRC32 Matches: " + (hashMe crc32= hashes("CRC32")) )
        println("BCrypt Matches: " + (hashMe bcrypt= hashes("BCrypt")) )
    }
}
```

The implicit methods currently work on the following types:

* `String`
* `StringBuilder`
* `Array[Byte]`
* `InputStream`
* `Reader`
* `Source`


Salting
-------

If you would like to add a salt to a hash, Hasher provides a `salt` method.
Using this method will be a tiny bit faster than doing the concatenation on
your own because the salt is injected directly into the hash digest rather
than being prepended to the plain text value.

Here is an example app that does salting:

```scala
package org.example.hasher

import com.roundeights.hasher.Hasher

object Main {
    def main(args: Array[String]) = {
        val hashMe = "Some String"
        println( "Salted MD5: " + hashMe.salt("sweet").md5 )
    }
}
```


Getting the bytes of a hash
---------------------------

If you need direct access to the bytes of a generated Hash instead of a
hex encoded string, you can implicitly convert them to an Array of Bytes,
like this:

```scala
package org.example.hasher

import com.roundeights.hasher.Implicits._

object Main {
    def main(args: Array[String]) = {
        val hashMe = "Some String"
        val hashBytes: Array[Byte] = hashMe.md5
        println("MD5 Hash Bytes: " + hashBytes.mkString(" "))
    }
}
```


Non-Implicit Usage
------------------

If implicit methods give you a sour taste, you don't have to use them. All
they do is generate a Hasher object, which is what actually has all the methods
attached to it. You can just do that on your own, if you want. Here is the same
application as above, but implemented without implicits:

```scala
package org.example.hasher

import com.roundeights.hasher.Hasher

object Main {
    def main(args: Array[String]) = {

        val hashMe = "Some String"

        // Generate set of hashes using various algorithms
        val hashes = Map(
            "MD5" -> Hasher( hashMe ).md5,
            "SHA1" -> Hasher( hashMe ).sha1,
            "SHA256" -> Hasher( hashMe ).sha256,
            "SHA384" -> Hasher( hashMe ).sha384,
            "SHA512" -> Hasher( hashMe ).sha512,
            "CRC32" -> Hasher( hashMe ).crc32,
            "BCrypt" -> Hasher( hashMe ).bcrypt
        )

        // Print each of the calculated hashes of Hashes
        hashes.foreach( pair => println( pair._1 + ": " + pair._2 ) )

        println(
            "MD5 Matches: " +
            (Hasher(hashMe).md5= hashes("MD5"))
        )
        println(
            "SHA1 Matches: " +
            (Hasher(hashMe).sha1= hashes("SHA1"))
        )
        println(
            "SHA256 Matches: " +
            (Hasher(hashMe).sha256= hashes("SHA256"))
        )
        println(
            "SHA384 Matches: " +
            (Hasher(hashMe).sha384= hashes("SHA384"))
        )
        println(
            "SHA512 Matches: " +
            (Hasher(hashMe).sha512= hashes("SHA512"))
        )
        println(
            "CRC32 Matches: " +
            (Hasher(hashMe).crc32= hashes("CRC32"))
        )
        println(
            "BCrypt Matches: " +
            (Hasher(hashMe).bcrypt= hashes("BCrypt"))
        )
    }
}
```


Why use the `hash=` methods?
--------------------------

The `hash=` methods compare a plain text value to a pre-computed hash. You
should be using them instead of re-generating the hash and doing a string
comparison for two reasons:

1. To prevent timing attacks. These methods don't short circuit when they find
   a character that doesn't match. This makes it harder for a malicious
   individual to progressively rebuild a hash by timing how long your app
   takes to reject a value.

2. Some hashes, like BCrypt, salt their input. This means that if you generate
   a hash for the same value twice, they won't be the same. There really is
   no other way for you to do the comparison on your own.


Swapping out Algorithms
-----------------------

You might want to build your application so that you can swap out your
hashing algorithm. This can be achieved by using the Algo class:

```scala
package org.example.hasher

import com.roundeights.hasher.{Hasher, Algo}

object Main {

    val hashMe = "Some String"

    def hashUsing ( algo: Algo ) = {
        val hash = algo( hashMe )
        println( "Hashed using " + algo + ": " + hash )
        println( "Matches: " + algo.compare(hashMe, hash) )
    }

    def main(args: Array[String]) = {
        hashUsing( Hasher.md5 )
        hashUsing( Hasher.sha1 )
        hashUsing( Hasher.sha256 )
        hashUsing( Hasher.sha384 )
        hashUsing( Hasher.sha512 )
        hashUsing( Hasher.crc32 )
        hashUsing( Hasher.bcrypt )
    }
}
```


Tapping into a data stream
--------------------------

When you are dealing with a data stream of some sort (Maybe an `InputStream`, a
`Reader`, or a `Source`), odds are good that you are trying to do something
with it besides generating a hash. If that's the case, you can use a `Tap` to
build a hash while you do other stuff.

`Tap`s are just decorators. When you tap into an `InputStream`, you get a new
`InputStream` that has the ability to return a hash. All you have to do is to
consume the new `InputStream` as you normally would. Once all the data has been
read, you can call the `hash` method.

Here is an example:

```scala
package org.example.hasher

import com.roundeights.hasher.Hasher

object Main {

    val hashMe = "Some String"

    def inputStreamTapping = {

        import java.io.ByteArrayInputStream

        val stream = Hasher.sha1.tap(
            new ByteArrayInputStream( hashMe.getBytes )
        )

        // Read everything out of the stream
        while ( stream.read() != -1 ) {}

        val hash = stream.hash

        println( "InputStream Hash: " + hash )
        println( "InputStream Hash Compare: " + (stream hash= hash) )
    }

    def readerTapping = {

        import java.io.StringReader

        val reader = Hasher.sha1.tap( new StringReader( hashMe ) )

        // Read everything out of the reader
        while ( reader.read() != -1 ) {}

        val hash = reader.hash

        println( "Reader Hash: " + hash )
        println( "Reader Hash Compare: " + (reader hash= hash) )
    }

    def sourceTapping = {

        import scala.io.Source

        val source = Hasher.sha1.tap( Source.fromString( hashMe ) )

        // Read everything out of the source
        source.mkString

        val hash = source.hash

        println( "Source Hash: " + hash )
        println( "Source Hash Compare: " + (source hash= hash) )
    }

    def main(args: Array[String]) = {
        inputStreamTapping
        readerTapping
        sourceTapping
    }

}
```


BCrypt
------

If you are interested in using BCrypt (Which is what you should be using for
hashing passwords, by the way), you will need to add the jBCrypt package as a
dependency of your project. If you are using SBT (v0.10), just add these lines
to your `build.sbt` configuration:

```
resolvers ++= Seq(
    "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/"
)

libraryDependencies ++= Seq(
    "org.mindrot" % "jbcrypt" % "0.3m"
)
```


License
-------

Hasher is released under the MIT License, which is pretty spiffy. You should
have received a copy of the MIT License along with this program. If not, see
<http://www.opensource.org/licenses/mit-license.php>.

