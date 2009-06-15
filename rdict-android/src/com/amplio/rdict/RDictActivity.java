package com.amplio.rdict;

import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class RDictActivity extends TabActivity {
	
	private static final String[] TABS = { "Dictionary", 
		"Review", 
		"History",
		"Settings"};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setDefaultTab(0);
    	
        TabHost tabs = getTabHost();
    	
        for (int i = 0; i < TABS.length; i++){
        	TabHost.TabSpec tab = tabs.newTabSpec(TABS[i]);

        	ComponentName activity =
        		new ComponentName("com.amplio.rdict", 
        						  "com.amplio.rdict." + TABS[i] + "Activity");

        	tab.setContent(new Intent().setComponent(activity));
        	tab.setIndicator(TABS[i]);
        	tabs.addTab(tab);
        }
    }
    
    public static class MyTabIndicator extends LinearLayout
    {
		public MyTabIndicator(Context context, String label)
		{
			super(context);
			
			View tab = View.inflate(this.getContext(), R.layout.tab_indicator, null);

			TextView tv = (TextView)tab.findViewById(R.id.tab_label);
			tv.setText(label);
		}
    }
}