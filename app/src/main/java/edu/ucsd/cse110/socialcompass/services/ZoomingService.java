package edu.ucsd.cse110.socialcompass.services;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import edu.ucsd.cse110.socialcompass.R;
import edu.ucsd.cse110.socialcompass.activity.MainActivity;

public class ZoomingService implements Animation.AnimationListener {
    private final Animation zoomInFirstFrom100Animation;
    private final Animation zoomInSecondFrom133Animation;
    private final Animation zoomInSecondFrom200Animation;
    private final Animation zoomInThirdFrom267Animation;
    private final Animation zoomInThirdBackAnimation;
    private final Animation zoomOutFirstFrom1000Animation;
    private final Animation zoomOutSecondFrom1000Animation;
    private final Animation zoomOutSecondFrom200Animation;
    private final Animation zoomOutThirdFrom1000Animation;
    private final Animation zoomOutThirdFrom300Animation;

    public ZoomingService(AppCompatActivity activity) {
        // Set up animations
        zoomInFirstFrom100Animation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.zoom_in_first_circle_from_100);
        zoomInSecondFrom133Animation = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.zoom_in_second_circle_from_133);
        zoomInSecondFrom200Animation = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.zoom_in_second_circle_from_200);
        zoomInThirdFrom267Animation = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.zoom_in_third_circle_from_267);
        zoomInThirdBackAnimation = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.zoom_in_third_circle_back);
        zoomOutFirstFrom1000Animation = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.zoom_out_first_circle_from_1000);
        zoomOutSecondFrom1000Animation = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.zoom_out_second_circle_from_1000);
        zoomOutSecondFrom200Animation = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.zoom_out_second_circle_from_200);
        zoomOutThirdFrom1000Animation = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.zoom_out_third_circle_from_1000);
        zoomOutThirdFrom300Animation = AnimationUtils.loadAnimation(activity.getApplicationContext(),R.anim.zoom_out_third_circle_from_300);
        zoomInFirstFrom100Animation.setAnimationListener(this);
    }

    public void reflectZoomingSetting(MainActivity activity, TextView zoomIn, TextView firstCircle, TextView secondCircle, TextView thirdCircle, int scaleOfCirclesSaved) {
        if(scaleOfCirclesSaved > 100) {
            firstCircle.startAnimation(zoomInFirstFrom100Animation);
            secondCircle.startAnimation(zoomOutSecondFrom200Animation);
            thirdCircle.startAnimation(zoomOutThirdFrom300Animation);
            activity.setRange(500);
            activity.setScaleOfCircles(200);
        }
        if(scaleOfCirclesSaved > 200) {
            secondCircle.startAnimation(zoomInSecondFrom133Animation);
            thirdCircle.startAnimation(zoomInThirdFrom267Animation);
            activity.setRange(10);
            activity.setScaleOfCircles(300);
        }
        if(scaleOfCirclesSaved > 300) {
            secondCircle.startAnimation(zoomInSecondFrom200Animation);
            activity.setRange(1);
            activity.setScaleOfCircles(400);
        }
    }

    public void zoomIn(MainActivity activity, TextView zoomIn, TextView firstCircle, TextView secondCircle, TextView thirdCircle) {
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity.getScaleOfCircles() == 100) {
                    firstCircle.startAnimation(zoomInFirstFrom100Animation);
                    secondCircle.startAnimation(zoomOutSecondFrom200Animation);
                    thirdCircle.startAnimation(zoomOutThirdFrom300Animation);
                    activity.setRange(500);
                    activity.setScaleOfCircles(200);
                } else if(activity.getScaleOfCircles() == 200) {
                    secondCircle.startAnimation(zoomInSecondFrom133Animation);
                    thirdCircle.startAnimation(zoomInThirdFrom267Animation);
                    activity.setRange(10);
                    activity.setScaleOfCircles(300);
                } else if(activity.getScaleOfCircles() == 300) {
                    secondCircle.startAnimation(zoomInSecondFrom200Animation);
                    activity.setRange(1);
                    activity.setScaleOfCircles(400);
                }
            }
        });
    }

    public void zoomOut(MainActivity activity, TextView zoomOut, TextView firstCircle, TextView secondCircle, TextView thirdCircle) {
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity.getScaleOfCircles() == 400) {
                    secondCircle.startAnimation(zoomOutSecondFrom1000Animation);
                    activity.setRange(10);
                    activity.setScaleOfCircles(300);
                } else if(activity.getScaleOfCircles() == 300) {
                    secondCircle.startAnimation(zoomOutSecondFrom200Animation);
                    thirdCircle.startAnimation(zoomOutThirdFrom1000Animation);
                    activity.setRange(500);
                    activity.setScaleOfCircles(200);
                } else if(activity.getScaleOfCircles() == 200) {
                    firstCircle.startAnimation(zoomOutFirstFrom1000Animation);
                    secondCircle.startAnimation(zoomInSecondFrom133Animation);
                    thirdCircle.startAnimation(zoomInThirdBackAnimation);
                    activity.setRange(1000);
                    activity.setScaleOfCircles(100);
                }
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
