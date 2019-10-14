package com.neopharma.datavault.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neopharma.datavault.R;
import com.neopharma.datavault.model.DrawerItem;

import java.util.ArrayList;

public class NavigationDrawerAdapter extends ArrayAdapter<DrawerItem> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<DrawerItem> drawerItems;

    public NavigationDrawerAdapter(Context mContext, int layoutResourceId, ArrayList<DrawerItem> drawerItems) {
        super(mContext, layoutResourceId, drawerItems);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.drawerItems = drawerItems;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView selectedIcon = listItem.findViewById(R.id.selected_icon);
        ImageView menuImage = listItem.findViewById(R.id.menu_image);
        TextView menuName = listItem.findViewById(R.id.menu_name);

        DrawerItem drawerItem = drawerItems.get(position);
        menuImage.setImageResource(drawerItem.icon);
        menuName.setText(drawerItem.name);
        selectedIcon.setVisibility(drawerItem.isSelected ? View.VISIBLE : View.INVISIBLE);

        return listItem;
    }
}