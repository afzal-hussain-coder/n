package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookingHistory {

    @SerializedName("api_status")
    @Expose
    private String apiStatus;
    @SerializedName("api_text")
    @Expose
    private String apiText;
    @SerializedName("api_version")
    @Expose
    private String apiVersion;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(String apiStatus) {
        this.apiStatus = apiStatus;
    }

    public String getApiText() {
        return apiText;
    }

    public void setApiText(String apiText) {
        this.apiText = apiText;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public class Datum implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("coach_user_id")
        @Expose
        private String coachUserId;
        @SerializedName("is_paymet")
        @Expose
        private String isPaymet;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("booking_slot_date")
        @Expose
        private String bookingSlotDate;
        @SerializedName("booking_slot_id")
        @Expose
        private String bookingSlotId;
        @SerializedName("booking_slot_txt")
        @Expose
        private String bookingSlotTxt;
        @SerializedName("online_session_start_time")
        @Expose
        private String onlineSessionStartTime;
        @SerializedName("online_session_end_time")
        @Expose
        private String onlineSessionEndTime;
        @SerializedName("booking_amount")
        @Expose
        private String bookingAmount;
        @SerializedName("booking_time")
        @Expose
        private String bookingTime;
        @SerializedName("slot_duration")
        @Expose
        private String slotDuration;
        @SerializedName("booking_status")
        @Expose
        private String bookingStatus;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("profile_link")
        @Expose
        private String profileLink;
        @SerializedName("btn1")
        @Expose
        private String btn1;

        @SerializedName("btn2")
        @Expose
        private String btn2;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoachUserId() {
            return coachUserId;
        }

        public void setCoachUserId(String coachUserId) {
            this.coachUserId = coachUserId;
        }

        public String getIsPaymet() {
            return isPaymet;
        }

        public void setIsPaymet(String isPaymet) {
            this.isPaymet = isPaymet;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getBookingSlotDate() {
            return bookingSlotDate;
        }

        public void setBookingSlotDate(String bookingSlotDate) {
            this.bookingSlotDate = bookingSlotDate;
        }

        public String getBookingSlotId() {
            return bookingSlotId;
        }

        public void setBookingSlotId(String bookingSlotId) {
            this.bookingSlotId = bookingSlotId;
        }

        public String getBookingSlotTxt() {
            return bookingSlotTxt;
        }

        public void setBookingSlotTxt(String bookingSlotTxt) {
            this.bookingSlotTxt = bookingSlotTxt;
        }

        public String getOnlineSessionStartTime() {
            return onlineSessionStartTime;
        }

        public void setOnlineSessionStartTime(String onlineSessionStartTime) {
            this.onlineSessionStartTime = onlineSessionStartTime;
        }

        public String getOnlineSessionEndTime() {
            return onlineSessionEndTime;
        }

        public void setOnlineSessionEndTime(String onlineSessionEndTime) {
            this.onlineSessionEndTime = onlineSessionEndTime;
        }

        public String getBookingAmount() {
            return bookingAmount;
        }

        public void setBookingAmount(String bookingAmount) {
            this.bookingAmount = bookingAmount;
        }

        public String getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }

        public String getSlotDuration() {
            return slotDuration;
        }

        public void setSlotDuration(String slotDuration) {
            this.slotDuration = slotDuration;
        }

        public String getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(String bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getProfileLink() {
            return profileLink;
        }

        public void setProfileLink(String profileLink) {
            this.profileLink = profileLink;
        }

        public String getBtn1() {
            return btn1;
        }

        public void setBtn1(String btn1) {
            this.btn1 = btn1;
        }

        public String getBtn2() {
            return btn2;
        }

        public void setBtn2(String btn2) {
            this.btn2 = btn2;
        }
    }
}
