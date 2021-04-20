package com.pb.criconet.Utills;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Pradeep on 1/31/2017.
 */

public class SessionManager {

    private static String NAME = "NAME";
    private static String PROFILETYPE = "PROFILETYPE";
    private static String FNAME = "FNAME";
    private static String LNAME = "LNAME";
    private static String EMAILID = "EMAILID";
    private static String PRICE = "PRICE";
    private static String mobile = "mobile";
    private static String user_id = "user_id";
    private static String user_type = "user_type";
    private static String SESSION_CHECK_LOGIN = "SESSION_CHECK_LOGIN";
    private static String check_agreement = "CHECK_AGREEMENT";
    private static String password = "password";
    private static String sex = "sex";
    private static String session_id = "session_id";
    private static String dob = "DOB";
    private static String fitness = "fitness";
    private static String address = "address";
    private static String pinCode = "pinCode";
    private static String country = "country";
    private static String states = "states";
    private static String city = "city";
    private static String stateid = "state_id";
    private static String cityid = "city_id";
    private static String image = "image";
    private static String cover = "cover";
    private static String select_type = "select_type";
    private static String firebaseId = "firebaseId";
    private static String school = "school";
    private static String studied = "studied";
    private static String employment = "employment";
    private static String devicetoken = "devicetoken";
    private static String Onlinestatus = "Onlinestatus";
    private static String Friends = "FRIENDS";
    private static String Notification_count = "NOTIFICATION_COUNT";
    private static String languagecode = "LANGUAGECODE";
    private static String languagekey = "LANGUAGEKEY";

//    private static String CARTYPEID = "CARTYPEID";
//    private static String CARTYPENAME = "CARTYPENAME";
//    private static String CARDDETAIL = "CARDDETAIL";
//    private static String PUNCHINSTATUS = "PUNCHINSTATUS";
//    private static String VEHICLENUMBER = "VEHICLENUMBER";

//    private static String vehicledetail = "vehicledetail";
//    private static String uploaddoc = "uploaddoc";
//    private static String docverified = "docverified";
//    private static String uploaddrivinglicence = "uploaddrivinglicence";
//    private static String uploadvehicleinsurance = "uploadvehicleinsurance";
//    private static String uploadvehiclepermit = "uploadvehiclepermit";
//    private static String uploadvehicleregistration = "uploadvehicleregistration";
//    private static String vahical_number = "vahical_number";
//    private static String brand_id = "brand_id";
//    private static String carmodel_id = "carmodel_id";
//    private static String year = "year";
//    private static String color = "color";
//    private static String carTypeId = "carTypeId";
//    private static String carTypeName = "carTypeName";
//    private static String barnd = "barnd";
//    private static String model = "model";


    public static void savePreference(SharedPreferences prefs, String key, Boolean value) {
        Editor e = prefs.edit();
        e.putBoolean(key, value);
        e.commit();
    }

    public static void savePreference(SharedPreferences prefs, String key, int value) {
        Editor e = prefs.edit();
        e.putInt(key, value);
        e.commit();
    }

    public static void savePreference(SharedPreferences prefs, String key, String value) {
        Editor e = prefs.edit();
        e.putString(key, value);
        e.commit();
    }

    public static void dataclear(SharedPreferences prefs) {
        Editor e = prefs.edit();
        e.clear();
        e.commit();
    }

//    public static void save_card(SharedPreferences prefs, String value) {
//        SessionManager.savePreference(prefs, CARDDETAIL, value);
//    }
//
//    public static String get_card(SharedPreferences prefs) {
//        return prefs.getString(CARDDETAIL, "");
//    }


