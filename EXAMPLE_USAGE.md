# Meta Ads Plugin - Example Usage

## Complete Integration Example

### 1. Installation and Setup

```bash
# Install the plugin
npm install capacitor-meta-ads
npx cap sync android
```

### 2. Android Configuration

Add to `android/app/src/main/AndroidManifest.xml`:

```xml
<application>
    <!-- Meta Audience Network App ID -->
    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="YOUR_META_APP_ID" />
</application>
```

### 3. TypeScript Implementation

```typescript
import { MetaAds } from 'capacitor-meta-ads';

export class AdManager {
  private isInitialized = false;
  
  // Test Placement IDs (use your real ones in production)
  private readonly REWARDED_PLACEMENT_ID = 'VID_HD_16_9_46S_APP_INSTALL#YOUR_PLACEMENT_ID';
  private readonly INTERSTITIAL_PLACEMENT_ID = 'IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID';

  async initialize() {
    try {
      await MetaAds.initialize({
        appId: 'YOUR_META_APP_ID',
        testMode: true // Set to false in production
      });
      
      // Add test device (get device ID from logs)
      await MetaAds.addTestDevice({ 
        deviceId: 'YOUR_TEST_DEVICE_ID' 
      });
      
      this.isInitialized = true;
      console.log('Meta Ads initialized successfully');
    } catch (error) {
      console.error('Failed to initialize Meta Ads:', error);
    }
  }

  async showRewardedVideo(): Promise<boolean> {
    if (!this.isInitialized) {
      console.error('Meta Ads not initialized');
      return false;
    }

    try {
      // Load the ad
      await MetaAds.loadRewardedVideo({
        placementId: this.REWARDED_PLACEMENT_ID
      });

      // Check if loaded
      const { loaded } = await MetaAds.isRewardedVideoLoaded();
      
      if (!loaded) {
        console.error('Rewarded video not loaded');
        return false;
      }

      // Show the ad
      const result = await MetaAds.showRewardedVideo();
      
      if (result.success && result.reward) {
        console.log('Reward earned:', result.reward);
        // Give reward to user
        this.giveRewardToUser(result.reward);
        return true;
      }
      
      return false;
    } catch (error) {
      console.error('Error showing rewarded video:', error);
      return false;
    }
  }

  async showInterstitial(): Promise<boolean> {
    if (!this.isInitialized) {
      console.error('Meta Ads not initialized');
      return false;
    }

    try {
      // Load the ad
      await MetaAds.loadInterstitial({
        placementId: this.INTERSTITIAL_PLACEMENT_ID
      });

      // Check if loaded
      const { loaded } = await MetaAds.isInterstitialLoaded();
      
      if (!loaded) {
        console.error('Interstitial not loaded');
        return false;
      }

      // Show the ad
      const result = await MetaAds.showInterstitial();
      
      if (result.success) {
        console.log('Interstitial shown successfully');
        return true;
      }
      
      return false;
    } catch (error) {
      console.error('Error showing interstitial:', error);
      return false;
    }
  }

  private giveRewardToUser(reward: { type: string; amount: number }) {
    // Implement your reward logic here
    console.log(`Giving ${reward.amount} ${reward.type} to user`);
    
    // Example: Add coins to user balance
    // UserService.addCoins(reward.amount);
  }

  async preloadAds() {
    if (!this.isInitialized) return;

    try {
      // Preload rewarded video
      await MetaAds.loadRewardedVideo({
        placementId: this.REWARDED_PLACEMENT_ID
      });

      // Preload interstitial
      await MetaAds.loadInterstitial({
        placementId: this.INTERSTITIAL_PLACEMENT_ID
      });

      console.log('Ads preloaded successfully');
    } catch (error) {
      console.error('Error preloading ads:', error);
    }
  }
}
```

### 4. React Component Example

```tsx
import React, { useEffect, useState } from 'react';
import { AdManager } from './AdManager';

const GameScreen: React.FC = () => {
  const [adManager] = useState(new AdManager());
  const [coins, setCoins] = useState(100);

  useEffect(() => {
    // Initialize ads when component mounts
    adManager.initialize().then(() => {
      // Preload ads for better user experience
      adManager.preloadAds();
    });
  }, []);

  const handleWatchAd = async () => {
    const success = await adManager.showRewardedVideo();
    if (success) {
      setCoins(prev => prev + 10); // Give 10 coins as reward
    }
  };

  const handleShowInterstitial = async () => {
    await adManager.showInterstitial();
  };

  return (
    <div className="game-screen">
      <div className="coins">Coins: {coins}</div>
      
      <button onClick={handleWatchAd}>
        Watch Ad for 10 Coins
      </button>
      
      <button onClick={handleShowInterstitial}>
        Show Interstitial
      </button>
    </div>
  );
};

export default GameScreen;
```

### 5. Error Handling Best Practices

```typescript
export class RobustAdManager extends AdManager {
  private retryCount = 0;
  private maxRetries = 3;

  async showRewardedVideoWithRetry(): Promise<boolean> {
    for (let i = 0; i < this.maxRetries; i++) {
      try {
        const success = await this.showRewardedVideo();
        if (success) {
          this.retryCount = 0; // Reset on success
          return true;
        }
      } catch (error) {
        console.warn(`Attempt ${i + 1} failed:`, error);
        
        if (i < this.maxRetries - 1) {
          // Wait before retry
          await new Promise(resolve => setTimeout(resolve, 1000 * (i + 1)));
        }
      }
    }
    
    console.error('All retry attempts failed');
    return false;
  }

  async checkAdAvailability(): Promise<{
    rewardedVideo: boolean;
    interstitial: boolean;
  }> {
    try {
      const [rewardedResult, interstitialResult] = await Promise.all([
        MetaAds.isRewardedVideoLoaded(),
        MetaAds.isInterstitialLoaded()
      ]);

      return {
        rewardedVideo: rewardedResult.loaded,
        interstitial: interstitialResult.loaded
      };
    } catch (error) {
      console.error('Error checking ad availability:', error);
      return { rewardedVideo: false, interstitial: false };
    }
  }
}
```

### 6. Production Checklist

- [ ] Replace test placement IDs with real ones
- [ ] Set `testMode: false` in initialize
- [ ] Remove test device IDs
- [ ] Test on real devices
- [ ] Implement proper error handling
- [ ] Add loading states in UI
- [ ] Respect user preferences (ads on/off)
- [ ] Follow platform ad policies

### 7. Getting Your Placement IDs

1. Go to [Facebook Business Manager](https://business.facebook.com/)
2. Navigate to Meta Audience Network
3. Create your app and ad placements
4. Copy the placement IDs for rewarded video and interstitial
5. Replace the test IDs in your code

### 8. Testing

Use Meta's test placement IDs during development:
- Rewarded Video: `VID_HD_16_9_46S_APP_INSTALL#YOUR_PLACEMENT_ID`
- Interstitial: `IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID`

These will show test ads that don't generate revenue but allow you to test the integration.
