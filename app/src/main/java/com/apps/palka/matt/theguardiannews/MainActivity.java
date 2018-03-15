package com.apps.palka.matt.theguardiannews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    /**
     * URL for articles data from the Guardian API
     */
    private static final String GUARDIAN_URL_REQUEST =
            "http://content.guardianapis.com/search?q=debates&api-key=test";

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int ARTICLE_LOADER_ID = 1;

    /**
     * Adapter for the list of articles
     */
    private ArticleAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Progress bar (spinning) that is displayed before list is loaded
     */
    private ProgressBar spinningProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Check if device is connected to the internet
         */
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        ListView articleListView = (ListView) findViewById(R.id.list);

        //Create a new adapter that takes an empty list of articles ad input
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);

        // Find views in activity_main.xml for displaying loading screen and empty list
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_state_text_view);
        spinningProgress = (ProgressBar) findViewById(R.id.loading_spinner);

        articleListView.setEmptyView(mEmptyStateTextView);

        /*
         * Set OnItemClickListener on List View, which sends the intent to a web browser
         * to open a website with the article from the list
         */
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Find the current article that was clicked on
                Article currentArticle = mAdapter.getItem(i);

                Uri articleUri = Uri.parse(currentArticle.getArticleURL());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                //Send intent to launch new activity
                startActivity(websiteIntent);
            }
        });

        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            spinningProgress.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No internet connection");
        }


    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        // Create new loader for given URL
        return new ArticleLoader(this, GUARDIAN_URL_REQUEST);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articlesList) {
        //clear adapter of previous article data
        mAdapter.clear();

        spinningProgress.setVisibility(View.GONE);

        // If there is a valid list of {@link Articles}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articlesList != null && !articlesList.isEmpty()) {
            mAdapter.addAll(articlesList);
        }
        // Set empty state text to display "No articles found."
        mEmptyStateTextView.setText(R.string.no_articles_found);
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }
}
