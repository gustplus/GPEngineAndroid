package com.gust.game_connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.content.Context;
import android.widget.Toast;

public class MMOClient {
	private Context context;
	private InputStream input;
	private OutputStream output;
	private Socket cilent;
	private CilentHandler handler;

	public MMOClient(Context context, CilentHandler handler)
	{
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
	}

	public void setupConnection(String serverIp, int port)
	{

		try {
			cilent = new Socket(serverIp, port);
			input = cilent.getInputStream();
			output = cilent.getOutputStream();
			handler.setStream(input, output);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this.context, "无法连接到服务器,请确认网络配置", Toast.LENGTH_LONG)
					.show();
			try {
				input.close();
			}
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				output.close();
			}
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public boolean isAvailable(){
		if(cilent == null)
			return false;
		try {
			//向服务器端发送数据以判断连接是否断开
			cilent.sendUrgentData(0XFF);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public InputStream getInputStream()
	{
		return input;
	}

	public OutputStream getOutputStream()
	{
		return output;
	}

}
