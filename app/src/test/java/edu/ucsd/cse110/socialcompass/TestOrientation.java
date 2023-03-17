//package edu.ucsd.cse110.socialcompass;
//
//import static junit.framework.TestCase.assertEquals;
//
//import android.os.Build;
//import android.widget.TextView;
//
//import androidx.lifecycle.MutableLiveData;
//import androidx.test.core.app.ActivityScenario;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;
//
//import edu.ucsd.cse110.socialcompass.activity.MainActivity;
//import edu.ucsd.cse110.socialcompass.services.OrientationService;
//
//@RunWith(RobolectricTestRunner.class)
//@Config(sdk = Build.VERSION_CODES.P)
//public class TestOrientation {
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
//}
