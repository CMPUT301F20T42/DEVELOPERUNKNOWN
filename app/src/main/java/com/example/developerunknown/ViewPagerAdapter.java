package com.example.developerunknown;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
/**this is a viewPagerAdapter to arrange child fragments,mainly applied to view different book list
 * according to their specific features
*/
 public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /**
     * Gets a item of a specific position
     * @return
     * Return specific fragment list
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    /**
     * counts the item on the list
     * @return
     * Return size of array list
     */
    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }


    /**
     *
     * @param fragment fragments used to display views
     * @param title the fragment title
     */
    public void addFragment(Fragment fragment,String title){
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
    }


    /**
     * Gets the page title
     * @return
     * Return the position of the fragment title
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return fragmentTitle.get(position);

    }
}
