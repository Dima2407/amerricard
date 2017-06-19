
package com.devtonix.amerricard.network.response;

import android.util.Log;

import com.devtonix.amerricard.model.CardItem;
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
    private List<JsonElement> data = null;

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public List<JsonElement> getData() {

        List<CategoryItem> categories = new ArrayList<>();

//        for (JsonElement element : data) {
//
//            final CategoryItem categoryItem = new Gson().fromJson(element, CategoryItem.class);
//            categories.add(categoryItem);
//            Log.d(TAG, "category name=" + categoryItem.getName().getEn());
//
//
//            for (JsonElement el : categoryItem.getData()) {
//                if (el.toString().contains("category")) {
//
//                    final CategoryItem secondLvl = new Gson().fromJson(el, CategoryItem.class);
////                    categories.get(i)
//
//                    for (JsonElement cardEl : secondLvl.getData()) {
//                        final CardItem card = new Gson().fromJson(cardEl, CardItem.class);
//                    }
//
//                } else {
//                    final CardItem card = new Gson().fromJson(el, CardItem.class);
//                }
//            }
//
//        }

//        for (int i = 0; i < data.size(); i++) {
//            final CategoryItem categoryItem = new Gson().fromJson(data.get(i), CategoryItem.class);
//            categories.add(categoryItem);
//
//            for (int j = 0; j < categoryItem.getData().size(); j++) {
//
//                if (categoryItem.getData().get(j).toString().contains("category")){
//
//                    final CategoryItem secondLvl = new Gson().fromJson(categoryItem.getData().get(j), CategoryItem.class);
//                    final List<CategoryItem> categoriesSecondLvl = categories.get(i).getData();
//
//                }
//
//
//            }
//
//
//        }


        return data;
    }
}
