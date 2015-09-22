package com.gust.system_implement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.gust.system.GPFileIO;

/**
 * 用于输入输出文件的类
 * @author gustplus
 *
 */
public class GPFileManager implements GPFileIO {
	private static GPFileManager manager;
	private AssetManager assets;
	private Context context;
	
	private String externalStoragePath;
	
	public static void create(Context context){
		if(manager == null){
			manager = new GPFileManager(context);
		}
	}
	
	public static GPFileManager getinstance(){
		return manager;
	}
	
	public AssetManager getAssetManager(){
		return assets;
	}
	
	private GPFileManager(Context context){
		this.context = context;
		this.assets = context.getAssets();
		this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}
	
	public Bitmap readBitmap(String fileName)
	{
		// TODO Auto-generated method stub
		InputStream in = null;
		try {
			in = assets.open(fileName);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return BitmapFactory.decodeStream(in);
	}
	
	/**
	 * 从asset文件夹中读取文件
	 * @param fileName文件名
	 * @return输入流
	 */
	public InputStream readAsset(String fileName) throws IOException
	{
		// TODO Auto-generated method stub
		return assets.open(fileName);
	}
	
	/**
	 * 根据文件名读取文件
	 * @param fileName文件名
	 *  * @return输入流
	 */
	public InputStream readFile(String fileName) throws IOException
	{
		// TODO Auto-generated method stub
		return new FileInputStream(externalStoragePath + fileName);
	}
	
	/**
	 * 根据文件id读取文件
	 * @param sourceId文件ID
	 * @return输入流
	 */
	public InputStream readDrawable(int drawableId) throws IOException
	{
		// TODO Auto-generated method stub
		InputStream is = context.getResources().openRawResource(drawableId);
		return is;
	}
	public OutputStream writeFile(String fileName) throws IOException
	{
		// TODO Auto-generated method stub
		return new FileOutputStream(externalStoragePath + fileName);
	}
	
	public AssetFileDescriptor getFileDescriptor(String assetsName) throws IOException{
		return assets.openFd(assetsName);
	}

}
