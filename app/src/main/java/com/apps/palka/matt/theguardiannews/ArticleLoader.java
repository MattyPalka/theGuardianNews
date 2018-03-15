package com.apps.palka.matt.theguardiannews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by matt on 15.03.2018.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    // Query URL
    private String mURL;

    public ArticleLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of articles.
        List<Article> articles = QueryUtils.fetchArticleData(mURL);
        return articles;
    }
}
