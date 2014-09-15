package com.donnfelker.android.bootstrap.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.donnfelker.android.bootstrap.BootstrapApplication;
import com.donnfelker.android.bootstrap.R;
import com.donnfelker.android.bootstrap.core.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsListAdapter extends AlternatingColorListAdapter<News> {
    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public NewsListAdapter(final LayoutInflater inflater, final List<News> items,
                           final boolean selectable) {
        super(R.layout.news_list_item, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public NewsListAdapter(final LayoutInflater inflater, final List<News> items) {
        super(R.layout.news_list_item, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.tv_title, R.id.tv_summary, R.id.tv_thumbnail,
                R.id.tv_date};
    }

    @Override
    protected void update(final int position, final News item) {
        super.update(position, item);

        setText(0, item.getTitle());
        setText(1, item.getContent());

        if (item.getThumbnail() != null) {
            Picasso.with(BootstrapApplication.getInstance())
                    .load(item.getThumbnail())
//                .placeholder(R.drawable.gravatar_icon)
                    .into(imageView(2));
            imageView(2).setVisibility(View.VISIBLE);
        } else {
            imageView(2).setVisibility(View.GONE);
        }
        //setNumber(R.id.tv_date, item.getCreatedAt());
    }
}
