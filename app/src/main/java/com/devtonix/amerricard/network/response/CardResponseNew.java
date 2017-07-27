
package com.devtonix.amerricard.network.response;

import android.util.Log;

import com.devtonix.amerricard.model.CardItem;
import com.devtonix.amerricard.model.CardResponseItem;
import com.devtonix.amerricard.model.CategoryItem;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CardResponseNew {

    private static final String TAG = CardResponseNew.class.getSimpleName();

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<CategoryItem> data = null;

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public List<CategoryItem> getCategories() {
        long startTime = System.currentTimeMillis();


        //base cycle, ~5 iterations
        for (int i = 0; i < data.size(); i++) {

            //first json element in current hierarchy is always a category
            final CategoryItem baseCategoryItem = data.get(i);
            Gson gson = new Gson();
            CardResponseItem[] responseItems = gson.fromJson(baseCategoryItem.getData(), CardResponseItem[].class);
            for (CardResponseItem responseItem : responseItems) {
                if ("category".equals(responseItem.getType())){
                    baseCategoryItem.addCategoryItem(new CategoryItem(responseItem));
                } else if ("card".equals(responseItem.getType())) {
                    baseCategoryItem.addCardItem(new CardItem(responseItem));
                }
            }
        }
        Log.i(TAG, "onResponse: " + (System.currentTimeMillis() - startTime) );

        return data;
    }
}
