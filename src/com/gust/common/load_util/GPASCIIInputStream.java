package com.gust.common.load_util;

import java.io.IOException;
import java.io.InputStream;

/**
 * ���ڶ�ȡASCII�����ʽ�ļ���������
 * @author gustplus
 */
public class GPASCIIInputStream {
	InputStream is;

	/**
	 * Constructor
	 * @param is �ײ��������
	 */
	public GPASCIIInputStream(InputStream is) {
		this.is = is;
	}

	/**
	 *��ȡһ��int
	 * @return ��ȡ��int
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
	 * ��ȡһ��unsigned short
	 * @return ��ȡ��unsigned short
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
	 *  ��ȡһ��signed short
	 * @return ��ȡ��signed short
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
	 * ��ȡһ��char/byte
	 * @return ��ȡ��char/byte
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
	 * ��ȡһ��float
	 * @return ��ȡ��float
	 * @throws IOException If the underlying InputStream throws an exception
	 */
	public final float readFloat() throws IOException {
		return Float.intBitsToFloat(this.readInt());
	}

	/**
	 * ��ȡһ��byte����
	 * @param ���ڴ����ȡ���������
	 * @return ��ȡ������ĳ���
	 * @throws IOException If the underlying InputStream throws an exception
	 */
	public int read(byte[] buff) 
	throws IOException
	{
		return is.read(buff);
	}

	/**
	 *��ȡString����
	 * @param Ҫ��ȡ��String����󳤶�
	 * @return ��ȡ��String
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