package ringerjk.com.themoviedb.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.List;

import ringerjk.com.themoviedb.R;


public class MyFragmentPageAdapter extends FragmentStatePagerAdapter{

    List<Fragment> viewList;

    public MyFragmentPageAdapter(FragmentManager fm, List<Fragment> viewList) {
        super(fm);
        this.viewList = viewList;
    }

    @Override
    public Fragment getItem(int position) {
        return viewList.get(position);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0){
            title = "Movie";
        } else if (position == 1){
            title = "Casts";
        } else {
            title = "New Position";
        }
        return title;
    }
}
