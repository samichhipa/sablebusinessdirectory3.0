package com.macinternetservices.sablebusinessdirectory.utils;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.HashMap;
import java.util.List;

public class GeofenceReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_ID = 1;
    public static int near = 0;


    @Override
    public void onReceive(Context context, Intent intent) {
        near=0;
        GeofencingEvent geoEvent = GeofencingEvent.fromIntent(intent);
        if (geoEvent.hasError()) {
            Log.e("GeoEventErro", "There was a geo even error");
        } else {

            int transitionType = geoEvent.getGeofenceTransition();
            //near = 0;
            if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER
                    || transitionType == Geofence.GEOFENCE_TRANSITION_DWELL
                    || transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                List<Geofence> triggerList = geoEvent.getTriggeringGeofences();

                SimpleGeofence sg = null;

                for (SimpleGeofence simpleGeofenceHashMap:GeolocationService.geofences.values()){
                    GeofenceNotification geofenceNotification = new GeofenceNotification(
                            context);
                    geofenceNotification
                            .displayNotification(simpleGeofenceHashMap, transitionType, near);
                }
              /*  for (Geofence geofence : triggerList) {
                    sg = SimpleGeofenceStore.getInstance().geofences.get(geofence.getRequestId());
                    String transitionName = "";
                    switch (transitionType) {
                        case Geofence.GEOFENCE_TRANSITION_DWELL:
                            transitionName = "dwell";
                            //near++;
                            break;

                        case Geofence.GEOFENCE_TRANSITION_ENTER:
                           *//* transitionName = "enter";
                            near++;*//*
                            GeofenceNotification geofenceNotification = new GeofenceNotification(
                                    context);
                            geofenceNotification
                                    .displayNotification(sg, transitionType, near);

                            break;

                        case Geofence.GEOFENCE_TRANSITION_EXIT:
                            transitionName = "exit";
                            near--;
                            break;
                    }*/
                }

              //  if(near > 0 && transitionType != Geofence.GEOFENCE_TRANSITION_DWELL){
                    /*Animation imgAnimationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

                    ivLoading.setVisibility(View.VISIBLE);
                    ivLoading.setAnimation(imgAnimationIn);
                    tvLoading.setVisibility(View.VISIBLE);
                    tvLoading.setAnimation(imgAnimationIn);
                    isLoggedIn = accessToken != null && !accessToken.isExpired();
                    if(isLoggedIn && firstName != null) {
                        String name = "<font color='#4FC1E9'>" +firstName+"</font>";
                        tvLoading.setText(Html.fromHtml(("Thanks for your patience " + name + "<br>There are " +near+ " black owned businesses within 5 miles of you")));
                    } else {
                        tvLoading.setText("Thank you for waiting while we search our directory for black owned businesses near you.");
                    } */
                  //  Toast.makeText(context, "There are " +near+ " black owned businesses within 5 km of you", Toast.LENGTH_LONG).show();
                    //MainActivity.tvLoading.setText("It looks like there are " +near+ " black owned businesses within 5 miles of you");
               // }else{
               //     Toast.makeText(context, "There are no black owned businesses near you", Toast.LENGTH_LONG).show();
             //   } /*else if (near > 0 && transitionType)


                }
            }
        }




