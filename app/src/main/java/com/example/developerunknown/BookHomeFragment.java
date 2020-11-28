package com.example.developerunknown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
// a Fragment that contains some child fragment by applying ViewPagerAdapter, customer can view specific book information
// for borrower and owner,there are different fragments to view
public class BookHomeFragment extends Fragment {
    public TabLayout tabLayout;
    public ViewPager viewPager;
    User currentUser;

    @javax.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.book_home, container, false);
        currentUser = (User) this.getArguments().getSerializable("current user");

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle args = new Bundle();
        args.putSerializable("current user", currentUser);
        Fragment bookListFragment = new BookListFragment();
        Fragment requestListFragment = new RequestListFragment();
        Fragment acceptedListFragment = new AcceptedListFragment();
        Fragment borrowedListFragment = new BorrowedListFragment();
        Fragment requestedHistoryFragment = new RequestHistoryList();
        bookListFragment.setArguments(args);
        requestListFragment.setArguments(args);
        acceptedListFragment.setArguments(args);
        borrowedListFragment.setArguments(args);
        requestedHistoryFragment.setArguments(args);

        adapter.addFragment(bookListFragment,"My Books");
        adapter.addFragment(requestListFragment,"Wish List");
        adapter.addFragment(acceptedListFragment,"Accepted Books");
        adapter.addFragment(borrowedListFragment,"My Borrowing");
        adapter.addFragment(requestedHistoryFragment,"Request History");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }

}
