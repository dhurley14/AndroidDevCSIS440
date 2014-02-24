package com.androidhw4.homework4;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.lang.Double;

import java.net.URL;

/**
 * @author Devin Hurley
 * @description checks current bitcoin exchange rates with the 
 * option of selecting different exchanges.
 */

public class MainActivity extends Activity {
    TextView textView;
    EditText amountBitcoin;
    EditText currencyText;
    EditText currencyText2;
    String shotInTheDark;
    String tempCurString;
    RadioButton blockChain;
    double myDub;
    RadioButton mtGox;
    RadioButton bitCoinCharts;
    RadioButton coinBase;
    RadioButton bitPay;
    String currencyString;
    String ticker;
    private Handler mHandler = new Handler();
    
    
    /**
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * @param savedInstanceState a Bundle Instance
     * @return void
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView)findViewById(R.id.amount_btc);
		amountBitcoin = (EditText)findViewById(R.id.entered_amount_btc);
		currencyText = (EditText)findViewById(R.id.entered_amount_currency);
		currencyText2 = (EditText)findViewById(R.id.currency_editText);
		blockChain = (RadioButton)findViewById(R.id.BlockChain);
		mtGox = (RadioButton)findViewById(R.id.MtGox);
		coinBase = (RadioButton)findViewById(R.id.CoinBase);
		bitPay = (RadioButton)findViewById(R.id.BitPay);
		textView.setText("Amount of \nBitcoin to convert");
		ImageView imageView = (ImageView)findViewById(R.id.image1);
		imageView.setImageResource(R.drawable.line);
		//textView.setText("Hello, World!");
		//startScrape();
		
	}
	/**
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 * @param 
	 * @return
	 */
	@Override 
	protected void onResume(){
		super.onResume();
	    textView.setText("Amount of \nBitcoin to convert");	
		
	}
	/**
	 * auto-generated
	 * @param menu a Menu object
	 * @return boolean
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * This method runs inside of the UIThread
	 * and creates a Handler Thread which sends the data back to the UI.
	 * This allows the program to make a network request
	 * and update the UI.  Found here http://stackoverflow.com/a/5400412/821557
	 */
	public void startScrape(){
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					//System.out.println(getBTC());
					shotInTheDark = getBTC();
					System.out.println(shotInTheDark);
					mHandler.post(new Runnable(){
						@Override
						public void run(){
							
							Intent i = new Intent(MainActivity.this , CurrencyActivity.class);
			                i.putExtra("num", shotInTheDark);
			                i.putExtra("cur",tempCurString);
							startActivity(i);
						}
					});
				}
				catch(Exception e){
					System.out.println("Error in startScrape");
				}
			}
		});
		thread.start();
	}
	
	
	/**
	 * getBTC() gets the current exchange rate for the amount of BitCoin
	 * entered into the EditText thing
	 * @param
	 * @return num the current value of the amount of bitcoin in the specified
	 * currency.
	 */
    public String getBTC() throws Exception{
    	// did have myDub = 1.0; and tempCurString = "USD";
    	try{
    	    myDub = Double.parseDouble(amountBitcoin.getText().toString());
    	    System.out.println(myDub);
    	    tempCurString = currencyText.getText().toString();
    	    System.out.println(tempCurString);
    	}
    	catch(Exception e){
    		System.out.println("No values found for myDub or tempCurString");
    		myDub = 1.0;
    		tempCurString = currencyText.getText().toString();
    	}
        URL url = new URL("https://bitcointy.herokuapp.com/convert/"+Double.toString(myDub)+"/"+tempCurString);
        System.out.println("Got URL correctly: " + url);
        Document text = Jsoup.parse(url,10000);
        String myStr = text.body().text();
        System.out.println("myStr = " + myStr);
        JSONObject json = new JSONObject(myStr);
        String num =  Double.toString(json.getDouble("value"));
        System.out.println("return (num) = "+ num);
        return (num);
    }
	
	
	public void onClick(View v){
		textView.setText("Processing...");
		startScrape();
	}
	
	
	 // Start methods for Second Button
	 
	
	/**
	 * startScrape2() is a second copy of the method used to display content
	 * retrieved from the web api.
	 * @param 
	 * @return
	 */
	public void startScrape2(){
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					//System.out.println(getBTC2());
					
					shotInTheDark = getBTC2();
					mHandler.post(new Runnable(){
						@Override
						public void run(){
							
							Intent i = new Intent(MainActivity.this , ExchangeActivity.class);
							i.putExtra("tick",ticker);
			                i.putExtra("exch", shotInTheDark);
			                i.putExtra("cur",currencyString);
							startActivity(i);
						}
					});
				}
				catch(Exception e){
					System.out.println("Error in startScrape2");
				}
			}
		});
		thread.start();
	}
	
	
	/**
	 * getBTC2() works the same as getBTC()
	 * This method gets currency and selected ticker.
	 * @param
	 * @return num the current exchange value for 
	 */
    public String getBTC2() throws Exception{
    	// initialize just in case.
    	ticker = "mtgox";
    	currencyString = "USD";
    	
    	// try-catch the radio buttons
    	try{
    		if(blockChain.isChecked()){
    		    ticker = "blockchain";	
    		}
    		else if(mtGox.isChecked()){
    		    ticker = "mtgox";	
    		}
    		else if(coinBase.isChecked()){
    			ticker = "coinbase";	
    		}
    		else if(bitPay.isChecked()){
    		    ticker = "bitpay";	
    		}
    		else{
    		    ticker = "mtgox";	
    		}
    	    currencyString = currencyText2.getText().toString();
    	}
    	catch(Exception e){
    		System.out.println("No values found for currencyString or tempCurString");
    	}
    	//Get URL
        URL url = new URL("https://bitcointy.herokuapp.com/"+ticker+"/"+currencyString);
        Document text = Jsoup.parse(url,10000);
        String myStr = text.body().text();
        JSONObject json = new JSONObject(myStr);
        // parse the JSON
        String num =  Double.toString(json.getDouble("value"));
        return (num);
    }
	
    
    /**
     * Onclick method for the "GO!" button
     * @param v
     */
	public void onClick2(View v){
		textView.setText("Processing 2...");
		startScrape2();
	}

}
