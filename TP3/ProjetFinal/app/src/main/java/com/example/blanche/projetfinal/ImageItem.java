package com.example.blanche.projetfinal;

import android.graphics.Bitmap;

/**
 * Created by Alexis on 25/04/2017.
 */

public class ImageItem {
    private Bitmap m_image;
    private String m_title;
    private String m_description;
    private String m_date;

    public ImageItem(Bitmap image, String title, String description, String date) {
        super();
        this.m_image = image;
        this.m_title = title;
        this.m_description = description;
        this.m_date = date;
    }

    public Bitmap getImage() {
        return m_image;
    }

    public void setImage(Bitmap image) {
        this.m_image = image;
    }

    public String getTitle() {
        return m_title;
    }

    public void setTitle(String title) {
        this.m_title = title;
    }

    public String get_description() {
        return m_description;
    }

    public String get_date() { return m_date; }
}
