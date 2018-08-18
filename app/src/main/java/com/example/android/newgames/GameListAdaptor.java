package com.example.android.newgames;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class GameListAdaptor extends ArrayAdapter<Game> {


    public GameListAdaptor(@NonNull Context context, @NonNull List<Game> earthquakes) {
        super(context,0, earthquakes);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem==null){
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.item1,parent,false);
        }

        Game currentGame = getItem(position);
        //for games with full dates
        if(currentGame.isHeader()==false && currentGame.getNoDate()==0) {
            TextView title = (TextView) listItem.findViewById(R.id.itemTest);
            TextView date = (TextView) listItem.findViewById(R.id.date);
            //Place set
            title.setText(currentGame.getTitle());
            date.setText(currentGame.getDate());
            TextView header = (TextView) listItem.findViewById(R.id.header);
            header.setText("");
        }
        //rest of year
        else if(currentGame.isHeader()==false&& currentGame.getNoDate()==1){
            TextView title = (TextView) listItem.findViewById(R.id.itemTest);
            TextView date = (TextView) listItem.findViewById(R.id.date);
            //Place set
            String dateString = currentGame.getDate();
            dateString = dateString.substring(0, dateString.length() - 6);;
            title.setText(currentGame.getTitle());
            date.setText(dateString);
            TextView header = (TextView) listItem.findViewById(R.id.header);
            header.setText("");
        }
        //for headers
        else{
            TextView title = (TextView) listItem.findViewById(R.id.itemTest);
            title.setText("");
            TextView date = (TextView) listItem.findViewById(R.id.date);
            date.setText("");
            TextView header = (TextView) listItem.findViewById(R.id.header);
            header.setText(currentGame.getTitle());
        }

        return listItem;
    }

}