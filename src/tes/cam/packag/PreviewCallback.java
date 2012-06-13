package tes.cam.packag;

import android.hardware.Camera;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;


public class PreviewCallback implements android.hardware.Camera.PreviewCallback{

	private int count;
	private TextView val;
	private FrameLayout preview;
	private long tm;
	public PreviewCallback(TextView tv, FrameLayout preview)
	{
		count=0;
		val = tv;
		this.preview = preview;
		tm = System.currentTimeMillis();
	}
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		count++;
		if((System.currentTimeMillis()-tm)>=1000)
		{
			this.tm = System.currentTimeMillis();
			val.setText(String.valueOf(count));
			count = 0;
		}
		else
		{
			//val.setText("tm: "+String.valueOf((System.currentTimeMillis()-tm)));
		}
	}

}
