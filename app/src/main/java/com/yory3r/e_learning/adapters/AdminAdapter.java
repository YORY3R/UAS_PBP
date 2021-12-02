package com.yory3r.e_learning.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.yory3r.e_learning.fragments.CourseAdminFragment;
import com.yory3r.e_learning.fragments.QuizAdminFragment;

public class AdminAdapter extends FragmentPagerAdapter
{
    private static int item = 2;

    public AdminAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        if(position == 0)
        {
            return CourseAdminFragment.newInstance(0,"Page 1");
        }
        else
        {
            return QuizAdminFragment.newInstance(1,"Page 2");
        }
    }

    @Override
    public int getCount()
    {
        return item;
    }

    public CharSequence getPageTitle(int position)
    {
        if(position == 0)
        {
            return "Course";
        }
        else
        {
            return "Quiz";
        }
    }
}

