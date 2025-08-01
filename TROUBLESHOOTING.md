# Meta Ads Plugin - Troubleshooting Guide

## 🚨 Common Issues and Solutions

### 1. **"Meta Audience Network not initialized" Error**

**Problem**: Plugin methods fail with initialization error.

**Solutions**:
```typescript
// ✅ Correct: Initialize before using
await MetaAds.initialize({
  appId: 'YOUR_META_APP_ID',
  testMode: true
});

// ❌ Wrong: Using methods without initialization
await MetaAds.loadRewardedVideo({ placementId: 'test' }); // Will fail
```

**Check**:
- Call `initialize()` before any other methods
- Wait for initialization to complete
- Handle initialization errors properly

### 2. **"SDK location not found" Build Error**

**Problem**: Android build fails with SDK location error.

**Solutions**:
1. **Install Android Studio** and set up Android SDK
2. **Set ANDROID_HOME** environment variable:
   ```bash
   # Windows
   set ANDROID_HOME=C:\Users\YourName\AppData\Local\Android\Sdk
   
   # macOS/Linux
   export ANDROID_HOME=$HOME/Android/Sdk
   ```
3. **Create local.properties** in android folder:
   ```properties
   sdk.dir=C:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
   ```

### 3. **"No ads available" or Ads Not Loading**

**Problem**: Ads fail to load even with correct placement IDs.

**Solutions**:
1. **Use test mode during development**:
   ```typescript
   await MetaAds.initialize({
     appId: 'YOUR_META_APP_ID',
     testMode: true // Enable test mode
   });
   ```

2. **Add test device**:
   ```typescript
   await MetaAds.addTestDevice({ 
     deviceId: 'YOUR_DEVICE_ID' // Get from logcat
   });
   ```

3. **Use test placement IDs**:
   ```typescript
   // Test IDs that always work
   const REWARDED_TEST_ID = 'VID_HD_16_9_46S_APP_INSTALL#YOUR_PLACEMENT_ID';
   const INTERSTITIAL_TEST_ID = 'IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID';
   ```

### 4. **"Placement ID is required" Error**

**Problem**: Methods fail with missing placement ID.

**Solution**:
```typescript
// ✅ Correct
await MetaAds.loadRewardedVideo({
  placementId: 'YOUR_PLACEMENT_ID'
});

// ❌ Wrong
await MetaAds.loadRewardedVideo({}); // Missing placementId
await MetaAds.loadRewardedVideo({ placementId: null }); // Null placementId
```

### 5. **Ads Show But No Reward Given**

**Problem**: Rewarded video plays but reward callback not triggered.

**Solutions**:
1. **Check reward handling**:
   ```typescript
   const result = await MetaAds.showRewardedVideo();
   
   if (result.success && result.reward) {
     // ✅ Reward was earned
     console.log('Reward:', result.reward);
   } else {
     // ❌ Ad was closed without completion
     console.log('No reward earned');
   }
   ```

2. **User must watch complete video** for reward
3. **Test with test ads** to ensure reward flow works

### 6. **"App ID is required" Error**

**Problem**: Initialization fails with missing App ID.

**Solution**:
```typescript
// ✅ Correct
await MetaAds.initialize({
  appId: 'YOUR_ACTUAL_META_APP_ID', // Get from Meta Business Manager
  testMode: true
});

// ❌ Wrong
await MetaAds.initialize({}); // Missing appId
await MetaAds.initialize({ appId: '' }); // Empty appId
```

### 7. **Plugin Not Found Error**

**Problem**: `MetaAds` is undefined or plugin not found.

**Solutions**:
1. **Check installation**:
   ```bash
   npm install capacitor-meta-ads
   npx cap sync android
   ```

2. **Verify import**:
   ```typescript
   import { MetaAds } from 'capacitor-meta-ads';
   ```

3. **Rebuild project**:
   ```bash
   npx cap clean android
   npx cap sync android
   npx cap run android
   ```

### 8. **Memory Leaks or App Crashes**

**Problem**: App crashes or memory issues after showing ads.

**Solutions**:
1. **Don't store ad instances** - let plugin manage them
2. **Call methods on main thread** only
3. **Handle errors properly**:
   ```typescript
   try {
     await MetaAds.showRewardedVideo();
   } catch (error) {
     console.error('Ad error:', error);
     // Don't crash the app
   }
   ```

### 9. **Ads Not Showing in Production**

**Problem**: Ads work in test mode but not in production.

**Solutions**:
1. **Disable test mode**:
   ```typescript
   await MetaAds.initialize({
     appId: 'YOUR_META_APP_ID',
     testMode: false // Disable for production
   });
   ```

2. **Use real placement IDs** (not test IDs)
3. **App must be approved** by Meta Audience Network
4. **Check Meta Business Manager** for app status

### 10. **Build Errors with Meta SDK**

**Problem**: Android build fails with Meta SDK conflicts.

**Solutions**:
1. **Check SDK version compatibility** in `android/build.gradle`
2. **Clean and rebuild**:
   ```bash
   cd android
   ./gradlew clean
   ./gradlew build
   ```

3. **Update Capacitor** if needed:
   ```bash
   npm update @capacitor/core @capacitor/android
   ```

## 🔍 Debugging Tips

### Enable Verbose Logging

```typescript
// Add this for debugging
console.log('Initializing Meta Ads...');
await MetaAds.initialize({
  appId: 'YOUR_APP_ID',
  testMode: true
});
console.log('Meta Ads initialized');
```

### Check Android Logs

```bash
# View Android logs
adb logcat | grep -i "meta\|audience\|facebook"
```

### Test Device ID

Get your test device ID from Android logs:
```bash
adb logcat | grep -i "test.*device"
```

### Verify Integration

```typescript
// Test basic functionality
async function testIntegration() {
  try {
    await MetaAds.initialize({ appId: 'test', testMode: true });
    console.log('✅ Initialization works');
    
    await MetaAds.loadRewardedVideo({ placementId: 'test' });
    console.log('✅ Load method works');
    
    const { loaded } = await MetaAds.isRewardedVideoLoaded();
    console.log('✅ Status check works:', loaded);
    
  } catch (error) {
    console.error('❌ Integration issue:', error);
  }
}
```

## 📞 Getting Help

1. **Check plugin logs** in Android Studio or Xcode
2. **Test with Meta's test placement IDs** first
3. **Verify Meta Business Manager** setup
4. **Check Capacitor compatibility** versions
5. **Review Meta Audience Network documentation**

## 🔗 Useful Links

- [Meta Audience Network Documentation](https://developers.facebook.com/docs/audience-network/)
- [Meta Business Manager](https://business.facebook.com/)
- [Capacitor Plugin Development](https://capacitorjs.com/docs/plugins)
- [Android SDK Setup](https://developer.android.com/studio/install)

## 📋 Pre-Release Checklist

Before releasing your app:

- [ ] Test with real placement IDs
- [ ] Disable test mode
- [ ] Remove test device IDs
- [ ] Test on multiple devices
- [ ] Verify ad policies compliance
- [ ] Check Meta app approval status
- [ ] Test error handling
- [ ] Verify reward system works
- [ ] Test network connectivity issues
- [ ] Performance test with ads
