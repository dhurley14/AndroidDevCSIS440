package com.example.todolist;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

class MainAdapter extends BaseAdapter {

    Context context;
    String[][] data;
    private static LayoutInflater inflater = null;
 
    public MainAdapter(Context context, String[][] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }
    
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    
    static class ViewHolder
    {
            
            TextView title;
            TextView price;
            TextView max;
            TextView min;
            TextView average;
            
    }
   
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
    	final ViewHolder holder;
        View vi = convertView;
        final int tp = position;
        
        if (vi == null) {
            vi = inflater.inflate(R.layout.mainlistrow, null);
            holder=new ViewHolder();
	        holder.title = (TextView) vi.findViewById(R.id.title);
	        holder.price = (TextView) vi.findViewById(R.id.price);
	        holder.max = (TextView) vi.findViewById(R.id.max);
	        holder.min = (TextView) vi.findViewById(R.id.min);
	        holder.average = (TextView) vi.findViewById(R.id.average);
	        holder.title.setText(data[position][0]);
	        holder.price.setText(data[position][1]);
	        holder.max.setText(data[position][2]);  
	        holder.min.setText(data[position][3]);  
	        holder.average.setText(data[position][4]);  
        }
        else
        {
                holder=(ViewHolder)convertView.getTag();
        }
        
        return vi;
    }
}
