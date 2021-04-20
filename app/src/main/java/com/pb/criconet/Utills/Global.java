package com.pb.criconet.Utills;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.pb.criconet.R;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import okhttp3.ResponseBody;
import timber.log.Timber;


public class Global {

    public static final String FILE_UPLOAD_URL = "https://criconetonline.com/app_api.php?type=new_post";
    public static final String URL = "https://stage.criconetonline.com/app_api.php?type=";
    //public static final String URL = "https://criconetonline.com/app_api.php?type=";
    public static String URL2 = "https://criconetonline.com/";
    public static String websiteURL = "https://criconetonline.com/webviewhome?";
    public static String GameURL = "https://criconetonline.com/cricket_js/index.php?";
    public static String websiteURL_demo = "https://criconetonline.com/webviewhome?username=dfordharma&password=dharma@123";
    public static String NOTIFICATION_KEY = "key=AIzaSyCKqEUtgG3zQq1JRI3s5bS5H4uk0qXhJB4";
    public static final int TYPE_VIDEO = 0,
            TYPE_IMAGE = 1, TYPE_MULTI_IMAGE = 2,
            TYPE_TEXT = 3, TYPE_LINK = 4,
            TYPE_YOUTUBE = 5;
    public static final String POST_TYPE_VIDEO = "video",
            POST_TYPE_IMAGE = "image", POST_TYPE_MULTI_IMAGE = "photo_multi",
            POST_TYPE_TEXT = "text", POST_TYPE_LINK = "link",
            POST_TYPE_YOUTUBE = "youtube";

    public static final int POST_PRIVACY_PUBLIC = 0, POST_PRIVACY_PRIVATE = 3;
    public static final int PRIVACY_EVERYONE = 0, PRIVACY_PEOPLE_I_FOLLOW = 1, PRIVACY_MY_FOLLOWERS = 2,
            PRIVACY_ONLY_ME = 3;

    public static final String NO_INTERNET_CONNECTION = "Please check your internet connection!";
    public static Map Notification_data;

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

    public static boolean validateName(String firstrname) {
        return firstrname.length() > 0;
    }

    public static boolean validateLength(String name, int len) {
        return name.length() >= len;
    }

    public static boolean password(String firstrname) {
        return firstrname.length() >= 6;
    }

    public static boolean ValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void msgDialog(Activity ac, String msg) {
        try {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
            alertbox.setTitle(ac.getResources().getString(R.string.app_name));
            alertbox.setMessage(Html.fromHtml(msg));
            alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
            alertbox.show();
        } catch (Exception e) {
        }
    }

    public static void showDialog(final Activity ac) {
        android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(ac);
        alertbox.setTitle(ac.getResources().getString(R.string.app_name));
        alertbox.setMessage(R.string.no_internet);
        alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ac.finish();
                    }
                });
        alertbox.show();
    }

    public static void showDialog(final Activity ac, String msg) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(ac);
        alertbox.setTitle(ac.getResources().getString(R.string.app_name));
        alertbox.setMessage(msg);
        alertbox.setPositiveButton(ac.getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
    }

    public static void showSettingsDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings(activity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    public static void Sleep(int sec) {
        try {
            //set time in mili
            Thread.sleep(sec * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // navigating user to app settings
    private static void openSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 101);
    }

    public static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public static Bitmap CheckRotationImage(String selectedImage) {

        String picturePath = selectedImage;
        Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

        ExifInterface exif = null;
        try {
            File pictureFile = new File(picturePath);
            exif = new ExifInterface(pictureFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ExifInterface.ORIENTATION_NORMAL;

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                loadedBitmap = rotateBitmap(loadedBitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                loadedBitmap = rotateBitmap(loadedBitmap, 180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                loadedBitmap = rotateBitmap(loadedBitmap, 270);
                break;
        }
        return loadedBitmap;
    }

    public static Bitmap rotateImage(Bitmap bmp, String imageUrl) {
        if (bmp != null) {
            ExifInterface ei;
            int orientation = 0;
            try {
                ei = new ExifInterface(imageUrl);
                orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
            }

            int bmpWidth = bmp.getWidth();
            int bmpHeight = bmp.getHeight();
            Matrix matrix = new Matrix();

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;

                default:
                    break;
            }

            Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);

            return resizedBitmap;

        } else {
            return bmp;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }


        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static String returnDate(String dateToConvert) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        String temp = dateToConvert;
        try {
            date = formatter.parse(temp);
            Log.e("formated date ", date + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formateDateMonthName = new SimpleDateFormat("dd MMM yyyy").format(date);
        Log.v("output_date ", formateDateMonthName);
        String formateTime = new SimpleDateFormat("hh:mm a").format(date);
        Log.v("output_time ", formateDateMonthName + "--" + formateTime);
        String finalDateTime = formateDateMonthName;

        return finalDateTime;
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-0];
    }



    public static ProgressDialog getProgressDialog(Context context, String message, Boolean canceleable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(canceleable);
        dialog.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.loading_placeholder));
        dialog.show();
        return dialog;
    }

    public static void dismissDialog(ProgressDialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.cancel();
    }

    public static Calendar convertStringToCalendar(String time) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateformat.parse(time);
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            return calendar;
        }
    }

    public static String getDateGot(String dateTime){
        Date date=null;
        String realdate=null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static String getMonth(String dateTime){
        Date date=null;
        String realdate=null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("MM").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static String getYear(String dateTime){
        Date date=null;
        String realdate=null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(dateTime);
            realdate = new SimpleDateFormat("yyyy").format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return realdate;
    }

    public static Drawable getThreeDots(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.sample_three_icons);
        //Add padding to too large icon
        return new InsetDrawable(drawable, 100, 0, 100, 0);
    }

    public static int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static void showSnackbar(View view,String msg){
        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
