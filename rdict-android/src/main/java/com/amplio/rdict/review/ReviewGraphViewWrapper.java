package com.amplio.rdict.review;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
		
		Bitmap graphBitmap = this.buildBitmap(data, false);
		this.graphBitmap.setImageBitmap( graphBitmap);
	}
	
	public Bitmap buildBitmap(Number[] data, boolean isPercentageGraph) {
		Bitmap bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_4444);
	    Canvas canvas = new Canvas(bitmap);
	    
	    Sparkline sl = new Sparkline(WIDTH, HEIGHT, data, SPACING, isPercentageGraph);
	    sl.setupRectangles();
	    
	    Paint paint = new Paint();
	    sl.draw(canvas, paint);
	    
		return bitmap;
	}
	
	public View getView() {
		return this.v;
	}

}
