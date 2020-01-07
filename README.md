# hashrotatecrypt
A symmetric crypto algorithm, on any number of bytes and any password size, bigO(number of bytes SQUARED), that uses double-sha256 on concat2Of(concat(all bytes except the first, password)) to choose a byte to xor the first byte, then rotate by 1 byte and repeat until its rotated 2 times around blockSize.

The main logic is in https://github.com/benrayfield/hashrotatecrypt/blob/master/immutable/hashrotatecrypt/HashRotateCrypt.java

I might make small adjustments to the algorithm, so dont count on it staying exactly the same yet. Still needs testing and use in blocks such as block size 56 is most efficient.

Testing class immutable.hashrotatecrypt.HashRotateCrypt

cryptMe: The quick brown fox jumps over the lazy dog

cryptMe to bytes then back to string: The quick brown fox jumps over the lazy dog

password: password

encryptedHex: 15632eaf266284a5934bcb52570b6d237961a28456383c663c452be61967a2c978e38c6d92541df38f1960

encryptedString: c.�&b���K�RWm#ya��V8<f<E+�g��x�m�T�`

DecryptedString: The quick brown fox jumps over the lazy dog
