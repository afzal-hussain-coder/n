package com.pb.criconet.Utills;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Ajay on 10/5/2017.
 */
public class MytextviewThin extends androidx.appcompat.widget.AppCompatTextView {

    public MytextviewThin(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MytextviewThin(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MytextviewThin(Context context) {
        super(context);
        init();
    }

    public void init() {
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"Montserrat_Thin.otf");
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Roboto_Thin.ttf");
        setTypeface(typeface, 1);

    }
}
