# hashrotatecrypt
A symmetric crypto algorithm, on any number of bytes and any password size, bigO(number of bytes SQUARED), that uses sha256 on all bytes except the first to choose a byte to xor the first byte, then rotate by 1 byte and repeat until its rotated 2 times

I might make small adjustments to the algorithm, so dont count on it staying exactly the same yet. Still needs testing and use in blocks such as block size 56 is most efficient.

Testing class immutable.hashrotatecrypt.HashRotateCrypt

cryptMe: The quick brown fox jumps over the lazy dog

cryptMe to bytes then back to string: The quick brown fox jumps over the lazy dog

password: password

encryptedHex: 5a824f18a851c2b219a293bb8978176783ef339395f979f48dd1e00a144ba30db25b1696a1557603fb8ee8

encryptedString: Z�O�Q²����xg��3���y���
K�
�[��Uv���

DecryptedString: The quick brown fox jumps over the lazy dog
