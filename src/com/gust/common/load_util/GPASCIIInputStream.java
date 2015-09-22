package com.gust.common.load_util;

import java.io.IOException;
import java.io.InputStream;

/**
 * 用于读取ASCII编码格式文件的输入流
 * @author gustplus
 */
public class GPASCIIInputStream {
	InputStream is;

	/**
	 * Constructor
	 * @param is 底层的输入流
	 */
	public GPASCIIInputStream(InputStream is) {
		this.is = is;
	}

	/**
	 *读取一个int
	 * @return 读取的int
	 * @throws IOException If the underlying InputStream throws an exception
	 */
	public int readInt() 
	throws IOException
	{
		int b1 = is.read();
		int b2 = is.read();
		int b3 = is.read();
		int b4 = is.read();

		return (b4 << 24) 
		+ ((b3 << 24) >>> 8) 
		+ ((b2 << 24) >>> 16) 
		+ ((b1 << 24) >>> 24);
	}

	/**
	 * 读取一个unsigned short
	 * @return 读取的unsigned short
	 * @throws IOException If the underlying InputStream throws an exception
	 */
	public short readUnsignedShort() 
	throws IOException 
	{
		int b1 = is.read();
		int b2 = is.read();
		if (b1 < 0)
			b1 += 256;
		if (b2 < 0)
			b2 += 256;

		return (short)(b2*256+b1);
	}

	/**
	 *  读取一个signed short
	 * @return 读取的signed short
	 * @throws IOException If the underlying InputStream throws an exception
	 */
	public short readShort()
	throws IOException
	{
		int b1 = is.read();
		int b2 = is.read();
		if (b1 < 0)
			b1 += 256;

		return (short)(b2*256+b1);
	}

	/**
	 * 读取一个char/byte
	 * @return 读取的char/byte
	 * @throws IOException If the underlying InputStream throws an exception
	 */
	public char readUnsignedChar()
	throws IOException
	{
		int b = is.read();
		if (b < 0)
			b+=256;
		return (char)b;
	}

	/**
	 * 读取一个float
	 * @return 读取的float
	 * @throws IOException If the underlying InputStream throws an exception
	 */
	public final float readFloat() throws IOException {
		return Float.intBitsToFloat(this.readInt());
	}

	/**
	 * 读取一个byte数组
	 * @param 用于储存读取结果的数组
	 * @return 读取的数组的长度
	 * @throws IOException If the underlying InputStream throws an exception
	 */
	public int read(byte[] buff) 
	throws IOException
	{
		return is.read(buff);
	}

	/**
	 *读取String类型
	 * @param 要读取的String的最大长度
	 * @return 读取的String
	 * @throws IOException If the underlying InputStream throws an exception
	 */
	public String readString(int length) 
	throws IOException
	{
		byte[] buff = new byte[length];
		is.read(buff);
		return new String(buff);
	}
}