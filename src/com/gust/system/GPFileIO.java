package com.gust.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;


public interface GPFileIO {
	public Bitmap readBitmap(String fileName);
	
	public InputStream readAsset(String fileName)throws IOException;
	
	public InputStream readFile(String fileName)throws IOException;
	
	public InputStream readDrawable(int drawableId)throws IOException;
	
	public OutputStream writeFile(String fileName)throws IOException;
}
