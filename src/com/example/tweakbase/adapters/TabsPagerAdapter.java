package com.example.tweakbase.adapters;

import com.example.tweakbase.layout.ApplicationFragment;
import com.example.tweakbase.layout.RingermodeFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Application fragment activity
			return new ApplicationFragment();
		case 1:
			// Ringermode fragment activity
			return new RingermodeFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}