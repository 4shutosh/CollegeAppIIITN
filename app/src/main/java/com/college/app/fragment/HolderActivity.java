package com.college.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.college.app.R;
import com.college.app.databinding.ActivityHolderBinding;

public class HolderActivity extends AppCompatActivity {

    public static String FRAGMENT_DATA = "transaction_data";
    public static String FRAGMENT_CLASS = "transation_target";
    public static String FRAGMENT_PROVIDER = "transation_provider";
    public ActivityHolderBinding activityHolderBinding;

    private void openFragment(Class<? extends Fragment> fragment, String provider, String[] data) {

        try {
            Fragment frag = fragment.newInstance();

            // adding the data for the fragment , not required for all cases
            Bundle bundle = new Bundle();
            bundle.putStringArray(FRAGMENT_DATA, data);
            bundle.putString(FRAGMENT_PROVIDER, provider);
            frag.setArguments(bundle);

            //Changing the fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentHolder, frag)
                    .commit();

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void startActivity(Context mContext, Class<? extends Fragment> fragment, String provider, String[] data) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(FRAGMENT_DATA, data);
        bundle.putSerializable(FRAGMENT_CLASS, fragment);
        bundle.putString(FRAGMENT_PROVIDER, provider);

        Intent intent = new Intent(mContext, HolderActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHolderBinding = ActivityHolderBinding.inflate(getLayoutInflater());
        setContentView(activityHolderBinding.getRoot());
        setSupportActionBar(activityHolderBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Class<? extends Fragment> fragmentClass = (Class<? extends Fragment>) getIntent().getExtras().getSerializable(FRAGMENT_CLASS);
        String[] args = getIntent().getExtras().getStringArray(FRAGMENT_DATA);
        String provider = getIntent().getExtras().getString(FRAGMENT_PROVIDER, "");

        if (fragmentClass != null) {
            openFragment(fragmentClass, provider, args);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}