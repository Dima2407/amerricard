package com.devtonix.amerricard.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.ui.adapter.CategoryGridAdapter;

public class FavoriteActivity extends DrawerActivity /*implements CategoryGridAdapter.OnFavoriteClickListener*/ {

    private CategoryGridAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyText;
//    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(R.layout.activity_favorite);

        setTitle(getString(R.string.favorites));

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        emptyText = (TextView) findViewById(R.id.card_empty_text);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                manageRecycler();
            }
        });

    }

    private void manageRecycler() {
//        items = SharedHelper.getInstance().getFavorites();
//        if (items != null && items.size() != 0) {
//            manageVisible(true);
//
//            int width = (recyclerView.getWidth())/2;
//            int height = (int) (width*1.6);
//            adapter = new CategoryGridAdapter(FavoriteActivity.this, items,
//                    FavoriteActivity.this, width, height);
//            recyclerView.setAdapter(adapter);
//        } else {
//            manageVisible(false);
//        }
    }

//    @Override
//    public void onItemClicked(int position) {
//        Intent intent = new Intent(this, DetailActivity.class);
//        intent.putExtra("list",new ArrayList<Item>(items));
//        intent.putExtra("position", position);
//        startActivity(intent);
//    }

//    @Override
//    public void onFavoriteClicked(int position) {
//        manageRecycler();
//    }


    private void manageVisible(boolean isListVisible) {
        if (isListVisible) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }
}
