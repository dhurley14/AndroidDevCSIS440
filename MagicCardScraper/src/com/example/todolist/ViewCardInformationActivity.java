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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
public class ViewCardInformationActivity extends Activity {
	ListView listview;
	Button deleteButton;
	LazyAdapter adapter;
	int pos;
	ArrayList<Card> lists = new ArrayList<Card>();
	ArrayList<String> listItems = new ArrayList<String>();
	private Handler mHandler= new Handler();
	final Context context = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list);
		Bundle b = new Bundle();
		b = getIntent().getExtras();
	    String name = b.getString("index");
	    pos = Integer.parseInt(name);
	    System.out.println(pos);
		loadData();
		
		listview = (ListView) findViewById(R.id.tasks);	
		deleteButton= (Button) findViewById(R.id.delete);	
		String[][] temp = new String[lists.get(pos).getPrices().size()][3];
        for(int i = 0 ;i<lists.get(pos).getPrices().size();i++) {
        	temp[i][0]=lists.get(pos).getName();
        	temp[i][1]="Price: $"+lists.get(pos).getPrices().get(i);
        	temp[i][2]=""+lists.get(pos).getDates().get(i);
        	
        }
       
	    adapter=new LazyAdapter(this, temp);
	    
        listview.setAdapter(adapter);			
        deleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				new AlertDialog.Builder(context)
            		  .setTitle("Delete")
            		  .setMessage("Are you sure you want to delete this card? All data will be lost.")
            		 
            		  .setPositiveButton("YES", new DialogInterface.OnClickListener() {
            		    public void onClick(DialogInterface dialog, int whichButton) {
            		    	
            		    	lists.remove(pos);
            				saveData();
            				onBackPressed();
            		      
            		    }
            		    
            		  })
            		  .setNegativeButton("NO", new DialogInterface.OnClickListener() {
              		    public void onClick(DialogInterface dialog, int whichButton) {
              		    	
              		     dialog.dismiss();
              		      
              		    }
              		    
              		  }).show();
				
			}
        });
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
	
	
	
}

