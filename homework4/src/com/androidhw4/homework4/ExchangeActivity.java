package com.androidhw4.homework4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ExchangeActivity extends Activity {
	TextView tv;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exch);
		tv = (TextView) findViewById(R.id.exchange_conversion_text);

		String myNum = "Error in CurrencyActivity.class";
		String myCurrency = "Error in CurrencyActivity.class using myCurrency var";
		String myTick = "Error in CurrencyActivity.class using myTick var";
		Bundle extras = new Bundle();

		extras = getIntent().getExtras();
		if (extras != null) {
			myNum = extras.getString("exch");
			myCurrency = extras.getString("cur");
			myTick = extras.getString("tick");
		}
		tv.setText("your conversion using " + myCurrency + " and " + myTick
				+ " is " + myNum + " for the equivalent of 1 BTC");

	}
	
	public void onClick(View v){
	    finish();	
		
	}

}
