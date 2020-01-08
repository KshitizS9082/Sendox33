import java.io.Serializable;
import java.math.BigInteger;


public class RSAPublicKey implements Serializable {

	private BigInteger N;
	 
	private BigInteger e;
	
	RSAPublicKey(BigInteger e, BigInteger N)
	{
		this.e = e;
		 
 
		this.N = N;
	}
	
	 public BigInteger getmodulus()
	 {
		 return N;
	 }
	 
	 public BigInteger getexponent()
	 {
		 return e;
	 }
	
	public byte[] encrypt(byte[] message) {
		 
		return (new BigInteger(message)).modPow(e, N).toByteArray();
 
	}
 
}