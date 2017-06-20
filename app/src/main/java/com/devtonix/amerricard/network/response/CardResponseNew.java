
package com.devtonix.amerricard.network.response;

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

    public List<CategoryItem> getCategories() {

        final List<CategoryItem> resultCategories = new ArrayList<>();

        //base cycle, ~5 iterations
        for (int i = 0; i < data.size(); i++) {


            //first json element in current hierarchy is always a category
            final CategoryItem baseCategoryItem = new Gson().fromJson(data.get(i), CategoryItem.class);
            final List<CardItem> cards = new ArrayList<>();
            final List<CategoryItem> categories = new ArrayList<>();


            for (int j = 0; j < baseCategoryItem.getData().size(); j++) {

                if (baseCategoryItem.getData().get(j).toString().contains("category")) {

                    final CategoryItem secondLvl = new Gson().fromJson(baseCategoryItem.getData().get(j), CategoryItem.class);


                    final List<CardItem> innerCards = new ArrayList<>();
                    for (JsonElement c : secondLvl.getData()) {
                        final CardItem cardItem = new Gson().fromJson(c, CardItem.class);
                        innerCards.add(cardItem);
                    }
                    secondLvl.setCardItems(innerCards);


                    categories.add(secondLvl);

                } else {
                    final  CardItem cardItem = new Gson().fromJson(baseCategoryItem.getData().get(j), CardItem.class);
                    cards.add(cardItem);
                }
            }

            baseCategoryItem.setCardItems(cards);
            baseCategoryItem.setCategoryItems(categories);

            resultCategories.add(baseCategoryItem);
        }

        return resultCategories;
    }
}
