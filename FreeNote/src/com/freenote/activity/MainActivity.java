package com.freenote.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.freenote.activity.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.Config;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Display mDisplay;  
    private WindowManager mWindowManager;  
    private DisplayMetrics mDisplayMetrics;  
    private Bitmap mScreenBitmap;  
    private Matrix mDisplayMatrix; 
	
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
				Toast.makeText(v.getContext(), "点击截图", Toast.LENGTH_LONG).show();
			}
		});
		
		mWindowManager.addView(imageView, wmParams);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private float getDegreesForRotation(int value) {  
        switch (value) {  
        case Surface.ROTATION_90:  
            return 360f - 90f;  
        case Surface.ROTATION_180:  
            return 360f - 180f;  
        case Surface.ROTATION_270:  
            return 360f - 270f;  
        }  
        return 0f;  
    }  
  
    private void takeScreenshot() {  
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);  
        mDisplay = mWindowManager.getDefaultDisplay();  
        mDisplayMetrics = new DisplayMetrics();  
        mDisplay.getRealMetrics(mDisplayMetrics);  
        mDisplayMatrix = new Matrix();  
        float[] dims = { mDisplayMetrics.widthPixels,  
                mDisplayMetrics.heightPixels };  
  
        int value = mDisplay.getRotation();
        String hwRotation = SystemProperties.get("ro.sf.hwrotation", "0");
        if (hwRotation.equals("270") || hwRotation.equals("90")) {  
            value = (value + 3) % 4;  
        }  
        float degrees = getDegreesForRotation(value);  
  
        boolean requiresRotation = (degrees > 0);  
        if (requiresRotation) {  
            // Get the dimensions of the device in its native orientation  
            mDisplayMatrix.reset();  
            mDisplayMatrix.preRotate(-degrees);  
            mDisplayMatrix.mapPoints(dims);  
  
            dims[0] = Math.abs(dims[0]);  
            dims[1] = Math.abs(dims[1]);  
        }  
  
        mScreenBitmap = Surface.screenshot((int) dims[0], (int) dims[1]);
  
        if (requiresRotation) {  
            // Rotate the screenshot to the current orientation  
            Bitmap ss = Bitmap.createBitmap(mDisplayMetrics.widthPixels,  
                    mDisplayMetrics.heightPixels, Bitmap.Config.ARGB_8888);  
            Canvas c = new Canvas(ss);  
            c.translate(ss.getWidth() / 2, ss.getHeight() / 2);  
            c.rotate(degrees);  
            c.translate(-dims[0] / 2, -dims[1] / 2);  
            c.drawBitmap(mScreenBitmap, 0, 0, null);  
            c.setBitmap(null);  
            mScreenBitmap = ss;  
        }  
  
        // If we couldn't take the screenshot, notify the user  
        if (mScreenBitmap == null) {  
            return;  
        }  
  
        // Optimizations  
        mScreenBitmap.setHasAlpha(false);  
        mScreenBitmap.prepareToDraw();  
          
        try {  
            saveBitmap(mScreenBitmap);  
        } catch (IOException e) {  
            System.out.println(e.getMessage());  
        }  
    }  
  
    public void saveBitmap(Bitmap bitmap) throws IOException {  
        String imageDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")  
                .format(new Date(System.currentTimeMillis()));  
        File file = new File("/mnt/sdcard/Pictures/"+imageDate+".png");  
        if(!file.exists()){  
            file.createNewFile();  
        }  
        FileOutputStream out;  
        try {  
            out = new FileOutputStream(file);  
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)) {  
                out.flush();  
                out.close();  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}
