
package com.donnfelker.android.bootstrap.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.donnfelker.android.bootstrap.R;
import com.donnfelker.android.bootstrap.R.id;
import com.donnfelker.android.bootstrap.R.layout;
import com.donnfelker.android.bootstrap.authenticator.LogoutService;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import com.github.kevinsawicki.wishlist.ViewUtils;

import java.util.Collections;
import java.util.List;


/**
 * Base fragment for displaying a list of items that loads with a progress bar
 * visible
 *
 * @param <E>
 */
public abstract class ItemPagerFragment<E> extends Fragment
        implements LoaderCallbacks<List<E>> {

    private static final String FORCE_REFRESH = "forceRefresh";

    /**
     * @param args bundle passed to the loader by the LoaderManager
     * @return true if the bundle indicates a requested forced refresh of the
     * items
     */
    protected static boolean isForceRefresh(final Bundle args) {
        return args != null && args.getBoolean(FORCE_REFRESH, false);
    }

    /**
     * List items provided to {@link #onLoadFinished(Loader, List)}
     */
    protected List<E> items = Collections.emptyList();

    /**
     * List view
     */
    protected ViewPager viewPager;

    /**
     * Empty view
     */
    protected TextView emptyView;

    /**
     * Progress bar
     */
    protected ProgressBar progressBar;

    /**
     * Is the list currently shown?
     */
    protected boolean listShown;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!items.isEmpty()) {
            setListShown(true, false);
        }

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(layout.item_list, null);
    }

    /**
     * Detach from list view.
     */
    @Override
    public void onDestroyView() {
        listShown = false;
        emptyView = null;
        progressBar = null;
        viewPager = null;

        super.onDestroyView();
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(android.R.id.list);
//        viewPager.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                onListItemClick((ListView) parent, view, position, id);
//            }
//        });
        progressBar = (ProgressBar) view.findViewById(id.pb_loading);

        emptyView = (TextView) view.findViewById(android.R.id.empty);

        configureList(getActivity(), getViewPager());
    }

    /**
     * Configure list after view has been created
     *
     * @param activity
     * @param listView
     */
    protected void configureList(final Activity activity, final ViewPager listView) {
        listView.setAdapter(createAdapter());
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu optionsMenu, final MenuInflater inflater) {
        inflater.inflate(R.menu.bootstrap, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (!isUsable()) {
            return false;
        }
        switch (item.getItemId()) {
            case id.refresh:
                forceRefresh();
                return true;
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract LogoutService getLogoutService();

    private void logout() {
        getLogoutService().logout(new Runnable() {
            @Override
            public void run() {
                // Calling a refresh will force the service to look for a logged in user
                // and when it finds none the user will be requested to log in again.
                forceRefresh();
            }
        });
    }

    /**
     * Force a refresh of the items displayed ignoring any cached items
     */
    protected void forceRefresh() {
        final Bundle bundle = new Bundle();
        bundle.putBoolean(FORCE_REFRESH, true);
        refresh(bundle);
    }

    /**
     * Refresh the fragment's list
     */
    public void refresh() {
        refresh(null);
    }

    private void refresh(final Bundle args) {
        if (!isUsable()) {
            return;
        }

        getActionBarActivity().setSupportProgressBarIndeterminateVisibility(true);

        getLoaderManager().restartLoader(0, args, this);
    }

    private ActionBarActivity getActionBarActivity() {
        return ((ActionBarActivity) getActivity());
    }

    /**
     * Get error message to display for exception
     *
     * @param exception
     * @return string resource id
     */
    protected abstract int getErrorMessage(final Exception exception);

    public void onLoadFinished(final Loader<List<E>> loader, final List<E> items) {

        getActionBarActivity().setSupportProgressBarIndeterminateVisibility(false);

        final Exception exception = getException(loader);
        if (exception != null) {
            showError(getErrorMessage(exception));
            showList();
            return;
        }

        this.items = items;
        getListAdapter().getWrappedAdapter().setItems(items.toArray());
        showList();
    }

    /**
     * Create adapter to display items
     *
     * @return adapter
     */
    protected PagerAdapter createAdapter() {
        final PagerAdapter wrapped = createAdapter(items);
        return wrapped;
    }

    /**
     * Create adapter to display items
     *
     * @param items
     * @return adapter
     */
    protected abstract PagerAdapter createAdapter(final List<E> items);

    /**
     * Set the list to be shown
     */
    protected void showList() {
        setListShown(true, isResumed());
    }

    @Override
    public void onLoaderReset(final Loader<List<E>> loader) {
        // Intentionally left blank
    }

    /**
     * Show exception in a Toast
     *
     * @param message
     */
    protected void showError(final int message) {
        Toaster.showLong(getActivity(), message);
    }

    /**
     * Get exception from loader if it provides one by being a
     * {@link ThrowableLoader}
     *
     * @param loader
     * @return exception or null if none provided
     */
    protected Exception getException(final Loader<List<E>> loader) {
        if (loader instanceof ThrowableLoader) {
            return ((ThrowableLoader<List<E>>) loader).clearException();
        } else {
            return null;
        }
    }

    /**
     * Refresh the list with the progress bar showing
     */
    protected void refreshWithProgress() {
        items.clear();
        setListShown(false);
        refresh();
    }

    /**
     * Get {@link ListView}
     *
     * @return viewPager
     */
    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * Get list adapter
     *
     * @return list adapter
     */
    @SuppressWarnings("unchecked")
    protected PagerAdapter getListAdapter() {
        if (viewPager != null) {
            return viewPager
                    .getAdapter();
        }
        return null;
    }

    /**
     * Set list adapter to use on list view
     *
     * @param adapter
     * @return this fragment
     */
    protected ItemPagerFragment<E> setListAdapter(final PagerAdapter adapter) {
        if (viewPager != null) {
            viewPager.setAdapter(adapter);
        }
        return this;
    }

    private ItemPagerFragment<E> fadeIn(final View view, final boolean animate) {
        if (view != null) {
            if (animate) {
                view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                        android.R.anim.fade_in));
            } else {
                view.clearAnimation();
            }
        }
        return this;
    }

    private ItemPagerFragment<E> show(final View view) {
        ViewUtils.setGone(view, false);
        return this;
    }

    private ItemPagerFragment<E> hide(final View view) {
        ViewUtils.setGone(view, true);
        return this;
    }

    /**
     * Set list shown or progress bar show
     *
     * @param shown
     * @return this fragment
     */
    public ItemPagerFragment<E> setListShown(final boolean shown) {
        return setListShown(shown, true);
    }

    /**
     * Set list shown or progress bar show
     *
     * @param shown
     * @param animate
     * @return this fragment
     */
    public ItemPagerFragment<E> setListShown(final boolean shown, final boolean animate) {
        if (!isUsable()) {
            return this;
        }

        if (shown == listShown) {
            if (shown) {
                // List has already been shown so hide/show the empty view with
                // no fade effect
                if (items.isEmpty()) {
                    hide(viewPager).show(emptyView);
                } else {
                    hide(emptyView).show(viewPager);
                }
            }
            return this;
        }

        listShown = shown;

        if (shown) {
            if (!items.isEmpty()) {
                hide(progressBar).hide(emptyView).fadeIn(viewPager, animate)
                        .show(viewPager);
            } else {
                hide(progressBar).hide(viewPager).fadeIn(emptyView, animate)
                        .show(emptyView);
            }
        } else {
            hide(viewPager).hide(emptyView).fadeIn(progressBar, animate)
                    .show(progressBar);
        }
        return this;
    }

    /**
     * Set empty text on list fragment
     *
     * @param message
     * @return this fragment
     */
    protected ItemPagerFragment<E> setEmptyText(final String message) {
        if (emptyView != null) {
            emptyView.setText(message);
        }
        return this;
    }

    /**
     * Set empty text on list fragment
     *
     * @param resId
     * @return this fragment
     */
    protected ItemPagerFragment<E> setEmptyText(final int resId) {
        if (emptyView != null) {
            emptyView.setText(resId);
        }
        return this;
    }

    /**
     * Callback when a list view item is clicked
     *
     * @param l
     * @param v
     * @param position
     * @param id
     */
    public void onListItemClick(final ListView l, final View v,
                                final int position, final long id) {
    }

    /**
     * Is this fragment still part of an activity and usable from the UI-thread?
     *
     * @return true if usable on the UI-thread, false otherwise
     */
    protected boolean isUsable() {
        return getActivity() != null;
    }
}
