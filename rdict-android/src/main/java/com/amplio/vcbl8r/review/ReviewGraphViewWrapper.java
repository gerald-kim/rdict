package com.amplio.vcbl8r.review;

import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplio.vcbl8r.R;

public class ReviewGraphViewWrapper {
	public static final Number[] DEMO_DATA_CARD_COUNT = new Number[]{0, 3, 5, 12, 12, 
																	12, 13, 16, 16, 16,
																	22, 25, 26, 29, 37,
																	37, 37, 38, 42, 43,
																	43, 45, 47, 51, 52,
																	55, 57, 59, 64, 66};
	
	public static final Number[] DEMO_DATA_GRADE = new Number[]{20, 25, 20, 30, 40, 
																50, 55, 60, 60, 60,
																75, 65, 70, 70, 75,
																80, 70, 72, 89, 90,
																85, 85, 80, 85, 85,
																75, 80, 85, 85, 90};
	
	public static final int WIDTH = 230;
	public static final int HEIGHT = 110;
	public static final int SPACING = 2;
	
	private Context context = null;
	private View v = null;
	private TextView graphLabel = null;
	private TextView todaysValueTextView = null;
	private ImageView graphBitmapView = null;
	
	private String todaysValStr = null;
	private Bitmap graphBitmap = null;
	
	public ReviewGraphViewWrapper(Context context, String label) {
		this.context = context;
		this.v = View.inflate( context, R.layout.graph, null);
		
		this.graphLabel = (TextView) this.v.findViewById(R.id.graph_label ); 
		this.graphLabel.setText(label);
		this.todaysValueTextView = (TextView) this.v.findViewById(R.id.graph_value_label);
		this.graphBitmapView = (ImageView) this.v.findViewById(R.id.graph_bitmap);
	}
	
	public void setValueAndData(String todaysValue, Number[] data, Handler handler, Runnable graphUpdateRunnable) {
		this.todaysValStr = todaysValue;
		
		StringBuilder strBuilder = new StringBuilder();
		boolean isFirst = true;
		for( Number n : data ) {
			if ( isFirst ) {
				isFirst = false;
			} else {
				strBuilder.append( "," );
			}
			strBuilder.append( n );
		}
		try {
			URL img = new URL("http://chart.apis.google.com/chart?chs=180x50&cht=ls&chco=0077CC&chm=B,E6F2FA,0,0,0&chls=1,0,0&chd=t:" + strBuilder.toString() ); 
	        this.graphBitmap = BitmapFactory.decodeStream( img.openStream());
        } catch( IOException e ) {
	        e.printStackTrace();
        }
        
        handler.post(graphUpdateRunnable);
	}
	
	public Runnable getDrawGraphRunnable() {
		return new Runnable() {
			public void run() {
				todaysValueTextView.setText(todaysValStr);
				graphBitmapView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade));
				graphBitmapView.setImageBitmap(graphBitmap);
            }
		};
	}
		
	public View getView() {
		return this.v;
	}

}
