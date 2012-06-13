package tes.cam.packag;

import java.io.File;
import java.io.IOException;
import java.util.List;

import tes.cam.packag.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;






public class CamTestActivity extends Activity {
    /** Called when the activity is first created. */

    
    
    private Camera mCamera;
    private Camera nCamera;
    private CameraPreview mPreview;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording;
    private Button captureButton;
    private TextView tv_text1;
    private TextView tv_values1;
    private TextView tv_text2;
    private TextView tv_values2;
    private TextView tv_text3;
    private TextView tv_values3;
    private boolean mes;
    private FrameLayout preview;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tv_text1 = (TextView) findViewById(id.textView1);
        tv_values1 = (TextView) findViewById(id.textView2);
        tv_text2 = (TextView) findViewById(id.textView3);
        tv_values2 = (TextView) findViewById(id.textView4);
        tv_text3 = (TextView) findViewById(id.textView5);
        tv_values3 = (TextView) findViewById(id.textView6);     
        tv_values1.setText("");
        tv_values2.setText("");
        preview = (FrameLayout) findViewById(id.camera_preview);
        
        
        mes = false;

        // Create an instance of Camera
        mCamera = getCameraInstance();
        CamTestActivity.setCameraDisplayOrientation(this, 1, mCamera);
        
        

        
        
        

        //-->GETTING THE POSSIBLE RANGE
        Camera.Parameters cp =  mCamera.getParameters();
        List<int[]> l = cp.getSupportedPreviewFpsRange();
        if(l.size()==1)
        {
        		tv_text1.setText("FPSMin: ");
        		tv_values1.setText(String.valueOf(l.get(0)[Parameters.PREVIEW_FPS_MIN_INDEX]/1000));
        		tv_text2.setText("FPSMax: ");
        		tv_values2.setText(String.valueOf(l.get(0)[Parameters.PREVIEW_FPS_MAX_INDEX]/1000));
        }
        mCamera.setParameters(cp);
      //<--GETTING THE POSSIBLE RANGE




     // Add a listener to the Capture button
	     captureButton = (Button) findViewById(id.button_capture);
	     captureButton.setText("Start");
	     captureButton.setOnClickListener(
         new View.OnClickListener() {
             @Override
             public void onClick(View v) {
            	 changeMes();

             }
         }
         
     );

    }
    private void changeMes()
    {
 		preview.removeAllViews();
    	mCamera.setPreviewCallback(new PreviewCallback(tv_values3,preview));
        mCamera.startPreview();
        mPreview = new CameraPreview(this, mCamera);
        preview.addView(mPreview);

    }
    
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
        mCamera.release();
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    
    
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    
	public static void setCameraDisplayOrientation(Activity activity,
	         int cameraId, android.hardware.Camera camera) {
	     android.hardware.Camera.CameraInfo info =
	             new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay()
	             .getRotation();
	     int degrees = 90;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }
}