package com.gust.game_connect;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ���ڴ���ӷ������˻�ͻ���ȡ�õ�����
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
	 * ������ӷ������˻�ͻ��˶�ȡ�õ����������
	 * @param input������
	 * @param output�����
	 */
	public void addStream(InputStream input, OutputStream output){
		this.inputs.add(input);
		this.outputs.add(output);
	}
	
	/**
	 * ����ȡ�õ����ݸ��·���������������Ϊ��������ʱʹ�ã���ʵ�ʵ���serverPerUpdate��������
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
	 * ����ȡ�õ����ݸ��¿ͻ��ˣ���������Ϊ�ͻ���ʱʹ�ã���ʵ�ʵ���cilentPerUpdate��������
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
	 * �������˶�ÿһ�ͻ��˴��������ݽ��и���
	 * @param deltaTime
	 * @param in
	 * @param out
	 */
	public abstract void serverPerUpdate(float deltaTime, InputStream in, OutputStream out);
	
	/**
	 * �ͻ��˶Է������˴��������ݽ��и���
	 * @param deltaTime
	 * @param in
	 * @param out
	 */
	public abstract void cilentPerUpdate(float deltaTime, InputStream in, OutputStream out);
}
