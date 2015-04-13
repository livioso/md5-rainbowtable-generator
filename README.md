[![Build Status](https://travis-ci.org/livioso/md5-rainbowtable-generator.svg?branch=master)](https://travis-ci.org/livioso/md5-rainbowtable-generator) 
[![Coverage Status](https://coveralls.io/repos/livioso/md5-rainbowtable-generator/badge.svg?branch=master)](https://coveralls.io/r/livioso/md5-rainbowtable-generator?branch=master)

### MD5 Rainbow Table Generator

A very simple rainbow table generator.

#### What is a Rainbow Table?

A rainbow table is a precomputed table for reversing cryptographic hash functions, usually for cracking password hashes. Tables are usually used in recovering a plaintext password up to a certain length consisting of a limited set of characters.
  [See Wikipedia](https://en.wikipedia.org/wiki/Rainbow_table).  
  
#### Build Instruction
  
In order to build the project simply do:
```
gradle build
```

To run the unit tests do:
```
gradle test
```

If you are using IDEA Intellij do: 
```
gradle idea
```
*This will create the `.ipr`, `.iml` and `.iws` files.*

