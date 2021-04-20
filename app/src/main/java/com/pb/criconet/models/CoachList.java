package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoachList {

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

    @SerializedName("errors")
    @Expose
    private Errors errors;

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


    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }


    public class Datum {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("profile_title")
        @Expose
        private String profileTitle;
        @SerializedName("about_coach_profile")
        @Expose
        private String aboutCoachProfile;
        @SerializedName("specialization")
        @Expose
        private String specialization;
        @SerializedName("charge_amount")
        @Expose
        private String chargeAmount;
        @SerializedName("cuurency_code")
        @Expose
        private String cuurencyCode;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProfileTitle() {
            return profileTitle;
        }

        public void setProfileTitle(String profileTitle) {
            this.profileTitle = profileTitle;
        }

        public String getAboutCoachProfile() {
            return aboutCoachProfile;
        }

        public void setAboutCoachProfile(String aboutCoachProfile) {
            this.aboutCoachProfile = aboutCoachProfile;
        }

        public String getSpecialization() {
            return specialization;
        }

        public void setSpecialization(String specialization) {
            this.specialization = specialization;
        }

        public String getChargeAmount() {
            return chargeAmount;
        }

        public void setChargeAmount(String chargeAmount) {
            this.chargeAmount = chargeAmount;
        }

        public String getCuurencyCode() {
            return cuurencyCode;
        }

        public void setCuurencyCode(String cuurencyCode) {
            this.cuurencyCode = cuurencyCode;
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

    }

    @Override
    public String toString() {
        return "CoachList{" +
                "apiStatus='" + apiStatus + '\'' +
                ", apiText='" + apiText + '\'' +
                ", apiVersion='" + apiVersion + '\'' +
                ", data=" + data +
                '}';
    }

    public class Errors {

        @SerializedName("error_id")
        @Expose
        private String errorId;
        @SerializedName("error_text")
        @Expose
        private String errorText;

        public String getErrorId() {
            return errorId;
        }

        public void setErrorId(String errorId) {
            this.errorId = errorId;
        }

        public String getErrorText() {
            return errorText;
        }

        public void setErrorText(String errorText) {
            this.errorText = errorText;
        }

    }
}
