# capacitor-meta-ads

Capacitor plugin for Meta Audience Network ads integration with support for rewarded video and interstitial ads.

## Features

- ✅ **Rewarded Video Ads** - Show rewarded video ads and handle rewards
- ✅ **Interstitial Ads** - Display full-screen interstitial ads
- ✅ **Test Mode Support** - Easy testing with Meta's test ads
- ✅ **Event Callbacks** - Comprehensive ad lifecycle events
- ✅ **Android Support** - Full Android implementation
- ✅ **iOS Support** - Full iOS implementation with Meta Audience Network SDK
- ✅ **AdMob Mediation** - Compatible with AdMob mediation (bidding only)

## Install

```bash
npm install capacitor-meta-ads
npx cap sync
```

## Configuration

### Android Setup

1. **Add Meta App ID** to your `android/app/src/main/AndroidManifest.xml`:

```xml
<application>
    <!-- Meta Audience Network App ID -->
    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="YOUR_META_APP_ID" />
</application>
```

2. **For AdMob Mediation** (optional - for maximum revenue):
   - Add AdMob mediation dependency: `implementation 'com.google.ads.mediation:facebook:6.11.0.1'`
   - Configure Meta as bidding source in AdMob console
   - Use AdMob ad units instead of direct Meta placement IDs

### iOS Setup

1. **Add Meta App ID** to your `ios/App/App/Info.plist`:

```xml
<key>FacebookAppID</key>
<string>YOUR_META_APP_ID</string>
<key>FacebookClientToken</key>
<string>YOUR_CLIENT_TOKEN</string>
```

2. **For AdMob Mediation** (optional):
   - Add AdMob mediation dependency in your Podfile
   - Configure Meta as bidding source in AdMob console
   - Use AdMob ad units instead of direct Meta placement IDs

## Usage

### Initialize the SDK

```typescript
import { MetaAds } from 'capacitor-meta-ads';

// Initialize with your Meta App ID
await MetaAds.initialize({
  appId: 'YOUR_META_APP_ID',
  testMode: true, // Set to false in production
});
```

### Rewarded Video Ads

```typescript
// Load a rewarded video ad
await MetaAds.loadRewardedVideo({
  placementId: 'YOUR_REWARDED_PLACEMENT_ID',
});

// Check if ad is loaded
const { loaded } = await MetaAds.isRewardedVideoLoaded();

if (loaded) {
  // Show the ad
  const result = await MetaAds.showRewardedVideo();

  if (result.success && result.reward) {
    console.log('Reward earned:', result.reward);
    // Give reward to user
  }
}
```

### Interstitial Ads

```typescript
// Load an interstitial ad
await MetaAds.loadInterstitial({
  placementId: 'YOUR_INTERSTITIAL_PLACEMENT_ID',
});

// Check if ad is loaded
const { loaded } = await MetaAds.isInterstitialLoaded();

if (loaded) {
  // Show the ad
  const result = await MetaAds.showInterstitial();

  if (result.success) {
    console.log('Interstitial ad shown successfully');
  }
}
```

### Test Mode

```typescript
// Enable test mode (should be done before initialize)
await MetaAds.setTestMode({ enabled: true });

// Add test device
await MetaAds.addTestDevice({ deviceId: 'YOUR_TEST_DEVICE_ID' });
```

## Test Placement IDs

For testing, use Meta's test placement IDs:

- **Rewarded Video**: `VID_HD_16_9_46S_APP_INSTALL#YOUR_PLACEMENT_ID`
- **Interstitial**: `IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID`

**For AdMob Mediation Testing:**
- Use AdMob test ad unit IDs
- Enable test mode in both AdMob and Meta consoles
- Register your test device in both platforms

## API

<docgen-index>

