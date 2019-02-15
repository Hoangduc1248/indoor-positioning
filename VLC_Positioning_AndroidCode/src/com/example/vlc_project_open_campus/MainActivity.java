package com.example.vlc_project_open_campus;

import android.support.v7.app.ActionBarActivity;
//import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
//import android.os.Build;

public class MainActivity
	extends ActionBarActivity implements OnClickListener {
	
	private static final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	
	private float WIDTH, HEIGHT;
	private EditText edit;
	private ImageButton button1, button2;
	private IndoorMapView map_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Display size
		WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		Point p = new Point();
		disp.getSize(p);
		this.WIDTH = p.x;
		this.HEIGHT = p.y;
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout);
		
		LinearLayout layoutin = new LinearLayout(this);
		layoutin.setOrientation(LinearLayout.HORIZONTAL);
		
		int size = Math.min(100, Math.max(20, (int)(WIDTH/10)));
		
		edit = new EditText(this);
		edit.setWidth((int)WIDTH-4*size);
		edit.setHeight((int)(1.5*size));
		edit.setTextSize(18.0f);
		layoutin.addView(edit, new LinearLayout.LayoutParams(WC, WC));
		
		Resources res = this.getResources();
		Bitmap bmp1 = BitmapFactory.decodeResource(res, R.drawable.search_icon);
		Bitmap bmp2 = BitmapFactory.decodeResource(res, R.drawable.update_icon);
		bmp1 = Bitmap.createScaledBitmap(bmp1, size, size, false);
		bmp2 = Bitmap.createScaledBitmap(bmp2, size, size, false);		
		
		button1 = new ImageButton(this);
		button1.setImageBitmap(bmp1);
		button1.setColorFilter(Color.rgb(0, 0, 128));
		button1.setOnClickListener(this);
		layoutin.addView(button1, new LinearLayout.LayoutParams(WC, WC));
		
		button2 = new ImageButton(this);
		button2.setImageBitmap(bmp2);
		button2.setColorFilter(Color.rgb(0, 0, 128));
		button2.setOnClickListener(this);
		layoutin.addView(button2, new LinearLayout.LayoutParams(WC, WC));
		
		layout.addView(layoutin,new LinearLayout.LayoutParams(WC, WC));

		map_view = new IndoorMapView(this, WIDTH, HEIGHT);
		layout.addView(map_view, new LinearLayout.LayoutParams(WC, WC));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==button1) {
			String str = ((SpannableStringBuilder)edit.getText()).toString();
			Log.i("Button1", "Search!");
			Log.i("TextEdit", str);
			map_view.setDestination(str);
			map_view.invalidate();
			return;
		}
		if(v==button2) {
			Log.i("Button2", "Update!");
			map_view.setCurrentLocation();
			map_view.invalidate();
			return;
		}
	}

}
