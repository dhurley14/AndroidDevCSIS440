package com.androidhw4.homework4;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CurrencyActivity extends Activity {
	TextView activity_currency;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_curr);
		activity_currency = (TextView) findViewById(R.id.bitcoin_to_currency_display);
		String myNum = "Error in CurrencyActivity.class";
		String myCurrency = "Error in CurrencyActivity.class using myCurrency var";
		Bundle extras = new Bundle();
		
		extras = getIntent().getExtras();
		if (extras != null) {
			myNum = extras.getString("num");
			myCurrency = extras.getString("cur");
		}
		activity_currency.setText("your conversion using " + myCurrency + " is " + myNum);
	}
	public void onClick(View v){
	    finish();	
	}
}
