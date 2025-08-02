package com.eliazavatta.plugins.metaads;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "MetaAds")
public class MetaAdsPlugin extends Plugin {

    private MetaAds implementation = new MetaAds();

    @PluginMethod
    public void initialize(PluginCall call) {
        String appId = call.getString("appId");
        Boolean testMode = call.getBoolean("testMode", false);

        if (appId == null) {
            call.reject("App ID is required");
            return;
        }

        implementation.initialize(
            getContext(),
            appId,
            testMode,
            new MetaAds.InitializationCallback() {
                @Override
                public void onSuccess() {
                    call.resolve();
                }

                @Override
                public void onError(String error) {
                    call.reject(error);
                }
            }
        );
    }

    @PluginMethod
    public void loadRewardedVideo(PluginCall call) {
        String placementId = call.getString("placementId");

        if (placementId == null) {
            call.reject("Placement ID is required");
            return;
        }

        implementation.loadRewardedVideo(
            getContext(),
            placementId,
            new MetaAds.AdLoadCallback() {
                @Override
                public void onAdLoaded() {
                    call.resolve();
                }

                @Override
                public void onError(String error) {
                    call.reject(error);
                }
            }
        );
    }

    @PluginMethod
    public void showRewardedVideo(PluginCall call) {
        implementation.showRewardedVideo(
            new MetaAds.RewardedVideoCallback() {
                @Override
                public void onAdShown() {
                    // Ad shown successfully
                }

                @Override
                public void onRewardEarned(String rewardType, int rewardAmount) {
                    JSObject ret = new JSObject();
                    ret.put("success", true);
                    JSObject reward = new JSObject();
                    reward.put("type", rewardType);
                    reward.put("amount", rewardAmount);
                    ret.put("reward", reward);
                    call.resolve(ret);
                }

                @Override
                public void onAdClosed() {
                    // Ad closed without reward
                    JSObject ret = new JSObject();
                    ret.put("success", false);
                    call.resolve(ret);
                }

                @Override
                public void onError(String error) {
                    call.reject(error);
                }
            }
        );
    }

    @PluginMethod
    public void isRewardedVideoLoaded(PluginCall call) {
        boolean loaded = implementation.isRewardedVideoLoaded();
        JSObject ret = new JSObject();
        ret.put("loaded", loaded);
        call.resolve(ret);
    }

    @PluginMethod
    public void loadInterstitial(PluginCall call) {
        String placementId = call.getString("placementId");

        if (placementId == null) {
            call.reject("Placement ID is required");
            return;
        }

        implementation.loadInterstitial(
            getContext(),
            placementId,
            new MetaAds.AdLoadCallback() {
                @Override
                public void onAdLoaded() {
                    call.resolve();
                }

                @Override
                public void onError(String error) {
                    call.reject(error);
                }
            }
        );
    }

    @PluginMethod
    public void showInterstitial(PluginCall call) {
        implementation.showInterstitial(
            new MetaAds.InterstitialCallback() {
                @Override
                public void onAdShown() {
                    JSObject ret = new JSObject();
                    ret.put("success", true);
                    call.resolve(ret);
                }

                @Override
                public void onAdClosed() {
                    // Ad closed
                }

                @Override
                public void onError(String error) {
                    call.reject(error);
                }
            }
        );
    }

    @PluginMethod
    public void isInterstitialLoaded(PluginCall call) {
        boolean loaded = implementation.isInterstitialLoaded();
        JSObject ret = new JSObject();
        ret.put("loaded", loaded);
        call.resolve(ret);
    }

    @PluginMethod
    public void setTestMode(PluginCall call) {
        Boolean enabled = call.getBoolean("enabled", false);
        implementation.setTestMode(enabled);
        call.resolve();
    }

    @PluginMethod
    public void addTestDevice(PluginCall call) {
        String deviceId = call.getString("deviceId");
        if (deviceId != null) {
            implementation.addTestDevice(deviceId);
        }
        call.resolve();
    }
}
