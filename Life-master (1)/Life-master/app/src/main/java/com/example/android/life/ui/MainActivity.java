package com.example.android.life.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.text.AutoText;
import android.util.Log;

import com.example.android.life.R;
import com.example.android.life.adapters.LifeAdapter;
import com.example.android.life.databinding.ActivityMainBinding;
import com.example.android.life.viewmodels.MainActivityViewModel;
import com.example.android.life.viewmodels.MainActivityViewModel.MainActivityViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding mBinding;
    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setLifecycleOwner(this);

        MainActivityViewModelFactory factory = new MainActivityViewModelFactory(getApplication());
        mViewModel = ViewModelProviders.of(this,factory).get(MainActivityViewModel.class);
        mBinding.setViewmodel(mViewModel);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LifeAdapter adapter = new LifeAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 10);
        mBinding.recyclerView.setLayoutManager(gridLayoutManager);
        mBinding.recyclerView.addItemDecoration(new SpaceItemDecoration(8));
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setAdapter(adapter);
    }
}
