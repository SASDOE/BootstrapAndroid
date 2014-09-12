package com.donnfelker.android.bootstrap.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.donnfelker.android.bootstrap.BootstrapApplication;
import com.donnfelker.android.bootstrap.R;
import com.donnfelker.android.bootstrap.core.Navigation;
import com.donnfelker.android.bootstrap.core.User;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by tulga on 9/10/14.
 */
public class NavListAdapter extends SingleTypeAdapter<Navigation> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM dd");

    /**
     * @param inflater
     * @param items
     */
    public NavListAdapter(final LayoutInflater inflater, final List<Navigation> items) {
        super(inflater, R.layout.nav_list_item);

        setItems(items);
    }

    /**
     * @param inflater
     */
    public NavListAdapter(final LayoutInflater inflater) {
        this(inflater, null);

    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
//        return !TextUtils.isEmpty(id) ? id.hashCode() : super
//                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[]{R.id.icon, R.id.text};
    }

    @Override
    protected void update(final int position, final Navigation nav) {

        Picasso.with(BootstrapApplication.getInstance())
                .load(nav.getImageId())
//                .placeholder(R.drawable.gravatar_icon)
                .into(imageView(0));

        setText(1, nav.getText());

    }
}
