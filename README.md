Hasher [![Build Status](https://secure.travis-ci.org/Nycto/Hasher.png?branch=master)](http://travis-ci.org/Nycto/Hasher)
======

Hasher is a small Scala library to make generating hashes a breeze.


Supported Hashing Algorithms
----------------------------

* `MD5`
* `SHA1`
* `SHA256`
* `SHA384`
* `SHA512`
* `HMAC-MD5`
* `HMAC-SHA1`
* `HMAC-SHA256`
* `HMAC-SHA512`
* `BCrypt`
* `CRC32`
* `PBKDF2`


Adding it to your Project
-------------------------

To import Hasher into your project, you just need to add the following
directives to your `build.sbt` file:

```
resolvers ++= Seq("RoundEights" at "http://maven.spikemark.net/roundeights")

libraryDependencies ++= Seq("com.roundeights" %% "hasher" % "1.2.0")
```


Basic Usage
-----------

Once you have added the Hasher package as a dependency, the easiest way to
use it is by importing the implicit conversion methods. Once you have them
in scope, you get a bunch of new methods attached to existing types. For each
supported algorithm, you get:

* Methods to generate a hash
* A method to compare a plain text value to an existing hash

Here is a sample app showing how to use these various methods:

```scala
package org.example.hasher

import com.roundeights.hasher.Implicits._
import scala.language.postfixOps

object Main extends App {

    val hashMe = "Some String"

    // Generate a few hashes
    val md5 = hashMe.md5
    val sha1 = hashMe.sha1
    val bcrypt = hashMe.bcrypt

    // Print each hex encoded hash
    println( "MD5: " + md5.hex )
    println( "SHA1: " + sha1.hex )
    println( "BCrypt: " + bcrypt.hex )

    // Compare the original value to each hashed value
    // and print the boolean result
    println("MD5 Matches: " + (hashMe.md5 hash= md5) )
    println("SHA1 Matches: " + (hashMe.sha1 hash= sha1) )
    println("BCrypt Matches: " + (hashMe.bcrypt hash= bcrypt) )
}
```

The implicit methods currently work on the following types:

* `String`
* `StringBuilder`
* `Array[Byte]`
* `InputStream`
* `Reader`
* `Source`


A Note About Resources
----------------------

Some of the data types you can use to generate hashes are consumable. In
other words, once these resources are read, they are spent. Specifically,
this applies if you are hashing an `InputStream`, a `Reader`, or a
`Source`.

There are two implications to this:

1. Make sure you don't read from the resource before generating the hash.
   Your resource will not be automatically reset before it is read, so
   the resulting digest will be based on the initial offset. That's
   probably not what you wanted as it means the hash will only reflect
   part of the data.
2. Your resource will be consumed after generating the hash.
   Unfortunately, there is no way out of this. The hash is based on the
   content of the resource, so the resource has to be read to generate
   the hash.

You do have options, though:

1. Depending on the type of resource, you may be able to reset it after
   generating your hash.
2. Use a `Tap` instead of directly building the hash. This allows the
   digest to be constructed passively as some other piece of code reads
   the resource. Details and examples are available further down in
   this document.


Salting
-------

If you would like to add a salt to a hash, Hasher provides a `salt` method.
Using this method will be a tiny bit faster than doing the concatenation on
your own because the salt is injected directly into the hash digest rather
than being prepended to the plain text value.

Here is an example app that does salting:

```scala
package org.example.hasher

import com.roundeights.hasher.Implicits._

object Main extends App {
    val hashMe = "Some String"
    println( "Salted MD5: " + hashMe.salt("sweet").md5.hex )
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

object Main extends App {
    val hashMe = "Some String"
    val hashBytes = hashMe.md5.bytes
    println("MD5 Hash Bytes: " + hashBytes.mkString(" "))
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
import scala.language.postfixOps

object Main extends App{

    val hashMe = "Some String"

    // Generate a few hashes
    val md5 = Hasher(hashMe).md5
    val sha1 = Hasher(hashMe).sha1
    val bcrypt = Hasher(hashMe).bcrypt

    // Print each hex encoded hash
    println( "MD5: " + md5.hex )
    println( "SHA1: " + sha1.hex )
    println( "BCrypt: " + bcrypt.hex )

    // Compare the original value to each hashed value
    // and print the boolean result
    println("MD5 Matches: " + (Hasher(hashMe).md5 hash= md5) )
    println("SHA1 Matches: " + (Hasher(hashMe).sha1 hash= sha1) )
    println("BCrypt Matches: " + (Hasher(hashMe).bcrypt hash= bcrypt) )
}
```


Why use the `hash=` method?
---------------------------

The `hash=` method compares a plain text value to a pre-computed hash. You
should be using it instead of re-generating the hash and doing a string
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
import scala.language.postfixOps

object Main extends App {

    val hashMe = "Some String"

    def hashUsing ( algo: Algo ) = {
        val hash = algo( hashMe )
        println( "Hashed using " + algo.name + ": " + hash.hex )
        println( "Matches: " + (algo(hashMe) hash= hash) )
    }

    hashUsing( Algo.md5 )
    hashUsing( Algo.sha1 )
    hashUsing( Algo.sha256 )
    hashUsing( Algo.sha384 )
    hashUsing( Algo.sha512 )
    hashUsing( Algo.hmac("secret").md5 )
    hashUsing( Algo.hmac("secret").sha1 )
    hashUsing( Algo.hmac("secret").sha256 )
    hashUsing( Algo.hmac("secret").sha512 )
    hashUsing( Algo.crc32 )
    hashUsing( Algo.pbkdf2("secret", 1000, 128) )
    hashUsing( Algo.bcrypt )
}
```


Accumulating a Hash
-------------------

There will be times when you will want to generate a hash based on the data
stored in a traversable object. Instead of joining the content into a giant
string and passing it in all at once, you might want to use a `Foldable`
instead. It allows you to progressively generate a hash using a fold. For
example:

```scala
def hash( list: List[String] ): String = {
    list.foldLeft( Algo.sha1.foldable ){ (accum, str) => accum(str) }.done.hex
}
```

Foldables enforce ordered digest creation by disabling each instance after you
add data. But when you add data, a new instance is returned that can be used
to add more data. When everything has been added, you just need to call `done`
and you get a fully formed digest.

Presenting an API like this provides a few benefits:

1. Your code will be more efficient as you don't need to allocate large chunks
   of memory to, for example, join a string together
2. It presents an externally immutable API
3. It helps prevent race conditions by ensuring continued forward motion

One trick that might come in handy is asynchronously folding over a list of
futures. For example:

```scala
def hash( list: List[Future[String]] ): Future[String] = {
    Future.fold(list)( Algo.sha1.foldable ) {
        (accum, str) => accum(str)
    }.maps(_.done.hex)
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

Here are a few examples using different types of streams:

```scala
// Tapping an InputStream
import java.io.ByteArrayInputStream

val stream = Algo.sha1.tap(
    new ByteArrayInputStream( "Some String".getBytes )
)

// Read everything out of the stream
while ( stream.read() != -1 ) {}

val hash = stream.hash

println( "InputStream Hash: " + hash )
println( "InputStream Hash Compare: " + (stream hash= hash) )
```

- - -

```scala
// Tapping a Reader
import java.io.StringReader

val reader = Algo.sha1.tap( new StringReader( "Some String" ) )

// Read everything out of the reader
while ( reader.read() != -1 ) {}

val hash = reader.hash

println( "Reader Hash: " + hash )
println( "Reader Hash Compare: " + (reader hash= hash) )
```

- - -

```scala
// Tapping a source
import scala.io.Source

val source = Algo.sha1.tap( Source.fromString( hashMe ) )

// Read everything out of the source
source.mkString

val hash = source.hash

println( "Source Hash: " + hash )
println( "Source Hash Compare: " + (source hash= hash) )
```


What about character encoding?
------------------------------

Any time a string is passed into Hasher, it is internally converted to to a
UTF-8 byte array. Any method that accepts a string will also accept a byte
array. Thus, if UTF-8 isn't your style, you can manually convert your
string to a byte array before passing it in.

For methods that can't accept a byte array, but are character encoding
sensitive (for example, the `tap` methods), you can pass in a Codec instance.


BCrypt
------

If you are interested in using BCrypt (Which is what you should be using for
hashing passwords, by the way), you will need to add the jBCrypt package as a
dependency of your project. If you are using SBT (v0.10), just add these lines
to your `build.sbt` configuration:

```
resolvers ++= Seq("jBCrypt Repository" at "http://repo1.maven.org/maven2/org/")

libraryDependencies ++= Seq("org.mindrot" % "jbcrypt" % "0.3m")
```


License
-------

Hasher is released under the MIT License, which is pretty spiffy. You should
have received a copy of the MIT License along with this program. If not, see
<http://www.opensource.org/licenses/mit-license.php>.

