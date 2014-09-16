package com.donnfelker.android.bootstrap.ui;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donnfelker.android.bootstrap.Injector;
import com.donnfelker.android.bootstrap.R;
import com.donnfelker.android.bootstrap.core.Category;
import com.donnfelker.android.bootstrap.core.Navigation;
import com.donnfelker.android.bootstrap.events.NavItemSelectedEvent;
import com.donnfelker.android.bootstrap.util.UIUtils;
import com.squareup.otto.Bus;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle drawerToggle;

    private DrawerLayout drawerLayout;
    private ExpandableListView drawerListView;
    private LinearLayout drawerLinearLayout;
    private View fragmentContainerView;

    private List<Navigation> navigationList = null;

    private int currentSelectedPosition = 0;
    private boolean fromSavedInstanceState;
    private boolean userLearnedDrawer;

    @Inject protected SharedPreferences prefs;
    @Inject protected Bus bus;


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        userLearnedDrawer = prefs.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            currentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            fromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(currentSelectedPosition);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        drawerLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        drawerListView = (ExpandableListView) drawerLinearLayout.findViewById(R.id.drawer_listview);
        navigationList = Arrays.asList(
                new Navigation(R.drawable.icon_events, "Events", Arrays.asList(
                        new Category("category 11"),
                        new Category("category 12"),
                        new Category("category 13")
                        )),
                new Navigation(R.drawable.icon_news, "News", Arrays.asList(
                        new Category("category 21"),
                        new Category("category 22"),
                        new Category("category 23")
                )),
                new Navigation(R.drawable.icon_tv, "TV Guide"),
                new Navigation(R.drawable.icon_video, "Video", Arrays.asList(
                        new Category("category 41"),
                        new Category("category 42"),
                        new Category("category 43")
                )));
        drawerListView.setAdapter(CategoryExpandableListAdapter.newExpandableListAdapter(getActivity(), navigationList));
        drawerListView.setOnChildClickListener(this);
        drawerListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    drawerListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
        return drawerLinearLayout;
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(fragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        fragmentContainerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        //drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        drawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                NavigationDrawerFragment.this.drawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                if (navigationList != null)
                    getActionBar().setTitle(navigationList.get(currentSelectedPosition).getText());
                refreshActionBarTitle();
                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!userLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    userLearnedDrawer = true;
                    prefs.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!userLearnedDrawer && !fromSavedInstanceState) {
            this.drawerLayout.openDrawer(fragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        this.drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

        this.drawerLayout.setDrawerListener(drawerToggle);
    }

    private void selectItem(int position) {
        currentSelectedPosition = position;
        if (drawerListView != null) {
            drawerListView.setItemChecked(position, true);
        }
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(fragmentContainerView);
        }
        // Fire the event off to the bus which.
        bus.post(new NavItemSelectedEvent(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, currentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(!isTablet()) {
            // Forward the new configuration the drawer toggle component.
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    private boolean isTablet() {
        if(getActivity() != null) {
            return UIUtils.isTablet(getActivity());
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (drawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isTablet() && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(R.string.app_name);
        refreshActionBarTitle();
    }

    public void refreshActionBarTitle() {
        View view = null;
        if (getActionBar() != null && (view = getActionBar().getCustomView()) != null) {
            CharSequence testRemoveMe = getActionBar().getTitle();
            TextView actionBarTitle = (TextView) view.findViewById(R.id.actionbar_title);
            actionBarTitle.setText(getActionBar().getTitle());
        }
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }


    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
        selectItem(groupPosition);
        return true;
    }
}
