/**
 * ScreenShotFb.java
 * ��Ȩ����(C) 2014
 * ������:cuiran 2014-4-3 ����4:55:23
 */
package com.floatdict.activity;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;






import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

/**
 * FrameBuffer�л�ȡAndroid��Ļ��ͼ 
 * @author cuiran
 * @version 1.0.0
 */
public class ScreenShotFb {

	private static final String TAG="ScreenShotFb";
	
	final static String FB0FILE1 = "/dev/graphics/fb0";

	static File fbFile;
	static DataInputStream dStream=null;
	static byte[] piex=null;
	static int[] colors =null;
	static int screenWidth;
	static int screenHeight;
	static int deepth;
	  //�������  
    public static  void shoot(String path){  
    	 try {  
    		 /************ ���������� ************/
    	        final Object lock = new Object();
    	        
    	    	synchronized (lock) {
    	    		long start=System.currentTimeMillis();
    	    		Bitmap bitmap=getScreenShotBitmap();
    	    		if(bitmap == null){
    	             	Log.e(TAG, "Bitmap is null");
    	    			return;
    	    		}
    	        	long end=System.currentTimeMillis();
    	        	Log.i(TAG, "getScreenShotBitmap time is :"+(end-start)+" ms");
    	        	String filePath= path + "/" + System.currentTimeMillis() + ".jpg";
//    	        	String filePath= ConstantValue.ROOT_SDCARD_DIR+"/screens/"+System.currentTimeMillis()+".png";
    	        	ScreenShotFb.savePic(bitmap,filePath);  
    	    	}
         }catch (Exception e) {  
         	Log.e(TAG, "Exception error",e);
         }
    }  
    
	/**���浽sdcard 
	 * @param b ���洢bmpͼƬ
	 * @param strFileName  �洢����·��
	 */
	public static void savePic(Bitmap b,String strFileName){  
        FileOutputStream fos = null;  
        try {  
            fos = new FileOutputStream(strFileName);  
            if (null != fos)  
            {  
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();  
                fos.close();  
            }  
        } catch (FileNotFoundException e) {  
        	Log.e(TAG, "FileNotFoundException error",e);
        } catch (IOException e) {  
        	Log.e(TAG, "IOException error",e);
        }  
        
        Log.i(TAG, "savePic success");
    }  
	
	 public static void init(Activity activity){
			try {
			
				DisplayMetrics dm = new DisplayMetrics();
				Display display = activity.getWindowManager().getDefaultDisplay();
				display.getMetrics(dm);
				screenWidth = dm.widthPixels; // ��Ļ�����أ��磺480px��
				screenHeight = dm.heightPixels; // ��Ļ�ߣ����أ��磺800p��
				int pixelformat = display.getPixelFormat();
				PixelFormat localPixelFormat1 = new PixelFormat();
				PixelFormat.getPixelFormatInfo(pixelformat, localPixelFormat1);
				deepth = localPixelFormat1.bytesPerPixel;// λ��
				Log.i(TAG, "deepth="+deepth);
				piex = new byte[screenHeight * screenWidth * deepth] ;// ����
				colors = new int[screenHeight * screenWidth];
				
				String apkRoot="chmod 777 " + FB0FILE1;
				RootCmd(apkRoot);
				
			}catch(Exception e){
				Log.e(TAG, "Exception error",e);
			}
	 }
	 
	public static synchronized Bitmap getScreenShotBitmap() {
		FileInputStream buf = null;
		int r, g, b, a;
		try {
			fbFile = new File(FB0FILE1);
			buf = new FileInputStream(fbFile);// ��ȡ�ļ�����
			dStream=new DataInputStream(buf);
			dStream.readFully(piex);
			dStream.close();
			// ��rgbתΪɫֵ
			for(int i=0;i<piex.length;i+=4)
	        {
				if(i%4 == 0){
                	b = (piex[i] & 0xFF);
                	g = (piex[i+1] & 0xFF);
                    r = (piex[i+2] & 0xFF);
                    a = (piex[i+3] & 0xFF);
                    colors[i/4]= (a << 24) + (r <<16) + (g <<8) + b;
                }
	        }

	       // �õ���Ļbitmap
		return Bitmap.createBitmap(colors, screenWidth, screenHeight,
					Bitmap.Config.ARGB_8888);
			
		} catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFoundException error",e);
		} catch (IOException e) {
			Log.e(TAG, "IOException error",e);
		}catch (Exception e) {
			Log.e(TAG, "Exception error",e);
		}  
		finally {
			if(buf!=null){
				try {
					buf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
    /**
     * ��ȡrootȨ��
     * @param cmd ��Ҫִ�е�ָ��
     */
	public static boolean RootCmd(String cmd){  
        Process process = null;  
        DataOutputStream os = null;  
        try{  
            process = Runtime.getRuntime().exec("su");  
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd+ "\n");  
            os.writeBytes("exit\n");  
            os.flush();  
            process.waitFor();  
        } catch (Exception e) {  
            return false;  
        } finally {  
            try {  
                if (os != null)   {  
                    os.close();  
                }  
                process.destroy();  
            } catch (Exception e) {  
            }  
        }  
        return true;  
    }
  
}
