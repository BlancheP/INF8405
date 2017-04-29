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

    private Double m_latitude;
    private Double m_longitude;

    public ImageItem(Bitmap image, String title, String description, String date, Double latitude, Double longitude) {
        super();
        this.m_image = image;
        this.m_title = title;
        this.m_description = description;
        this.m_date = date;
        this.m_latitude = latitude;
        this.m_longitude = longitude;
    }

    public Bitmap getImage() {
        return m_image;
    }

    public Double getLatitude() {
        return m_latitude;
    }

    public Double getLongitude() {
        return m_longitude;
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

    public String getDescription() {
        return m_description;
    }

    public String getDate() { return m_date; }
}
