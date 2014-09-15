package com.donnfelker.android.bootstrap.ui;

import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

import com.donnfelker.android.bootstrap.core.Category;
import com.donnfelker.android.bootstrap.core.Navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tulga on 9/15/14.
 */
public class CategoryExpandableListAdapter extends SimpleExpandableListAdapter {

    public static CategoryExpandableListAdapter newExpandableListAdapter(Context context, List<Navigation> groups) {
        List<Map<String, ?>> groupData = new ArrayList<Map<String, ?>>();
        List<List<Map<String, String>>> listOfChildGroups = new ArrayList<List<Map<String, String>>>();

        for(Navigation nav: groups) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("title", nav.getText());
            m.put("thumbnail", nav.getImageId());
            groupData.add(m);
            List<Map<String, String>> listOfChild = new ArrayList<Map<String, String>>();
            for (final Category cat: nav.getCategories()) {
                listOfChild.add(new HashMap<String, String>() {{
                    put("title", cat.getTitle());
                }});
            }
            listOfChildGroups.add(listOfChild);
        }

        CategoryExpandableListAdapter listAdapter = new CategoryExpandableListAdapter(
                context,
                groupData,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { "title" },
                new int[] { android.R.id.text1 },

                listOfChildGroups,
                android.R.layout.simple_list_item_1,
                new String[] { "title" },
                new int[] { android.R.id.text1 }
        );

        return listAdapter;
    }

    public CategoryExpandableListAdapter(Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
    }
}
