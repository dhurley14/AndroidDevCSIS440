package com.example.todolist;



import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

class LazyAdapter extends BaseAdapter {

    Context context;
    String[][] data;
    private static LayoutInflater inflater = null;
   
 
    public LazyAdapter(Context context, String[][] data) {
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
            TextView date;
            TextView price;
            
    }
   
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
    	final ViewHolder holder;
        View vi = convertView;
        
        if (vi == null) {
            vi = inflater.inflate(R.layout.listrow, null);
            holder=new ViewHolder();
	        holder.title = (TextView) vi.findViewById(R.id.title);
	        holder.date = (TextView) vi.findViewById(R.id.date);
	        holder.price = (TextView) vi.findViewById(R.id.price);
	        holder.title.setText(data[position][0]);
	        holder.price.setText(data[position][1]);  
	        holder.date.setText(data[position][2]);  
        }
        else
        {
                holder=(ViewHolder)convertView.getTag();
        }
        
        return vi;
    }
}
