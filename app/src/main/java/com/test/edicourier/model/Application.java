package com.test.edicourier.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Application implements Parcelable {

    private String city;
    private String uid;
    private String address;
    private String client;
    private String timeWindow;
    private String meetingDate;

    private Application(Parcel in) {
        city = in.readString();
        uid = in.readString();
        address = in.readString();
        client = in.readString();
        timeWindow = in.readString();
        meetingDate = in.readString();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(String timeWindow) {
        this.timeWindow = timeWindow;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(uid);
        dest.writeString(address);
        dest.writeString(client);
        dest.writeString(timeWindow);
        dest.writeString(meetingDate);
    }

    public static final Creator<Application> CREATOR = new Creator<Application>() {
        @Override
        public Application createFromParcel(Parcel in) {
            return new Application(in);
        }

        @Override
        public Application[] newArray(int size) {
            return new Application[size];
        }
    };
}
