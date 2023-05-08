package com.example.sustmedicalcenter.model;

import java.io.Serializable;

public class NewsModel implements Serializable {
    private String NewsTitle,NewsContent;
    private String NewsImage;
    private long postingDate;

    //required for firestore
    public NewsModel() {
    }

    public NewsModel(String newsTitle, String newsContent, String newsImage, long postingDate) {
        NewsTitle = newsTitle;
        NewsContent = newsContent;
        NewsImage = newsImage;
        this.postingDate = postingDate;
    }



    public String getNewsTitle() {
        return NewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        NewsTitle = newsTitle;
    }

    public String getNewsContent() {
        return NewsContent;
    }

    public void setNewsContent(String newsContent) {
        NewsContent = newsContent;
    }

    public String getNewsImage() {
        return NewsImage;
    }

    public void setNewsImage(String newsImage) {
        NewsImage = newsImage;
    }

    public long getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(long postingDate) {
        this.postingDate = postingDate;
    }
}
