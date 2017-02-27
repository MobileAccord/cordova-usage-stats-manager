package com.mobileaccord.geopoll.plugins;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.widget.Toast;
import android.content.Context;


/**
 * 
 */
public class MyUsageStatsManager extends CordovaPlugin {

    private static final String LOG_TAG = "MyUsageStatsManager";

    UsageStatsManager mUsageStatsManager;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        mUsageStatsManager = (UsageStatsManager) this.cordova.getActivity().getApplicationContext().getSystemService("usagestats"); //Context.USAGE_STATS_SERVICE
        if (action.equals("getUsageStatistics")) {
            String arg = args.getString(0);
            this.getUsageStatistics(arg, callbackContext);
            return true;
        }else if(action.equals("openPermissionSettings")){
            this.openPermissionSettings(callbackContext);
            return true;
        }
        return false;
    }

    private void getUsageStatistics(String interval, CallbackContext callbackContext) {
        try{
            Log.i(LOG_TAG, interval);
            StatsUsageInterval statsUsageInterval = StatsUsageInterval.getValue(interval);
            List<UsageStats> usageStatsList = new ArrayList<UsageStats>();
            if (statsUsageInterval != null) {
                usageStatsList = queryUsageStatistics(statsUsageInterval.mInterval);
                Collections.sort(usageStatsList, new LastTimeLaunchedComparatorDesc());
            }
            JSONArray jsonArray = new JSONArray();
            for (UsageStats stat : usageStatsList){
                JSONObject obj = toJSON(stat);
                jsonArray.put(obj);
            }

            String result = jsonArray.toString();
            Log.d(LOG_TAG, result);
            callbackContext.success(result);

        }catch (Exception e){
            e.printStackTrace();
            callbackContext.error(e.toString());
        }
    }


    /**
     * Returns the List of UsageStats including the time span specified by the
     * intervalType argument.
     *
     * @param intervalType The time interval by which the stats are aggregated.
     *                     Corresponding to the value of {@link UsageStatsManager}.
     *                     E.g. {@link UsageStatsManager#INTERVAL_DAILY}, {@link
     *                     UsageStatsManager#INTERVAL_WEEKLY},
     *
     * @return A list of {@link android.app.usage.UsageStats}.
     */
    public List<UsageStats> queryUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(intervalType, cal.getTimeInMillis(), System.currentTimeMillis());
        return queryUsageStats;
    }

    /**
     * The {@link Comparator} to sort a collection of {@link UsageStats} sorted by the timestamp
     * last time the app was used in the descendant order.
     */
    private static class LastTimeLaunchedComparatorDesc implements Comparator<UsageStats> {
        @Override
        public int compare(UsageStats left, UsageStats right) {
            return Long.compare(right.getLastTimeUsed(), left.getLastTimeUsed());
        }
    }

    /**
     * Enum represents the intervals for {@link android.app.usage.UsageStatsManager} so that
     * values for intervals can be found by a String representation.
     *
     */
    //VisibleForTesting
    static enum StatsUsageInterval {
        DAILY("Daily", UsageStatsManager.INTERVAL_DAILY),
        WEEKLY("Weekly", UsageStatsManager.INTERVAL_WEEKLY),
        MONTHLY("Monthly", UsageStatsManager.INTERVAL_MONTHLY),
        YEARLY("Yearly", UsageStatsManager.INTERVAL_YEARLY);

        private int mInterval;
        private String mStringRepresentation;

        StatsUsageInterval(String stringRepresentation, int interval) {
            mStringRepresentation = stringRepresentation;
            mInterval = interval;
        }

        static StatsUsageInterval getValue(String stringRepresentation) {
            for (StatsUsageInterval statsUsageInterval : values()) {
                if (statsUsageInterval.mStringRepresentation.equals(stringRepresentation)) {
                    return statsUsageInterval;
                }
            }
            return null;
        }
    }

    /**
     * Converts UsageStats into a JSONObject
     * @param usageStats
     * @return
     * @throws Exception
     */
    public static JSONObject toJSON(UsageStats usageStats) throws Exception{
        JSONObject object= new JSONObject();
        object.put("PackageName", usageStats.getPackageName());
        object.put("FirstTimeStamp", usageStats.getFirstTimeStamp());
        object.put("LastTimeStamp", usageStats.getLastTimeStamp());
        object.put("LastTimeUsed", usageStats.getLastTimeUsed());
        object.put("TotalTimeInForeground", usageStats.getTotalTimeInForeground());
        return object;
    }

    /**
     * Launch UsageStatsManager settings
     * @return
     */
    private void openPermissionSettings(CallbackContext callbackContext){
        try {

            Context context = this.cordova.getActivity().getApplicationContext(); 
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            callbackContext.success("OK");

        } catch(Exception e){
            e.printStackTrace();
            callbackContext.error(e.toString());
        }
        
    }

}
