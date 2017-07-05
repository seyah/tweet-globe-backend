package uk.co.seyah.tweetglobebackend.model;

import java.util.Date;

public class Tweet {

    private String author, lang, text;
    private String authorImageURL;

    private int favouriteCount, retweetCount;
    private boolean isRetweeted;
    private Date creationDate;

    public Tweet() {
    }

    public Tweet(String author, String lang, String text) {
        this.author = author;
        this.lang = lang;
        this.text = text;
    }

    public Tweet(String author, String lang, String text, String authorImageURL, int favouriteCount, int retweetCount, boolean isRetweeted, Date creationDate) {
        this.author = author;
        this.lang = lang;
        this.text = text;
        this.authorImageURL = authorImageURL;
        this.favouriteCount = favouriteCount;
        this.retweetCount = retweetCount;
        this.isRetweeted = isRetweeted;
        this.creationDate = creationDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthorImageURL() {
        return authorImageURL;
    }

    public void setAuthorImageURL(String authorImageURL) {
        this.authorImageURL = authorImageURL;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(int favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public boolean isRetweeted() {
        return isRetweeted;
    }

    public void setRetweeted(boolean retweeted) {
        isRetweeted = retweeted;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
