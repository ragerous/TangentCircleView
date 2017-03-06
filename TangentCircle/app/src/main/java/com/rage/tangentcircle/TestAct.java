package com.rage.tangentcircle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by nzq on 17/3/2.
 */

public class TestAct extends AppCompatActivity {
    TangentCircleView tangentCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test);
        tangentCircleView = (TangentCircleView) findViewById(R.id.tangentCircleView);

    }

    public void tangentcircleStart(View view) {
        tangentCircleView.startTangent();
    }

    public void tangentcircleStop(View view) {
        tangentCircleView.stopTangent();
    }
}
