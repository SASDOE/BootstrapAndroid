package com.donnfelker.android.bootstrap.ui;

import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.donnfelker.android.bootstrap.BootstrapApplication;
import com.donnfelker.android.bootstrap.BootstrapServiceProvider;
import com.donnfelker.android.bootstrap.Injector;
import com.donnfelker.android.bootstrap.R;
import com.donnfelker.android.bootstrap.authenticator.LogoutService;
import com.donnfelker.android.bootstrap.core.SPSEvent;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by tulga on 9/12/14.
 */
public class SPSEventsListFragment extends ItemPagerFragment<SPSEvent> {

    @Inject
    protected BootstrapServiceProvider serviceProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
    }

    @Override
    protected LogoutService getLogoutService() {
        return null;
    }

    @Override
    public void onDestroyView() {
        setPagerAdapter(null);
        super.onDestroyView();
    }

    @Override
    public Loader<List<SPSEvent>> onCreateLoader(int id, Bundle args) {
        final List<SPSEvent> initialItems = items;
        return new ThrowableLoader<List<SPSEvent>>(getActivity(), items) {

            @Override
            public List<SPSEvent> loadData() throws Exception {
                    try {
                if (getActivity() != null) {
                    return serviceProvider.getService(getActivity()).getEvents();
                } else {
                    return Collections.emptyList();
                }
                    } catch (OperationCanceledException e) {
                        Activity activity = getActivity();
                        if (activity != null)
                            activity.finish();
                        return initialItems;
                    }
            }
        };
    }

    @Override
    protected MPagerAdapter createAdapter(List<SPSEvent> items) {
        MPagerAdapter mPagerAdapter = new MPagerAdapter(R.layout.event_page_item) {
            @Override
            protected void update(View view, Object object) {
                SPSEvent event = ((SPSEvent) object);
                TextView textViewTitle = ((TextView) view.findViewById(R.id.title));
                textViewTitle.setText(event.getTitle());
                Picasso.with(BootstrapApplication.getInstance())
                        .load(event.getThumbnail())
//                        .placeholder(R.drawable.gravatar_icon)
                        .into(((ImageView) view.findViewById(R.id.thumbnail)));
            }
        };
        mPagerAdapter.setItems(items);
        return mPagerAdapter;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
//        SPSEvent news = ((SPSEvent) l.getItemAtPosition(position));
//
//        startActivity(new Intent(getActivity(), NewsActivity.class).putExtra(NEWS_ITEM, news));
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_news;
    }

//    @InjectView(R.id.tpi_header)
//    protected TitlePageIndicator indicator;

    @InjectView(R.id.vp_pages)
    protected ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carousel, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Views.inject(this, getView());

        pager.setAdapter(new BootstrapPagerAdapter(getResources(), getChildFragmentManager()));
//        indicator.setViewPager(pager);
        pager.setPageMargin(-300);
        pager.setOffscreenPageLimit(2);
//        pager.setCurrentItem(1);
        pager.setPageTransformer(true, new DepthPageTransformer());

    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
//                view.setTranslationX(0);
//                view.setScaleX(1);
//                view.setScaleY(1);

                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1);

                // Counteract the default slide transition
//                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}