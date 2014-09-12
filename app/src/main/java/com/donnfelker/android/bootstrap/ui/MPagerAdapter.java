package com.donnfelker.android.bootstrap.ui;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class MPagerAdapter extends PagerAdapter {

    List items;
    int resourceId;

    public MPagerAdapter(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resourceId, null);
        update(view, items.get(position));
        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    protected abstract void update(View view, Object object);


    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }
}
