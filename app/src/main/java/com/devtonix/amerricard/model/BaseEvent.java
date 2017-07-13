package com.devtonix.amerricard.model;

import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;
import com.devtonix.amerricard.utils.LanguageUtils;

public abstract class BaseEvent {

    public static final int TYPE_EVENT = 0;
    public static final int TYPE_CONTACT = 1;
    public static final int TYPE_CELEBRITY = 2;

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
}
