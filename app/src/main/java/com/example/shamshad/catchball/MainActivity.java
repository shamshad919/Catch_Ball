package com.example.shamshad.catchball;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {
    private TextView startLabel;
    private TextView scoreLabel;
    private ImageView black;
    private ImageView orange;
    private ImageView pink;
    private ImageView box;

    private int screenwidth;
    private int screenheight;


    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private soundPlayer sound;


    private boolean action = false;
    private boolean start_flg = false;

    private int score = 0;

    private int frameHeight;
    private int boxSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new soundPlayer(this);

        startLabel = (TextView) findViewById(R.id.startLabel);
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        black = (ImageView) findViewById(R.id.black);
        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);

        //getScreen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenwidth = size.x;
        screenheight = size.y;


        //Move to out of screen
        orange.setX(-80);
        orange.setY(-80);
        black.setX(-80);
        black.setY(-80);
        pink.setX(-80);
        pink.setY(-80);

        scoreLabel.setText("SCORE : 0");

    }

    public void changePos() {

        hitcheck();

        //orange
        orangeX -= 12;
        if (orangeX < 0) {
            orangeX = screenwidth + 20;
            orangeY = (int) Math.floor(Math.random() * frameHeight - orange.getHeight());
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //black
        blackX -= 16;
        if (blackX < 0) {
            blackX = screenwidth + 10;
            blackY = (int) Math.floor(Math.random() * frameHeight - black.getHeight());
        }
        black.setX(blackX);
        black.setY(blackY);

        //pink
        pinkX -= 20;
        if (pinkX < 0) {
            pinkX = screenwidth + 5000;
            pinkY = (int) Math.floor(Math.random() * frameHeight - pink.getHeight());
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        //black box
        if (action == true) {
            boxY -= 20;
        } else {
            boxY += 20;
        }
        if (boxY < 0) {
            boxY = 0;
        }
        if (boxY > (frameHeight - boxSize)) {
            boxY = frameHeight - boxSize;
        }
        box.setY(boxY);

        scoreLabel.setText("SCORE : " + score);
    }

    public void hitcheck() {

        // If the center of the ball is in the box, it counts as a hit.

        // Orange
        int orangeCenterX = orangeX + orange.getWidth() / 2;
        int orangeCenterY = orangeY + orange.getHeight() / 2;

        // 0 <= orangeCenterX <= boxWidth
        // boxY <= orangeCenterY <= boxY + boxHeight

        if (0 <= orangeCenterX && orangeCenterX <= boxSize &&
                boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize) {

            score += 10;
            orangeX = -10;
            sound.hitplaysound();


        }

        // Pink
        int pinkCenterX = pinkX + pink.getWidth() / 2;
        int pinkCenterY = pinkY + pink.getHeight() / 2;

        if (0 <= pinkCenterX && pinkCenterX <= boxSize &&
                boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize) {

            score += 30;
            pinkX = -10;
            sound.hitplaysound();

        }
        //black
        int blackCentreX = blackX + black.getWidth() / 2;
        int blackCentreY = blackY + black.getHeight() / 2;

        if (0 <= blackCentreX && blackCentreX <= boxSize &&
                boxY <= blackCentreY && blackCentreY <= boxY + boxSize) {

            timer.cancel();
            timer = null;
            sound.overplaysound();

            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);


        }
    }

    public boolean onTouchEvent(MotionEvent me) {


        if (start_flg == false) {
            start_flg = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int) box.getY();

            boxSize = box.getHeight();


            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        } else {

            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action = false;
            }

        }

        return true;

    }

}
