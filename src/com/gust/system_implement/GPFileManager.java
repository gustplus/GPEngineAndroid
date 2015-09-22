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
 * ������������ļ�����
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
	 * ��asset�ļ����ж�ȡ�ļ�
	 * @param fileName�ļ���
	 * @return������
	 */
	public InputStream readAsset(String fileName) throws IOException
	{
		// TODO Auto-generated method stub
		return assets.open(fileName);
	}
	
	/**
	 * �����ļ�����ȡ�ļ�
	 * @param fileName�ļ���
	 *  * @return������
	 */
	public InputStream readFile(String fileName) throws IOException
	{
		// TODO Auto-generated method stub
		return new FileInputStream(externalStoragePath + fileName);
	}
	
	/**
	 * �����ļ�id��ȡ�ļ�
	 * @param sourceId�ļ�ID
	 * @return������
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
