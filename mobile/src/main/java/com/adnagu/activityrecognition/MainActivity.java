package com.adnagu.activityrecognition;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adnagu.common.database.AppDatabase;
import com.adnagu.common.ml.ArffFile;
import com.adnagu.common.utils.DatabaseUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.message)
    TextView mTextMessage;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.progress_message)
    TextView progressMessage;

    @OnClick(R.id.button_save_as)
    public void saveAsArff() {
        Handler handler = new Handler();
        new Thread(() -> {
            ArffFile arffFile = new ArffFile(this, progress -> handler.post(() -> setProgressBar(progress)));
            arffFile.save(4, 50);
            setProgressMessage("Completed.");
        }).start();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DatabaseUtils.prepareDatabase(this);

        setProgressMessage(String.valueOf(
                AppDatabase.getInstance(this).sensorRecordDao().getCount()
        ));

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void setProgressBar(int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(progress, true);
        } else {
            progressBar.setProgress(progress);
        }
    }

    public void setProgressMessage(CharSequence message) {
        progressMessage.setText(message);
    }
}
