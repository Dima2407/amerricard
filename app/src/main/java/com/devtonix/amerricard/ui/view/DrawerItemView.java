package com.devtonix.amerricard.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devtonix.amerricard.R;

public class DrawerItemView extends LinearLayout {

    private final TextView titleView;
    private String titleText;
    private final Drawable icon;
    private String infoText;
    private int color;


    private final TextView infoView;

    public DrawerItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_drawer_item, this);
        setOrientation(HORIZONTAL);

        TypedArray typedArray =
                context.getTheme().obtainStyledAttributes(attrs, R.styleable.DrawerItemView, 0, 0);
        try {
            titleText = typedArray.getString(R.styleable.DrawerItemView_item_title);
            icon = typedArray.getDrawable(R.styleable.DrawerItemView_item_icon);
            infoText = typedArray.getString(R.styleable.DrawerItemView_item_info);
            color = typedArray.getColor(R.styleable.DrawerItemView_item_tint, -1);
        } finally {
            typedArray.recycle();
        }

        ImageView iconView = (ImageView) findViewById(R.id.view_drawer_item_icon);
        titleView = (TextView) findViewById(R.id.view_drawer_item_text);
        infoView = (TextView) findViewById(R.id.coin_value_text_view);

        if (color != -1 && Build.VERSION.SDK_INT >= 21) {
            iconView.setImageTintList(ColorStateList.valueOf(color));
        }
        iconView.setImageDrawable(icon);
        titleView.setText(titleText);

        setInfoText(infoText);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
        if (TextUtils.isEmpty(infoText)) {
            infoView.setVisibility(GONE);
        } else {
            infoView.setVisibility(VISIBLE);
            infoView.setText(infoText);
        }
        invalidate();
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
        titleView.setText(titleText);
        invalidate();
    }
}
