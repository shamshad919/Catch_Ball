package com.example.shamshad.catchball;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.shamshad.catchball.R;


/**
 * Created by shamshad on 25/12/17.
 */

public class soundPlayer {
    private static SoundPool soundpool;
    private static int hitsound;
    private static int oversound;

    public soundPlayer(Context context) {

        soundpool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        hitsound = soundpool.load(context, R.raw.hit, 1);
        oversound = soundpool.load(context, R.raw.over, 1);

    }

    public void hitplaysound() {
        soundpool.play(hitsound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void overplaysound() {
        soundpool.play(oversound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
