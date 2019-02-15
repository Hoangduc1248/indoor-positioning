package com.example.vlc_project_open_campus;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

class LEDInfo {
	private String name, bits;
	private int x, y;

	public LEDInfo(String name, int x, int y, String bits) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.bits = bits;
	}
	public void print(int n) {
		Log.i("LEDInfo", String.valueOf(n) + ": " + name + " " + String.valueOf(x) +
				" " + String.valueOf(y) + " " + bits);
	}
	public String getName() { return this.name; }
	public int getX() { return this.x; }
	public int getY() { return this.y; }	
	public String getBits() { return this.bits; }
}

public class LEDData {
	public Map<String, LEDInfo> data;

	public LEDData() {
		this.data = new HashMap<String, LEDInfo>();
	}

	public void put(String str){
		String[] array = str.split(",");
		data.put(array[0], new LEDInfo(array[0], Integer.parseInt(array[1]),
				Integer.parseInt(array[2]), array[3]));		
	}

	public LEDInfo get(String name){ return data.get(name); }
	public LEDInfo get2(String bits){
		for(String key : data.keySet()) {
			LEDInfo value = data.get(key);
			if(bits.equals(value.getBits())) return value;
		}
		return null;
	}
}
