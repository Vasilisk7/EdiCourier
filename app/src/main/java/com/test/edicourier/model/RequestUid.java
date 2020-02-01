package com.test.edicourier.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RequestUid implements Parcelable {

    private String city;
    private String uid;
    private String address;
    private String mobilePhone;
    private String product;
    private String client;
    private String timeWindow;
    private String meetingDate;
    private String dateKko;
    private String assignDate;
    private String sdo;

    public RequestUid() {
    }
    
    private RequestUid(Parcel in) {
        city = in.readString();
        uid = in.readString();
        address = in.readString();
        mobilePhone = in.readString();
        product = in.readString();
        client = in.readString();
        timeWindow = in.readString();
        meetingDate = in.readString();
        dateKko = in.readString();
        assignDate = in.readString();
        sdo = in.readString();
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

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

    public String getDateKko() {
        return dateKko;
    }

    public void setDateKko(String dateKko) {
        this.dateKko = dateKko;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getSdo() {
        return sdo;
    }

    public void setSdo(String sdo) {
        this.sdo = sdo;
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
        dest.writeString(mobilePhone);
        dest.writeString(product);
        dest.writeString(client);
        dest.writeString(timeWindow);
        dest.writeString(meetingDate);
        dest.writeString(dateKko);
        dest.writeString(assignDate);
        dest.writeString(sdo);
    }

    public static final Creator<RequestUid> CREATOR = new Creator<RequestUid>() {
        @Override
        public RequestUid createFromParcel(Parcel in) {
            return new RequestUid(in);
        }

        @Override
        public RequestUid[] newArray(int size) {
            return new RequestUid[size];
        }
    };
}
