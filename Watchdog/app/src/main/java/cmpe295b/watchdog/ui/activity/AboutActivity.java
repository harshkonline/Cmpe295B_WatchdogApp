package cmpe295b.watchdog.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import cmpe295b.watchdog.R;
import cmpe295b.watchdog.core.WatchdogApplication;

public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Get tracker.
        Tracker t = ((WatchdogApplication) getApplication()).getTracker(WatchdogApplication.TrackerName.APP_TRACKER);

        // Set screen name.
        t.setScreenName("AboutActivity");
        t.send(new HitBuilders.ScreenViewBuilder().build());

    }
}
