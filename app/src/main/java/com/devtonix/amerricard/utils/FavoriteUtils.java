package com.devtonix.amerricard.utils;

import com.devtonix.amerricard.model.Item;

import java.util.ArrayList;
import java.util.List;

public class FavoriteUtils {

    public static List<Item> getFavorites() {
        List<Item> items = Preferences.getInstance().getFavorites();
        return items==null ? new ArrayList<Item>() : items;
    }

    public static void saveFavorites(List<Item> items) {
        Preferences.getInstance().saveFavorites(items);
    }
    public static void addToFavorites(Item item) {
        List<Item> items = getFavorites();
        items.add(item);
        saveFavorites(items);
    }

    public static void removeFromFavorites(Item item) {
        List<Item> items = getFavorites();
        int position = -1;
        for (int i=0;i<items.size();i++) {
            if (items.get(i).id==item.id) {
                position = i;
            }
        }
        if (position != -1) {
            items.remove(position);
            saveFavorites(items);
        }
    }

}

