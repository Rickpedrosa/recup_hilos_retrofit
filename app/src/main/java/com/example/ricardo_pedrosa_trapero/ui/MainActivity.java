package com.example.ricardo_pedrosa_trapero.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.ricardo_pedrosa_trapero.R;
import com.example.ricardo_pedrosa_trapero.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private MainActivityViewModel viewModel;
    public static final String CHANNEL_ID = "1";
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);
        b.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        initViews();
        getJoke();
        observeData();
    }

    private void initViews() {
        b.progressBar.setVisibility(View.INVISIBLE);
        notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
        String background = "http://pngimg.com/uploads/chuck_norris/chuck_norris_PNG27.png";
        Picasso.with(b.ourSaviour.getContext()).load(background).error(R.drawable.ic_launcher_background)
                .into(b.ourSaviour);
    }

    private void getJoke() {
        b.fabSearch.setOnClickListener(v -> viewModel.callChuckApi(
                b.categorySpinner.getSelectedItem().toString(),
                b.lblChuckJoke));
    }

    private void observeData() {
        viewModel.getChuckNorrisJokeMutableLiveData().observe(this,
                chuckNorrisJoke -> b.lblChuckJoke.setText(chuckNorrisJoke.getValue()));

        viewModel.getLoading().observe(this, aBoolean
                -> b.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE));

        viewModel.getNotificationEvent().observe(this, notificationEvent -> {
            if (!notificationEvent.hasBeenHandled()) {
                notificationManagerCompat.notify(1, notificationEvent.getContentIfNotHandled());
            }
        });
    }
}
