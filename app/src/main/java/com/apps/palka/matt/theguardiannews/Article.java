package com.apps.palka.matt.theguardiannews;

/**
 * Created by matt on 15.03.2018.
 */

public class Article {

    // Section of the article
    private String mArticleSection;

    // Title of the article
    private String mArticleTitle;

    // Date of the article
    private String mArticleDate;

    // URL to the site with full article
    private String mArticleURL;

    /**
     * Create a new Article object
     *
     * @param articleSection is the section that the article is in
     * @param articleTitle   is the title that the article have
     * @param articleDate    is the date the article was published
     * @param articleURL     is the URL to the site with the article
     */
    public Article(String articleSection, String articleTitle, String articleDate, String articleURL) {
        mArticleSection = articleSection;
        mArticleTitle = articleTitle;
        mArticleDate = articleDate;
        mArticleURL = articleURL;
    }

    // returns the section of the article
    public String getArticleSection() {
        return mArticleSection;
    }

    // returns the title of the article
    public String getArticleTitle() {
        return mArticleTitle;
    }

    // returns the date of the article
    public String getArticleDate() {
        return mArticleDate;
    }

    // returns the URL to the article
    public String getArticleURL() {
        return mArticleURL;
    }
}
