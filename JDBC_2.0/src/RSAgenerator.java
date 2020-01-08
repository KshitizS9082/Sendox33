import java.math.BigInteger;
 
import java.util.Random;
 
import java.io.*;
 
public class RSAgenerator {
 
	private BigInteger p;
 
	private BigInteger q;
 
	private BigInteger N;
 
	private BigInteger phi;
 
	private BigInteger e;
 
	private BigInteger d;
	
	private RSAPublicKey pub;
	
	private RSAPrivateKey pri;
 
	private int bitlength = 1024;
 
	private int blocksize = 256;
 
	//blocksize in byte
 
	private Random r;
 
	public RSAgenerator() {
 
		r = new Random();
		 
		p = BigInteger.probablePrime(bitlength, r);
 
		q = BigInteger.probablePrime(bitlength, r);
 
		N = p.multiply(q);
 
		phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
 
		e = BigInteger.probablePrime(bitlength/2, r);
 
		while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0 ) {
 
			e.add(BigInteger.ONE);
 
		}
 
		d = e.modInverse(phi);
		
		pub = new RSAPublicKey(e,N);
		
		pri= new RSAPrivateKey(e,d,N);
 
	}
	

 
	public RSAgenerator(BigInteger e, BigInteger d, BigInteger N) {
 
		this.e = e;
 
		this.d = d;
 
		this.N = N;
 
	}
 
	public static void main (String[] args) throws IOException {
 
	//	RSA rsa = new RSA();
 
	//	DataInputStream in=new DataInputStream(System.in);
 
	//	String teststring ;
 
	//	System.out.println("Enter the plain text:");
 
	//	teststring=in.readLine();//change this
 
	//	System.out.println("Encrypting String: " + teststring);
 
	//	System.out.println("String in Bytes: " + bytesToString(teststring.getBytes()));
 
		// encrypt
 
	//	byte[] encrypted = rsa.encrypt(teststring.getBytes());
 
	//	System.out.println("Encrypted String in Bytes: " + bytesToString(encrypted));
 
		// decrypt
 
	//	byte[] decrypted = rsa.decrypt(encrypted);
 
	//	System.out.println("Decrypted String in Bytes: " +  bytesToString(decrypted));
 
	//	System.out.println("Decrypted String: " + new String(decrypted));
 
	}
 
	private static String bytesToString(byte[] encrypted) {
 
		String test = "";
 
		for (byte b : encrypted) {
 
			test += Byte.toString(b);
 
		}
 
		return test;
 
	}
 
	//Encrypt message
 
	// Decrypt message
 

 
}