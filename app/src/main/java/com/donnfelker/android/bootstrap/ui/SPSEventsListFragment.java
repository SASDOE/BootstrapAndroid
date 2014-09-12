package com.donnfelker.android.bootstrap.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donnfelker.android.bootstrap.R;
import com.donnfelker.android.bootstrap.core.SPSEvent;
import com.viewpagerindicator.TitlePageIndicator;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by tulga on 9/12/14.
 */
public class SPSEventsListFragment extends ItemListFragment<SPSEvent> {

        @Inject protected BootstrapServiceProvider serviceProvider;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Injector.inject(this);
        }

        @Override
        protected void configureList(Activity activity, ListView listView) {
            super.configureList(activity, listView);

            listView.setFastScrollEnabled(true);
            listView.setDividerHeight(0);

            getListAdapter()
                    .addHeader(activity.getLayoutInflater()
                            .inflate(R.layout.news_list_item_labels, null));
        }

        @Override
        protected LogoutService getLogoutService() {
            return logoutService;
        }

        @Override
        public void onDestroyView() {
            setListAdapter(null);

            super.onDestroyView();
        }

        @Override
        public Loader<List<News>> onCreateLoader(int id, Bundle args) {
            final List<News> initialItems = items;
            return new ThrowableLoader<List<News>>(getActivity(), items) {

                @Override
                public List<News> loadData() throws Exception {
                    try {
                        if (getActivity() != null) {
                            return serviceProvider.getService(getActivity()).getNews();
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
        protected SingleTypeAdapter<News> createAdapter(List<News> items) {
            return new NewsListAdapter(getActivity().getLayoutInflater(), items);
        }

    public void onListItemClick(ListView l, View v, int position, long id) {
        News news = ((News) l.getItemAtPosition(position));

        startActivity(new Intent(getActivity(), NewsActivity.class).putExtra(NEWS_ITEM, news));
    }

    @Override
    protected int getErrorMessage(Exception exception) {
        return R.string.error_loading_news;
    }

    @InjectView(R.id.tpi_header)
    protected TitlePageIndicator indicator;

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
        indicator.setViewPager(pager);
        pager.setCurrentItem(1);

    }

    class SPSEventsPageAdapter extends PagerAdapter {
        public int getCount() {
            return 5;
        }
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resId = 0;
            switch (position) {
//                case 0:
//                    resId = R.layout.swipe1;
//                    break;
//                case 1:
//                    resId = R.layout.swipe2;
//                    break;
//                case 2:
//                    resId = R.layout.swipe3;
//                    break;
//                case 3:
//                    resId = R.layout.swipe4;
//                    break;
//                case 4:
//                    resId = R.layout.swipe5;
//                    break;
            }
            View view = inflater.inflate(resId, null);
            ((ViewPager) collection).addView(view, 0);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);
        }
    }
}