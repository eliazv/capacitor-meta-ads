import { WebPlugin } from '@capacitor/core';

import type { MetaAdsPlugin, RewardInfo } from './definitions';

export class MetaAdsWeb extends WebPlugin implements MetaAdsPlugin {
  async initialize(options: { appId: string; testMode?: boolean }): Promise<void> {
    console.log('MetaAds Web: initialize', options);
    // Web implementation not available for Meta Audience Network
    throw new Error('Meta Audience Network is not available on web platform');
  }

  async loadRewardedVideo(options: { placementId: string }): Promise<void> {
    console.log('MetaAds Web: loadRewardedVideo', options);
    throw new Error('Meta Audience Network is not available on web platform');
  }

  async showRewardedVideo(): Promise<{ success: boolean; reward?: RewardInfo }> {
    console.log('MetaAds Web: showRewardedVideo');
    throw new Error('Meta Audience Network is not available on web platform');
  }

  async isRewardedVideoLoaded(): Promise<{ loaded: boolean }> {
    console.log('MetaAds Web: isRewardedVideoLoaded');
    return { loaded: false };
  }

  async loadInterstitial(options: { placementId: string }): Promise<void> {
    console.log('MetaAds Web: loadInterstitial', options);
    throw new Error('Meta Audience Network is not available on web platform');
  }

  async showInterstitial(): Promise<{ success: boolean }> {
    console.log('MetaAds Web: showInterstitial');
    throw new Error('Meta Audience Network is not available on web platform');
  }

  async isInterstitialLoaded(): Promise<{ loaded: boolean }> {
    console.log('MetaAds Web: isInterstitialLoaded');
    return { loaded: false };
  }

  async setTestMode(options: { enabled: boolean }): Promise<void> {
    console.log('MetaAds Web: setTestMode', options);
    // No-op on web
  }

  async addTestDevice(options: { deviceId: string }): Promise<void> {
    console.log('MetaAds Web: addTestDevice', options);
    // No-op on web
  }
}
