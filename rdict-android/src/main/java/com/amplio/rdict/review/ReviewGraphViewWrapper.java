package com.amplio.rdict.review;

import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplio.rdict.R;

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
	
	private View v = null;
	private TextView graphLabel = null;
	private TextView todaysValue = null;
	private ImageView graphBitmap = null;
	
	public ReviewGraphViewWrapper(Context context, String label) {
		this.v = View.inflate( context, R.layout.graph, null);
		
		this.graphLabel = (TextView) this.v.findViewById(R.id.graph_label ); 
		this.graphLabel.setText(label);
		this.todaysValue = (TextView) this.v.findViewById(R.id.graph_value_label);
		this.graphBitmap = (ImageView) this.v.findViewById(R.id.graph_bitmap);
	}
	
	public void setValueAndData(String todaysValue, Number[] data) {
		this.todaysValue.setText( todaysValue );
		
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
			URL img = new URL("http://chart.apis.google.com/chart?chs=200x30&cht=ls&chco=0077CC&chm=B,E6F2FA,0,0,0&chls=1,0,0&chd=t:" + strBuilder.toString() ); 
	        this.graphBitmap.setImageBitmap( BitmapFactory.decodeStream( img.openStream() ) );
        } catch( IOException e ) {
	        e.printStackTrace();
        }
	}
		
	public View getView() {
		return this.v;
	}

}
