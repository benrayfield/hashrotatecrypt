/** Ben F Rayfield offers this software opensource MIT license */
package immutable.hashrotatecrypt;
import static mutable.util.Lg.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import immutable.util.Text;

public class HashRotateCrypt{
	
	public static byte[] cryptBig(byte[] cryptMe, int blockSize, byte[] symmetricPassword){
		throw new Error("TODO make last block up to blockSize*2-1 so no padding, works for any byte size");
	}
	
	public static byte[] encrypt(byte[] cryptMe, byte[] symmetricPassword){
		return crypt(cryptMe.length*2, cryptMe, symmetricPassword);
	}
	
	public static byte[] decrypt(byte[] cryptMe, byte[] symmetricPassword){
		return crypt(-cryptMe.length*2, cryptMe, symmetricPassword);
	}
	
	/** BigO(cryptMe.length*abs(cyclesPositiveOrNegative)).
	cyclesPositiveOrNegative must be at least cryptMe.length.
	A good value is cryptMe.length*2.
	Negate cyclesPositiveOrNegative to decrypt.
	You would normally do this to blocks such as
	size 56 (max block size for 1 sha256 cycle per loop body)
	*/
	public static byte[] crypt(int cyclesPositiveOrNegative, byte[] cryptMe, byte[] symmetricPassword){
		byte[] b = cryptMe.clone();
		MessageDigest hasher = null;
		try{
			hasher = MessageDigest.getInstance("SHA-256");
		}catch (NoSuchAlgorithmException e){ throw new Error(e); }
		final boolean doubleHash = true;
		final int repeatInputAndPasswordNTimes = 2;
		int cycles = b.length*2;
		while(Math.abs(cyclesPositiveOrNegative) != 0){
			//hash concat(b[1..end],symmetricPassword)
			//and use 1 of those bytes to xor b[0], then rotate b by 1 byte.
			boolean forward = cyclesPositiveOrNegative > 0;
			
			if(!forward){ //rotate by 1 byte other direction
				byte temp = b[b.length-1];
				System.arraycopy(b, 0, b, 1, b.length-1);
				b[0] = temp;
				cyclesPositiveOrNegative++;
			}
			
			for(int repeatInput=0; repeatInput<repeatInputAndPasswordNTimes; repeatInput++){
				hasher.update(b, 1, b.length-1);
				hasher.update(symmetricPassword);
			}
			byte[] hash = hasher.digest();
			if(doubleHash){
				hasher.reset();
				hasher.update(hash);
				hash = hasher.digest();
				//use doubleSha256 cuz might have saw patterns in it with single sha,
				//near hex digits being the same too often.
				//doubleSha256 is used in proofOfWork so is safe for this.
			}
			byte hashByte = hash[hash.length-1]; //TODO which is better, the start or the end of sha256?
			b[0] ^= hashByte;
			
			if(forward){ //rotate by 1 byte
				byte temp = b[0];
				System.arraycopy(b, 1, b, 0, b.length-1);
				b[b.length-1] = temp;
				cyclesPositiveOrNegative--;
			}
			
			if(cyclesPositiveOrNegative != 0) hasher.reset();
		}
		return b;
	}
	
	public static void main(String... args){
		lg("Testing "+HashRotateCrypt.class);
		String cryptMe = "The quick brown fox jumps over the lazy dog";
		//String password = "password";
		String password = "password";
		byte[] encrypted = encrypt(Text.stringToBytes(cryptMe), Text.stringToBytes(password));
		String encryptedHex = Text.bytesToHex(encrypted);
		lg("cryptMe: "+cryptMe);
		lg("cryptMe to bytes then back to string: "+Text.bytesToString(Text.stringToBytes(cryptMe)));
		lg("password: "+password);
		lg("encryptedHex: "+encryptedHex);
		lg("encryptedString: "+Text.bytesToString(encrypted)); //FIXME this is usually not valid utf8 so will that throw?
		byte[] decrypted = decrypt(encrypted,Text.stringToBytes(password));
		String decryptedString = Text.bytesToString(decrypted);
		lg("DecryptedString: "+decryptedString);
		if(!decryptedString.equals(cryptMe)) throw new Error("Decrypted to ["+decryptedString+"] not equal original["+cryptMe+"]");
	}

}
