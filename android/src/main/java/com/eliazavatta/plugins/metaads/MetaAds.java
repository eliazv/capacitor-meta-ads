package com.eliazavatta.plugins.metaads;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.facebook.ads.AdSettings;

import java.util.ArrayList;
import java.util.List;

public class MetaAds {
    private static final String TAG = "MetaAds";

    private RewardedVideoAd rewardedVideoAd;
    private InterstitialAd interstitialAd;
    private boolean isInitialized = false;
    private List<String> testDevices = new ArrayList<>();

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

    public void loadRewardedVideo(String placementId, AdLoadCallback callback) {
        if (!isInitialized) {
            callback.onError("Meta Audience Network not initialized");
            return;
        }

        try {
            Log.d(TAG, "Loading rewarded video with placement ID: " + placementId);

            rewardedVideoAd = new RewardedVideoAd(null, placementId);

            RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "Rewarded video failed to load: " + adError.getErrorMessage());
                    callback.onError("Failed to load rewarded video: " + adError.getErrorMessage());
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
                }

                @Override
                public void onRewardedVideoCompleted() {
                    Log.d(TAG, "Rewarded video completed");
                }

                @Override
                public void onRewardedVideoClosed() {
                    Log.d(TAG, "Rewarded video closed");
                }
            };

            rewardedVideoAd.loadAd(
                rewardedVideoAd.buildLoadAdConfig()
                    .withAdListener(rewardedVideoAdListener)
                    .build()
            );

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

            RewardedVideoAdListener showListener = new RewardedVideoAdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "Error showing rewarded video: " + adError.getErrorMessage());
                    callback.onError("Error showing rewarded video: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Not called during show
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Log.d(TAG, "Rewarded video clicked during show");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Log.d(TAG, "Rewarded video shown");
                    callback.onAdShown();
                }

                @Override
                public void onRewardedVideoCompleted() {
                    Log.d(TAG, "Rewarded video completed - reward earned");
                    callback.onRewardEarned("coins", 1); // Default reward
                }

                @Override
                public void onRewardedVideoClosed() {
                    Log.d(TAG, "Rewarded video closed");
                    callback.onAdClosed();
                }
            };

            rewardedVideoAd.show();

        } catch (Exception e) {
            Log.e(TAG, "Error showing rewarded video", e);
            callback.onError("Error showing rewarded video: " + e.getMessage());
        }
    }

    public boolean isRewardedVideoLoaded() {
        return rewardedVideoAd != null && rewardedVideoAd.isAdLoaded();
    }

    public void loadInterstitial(String placementId, AdLoadCallback callback) {
        if (!isInitialized) {
            callback.onError("Meta Audience Network not initialized");
            return;
        }

        try {
            Log.d(TAG, "Loading interstitial with placement ID: " + placementId);

            interstitialAd = new InterstitialAd(null, placementId);

            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "Interstitial failed to load: " + adError.getErrorMessage());
                    callback.onError("Failed to load interstitial: " + adError.getErrorMessage());
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
                }

                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    Log.d(TAG, "Interstitial displayed");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    Log.d(TAG, "Interstitial dismissed");
                }
            };

            interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                    .withAdListener(interstitialAdListener)
                    .build()
            );

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

            InterstitialAdListener showListener = new InterstitialAdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "Error showing interstitial: " + adError.getErrorMessage());
                    callback.onError("Error showing interstitial: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Not called during show
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Log.d(TAG, "Interstitial clicked during show");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Log.d(TAG, "Interstitial shown");
                    callback.onAdShown();
                }

                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    Log.d(TAG, "Interstitial displayed");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    Log.d(TAG, "Interstitial dismissed");
                    callback.onAdClosed();
                }
            };

            interstitialAd.show();

        } catch (Exception e) {
            Log.e(TAG, "Error showing interstitial", e);
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
