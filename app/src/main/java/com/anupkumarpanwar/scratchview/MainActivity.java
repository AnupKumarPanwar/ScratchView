package com.anupkumarpanwar.scratchview;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ScratchImageView scratchImageView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scratchImageView = findViewById(R.id.scratch_view);
        scratchImageView.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView scratchImageView) {
                Toast.makeText(getApplicationContext(), "Image reveled", Toast.LENGTH_LONG).show();;
            }

            @Override
            public void onRevealPercentChangedListener(ScratchImageView scratchImageView, float percent) {
                if (percent>=0.5) {
                    Toast.makeText(getApplicationContext(), "Image reveled", Toast.LENGTH_LONG).show();;
                    scratchImageView.reveal();
                }
            }
        });
    }
}
