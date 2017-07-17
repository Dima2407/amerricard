package com.devtonix.amerricard.model;

import android.graphics.Color;
import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;
import com.devtonix.amerricard.R;
import com.devtonix.amerricard.utils.LanguageUtils;

import java.util.Random;

public abstract class BaseEvent {

    public static final BaseEvent EMPTY = new BaseEvent() {
        @Override
        public String getEventDate() {
            return null;
        }

        @Override
        public Name getEventName() {
            return null;
        }

        @Override
        public int getEventType() {
            return 0;
        }

        @Override
        public GlideUrl getEventImage() {
            return null;
        }

        @Override
        public GlideUrl getThumbImageUrl() {
            return null;
        }
    };

    public static final int TYPE_EVENT = 0;
    public static final int TYPE_CONTACT = 1;
    public static final int TYPE_CELEBRITY = 2;
    private int color = -1;
    private final Random rand = new Random();

    public abstract String getEventDate();
    public abstract Name getEventName();
    public abstract int getEventType();
    public abstract GlideUrl getEventImage();
    public abstract GlideUrl getThumbImageUrl();
    public String getLetters(String language){
        String name = getName(language);
        if (TextUtils.isEmpty(name))
            return null;
        String strs[] = name.split(" ");
        if (strs.length > 1) {
            return strs[0].substring(0, 1).concat(strs[1].substring(0, 1)).toUpperCase();
        } else {
            return name.substring(0, 2).toUpperCase();
        }
    }

    public String getName(String currLang){
        switch (getEventType()){
            case BaseEvent.TYPE_EVENT:
                return LanguageUtils.convertLang(getEventName(), currLang);
            case TYPE_CELEBRITY:
                return getEventName().getBaseName();
            case TYPE_CONTACT:
                return getEventName().getBaseName();
        }
        return null;
    }

    public int getColor(){
        if(color == -1){
            color = getRandomColor();
        }
        return color;
    }
    private int getRandomColor() {
        int numberOfColor = rand.nextInt(19);
        int color = 0;
        switch (numberOfColor) {
            case 0:
                color = R.color.colorRed;
                break;
            case 1:
                color = R.color.colorPink;
                break;
            case 2:
                color = R.color.colorPurple;
                break;
            case 3:
                color = R.color.colorDeepPurple;
                break;
            case 4:
                color = R.color.colorIndigo;
                break;
            case 5:
                color = R.color.colorBlue;
                break;
            case 6:
                color = R.color.colorLightBlue;
                break;
            case 7:
                color = R.color.colorCyan;
                break;
            case 8:
                color = R.color.colorTeal;
                break;
            case 9:
                color = R.color.colorGreen;
                break;
            case 10:
                color = R.color.colorLightGreen;
                break;
            case 11:
                color = R.color.colorLime;
                break;
            case 12:
                color = R.color.colorYellow;
                break;
            case 13:
                color = R.color.colorAmber;
                break;
            case 14:
                color = R.color.colorOrange;
                break;
            case 15:
                color = R.color.colorDeepOrange;
                break;
            case 16:
                color = R.color.colorBrown;
                break;
            case 17:
                color = R.color.colorGrey;
                break;
            case 18:
                color = R.color.colorBlueGrey;
                break;
        }
        return color;
    }
}
