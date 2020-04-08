package com.speech.voice_text;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Adapter extends FragmentPagerAdapter {

    int countTab;
    public Adapter(@NonNull FragmentManager fm,int countTab) {
        super(fm);
        this.countTab=countTab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new voice_to_text();
            case 1:return new text_to_voice();
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
