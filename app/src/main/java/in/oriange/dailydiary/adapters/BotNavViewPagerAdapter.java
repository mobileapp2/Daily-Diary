package in.oriange.dailydiary.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.oriange.dailydiary.fragments.Fragment1_Fragment;
import in.oriange.dailydiary.fragments.Fragment2_Fragment;
import in.oriange.dailydiary.fragments.Fragment3_Fragment;
import in.oriange.dailydiary.fragments.Fragment4_Fragment;

public class BotNavViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private Fragment currentFragment;

    public BotNavViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.clear();
        fragments.add(new Fragment1_Fragment());
        fragments.add(new Fragment2_Fragment());
        fragments.add(new Fragment3_Fragment());
        fragments.add(new Fragment4_Fragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}