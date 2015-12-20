package ringerjk.com.todoisapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ringerjk.com.todoisapp.R;

public class CustomImageListViewAdapter extends ArrayAdapter<String>{
    final static String LOG_TAG = "myLogs";
    List<String> btmObjects;
    //LayoutInflater lInflater;

    public CustomImageListViewAdapter(Context context, List<String> objects) {
        super(context, R.layout.custom_list_pictures, objects);
        Log.i(LOG_TAG, "CustomImageListViewAdapter constructor");
        this.btmObjects = objects;
    }

    @Override
    public int getCount() {
        return btmObjects.size();
    }

    @Override
    public String getItem(int position) {
        return btmObjects.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(LOG_TAG, "CustomImageListViewAdapter getView, position = " + position);
        String bitmapPath = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_pictures, parent, false);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(bitmapPath));
        return convertView;
    }

    public void updateListView(ArrayList<String> bitmapArrayList){
        btmObjects = bitmapArrayList;
        notifyDataSetChanged();
    }
}
