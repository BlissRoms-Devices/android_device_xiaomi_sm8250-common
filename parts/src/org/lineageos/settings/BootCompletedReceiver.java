/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.lineageos.settings.doze.DozeUtils;
import org.lineageos.settings.popupcamera.PopupCameraUtils;
import org.lineageos.settings.touchsampling.TouchSamplingUtils;
import org.lineageos.settings.thermal.ThermalUtils;
import org.lineageos.settings.refreshrate.RefreshUtils;
import org.lineageos.settings.utils.FileUtils;
import org.lineageos.settings.display.DisplayNodes;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final boolean DEBUG = false;
    private static final String TAG = "XiaomiParts";
    private String DC_DIMMING_ENABLE_KEY;
    private String DC_DIMMING_NODE;
    private String HBM_ENABLE_KEY;
    private String HBM_NODE;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (DEBUG) Log.d(TAG, "Received boot completed intent");
        DozeUtils.checkDozeService(context);
        PopupCameraUtils.startService(context);
        TouchSamplingUtils.restoreSamplingValue(context);
        ThermalUtils.startService(context);
        RefreshUtils.startService(context);    
        
        DC_DIMMING_ENABLE_KEY = DisplayNodes.getDcDimmingEnableKey();
        DC_DIMMING_NODE = DisplayNodes.getDcDimmingNode();
        HBM_ENABLE_KEY = DisplayNodes.getHbmEnableKey();
        HBM_NODE = DisplayNodes.getHbmNode();  
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        boolean dcDimmingEnabled = sharedPrefs.getBoolean(DC_DIMMING_ENABLE_KEY, false);
        FileUtils.writeLine(DC_DIMMING_NODE, dcDimmingEnabled ? "1" : "0");
        boolean hbmEnabled = sharedPrefs.getBoolean(HBM_ENABLE_KEY, false);
        FileUtils.writeLine(HBM_NODE, hbmEnabled ? "1" : "0");
    }
}
