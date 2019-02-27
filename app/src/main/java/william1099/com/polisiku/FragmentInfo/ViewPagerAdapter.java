package william1099.com.polisiku.FragmentInfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> listFragment;
    List<String> listTitle;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        listFragment = new ArrayList<>();
        listTitle = new ArrayList<>();
    }
    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listTitle.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }

    public void addFragment(Fragment frag, String title) {
        this.listFragment.add(frag);
        this.listTitle.add(title);
    }
}
