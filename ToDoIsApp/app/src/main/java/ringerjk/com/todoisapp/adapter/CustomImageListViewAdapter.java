package ringerjk.com.todoisapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import ringerjk.com.todoisapp.R;

public class CustomImageListViewAdapter extends ArrayAdapter<String> {
    private final static String LOG_TAG = "myLogs";
    private List<String> btmList;

    static class ViewHolder{
        ImageView image;
    }
    public CustomImageListViewAdapter(Context context, List<String> objects) {
        super(context, R.layout.custom_list_pictures, objects);
        Log.i(LOG_TAG, "CustomImageListViewAdapter constructor");
        this.btmList = objects;
    }

    @Override
    public int getCount() {
        return btmList.size();
    }

    @Override
    public String getItem(int position) {
        return btmList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_pictures, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.image.setImageURI(Uri.parse(getItem(position)));
        return convertView;

        /*Log.i(LOG_TAG, "CustomImageListViewAdapter getView, position = " + position);
        String bitmapPath = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_pictures, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(bitmapPath));
        return convertView;*/
    }

    public void updateListView(List<String> bitmapArrayList) {
        btmList = bitmapArrayList;
        notifyDataSetChanged();
    }
}
