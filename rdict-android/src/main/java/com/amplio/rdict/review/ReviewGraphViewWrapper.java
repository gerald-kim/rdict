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
		for( Number n : data ) {
			strBuilder.append( n ).append( "," );
		}
		//strBuilder.append( "10,20,30,300" );
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
