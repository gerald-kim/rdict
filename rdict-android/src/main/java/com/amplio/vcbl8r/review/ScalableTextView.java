package com.amplio.vcbl8r.review;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScalableTextView extends TextView {
	
	private Rect textBounds = null;
	
	public ScalableTextView( Context context ) {
	    super( context );
    }
	
	public ScalableTextView( Context context, AttributeSet attrs) {
        super( context, attrs );
    }		

	@Override
	public void onDraw(Canvas canvas) {
		this.textBounds = this.growText(this.getWidth(), this.getHeight());
		
		positionTextBounds(this.textBounds);
		
		this.getPaint().setColor(Color.BLACK);
		canvas.drawText(this.getText().toString(), 
				this.textBounds.left, 
				this.textBounds.top - this.getPaint().ascent() - this.getPaint().descent(),
				this.getPaint());
	}

	public Rect growText(int w, int h) {
		int textSize = 1;
        
		this.textBounds = setTextSizeAndGetBounds(textSize);
		
		int tempW = this.calcWidth(this.textBounds);
		int tempH = this.calcHeight(this.textBounds);
		
		for(;tempW <= this.getAvailableWidth(w) && tempH <= this.getAvailableHeight(h); textSize++){
			Rect tempBounds = setTextSizeAndGetBounds(textSize);
	    	tempW = this.calcWidth(tempBounds);
	    	tempH = this.calcHeight(tempBounds);
	    	
//	    	System.out.println("Baseline shift: " + this.getPaint().baselineShift);
//	    	System.out.println("Ascent: " + this.getPaint().ascent());
//	    	System.out.println("Descent: " + this.getPaint().descent());
	    }
		
		return textBounds = setTextSizeAndGetBounds(textSize - 1);
	}
	
	private void positionTextBounds(Rect r) {
	    this.textBounds.left = this.getLeft() + this.getPaddingLeft() + this.getAvailableWidth(this.getWidth())/2 - this.calcWidth(this.textBounds)/2;
		this.textBounds.top = this.getTop() + this.getPaddingTop() + this.getAvailableHeight(this.getHeight())/2  - this.calcHeight(this.textBounds)/2;
    }
	
	private Rect setTextSizeAndGetBounds(int textSize) {
		this.getPaint().setTextSize(textSize);
		
		Rect bounds = new Rect();
		this.getPaint().getTextBounds(this.getText().toString(), 0, this.getText().toString().length(), bounds);
		
		return bounds;
    }
	
	public int getAvailableWidth(int w) {
		return w - this.getPaddingLeft() - this.getPaddingRight();
	}
	private int getAvailableHeight(int h) {
		return h - this.getTotalPaddingTop() - this.getPaddingBottom();
    }
	
	private int calcWidth(Rect bounds) {
		return bounds.right - bounds.left;
	}
	
	private int calcHeight(Rect bounds) {
		return bounds.bottom - bounds.top;
	}
}
