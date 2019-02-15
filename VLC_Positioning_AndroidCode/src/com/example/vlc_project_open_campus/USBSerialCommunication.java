package com.example.vlc_project_open_campus;

import java.io.IOException;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

public class USBSerialCommunication {
	
	private UsbManager manager;
	private UsbSerialDriver usb;
	private int rate;
	
	public USBSerialCommunication(int rate, Context context) {
		this.rate = rate;
		this.manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		this.usb = UsbSerialProber.acquire(manager);
	}
	public String run(){
		if(!this.open()) return null;
		String signal = this.readSignal();
		if(!this.close()) return null;
		return signal;
	}

	private boolean open() {
		if(usb == null) {
			Log.e("USB Serial", "Error: Cannot Open USB Device.");
			return false;
		}
		try {
			this.usb.open();
			this.usb.setBaudRate(this.rate);
		} catch (IOException e) {
			Log.e("USB Serial", "Error: Cannot Open USB Device.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private boolean close() {
		try {
			usb.close();
		} catch (IOException e) {
			Log.e("USB Serial", "Error: Cannot Close USB Device.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private String readSignal() {
		try {
			for(int i=0; i<100; ++i) {
				byte buf[] = new byte[256];
				int num = usb.read(buf, buf.length);
				if(num > 0) {
					String signal = new String(buf, 0, num);
					if(signal.charAt(0) == '0') return "0";
					if(signal.charAt(0) == '1') return "1";
				}
				Thread.sleep(50);
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new String();
	}
	
}
