package com.gust.game_connect;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class CilentHandler {
	private InputStream input;
	private OutputStream output;

	public CilentHandler()
	{
	}

	public void setStream(InputStream input, OutputStream output)
	{
		this.input = input;
		this.output = output;
	}

	public void cilentUpdate(float deltaTime)
	{
		cilentPerUpdate(deltaTime, input, output);
	}

	public abstract void cilentPerUpdate(float deltaTime, InputStream in,
			OutputStream out);
}
