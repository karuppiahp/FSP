package activities.mswift.info.walaapp.wala.support;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import activities.mswift.info.walaapp.wala.asyntasks.UpdateGoalResultTask;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 4/28/2016.
 */
public class GoalsGenerateService extends Service {
    @Override
    public void onStart(Intent intent, int startId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_email", "" + SessionStores.getUserEmail(getApplicationContext()));
        new UpdateGoalResultTask(getApplicationContext(), params);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
