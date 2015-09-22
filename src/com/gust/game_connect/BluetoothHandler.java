package com.gust.game_connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

public class BluetoothHandler {

	private Activity activity;
	private BluetoothAdapter bluetooth;
	private Set<BluetoothDevice> connectedList;
	private int maxConnectNum;
	private int connectedNum;
	private final UUID MY_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	public BluetoothHandler(Activity activity, int maxConnectNum)
	{
		this.maxConnectNum = maxConnectNum;
		this.activity = activity;
		bluetooth = BluetoothAdapter.getDefaultAdapter();
		if (bluetooth != null) {
			if (!bluetooth.isEnabled()) {
				Intent openBluetooth = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				this.activity.startActivity(openBluetooth);
			}
		} else {
			Toast.makeText(activity,
					"sorry,your device doesn't have this function!",
					Toast.LENGTH_SHORT).show();
		}
		connectedList = new HashSet<BluetoothDevice>();
		IntentFilter bluetoothFoundFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		activity.registerReceiver(bluetoothReceiver, bluetoothFoundFilter);

	}

	public void startScan()
	{
		if (bluetooth.isDiscovering())
			bluetooth.cancelDiscovery();
		bluetooth.startDiscovery();
	}
	
	public boolean isDiscovering(){
		return bluetooth.isDiscovering();
	}

	public void setDiscoverable(int duration)
	{
		Intent setBtDiscoverable = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		setBtDiscoverable.putExtra(
				BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
		activity.startActivity(setBtDiscoverable);
	}

	public void connectAsCilent(BluetoothDevice device,CilentHandler handler)
	{
		BluetoothSocket cilentSocket;
		try {
			cilentSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			cilentSocket.connect();
			InputStream input = cilentSocket.getInputStream();
			OutputStream output = cilentSocket.getOutputStream();
			handler.setStream(input, output);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BluetoothDevice getDevice(String address){
		return bluetooth.getRemoteDevice(address);
	}

	public void connectAsServer(String name,final ServerHandler handler)
	{
		final BluetoothServerSocket serverSocket;
		try {
			Log.e("", "run0");
			serverSocket = bluetooth.listenUsingRfcommWithServiceRecord(name,
					MY_UUID);
				new Thread(new Runnable()
				{
					
					public void run()
					{
						// TODO Auto-generated method stub
						BluetoothSocket socket;
						try {
							while(connectedNum<maxConnectNum){
							socket = serverSocket.accept();
							++connectedNum;
							createServerConnection(socket,handler);}
						}
						catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createServerConnection(final BluetoothSocket socket,final ServerHandler handler)
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				// TODO Auto-generated method stub
				try {
					InputStream input = socket.getInputStream();
					OutputStream output = socket.getOutputStream();
					handler.addStream(input, output);
					Log.e("", "connected");
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
			if (connectedNum >= maxConnectNum)
				return;
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				connectedList.add(device);
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {

			}

		}
	};
	
	public Set<BluetoothDevice> getFoundDevices(){
		return connectedList;
	}

}
