package com.devtonix.amerricard.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devtonix.amerricard.R;
import com.devtonix.amerricard.model.Item;
import com.devtonix.amerricard.ui.activity.CategoryActivity;
import com.devtonix.amerricard.ui.activity.DetailActivity;
import com.devtonix.amerricard.ui.adapter.CategoryGridAdapter;

import java.util.ArrayList;

/**
 * Created by Oleksii on 10.05.17.
 */
public class CategoryFragment extends Fragment implements CategoryGridAdapter.OnFavoriteClickListener {


    private CategoryGridAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private int position;

    public static CategoryFragment getInstance(int position) {
        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setPosition(position);
        return categoryFragment;
    }

    private void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        emptyText = (TextView) view.findViewById(R.id.card_empty_text);

        int countRow = 2;

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), countRow));
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (((CategoryActivity) getActivity()).getCategories(position).size() != 0) {

                    Log.d("CategoryFragment", "visible");
                    manageVisible(true);

                    int width;
                    int height;
                    if (recyclerView.getWidth() > recyclerView.getHeight()) {
                        width = (recyclerView.getWidth()) / 4;
                    } else {
                        width = (recyclerView.getWidth()) / 2;

                    }
                    height = (int) (width * 1.6);

                    adapter = new CategoryGridAdapter(getActivity(), ((CategoryActivity) getActivity()).getCategories(position),
                            CategoryFragment.this, width, height);
                    recyclerView.setAdapter(adapter);
                } else {
                    manageVisible(false);
                    Log.d("CategoryFragment", "not visible");
                }
            }
        });

        return view;
    }

    @Override
    public void onItemClicked(int pos) {

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("list",new ArrayList<Item>(((CategoryActivity) getActivity()).getCategories(position)));
        intent.putExtra("position", pos);
        startActivity(intent);
    }

    @Override
    public void onFavoriteClicked(int position) {}


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
