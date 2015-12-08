package de.brennecke.musicarchivst.buttonlistener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.brennecke.musicarchivst.R;
import de.brennecke.musicarchivst.model.NavDrawerItem;

/**
 * Created by Alexander on 08.11.2015.
 */
public class NavigationDrawerListener extends ArrayAdapter implements AdapterView.OnItemClickListener {

    private List<NavDrawerItem> navDrawerItems;
    private Context context;
    private int resourceID;

    public NavigationDrawerListener(Context context, int textViewResourceId,
                                    List<NavDrawerItem> navDrawerItems) {
        super(context, textViewResourceId, navDrawerItems);
        this.navDrawerItems = navDrawerItems;
        this.context = context;
        resourceID = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(position == 0){
            v = initHeader(v);
        }
        else{
            v = createRow(position,v);
        }
        return v;
    }

    private View createRow(int position, View v){
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(resourceID, null);
        }

        TextView itemText = (TextView) v.findViewById(R.id.nav_drawer_row_text);
        itemText.setText(navDrawerItems.get(position).getText());

        ImageView image = (ImageView)v.findViewById(R.id.nav_drawer_row_image);
        Drawable drawable = context.getResources().getDrawable(navDrawerItems.get(position).getImageResource());
        image.setImageDrawable(drawable);
        return v;
    }

    private View initHeader(View v){
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(resourceID, null);
        }

        TextView itemText = (TextView) v.findViewById(R.id.nav_drawer_row_text);
        itemText.setText(navDrawerItems.get(0).getText());

        ImageView image = (ImageView)v.findViewById(R.id.nav_drawer_row_image);
        Drawable drawable = context.getResources().getDrawable(navDrawerItems.get(0).getImageResource());
        image.setImageDrawable(drawable);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
