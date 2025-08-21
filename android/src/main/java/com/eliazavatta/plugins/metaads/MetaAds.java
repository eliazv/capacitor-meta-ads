package com.eliazavatta.plugins.metaads;

import android.content.Context;
import android.util.Log;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import java.util.ArrayList;
import java.util.List;

public class MetaAds {

    private static final String TAG = "MetaAds";

    private RewardedVideoAd rewardedVideoAd;
    private InterstitialAd interstitialAd;
    private boolean isInitialized = false;
    private List<String> testDevices = new ArrayList<>();

    // Store callbacks for show events
    private RewardedVideoCallback currentRewardedCallback;
    private InterstitialCallback currentInterstitialCallback;

    // Callback interfaces
    public interface InitializationCallback {
        void onSuccess();
        void onError(String error);
    }

    public interface AdLoadCallback {
        void onAdLoaded();
        void onError(String error);
    }

    public interface RewardedVideoCallback {
        void onAdShown();
        void onRewardEarned(String rewardType, int rewardAmount);
        void onAdClosed();
        void onError(String error);
    }

    public interface InterstitialCallback {
        void onAdShown();
        void onAdClosed();
        void onError(String error);
    }

    public void initialize(Context context, String appId, boolean testMode, InitializationCallback callback) {
        try {
            Log.d(TAG, "Initializing Meta Audience Network with App ID: " + appId);

            // Set test mode if enabled
            if (testMode) {
                AdSettings.setTestMode(true);
                Log.d(TAG, "Test mode enabled");
            }

            // Add test devices if any
            if (!testDevices.isEmpty()) {
                AdSettings.addTestDevices(testDevices);
                Log.d(TAG, "Added " + testDevices.size() + " test devices");
            }

            // Initialize Audience Network
            AudienceNetworkAds.initialize(context);

            isInitialized = true;
            Log.d(TAG, "Meta Audience Network initialized successfully");
            callback.onSuccess();
        } catch (Exception e) {
            Log.e(TAG, "Failed to initialize Meta Audience Network", e);
            callback.onError("Initialization failed: " + e.getMessage());
        }
    }

    public void loadRewardedVideo(Context context, String placementId, AdLoadCallback callback) {
        if (!isInitialized) {
            callback.onError("Meta Audience Network not initialized");
            return;
        }

        try {
            Log.d(TAG, "Loading rewarded video with placement ID: " + placementId);

            rewardedVideoAd = new RewardedVideoAd(context, placementId);

            RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "Rewarded video error: " + adError.getErrorMessage());
                    // Handle both load and show errors
                    if (currentRewardedCallback != null) {
                        currentRewardedCallback.onError("Error with rewarded video: " + adError.getErrorMessage());
                        currentRewardedCallback = null;
                    } else {
                        callback.onError("Failed to load rewarded video: " + adError.getErrorMessage());
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.d(TAG, "Rewarded video loaded successfully");
                    callback.onAdLoaded();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Log.d(TAG, "Rewarded video clicked");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Log.d(TAG, "Rewarded video impression logged");
                    if (currentRewardedCallback != null) {
                        currentRewardedCallback.onAdShown();
                    }
                }

                @Override
                public void onRewardedVideoCompleted() {
                    Log.d(TAG, "Rewarded video completed - reward earned");
                    if (currentRewardedCallback != null) {
                        currentRewardedCallback.onRewardEarned("coins", 1);
                        currentRewardedCallback = null;
                    }
                }

                @Override
                public void onRewardedVideoClosed() {
                    Log.d(TAG, "Rewarded video closed");
                    if (currentRewardedCallback != null) {
                        currentRewardedCallback.onAdClosed();
                        currentRewardedCallback = null;
                    }
                }
            };

            rewardedVideoAd.loadAd(rewardedVideoAd.buildLoadAdConfig().withAdListener(rewardedVideoAdListener).build());
        } catch (Exception e) {
            Log.e(TAG, "Error loading rewarded video", e);
            callback.onError("Error loading rewarded video: " + e.getMessage());
        }
    }

    public void showRewardedVideo(RewardedVideoCallback callback) {
        if (rewardedVideoAd == null || !rewardedVideoAd.isAdLoaded()) {
            callback.onError("Rewarded video not loaded");
            return;
        }

        try {
            Log.d(TAG, "Showing rewarded video");

            // Store callback for the unified listener to use
            currentRewardedCallback = callback;

            rewardedVideoAd.show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing rewarded video", e);
            currentRewardedCallback = null;
            callback.onError("Error showing rewarded video: " + e.getMessage());
        }
    }

    public boolean isRewardedVideoLoaded() {
        return rewardedVideoAd != null && rewardedVideoAd.isAdLoaded();
    }

    public void loadInterstitial(Context context, String placementId, AdLoadCallback callback) {
        if (!isInitialized) {
            callback.onError("Meta Audience Network not initialized");
            return;
        }

        try {
            Log.d(TAG, "Loading interstitial with placement ID: " + placementId);

            interstitialAd = new InterstitialAd(context, placementId);

            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "Interstitial error: " + adError.getErrorMessage());
                    // Handle both load and show errors
                    if (currentInterstitialCallback != null) {
                        currentInterstitialCallback.onError("Error with interstitial: " + adError.getErrorMessage());
                        currentInterstitialCallback = null;
                    } else {
                        callback.onError("Failed to load interstitial: " + adError.getErrorMessage());
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.d(TAG, "Interstitial loaded successfully");
                    callback.onAdLoaded();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Log.d(TAG, "Interstitial clicked");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Log.d(TAG, "Interstitial impression logged");
                    if (currentInterstitialCallback != null) {
                        currentInterstitialCallback.onAdShown();
                    }
                }

                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    Log.d(TAG, "Interstitial displayed");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    Log.d(TAG, "Interstitial dismissed");
                    if (currentInterstitialCallback != null) {
                        currentInterstitialCallback.onAdClosed();
                        currentInterstitialCallback = null;
                    }
                }
            };

            interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        } catch (Exception e) {
            Log.e(TAG, "Error loading interstitial", e);
            callback.onError("Error loading interstitial: " + e.getMessage());
        }
    }

    public void showInterstitial(InterstitialCallback callback) {
        if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
            callback.onError("Interstitial not loaded");
            return;
        }

        try {
            Log.d(TAG, "Showing interstitial");

            // Store callback for the unified listener to use
            currentInterstitialCallback = callback;

            interstitialAd.show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing interstitial", e);
            currentInterstitialCallback = null;
            callback.onError("Error showing interstitial: " + e.getMessage());
        }
    }

    public boolean isInterstitialLoaded() {
        return interstitialAd != null && interstitialAd.isAdLoaded();
    }

    public void setTestMode(boolean enabled) {
        Log.d(TAG, "Setting test mode: " + enabled);
        AdSettings.setTestMode(enabled);
    }

    public void addTestDevice(String deviceId) {
        Log.d(TAG, "Adding test device: " + deviceId);
        testDevices.add(deviceId);
        if (isInitialized) {
            AdSettings.addTestDevice(deviceId);
        }
    }
}
