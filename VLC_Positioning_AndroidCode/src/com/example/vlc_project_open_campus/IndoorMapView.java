package com.example.vlc_project_open_campus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class IndoorMapView extends View
	implements GestureDetector.OnGestureListener,
				GestureDetector.OnDoubleTapListener {

	private static final float DOUBLE = 2;
	private static final String ROOM_INFO_FILE = "ROOM-Info_F3.csv";
	private static final String POSITION_INFO_FILE = "PS-Info_F3.csv";
	private static final String CONNECTION_INFO_FILE = "CN-Info_F3.csv";
	
	private float WIDTH, HEIGHT;
	private float width, height;
	private float x, y;
	private float ratio;
	private boolean zoom_up;
	
	private String current, destination;
	private AlertDialog.Builder debug_dialog, warning_dialog;
	private Bitmap bmp1, bmp2;
	private GestureDetector gd;
	
	private Map<String, String> room_name;
	private LEDData led_data;
	private PathFinding path_finding;
	
	public IndoorMapView(Context context, float WIDTH, float HEIGHT) {
		super(context);
		this.setFocusable(true);
		
		// Display size
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		
		this.x = 0;
		this.y = 0;
		this.current = null;
		this.destination = null;
		
		// Dialog
		this.debug_dialog = new AlertDialog.Builder(context);
		this.debug_dialog.setTitle("DEBUG MODE");
		this.debug_dialog.setMessage(
				"Cannot connect Arduino via USB serial. The Application will be changed DEBUG MODE.");
		this.debug_dialog.setPositiveButton("OK", null);
		
		this.warning_dialog = new AlertDialog.Builder(context);
		this.warning_dialog.setTitle("WARNING!");
		this.warning_dialog.setMessage(
				"Cannot Find the Destination or the Current Locaiton.");
		this.warning_dialog.setPositiveButton("OK", null);
		
		Vector<String> lines = new Vector<String>();
		// Room Size & LED data
		lines = this.read(POSITION_INFO_FILE);
		this.led_data = new LEDData();
		for(int i=0; i<lines.size(); i++){
			if(i<3) {
				String[] tmp = lines.get(i).split(",");
				if(tmp[0].equals("WIDTH")) this.width = Integer.parseInt(tmp[1]);
				if(tmp[0].equals("HEIGHT")) this.height = Integer.parseInt(tmp[1]);
			} else {
				this.led_data.put(lines.get(i));
			}
		}
		this.ratio = this.WIDTH/this.width;
		this.zoom_up = false;
		
		// Room name & Node
		lines = this.read(ROOM_INFO_FILE);
		this.room_name = new HashMap<String, String>();
		for(int i=1; i<lines.size(); i++){
			String[] tmp = lines.get(i).split(",");
			this.room_name.put(tmp[0], tmp[1]);
		}
		
		// Path Setting
		lines = this.read(CONNECTION_INFO_FILE);
		this.path_finding = new PathFinding(lines);
		
		// Get PNG map.
		Resources res = this.getContext().getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.map_f3);
		this.bmp1 = Bitmap.createScaledBitmap(
				bmp, (int)WIDTH, (int)(height*ratio), false);
//		this.bmp2 = Bitmap.createScaledBitmap(
//				bmp, (int)(WIDTH*DOUBLE), (int)(height*ratio*DOUBLE), false);
		this.gd = new GestureDetector(context, this);
	}
		
	public void setCurrentLocation(){
		this.current = null;
		USBSerialCommunication usb_serial
			= new USBSerialCommunication(9600, this.getContext());
		String signal = usb_serial.run();

		// NO INCOMING SIGNAL 
		if(signal == null) {
			this.current = "EA"; // DEBUG MODE open
			this.debug_dialog.show(); // Show Dialog
			return;
		}
		if(signal.isEmpty()){
			this.warning_dialog.show(); // Show Dialog
			return;
		}
		LEDInfo info = led_data.get2(signal);
		if(info == null) return;
		this.current = info.getName();
	}
	public void setDestination(String destination){
		path_finding.clear();
		if(!destination.isEmpty() && this.current != null &&
				!this.current.equals(destination) &&
				this.room_name.containsKey(destination) &&
				this.led_data.data.containsKey(this.room_name.get(destination))) {
			this.destination = this.room_name.get(destination);
			path_finding.setTree(this.destination);
		} else {
			this.warning_dialog.show(); // Show Dialog
			this.destination = null;
		}
	}
	
	private Vector<String> read(String file_name) {
		Vector<String> lines = new Vector<String>();
		AssetManager as = getResources().getAssets();
		try {
			InputStream is = as.open(file_name);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str;
			while((str = br.readLine()) != null) lines.add(str);
			br.close();
		} catch(IOException e) {
			Log.e("FileRead", "Cannot open " + file_name + ".");
		}
		return lines;
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);

		Paint paint = new Paint();
		canvas.drawBitmap(bmp1, -x, -y, paint);
		
		float ratio_ = (this.zoom_up ? this.ratio*2 : this.ratio);

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(6);
		
		// Draw path line.
		if(this.current != null && this.destination != null &&
				path_finding.getParent(this.current) != null){
			LEDInfo value1, value2;
			value1 = led_data.get(current);
			String name=current, parent=null;
			while(true){
				parent = path_finding.getParent(name);
				if(name.equals(parent)) break;
				value2 = led_data.get(parent);
				canvas.drawLine(value1.getX()*ratio_-x, value1.getY()*ratio_-y,
						value2.getX()*ratio_-x, value2.getY()*ratio_-y, paint);
				value1 = value2;
				name = parent;
			}
		}

		float radius = (this.zoom_up ? 20.0f : 10.0f);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		
		if(this.current != null && led_data.data.containsKey(this.current)) {
			LEDInfo value = led_data.get(current);
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.BLUE);
			canvas.drawCircle(
					value.getX()*ratio_-x, value.getY()*ratio_-y, radius, paint);
		}
		if(this.destination != null && led_data.data.containsKey(this.destination)) {
			LEDInfo value = led_data.get(destination);
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.RED);
			canvas.drawCircle(
					value.getX()*ratio_-x, value.getY()*ratio_-y, radius, paint);
		}
	}
	
	// Touch Event
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		Log.d("TouchEvent", "X:" + event.getX() + ",Y:" + event.getY());
		if(this.gd.onTouchEvent(event)) return true;
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		/*
		Log.d("onDoubleTap","onDoubleTap");
		this.zoom_up = !this.zoom_up;
		if(this.zoom_up) {
			this.x = Math.min(WIDTH, Math.max(
					0, (this.x+e.getX())*DOUBLE - WIDTH/DOUBLE));
			this.y = Math.min(2*HEIGHT-WIDTH, Math.max(
					0, (this.y+e.getY())*DOUBLE - HEIGHT/DOUBLE));
		} else {
			this.x = 0;
			this.y = Math.min(HEIGHT-WIDTH, Math.max(
					0, (this.y+e.getY()-HEIGHT)/DOUBLE));
		}
		this.invalidate();
		*/
		return true;
	}
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
			float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		Log.i("onScroll", "X: " + distanceX + ", Y: " + distanceY);
		if(this.zoom_up) {
			this.x = Math.min(WIDTH, Math.max(0, this.x+distanceX));
			this.y = Math.min(2*HEIGHT-WIDTH, Math.max(0, this.y+distanceY));
		} else {
			this.x = 0;
			this.y = Math.min(HEIGHT-WIDTH, Math.max(0, this.y+distanceY));
		}
		this.invalidate();
		return true;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2,
			float velocityX, float velocityY) {
		// TODO Auto-generated method stub
		return true;
	}
}
