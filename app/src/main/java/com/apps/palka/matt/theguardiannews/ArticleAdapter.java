package com.apps.palka.matt.theguardiannews;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by matt on 15.03.2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context  The current context. Used to inflate the layout file.
     * @param articles A List of articles objects to display in a list
     */
    public ArticleAdapter(Activity context, ArrayList<Article> articles) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Article} object located at this position in the list
        Article currentArticle = getItem(position);

        // Find the Text View in the list_item.xml with the ID section_text_view and set it's text
        TextView sectionTextView = listItemView.findViewById(R.id.section_text_view);
        sectionTextView.setText(currentArticle.getArticleSection());

        // Find the Text View in the list_item.xml with the ID title_text_view and set it's text
        TextView titleTextView = listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentArticle.getArticleTitle());

        // Find the Text Views in the list_item.xml with the IDs date_text_view and time_text_view
        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
        TextView timeTextView = listItemView.findViewById(R.id.time_text_view);

        // Split the downloaded JSON time of article to DATE and TIME
        // desired format is YYYY-MM-DD for date and HH:MM:SS for time
        String[] parts = currentArticle.getArticleDate().split("T");
        String[] timeParts = parts[1].split("Z");

        // Set text for the dateTextView and timeTextView
        dateTextView.setText(parts[0]);
        timeTextView.setText(" at " + timeParts[0]);

        //Find the Text View in the list_item.xml with the ID author_text_view and it's prefix (Text View
        // containing word AUTHOR: with the ID author_text_container
        TextView authorTextView = listItemView.findViewById(R.id.author_text_view);
        TextView authorTextContainer = listItemView.findViewById(R.id.author_text_container);
        // Check if there is an author of the article and Set the author name in the authorTextView
        // if there is, or hide the authorTextContainer if there isn't
        if (!(currentArticle.getArticleAuthor().isEmpty())){
            authorTextView.setText(currentArticle.getArticleAuthor());
        } else {
            authorTextContainer.setVisibility(View.GONE);
            authorTextView.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
