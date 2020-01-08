package com.example.sendox33;

import java.math.BigInteger;


public class RSAPrivateKey {

	private BigInteger N;
 
	private BigInteger e;
	
	private BigInteger d;
	
	public RSAPrivateKey(BigInteger e, BigInteger d, BigInteger N) {
		 
		this.e = e;
 
		this.d = d;
 
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
	public BigInteger getd()
	{
		return d;
	}
	
	public byte[] decrypt(byte[] message) {
		 
		return (new BigInteger(message)).modPow(d, N).toByteArray();
 
	}
}
