package com.anupkumarpanwar.scratchview;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ScratchView scratchView;
    boolean revealed = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scratchView = findViewById(R.id.scratch_view);
        scratchView.setRevealListener(new ScratchView.IRevealListener() {
            @Override
            public void onRevealed(ScratchView scratchView) {
                Toast.makeText(getApplicationContext(), "Image reveled", Toast.LENGTH_LONG).show();;
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView scratchView, float percent) {
                if (percent>=0.5 && ! revealed) {
                    Toast.makeText(getApplicationContext(), "Revealed", Toast.LENGTH_LONG).show();;
                    revealed = true;
                }
            }
        });
    }
}
