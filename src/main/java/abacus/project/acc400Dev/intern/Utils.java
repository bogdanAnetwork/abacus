package abacus.project.acc400Dev.intern;

import com.google.common.io.BaseEncoding;

import akka.util.ByteString;
/**
 * various useful methods for working and converting bytestrings and hex numbers 
 * @author BMA
 *
 */
public class Utils {
	// turn the Hex string into a ByteString
	public static ByteString HextoBS(String S) {
		return ByteString.fromArray(BaseEncoding.base16().decode(S));
	}

	// turn the ByteString into a Hex string
	public static String BStoHex(ByteString b) {
		return BaseEncoding.base16().encode(b.toArray());
	}
	// turn the binary strings (1 octet each) into ByteString
	public static ByteString BintoBS(String s1, String s2) {
		return ByteString.fromInts(Integer.parseInt(s1, 2), Integer.parseInt(s2, 2));
	}
	// turn the binary string (1 octet) into ByteString
	public static ByteString BintoBS(String s1) {
		return ByteString.fromInts(Integer.parseInt(s1, 2));
	}
	//public static int BStoInt(ByteString bs){
	//	return bs.toArray();
	//}
	//convert a ByteSintrg into a binary strings array
	//only used for two bytes now
	public static String[] BStoB(ByteString b){
		String[] bin = new String[2];
		for(int i = 0; i<b.toArray().length;i++)
			bin[i] =String.format("%8s", Integer.toBinaryString(b.toArray()[i] & 0xFF)).replace(' ', '0');
		return bin;
		
	}
	public static ByteString maskData(ByteString data, ByteString mask) {
		//if data and mask size does not correspond then return the original data unmasked
		if (data.size()!=mask.size()){
			System.out.println("mask and data are not the same ");	
			return data;
		}
		ByteString wrData = ByteString.empty();
		for (int i = 0; i < (data.toArray().length) ; i++)
			wrData = wrData
					.concat((ByteString.fromInts(data.toArray()[i] & mask.toArray()[i])));
		return wrData;
	}
}
