package com.pb.criconet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderCreate implements Serializable {

    @SerializedName("errors")
    @Expose
    private Errors errors;

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("booking_id")
    @Expose
    private int bookingId;
    @SerializedName("payment")
    @Expose
    private int payment;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("payment_option")
    @Expose
    private PaymentOption paymentOption;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaymentOption getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(PaymentOption paymentOption) {
        this.paymentOption = paymentOption;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public class Notes implements Serializable{

        @SerializedName("address")
        @Expose
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }


    public class PaymentOption implements Serializable{

        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("amount")
        @Expose
        private int amount;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("callback_url")
        @Expose
        private String callbackUrl;
        @SerializedName("prefill")
        @Expose
        private Prefill prefill;
        @SerializedName("notes")
        @Expose
        private Notes notes;
        @SerializedName("theme")
        @Expose
        private Theme theme;
        @SerializedName("session_date")
        @Expose
        private String sessionDate;
        @SerializedName("session_time")
        @Expose
        private String sessionTime;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
        }

        public Prefill getPrefill() {
            return prefill;
        }

        public void setPrefill(Prefill prefill) {
            this.prefill = prefill;
        }

        public Notes getNotes() {
            return notes;
        }

        public void setNotes(Notes notes) {
            this.notes = notes;
        }

        public Theme getTheme() {
            return theme;
        }

        public void setTheme(Theme theme) {
            this.theme = theme;
        }

        public String getSessionDate() {
            return sessionDate;
        }

        public void setSessionDate(String sessionDate) {
            this.sessionDate = sessionDate;
        }

        public String getSessionTime() {
            return sessionTime;
        }

        public void setSessionTime(String sessionTime) {
            this.sessionTime = sessionTime;
        }

    }

    public class Prefill implements Serializable{

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("contact")
        @Expose
        private String contact;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

    }


    public class Theme implements Serializable{

        @SerializedName("color")
        @Expose
        private String color;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

    }

    public class Errors implements Serializable{

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
