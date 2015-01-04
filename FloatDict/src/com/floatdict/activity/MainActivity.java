package com.floatdict.activity;

import java.io.File;

import com.floatdict.activity.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
		WindowManager mWindowManager = (WindowManager) getApplication()
				.getSystemService(Context.WINDOW_SERVICE);
		wmParams.type = LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = mWindowManager.getDefaultDisplay().getWidth() - 20;
		wmParams.y = 200;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.tt);
		
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				ScreenShotFb.shoot(getSDCardPath() + "/FloatDict");
				Toast.makeText(v.getContext(), "点击截图", Toast.LENGTH_LONG).show();
			}
		});
		
		mWindowManager.addView(imageView, wmParams);
		
		ScreenShotFb.init(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    /**
     * 获取SDCard的目录路径功能
     * @return
     */
	private String getSDCardPath(){
		File sdcardDir = null;
		//判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment. MEDIA_MOUNTED);
		if(sdcardExist){
			sdcardDir = Environment. getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}


}
