package com.gust.game_connect;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理从服务器端或客户端取得的数据
 * @author gustplus
 *
 */
public abstract class NetWorkHandler {
	private List<InputStream> inputs;
	private List<OutputStream> outputs;
	public NetWorkHandler(){
		inputs = new ArrayList<InputStream>();
		outputs = new ArrayList<OutputStream>();
	}
	
	/**
	 * 添加连接服务器端或客户端而取得的输入输出流
	 * @param input输入流
	 * @param output输出流
	 */
	public void addStream(InputStream input, OutputStream output){
		this.inputs.add(input);
		this.outputs.add(output);
	}
	
	/**
	 * 根据取得的数据更新服务器（当该主机为服务器端时使用），实际调用serverPerUpdate（）方法
	 * @param deltaTime
	 */
	public void serverUpdate(float deltaTime){
		int size = inputs.size();
		if(size == 0)
			return;
		InputStream in;
		OutputStream out;
		for(int i= 0;i<size;++i){
			 in = inputs.get(i);
			 out = outputs.get(i);
			 serverPerUpdate(deltaTime, in, out);
		}
	}
	/**
	 * 根据取得的数据更新客户端（当该主机为客户端时使用），实际调用cilentPerUpdate（）方法
	 * @param deltaTime
	 */
	public void cilentUpdate(float deltaTime){
		if (inputs.size() != 0) {
			InputStream in = inputs.get(0);
			OutputStream out = outputs.get(0);
			cilentPerUpdate(deltaTime, in, out);
		}
	}
	
	/**
	 * 服务器端对每一客户端传来的数据进行更新
	 * @param deltaTime
	 * @param in
	 * @param out
	 */
	public abstract void serverPerUpdate(float deltaTime, InputStream in, OutputStream out);
	
	/**
	 * 客户端对服务器端传来的数据进行更新
	 * @param deltaTime
	 * @param in
	 * @param out
	 */
	public abstract void cilentPerUpdate(float deltaTime, InputStream in, OutputStream out);
}
