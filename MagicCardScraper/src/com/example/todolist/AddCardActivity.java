package com.example.todolist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AddCardActivity extends Activity {
	
	final Context context = this;
	Button nextButton;
	Button submitButton;
	TextView textCount;
	EditText cardName;
	String listName;
	ArrayList<Card> lists = new ArrayList<Card>();
	boolean found=false;
	private Handler mHandler= new Handler();
	int count=1;
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		loadData();
		setContentView(R.layout.create_task); 

		submitButton= (Button) findViewById(R.id.submit);

		textCount = (TextView) findViewById(R.id.taskCountText);
		cardName = (EditText) findViewById(R.id.editText1);
		
		submitButton.setOnClickListener(
	   	        new View.OnClickListener()
	   	        {
	   	            public void onClick(View view)
	   	            {
	   	            	if(!cardName.getText().toString().equals("") ) {
	   	            		submitButton.setText("Searching...");
	   	            		Thread thread = new Thread(new Runnable(){
	   	 					@Override
	   	 					public void run(){
	   	 						
	   	 					String price=getCardPrice(cardName.getText().toString());
	   	            		if(!price.equals("Not Found")) {
	   	            			 price=price.substring(1, price.length()-1);
	   	            			found=true;
	   	            			Date date = new Date();
	   	            			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	   	            			String dateString = sdf.format(date).toString();
		   	            		lists.add(new Card(cardName.getText().toString()));	
		   	            		lists.get(lists.size()-1).addDate(dateString);
		   	            		lists.get(lists.size()-1).addPrice(Double.parseDouble(price));
		   	            		
	   	            		}
	   	 						mHandler.post(new Runnable(){
	   	 							@Override
	   	 							public void run(){
	   	 							submitButton.setText("Submit");
	   	 								if(!found) {
			   		   	            		new AlertDialog.Builder(context)
			   		   	            		  .setTitle("List Name")
			   		   	            		  .setMessage("Card not found, make sure you correctly input spaces commas and apostrophes.")
			   		   	            		 
			   		   	            		  .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			   		   	            		    public void onClick(DialogInterface dialog, int whichButton) {
			   		   	            		    	
			   		   	            		     dialog.dismiss();
			   		   	            		      
			   		   	            		    }
			   		   	            		  })
			   		   	       
			   		   	            		  .show();
	   	 								}
	   	 								else {
		   	 								cardName.setText("");
		   	 								saveData();
		   			   	            		onBackPressed();
	   	 								}
	   	 							}
	   	 						});
	   	 					}
	   	 				});
	   	 				thread.start();
	   	            		
	   	            		   	            		
	   		   	            		
	   		   	            	 
	   	            	
	   	            		
	   	            	}
	   	            	
	   	            	
	   	            }
	   	        });
        
	}

	public void loadData() {
		
	      try
	      {
	         FileInputStream fileIn = new FileInputStream(getFilesDir()+File.separator+"cards.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         lists = (ArrayList<Card>) in.readObject();
	         
	         in.close();
	         fileIn.close();
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Card class not found");
	         c.printStackTrace();
	         return;
	      }		
	}
	
	public void saveData() {

	     
	      try
	      {
	         FileOutputStream fileOut =
	         new FileOutputStream(getFilesDir()+File.separator+"cards.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(lists);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in cards.ser");
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	public String getCardPrice(String name) {
		name=name.toLowerCase();
		String[] names=name.split(" ");
		String urlName="";
		for(int i=0;i<names.length;i++) {
			if(i>0) {
				urlName=urlName+"+"+names[i];
			}
			else {
				urlName=names[0];
			}
		}
		String answer="Not Found";
		try {
			
			Document doc = Jsoup.parse(new URL("http://www.trollandtoad.com/products/search.php?search_category=1041&search_words="+urlName+"&searchmode=basic"),3000);
			
			//System.out.println(doc.outerHtml());
			String lowerCase=doc.outerHtml().toLowerCase();
			//System.out.println(lowerCase);
			String[] links=lowerCase.split("<div class=\"search_result_text\">");
			for(int i =1;i<links.length;i++) {
					boolean found=false;
					for(int j =0;j<names.length;j++) {
						if(names.length ==1) {
							if(links[i].contains("<strong>"+name+"</strong></a>")) {
								found=true;
								break;
							}
						}
						else {
							
							if(!names[j].equals("of")) {
								if(links[i].contains("<strong>"+names[j]+"</strong>")) {
									
									found=true;							
								}
								else {
									found=false;
								}
							}
						}
						
					}
					if(found) {
							//System.out.println(links[i]);
							String[] link=links[i].split("<span class=\"price_text\">");
							if(link.length>1) {
								String temp=link[1];
								String[] price=temp.split("</span>");
								String cardPrice=price[0];
								//System.out.println(cardPrice);
								answer=cardPrice;
								break;
							}					
					}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return answer;
	}
}

