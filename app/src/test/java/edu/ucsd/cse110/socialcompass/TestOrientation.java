package edu.ucsd.cse110.socialcompass;

import static junit.framework.TestCase.assertEquals;

import android.os.Build;
import android.widget.TextView;
import androidx.lifecycle.MutableLiveData;
import androidx.test.core.app.ActivityScenario;
import androidx.test.internal.platform.content.PermissionGranter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class TestOrientation {
//    @Test
//    public void testMockSourceInput() {
//        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
//
//        scenario.onActivity(activity -> {
//            OrientationService service = OrientationService.singleton(activity);
//            MutableLiveData<Float> source = new MutableLiveData<>();
//            service.setMockOrientationSource(source);
//            source.setValue((float)90);
//            assertEquals(service.getOrientation().getValue(), (float)90);
//        });
//    }

    @Test
    public void testDirectionSignsRotated180Degrees() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            OrientationService service = OrientationService.singleton(activity);
            MutableLiveData<Float> source = new MutableLiveData<>();
            service.setMockOrientationSource(source);

            TextView northView = activity.findViewById(R.id.north);
            TextView southView = activity.findViewById(R.id.south);
            TextView eastView = activity.findViewById(R.id.east);
            TextView westView = activity.findViewById(R.id.west);

            int[] locationOfNorth = new int[2];
            northView.getLocationOnScreen(locationOfNorth);
            int[] locationOfSouth = new int[2];
            southView.getLocationOnScreen(locationOfSouth);
            int[] locationOfEast = new int[2];
            eastView.getLocationOnScreen(locationOfEast);
            int[] locationOfWest = new int[2];
            westView.getLocationOnScreen(locationOfWest);

            source.setValue((float)180);

            int[] locationOfNorthAfter = new int[2];
            northView.getLocationOnScreen(locationOfNorthAfter);
            int[] locationOfSouthAfter = new int[2];
            southView.getLocationOnScreen(locationOfSouthAfter);
            int[] locationOfEastAfter = new int[2];
            eastView.getLocationOnScreen(locationOfEastAfter);
            int[] locationOfWestAfter = new int[2];
            westView.getLocationOnScreen(locationOfWestAfter);

            //Test if each sign is still on the same axis
            assertEquals(locationOfNorth[0], locationOfNorthAfter[0]);
            assertEquals(locationOfSouth[0], locationOfSouthAfter[0]);
            assertEquals(locationOfEast[1], locationOfEastAfter[1]);
            assertEquals(locationOfWest[1], locationOfWestAfter[1]);
        });
    }

    /*
    @Test
    public void testDirectionSignsRotated45Degrees() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            OrientationService service = OrientationService.singleton(activity);
            MutableLiveData<Float> source = new MutableLiveData<>();
            service.setMockOrientationSource(source);
            TextView northView = activity.findViewById(R.id.north);
            TextView eastView = activity.findViewById(R.id.east);
            TextView southView = activity.findViewById(R.id.south);
            TextView westView = activity.findViewById(R.id.west);
            ConstraintLayout.LayoutParams northLayoutParams = (ConstraintLayout.LayoutParams) northView.getLayoutParams();
            //N = 315, S = 135, E = 45, W = 225
            assertEquals(northLayoutParams, 315);
        });
    }
    */
}