* [`initialize(...)`](#initialize)
* [`loadRewardedVideo(...)`](#loadrewardedvideo)
* [`showRewardedVideo()`](#showrewardedvideo)
* [`isRewardedVideoLoaded()`](#isrewardedvideoloaded)
* [`loadInterstitial(...)`](#loadinterstitial)
* [`showInterstitial()`](#showinterstitial)
* [`isInterstitialLoaded()`](#isinterstitialloaded)
* [`setTestMode(...)`](#settestmode)
* [`addTestDevice(...)`](#addtestdevice)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### initialize(...)

```typescript
initialize(options: { appId: string; testMode?: boolean; }) => Promise<void>
```

Initialize Meta Audience Network SDK

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code>{ appId: string; testMode?: boolean; }</code> |

--------------------


### loadRewardedVideo(...)

```typescript
loadRewardedVideo(options: { placementId: string; }) => Promise<void>
```

Load a rewarded video ad

| Param         | Type                                  |
| ------------- | ------------------------------------- |
| **`options`** | <code>{ placementId: string; }</code> |

--------------------


### showRewardedVideo()

```typescript
showRewardedVideo() => Promise<{ success: boolean; reward?: RewardInfo; }>
```

Show a rewarded video ad

**Returns:** <code>Promise&lt;{ success: boolean; reward?: <a href="#rewardinfo">RewardInfo</a>; }&gt;</code>

--------------------


### isRewardedVideoLoaded()

```typescript
isRewardedVideoLoaded() => Promise<{ loaded: boolean; }>
```

Check if rewarded video ad is loaded

**Returns:** <code>Promise&lt;{ loaded: boolean; }&gt;</code>

--------------------


### loadInterstitial(...)

```typescript
loadInterstitial(options: { placementId: string; }) => Promise<void>
```

Load an interstitial ad

| Param         | Type                                  |
| ------------- | ------------------------------------- |
| **`options`** | <code>{ placementId: string; }</code> |

--------------------


### showInterstitial()

```typescript
showInterstitial() => Promise<{ success: boolean; }>
```

Show an interstitial ad

**Returns:** <code>Promise&lt;{ success: boolean; }&gt;</code>

--------------------


### isInterstitialLoaded()

```typescript
isInterstitialLoaded() => Promise<{ loaded: boolean; }>
```

Check if interstitial ad is loaded

**Returns:** <code>Promise&lt;{ loaded: boolean; }&gt;</code>

--------------------


### setTestMode(...)

```typescript
setTestMode(options: { enabled: boolean; }) => Promise<void>
```

Set test mode

| Param         | Type                               |
| ------------- | ---------------------------------- |
| **`options`** | <code>{ enabled: boolean; }</code> |

--------------------


### addTestDevice(...)

```typescript
addTestDevice(options: { deviceId: string; }) => Promise<void>
```

Add test device

| Param         | Type                               |
| ------------- | ---------------------------------- |
| **`options`** | <code>{ deviceId: string; }</code> |

--------------------


### Interfaces


#### RewardInfo

| Prop         | Type                |
| ------------ | ------------------- |
| **`type`**   | <code>string</code> |
| **`amount`** | <code>number</code> |

</docgen-api>

## Requirements

- **Android**: API level 23+ (Android 6.0+)
- **Meta Audience Network SDK**: 6.17.0+
- **Capacitor**: 7.0.0+
- **iOS**: iOS 14.0+

## AdMob Mediation Support

✅ **Fully Compatible** - This plugin works with AdMob mediation:
- Meta Audience Network operates as **bidding-only** partner (no waterfall)
- Increases revenue through real-time bidding competition
- Improves fill rates across your ad inventory
- Configure in AdMob console, not directly in app code

**Setup Steps:**
1. Add mediation dependency to `android/build.gradle`
2. Create mediation group in AdMob console
3. Add Meta as bidding source
4. Use AdMob ad unit IDs in your app

## Related Plugin

For Unity Ads integration, check out our companion plugin:

- **Unity Ads Plugin**: [capacitor-unity-ads](https://github.com/eliazv/capacitor-unity-ads)

Use both plugins together with AdMob mediation for maximum revenue!

## License

MIT
