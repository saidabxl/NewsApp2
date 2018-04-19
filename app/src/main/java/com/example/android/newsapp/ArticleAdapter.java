package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class ArticleAdapter extends ArrayAdapter<Article> {

    private Context context ;
    private Article currentArticle ;

    /*
    Public constructor that passes the context and the source of information for the adapter
    @param context is the context of the activity
    @param article is the List that contains objects of type Article
     */
    public ArticleAdapter(Context context , List<Article> article){
        super(context,0 ,article);
        this.context = context ;

    }

    @NonNull
    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_article_item, parent, false);
        }

        currentArticle = getItem(position);

        // Find the TextView with view ID tv1 for setting title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.tv1) ;
        // Display the title of the current article in that TextView
        titleTextView.setText(currentArticle.getTitle()) ;

        // Find the TextView with view ID tv2 for setting section
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.tv2) ;
        // Display the section of the current article in that TextView
        sectionTextView.setText(currentArticle.getSection()) ;


        // Find the TextView with view ID tv4 for setting author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.tv4) ;
        authorTextView.setText(currentArticle.getAuthor()) ;


        // Find the TextView with view ID tv1 for setting date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.tv3) ;
        //Format the date String
        String date = currentArticle.getDate() ;
        if (date.contains("T")) {
            String[] separated = date.split("T");
            // Display the date of the current article in that TextView
            dateTextView.setText(separated[0]) ;
        }else{
            dateTextView.setText(date);
        }

        return listItemView ;
    }

}
