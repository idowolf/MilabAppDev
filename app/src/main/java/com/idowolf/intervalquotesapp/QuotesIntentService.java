package com.idowolf.intervalquotesapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class QuotesIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.idowolf.intervalquotesapp.action.FOO";

    private static final String EXTRA_PARAM1 = "com.idowolf.intervalquotesapp.extra.PARAM1";
    private Random random = new Random();

    public QuotesIntentService() {
        super("QuotesIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static Intent setData(Context context, HashMap<String, String> quotes) {
        Intent intent = new Intent(context, QuotesIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, quotes);
        //context.startService(intent);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final HashMap<String, String> param1 = (HashMap<String, String>)intent.getSerializableExtra(EXTRA_PARAM1);
                handleActionFoo(param1);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(HashMap<String, String> param1) {
        List<String> keys = new ArrayList<String>(param1.keySet());
        String randomKey = keys.get( random.nextInt(keys.size()) );
        String value = param1.get(randomKey);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(randomKey)
                .setContentText(value);
        NotificationManager notificationManager = (NotificationManager)this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        int id = 1;
        notificationManager.notify(id, builder.build());
    }
}
