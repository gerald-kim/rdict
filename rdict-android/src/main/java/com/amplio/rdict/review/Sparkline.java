package com.amplio.rdict.review;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sparkline {
	public final static int LEFT_MARGIN = 5;
	public final static int RIGHT_MARGIN = 5;
	public final static int BOTTOM_MARGIN = 5;
	public final static int TOP_MARGIN = 5;
	
	public int w = 0;
	public int h = 0;
	public int spacing = 0;
	public Number[] data = null;
	public Vector<Rectangle> rectangles = null;
	
	public int rectangleWidth = 0;
	public float divisor = 0;
	
	public Sparkline(int w, int h, Number[] data, int spacing, boolean isPercentageGraph) {
		this.w = w;
		this.h = h;
		this.spacing = spacing;
		this.data = data;
		
		this.rectangleWidth = this.getWidthAvailableToRectangles() / data.length;
		this.divisor = calcDivisor(data, this.getHeightAvailableToRectangles(), isPercentageGraph);
	}
	
	public void setupRectangles() {
		Vector<Rectangle> tmp = new Vector<Rectangle>();
		
		int x_to_draw = 2*RIGHT_MARGIN;
		
		for (Number n : this.data){
			int w = this.rectangleWidth;
			int h = new Float(n.intValue() * this.divisor).intValue();
			int x = x_to_draw;
			int y = this.h - h - BOTTOM_MARGIN;
			
			tmp.add(new Rectangle(w, h, x, y));
			
			x_to_draw += this.rectangleWidth + this.spacing;
		}
		
		this.rectangles = tmp;
	}
	
	public int getWidthAvailableToRectangles() {
	    return this.w - 2*Sparkline.LEFT_MARGIN - 2*Sparkline.RIGHT_MARGIN - (this.data.length - 1) * this.spacing;
    }

	public int getHeightAvailableToRectangles() {
		return this.h - Sparkline.TOP_MARGIN - Sparkline.BOTTOM_MARGIN;
    }
	
	public static float calcDivisor(Number[] data, int height, boolean isPercentageGraph) {
		if( isPercentageGraph) {
			float oneHundred = 100;
			return height / oneHundred;
		}
		else {
			float max = 0;
			
			for(Number val : data)
				max = Math.max(max, val.intValue());
		
			return height / max;
		}
	}
	
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor( Color.WHITE );
		canvas.drawRect(0, 0, this.w, this.h, paint);
		
		paint.setColor( Color.RED );
		canvas.drawLine(LEFT_MARGIN, TOP_MARGIN, LEFT_MARGIN, this.h - BOTTOM_MARGIN, paint);
		canvas.drawLine(LEFT_MARGIN, this.h - BOTTOM_MARGIN, this.w - RIGHT_MARGIN, this.h - BOTTOM_MARGIN, paint);
		
		paint.setColor(Color.BLUE);
		
		for( Sparkline.Rectangle r : this.rectangles) {
		    canvas.drawRect(r.toAndroidRect(), paint);
	    }
	}
	
	public class Rectangle {
		public int w = 0;
		public int h = 0;
		public int x = 0;
		public int y = 0;
		
		
		public Rectangle( int w, int h, int x, int y ) {
			this.w = w;
			this.h = h;
			this.x = x;
			this.y = y;
        }

		public Rect toAndroidRect(){
			Rect r = new Rect();
		    r.left = this.x;
		    r.right = this.x + this.w;
		    r.bottom = this.y + this.h;
		    r.top = this.y;
		    
		    return r;
		}
		
		public String toString() {
			return "(" + this.w + ", " + this.h + ", " + this.x + ", " + this.y + ")";
		}
	}
}
