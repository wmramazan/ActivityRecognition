package com.adnagu.activityrecognition;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adnagu.common.ml.ArffFile;
import com.adnagu.common.utils.DatabaseUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ArffActivity
 *
 * @author ramazan.vapurcu
 * Created on 5/28/2019
 */
public class ArffActivity extends AppCompatActivity {

    @BindView(R.id.window_length)
    EditText windowLength;

    @BindView(R.id.overlapping)
    EditText overlapping;

    @BindView(R.id.limit)
    EditText limit;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.progress_message)
    TextView progressMessage;

    boolean progressing;

    @OnClick(R.id.button_save_as)
    public void saveAsArff() {
        if (!isProgressing()) {
            setProgressMessage(getString(R.string.saving));
            progressing = true;

            Handler handler = new Handler();
            new Thread(() -> {
                ArffFile arffFile = new ArffFile(this, progress -> handler.post(() -> setProgressBar(progress)));
                arffFile.save(
                        Integer.valueOf(windowLength.getText().toString()),
                        Integer.valueOf(overlapping.getText().toString()),
                        Integer.valueOf(limit.getText().toString())
                );
                setProgressMessage("Completed.");
                progressing = false;
            }).start();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arff);
        ButterKnife.bind(this);

        DatabaseUtils.prepareDatabase(this);
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

    public boolean isProgressing() {
        return progressing;
    }
}
