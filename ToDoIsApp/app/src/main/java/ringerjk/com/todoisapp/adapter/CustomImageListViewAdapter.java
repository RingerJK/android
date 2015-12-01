package ringerjk.com.todoisapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import ringerjk.com.todoisapp.R;

public class CustomImageListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<Bitmap> btmFilesArray;
    LayoutInflater lInflater;

    public CustomImageListViewAdapter(Context context, ArrayList<Bitmap> btmFile) {
        this.context = context;
        this.btmFilesArray = btmFile;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return btmFilesArray.size();
    }

    @Override
    public Object getItem(int position) {
        return btmFilesArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = lInflater.inflate(R.layout.custom_list_pictures, parent, false);
        }
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageBitmap(btmFilesArray.get(position));
        return view;
    }

    public void updateListView(ArrayList<Bitmap> bitmapArrayList){
        btmFilesArray = bitmapArrayList;
        notifyDataSetChanged();
    }
}