    public static void save_check_login(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, SESSION_CHECK_LOGIN, value);
    }

    public static Boolean get_check_login(SharedPreferences prefs) {
        return prefs.getBoolean(SESSION_CHECK_LOGIN, false);
    }

    public static void save_check_agreement(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, check_agreement, value);
    }

    public static Boolean get_check_agreement(SharedPreferences prefs) {
        return prefs.getBoolean(check_agreement, false);
    }

    public static void save_name(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, NAME, value);
    }

    public static String get_name(SharedPreferences prefs) {
        return prefs.getString(NAME, "");
    }

    public static void save_profiletype(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, PROFILETYPE, value);
    }

    public static String get_profiletype(SharedPreferences prefs) {
        return prefs.getString(PROFILETYPE, "");
    }


    public static void save_fname(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, FNAME, value);
    }

    public static String get_fname(SharedPreferences prefs) {
        return prefs.getString(FNAME, "");
    }

    public static void save_lname(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, LNAME, value);
    }

    public static String get_lname(SharedPreferences prefs) {
        return prefs.getString(LNAME, "");
    }

    public static void save_emailid(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, EMAILID, value);
    }

    public static String get_emailid(SharedPreferences prefs) {
        return prefs.getString(EMAILID, "");
    }

    public static void save_password(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, password, value);
    }

    public static String get_password(SharedPreferences prefs) {
        return prefs.getString(password, "");
    }

    public static void save_sex(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, sex, value);
    }

    public static String get_sex(SharedPreferences prefs) {
        return prefs.getString(sex, "");
    }

    public static void save_session_id(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, session_id, value);
    }

    public static String get_session_id(SharedPreferences prefs) {
        return prefs.getString(session_id, "");
    }

    public static void save_dob(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, dob, value);
    }

    public static String get_dob(SharedPreferences prefs) {
        return prefs.getString(dob, "");
    }

    public static void save_mobile(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, mobile, value);
    }

    public static String get_mobile(SharedPreferences prefs) {
        return prefs.getString(mobile, "");
    }

    public static void save_user_id(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, user_id, value);
    }

    public static String get_user_id(SharedPreferences prefs) {
        return prefs.getString(user_id, "");
    }

    public static void save_select_type(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, select_type, value);
    }

    public static String get_select_type(SharedPreferences prefs) {
        return prefs.getString(select_type, "");
    }

    public static void save_userType(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, user_type, value);
    }

    public static String get_userType(SharedPreferences prefs) {
        return prefs.getString(user_type, "");
    }

    public static void save_price(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, PRICE, value);
    }

    public static String get_price(SharedPreferences prefs) {
        return prefs.getString(PRICE, "");
    }


    public static void save_address(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, address, value);
    }

    public static String get_address(SharedPreferences prefs) {
        return prefs.getString(address, "");
    }

    public static void savepinCode(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, pinCode, value);
    }

    public static String getpinCode(SharedPreferences prefs) {
        return prefs.getString(pinCode, "");
    }

    public static void saveCountry(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, country, value);
    }

    public static String getCountry(SharedPreferences prefs) {
        return prefs.getString(country, "");
    }

    public static void saveStates(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, states, value);
    }

    public static String getStates(SharedPreferences prefs) {
        return prefs.getString(states, "");
    }

    public static void saveCity(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, city, value);
    }

    public static String getCity(SharedPreferences prefs) {
        return prefs.getString(city, "");
    }

    public static void saveStateId(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, stateid, value);
    }

    public static String getStateId(SharedPreferences prefs) {
        return prefs.getString(stateid, "");
    }

    public static void saveCityId(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, cityid, value);
    }

    public static String getCityid(SharedPreferences prefs) {
        return prefs.getString(cityid, "");
    }


    public static void save_image(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, image, value);
    }

    public static String get_image(SharedPreferences prefs) {
        return prefs.getString(image, "");
    }

    public static void save_cover(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, cover, value);
    }

    public static String get_cover(SharedPreferences prefs) {
        return prefs.getString(cover, "");
    }

    public static void save_devicetoken(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, devicetoken, value);
    }

    public static String get_devicetoken(SharedPreferences prefs) {
        return prefs.getString(devicetoken, "");
    }

    public static void save_fitness(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, fitness, value);
    }

    public static String get_fitness(SharedPreferences prefs) {
        return prefs.getString(fitness, "");
    }

    public static void save_firebaseId(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, firebaseId, value);
    }

    public static String get_firebaseId(SharedPreferences prefs) {
        return prefs.getString(firebaseId, "");
    }

    public static void save_school(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, school, value);
    }

    public static String get_school(SharedPreferences prefs) {
        return prefs.getString(school, "");
    }

    public static void save_studied(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, studied, value);
    }

    public static String get_studied(SharedPreferences prefs) {
        return prefs.getString(studied, "");
    }

    public static void save_employment(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, employment, value);
    }

    public static String get_employment(SharedPreferences prefs) {
        return prefs.getString(employment, "");
    }

    public static void save_onlinestatus(SharedPreferences prefs, Boolean value) {
        SessionManager.savePreference(prefs, Onlinestatus, value);
    }

    public static Boolean get_onlinestatus(SharedPreferences prefs) {
        return prefs.getBoolean(Onlinestatus, false);
    }

    public static void save_Friends(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, Friends, value);
    }

    public static String get_Friends(SharedPreferences prefs) {
        return prefs.getString(Friends, "0");
    }

    public static void save_Notification(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, Notification_count, value);
    }

    public static String get_Notification(SharedPreferences prefs) {
        return prefs.getString(Notification_count, "0");
    }

    public static void save_languagecode(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, languagecode, value);
    }

    public static String get_languagecode(SharedPreferences prefs) {
        return prefs.getString(languagecode, "en");
    }

    public static void save_languagekey(SharedPreferences prefs, String value) {
        SessionManager.savePreference(prefs, languagekey, value);
    }

    public static String get_languagekey(SharedPreferences prefs) {
        return prefs.getString(languagekey, "home");
    }

}