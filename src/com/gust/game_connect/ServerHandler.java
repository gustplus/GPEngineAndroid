package com.gust.game_connect;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class ServerHandler {
	private List<InputStream> inputs;
	private List<OutputStream> outputs;

	public ServerHandler()
	{
		inputs = new ArrayList<InputStream>();
		outputs = new ArrayList<OutputStream>();
	}

	public void addStream(InputStream input, OutputStream output)
	{
		this.inputs.add(input);
		this.outputs.add(output);
	}

	public void serverUpdate(float deltaTime)
	{
		int size = inputs.size();
		if(size == 0)
			return;
		InputStream in;
		OutputStream out;
		for (int i = 0; i < size; ++i) {
			in = inputs.get(i);
			out = outputs.get(i);
			serverPerUpdate(deltaTime, in, out);
		}
	}

	public abstract void serverPerUpdate(float deltaTime, InputStream in,
			OutputStream out);
}
