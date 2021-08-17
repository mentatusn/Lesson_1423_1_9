package ru.geekbrains.socialnetwork.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class CardData implements Parcelable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;       // заголовок
    private String title;       // заголовок
    private String description; // описание
    private int picture;        // изображение
    private boolean like;       // флажок
    private Date date;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    protected CardData(Parcel in) {
        title = in.readString();
        description = in.readString();
        picture = in.readInt();
        like = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(picture);
        dest.writeByte((byte) (like ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CardData> CREATOR = new Creator<CardData>() {
        @Override
        public CardData createFromParcel(Parcel in) {
            return new CardData(in);
        }

        @Override
        public CardData[] newArray(int size) {
            return new CardData[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }



    public Date getDate() {
        return date;
    }


    public CardData(String title, String description, int picture, boolean like, Date date){
        this.title = title;
        this.description=description;
        this.picture=picture;
        this.like=like;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public int getPicture() {
        return picture;
    }

    public boolean isLike() {
        return like;
    }
}