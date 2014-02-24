package com.example.todolist;

import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
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

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Magic Cards List
 * @author Troy Valle
 *
 */
public class MainActivity extends Activity {
	ListView listview;
	Button createButton;
	Button scrapeButton;
	MainAdapter adapter;
	ArrayList<Card> lists = new ArrayList<Card>();
	ArrayList<String> listItems = new ArrayList<String>();
	private Handler mHandler= new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadData();
		
		/**
		lists.add(new Card("Overgrown Tomb","12/12/13"));
		lists.get(lists.size()-1).addPrice(8.00);
		lists.add(new Card("Hellfire","12/12/13"));
		lists.get(lists.size()-1).addPrice(6.00);
		*/
	
		listview = (ListView) findViewById(R.id.tasks);
		
		String[][] temp = new String[lists.size()][5];
        for(int i = 0 ;i<lists.size();i++) {
        	temp[i][0]=lists.get(i).getName();
        	temp[i][1]="Current Price: $"+lists.get(i).getCurrentPrice();
        	temp[i][2]="Max: $"+lists.get(i).getMax();
        	temp[i][3]="Min: $"+lists.get(i).getMin();
        	temp[i][4]="Average: $"+lists.get(i).getAverage();
        }
	    
	    adapter=new MainAdapter(this, temp);
	    
        listview.setAdapter(adapter);
		createButton = (Button) findViewById(R.id.addCard);
		createButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this,
						AddCardActivity.class);

				startActivity(intent);
			}
		});
		
		scrapeButton = (Button) findViewById(R.id.Update);
		scrapeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				scrapeButton.setText("Processing...");
				Thread thread = new Thread(new Runnable(){
					@Override
					public void run(){
						for(int i =0;i<lists.size();i++) {
							System.out.println("Name = " + lists.get(i).getName());
						String newPrice=getCardPrice(lists.get(i).getName());
						if(!newPrice.equals("Not Found")) {
							newPrice=newPrice.substring(1, newPrice.length()-1);
							System.out.println(newPrice);
							double newPrice2=Double.parseDouble(newPrice);
							lists.get(i).addPrice(newPrice2);
							Date date = new Date();
   	            			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
   	            			String dateString = sdf.format(date).toString();	
	   	            		lists.get(i).addDate(dateString);
						}
						
					
					}
						mHandler.post(new Runnable(){
							@Override
							public void run(){
								scrapeButton.setText("Update");
								saveData();
								updatePrices();
							}
						});
					}
				});
				thread.start();
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(MainActivity.this,
						ViewCardInformationActivity.class);
				intent.putExtra("index", "" + position);
				startActivity(intent);
				// TODO Auto-generated method stub

			}
		});

	}
	public void updatePrices() {
		String[][] temp = new String[lists.size()][5];
        for(int i = 0 ;i<lists.size();i++) {
        	temp[i][0]=lists.get(i).getName();
        	temp[i][1]="Current Price: $"+lists.get(i).getCurrentPrice();
        	temp[i][2]="Max: $"+lists.get(i).getMax();
        	temp[i][3]="Min: $"+lists.get(i).getMin();
        	temp[i][4]="Average: $"+lists.get(i).getAverage();
        }
	    
	    adapter=new MainAdapter(this, temp);
	    
        listview.setAdapter(adapter);
	}
	@Override
	public void onResume() {

		super.onResume();
		
		loadData();
		String[][] temp = new String[lists.size()][5];
        for(int i = 0 ;i<lists.size();i++) {
        	temp[i][0]=lists.get(i).getName();
        	temp[i][1]="Current Price: $"+lists.get(i).getCurrentPrice();
        	temp[i][2]="Max: $"+lists.get(i).getMax();
        	temp[i][3]="Min: $"+lists.get(i).getMin();
        	temp[i][4]="Average: $"+lists.get(i).getAverage();
        }
	    
	    adapter=new MainAdapter(this, temp);
	    
        listview.setAdapter(adapter);
		
	}

	@Override
	public void onBackPressed() {
		return;
	}
	@SuppressWarnings("unchecked")
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
	
	/**
	 * 
	 * @param name, the name of the magic card
	 * @return the price of the card as a string
	 * 
	 * This method searches trollandtoad.com for a card and returns the price
	 */
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
