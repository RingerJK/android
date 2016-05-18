package ringerjk.com.themoviedb.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ringerjk.com.themoviedb.MyConst;
import ringerjk.com.themoviedb.R;
import ringerjk.com.themoviedb.entity.CastInList;

public class CustomCastsListAdapter extends ArrayAdapter<CastInList> {

    List<CastInList> castsLists;

    static class ViewHolder{
        ImageView image;
        TextView actorName;
        TextView characterName;
    }

    public CustomCastsListAdapter(Context context, List<CastInList> objects) {
        super(context, R.layout.custom_cast_list_item, objects);
        castsLists = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_cast_list_item, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView)convertView.findViewById(R.id.imageCast);
            holder.actorName = (TextView) convertView.findViewById(R.id.actorName);
            holder.characterName = (TextView) convertView.findViewById(R.id.characterName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (castsLists.get(position).getProfilePath() == null){
            holder.image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.no_avatar));
        } else {
            Picasso.with(getContext()).load(MyConst.urlDefaultImage + castsLists.get(position).getProfilePath()).into(holder.image);
        }
        holder.actorName.setText(castsLists.get(position).getName());
        holder.characterName.setText(castsLists.get(position).getCharacter());
        Log.i(MyConst.MY_LOG, "Pic = "+castsLists.get(position).getProfilePath());
        convertView.setId(castsLists.get(position).getId());
        return convertView;
    }
}
