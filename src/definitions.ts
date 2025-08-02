export interface MetaAdsPlugin {
  /**
   * Initialize Meta Audience Network SDK
   */
  initialize(options: { appId: string; testMode?: boolean }): Promise<void>;

  /**
   * Load a rewarded video ad
   */
  loadRewardedVideo(options: { placementId: string }): Promise<void>;

  /**
   * Show a rewarded video ad
   */
  showRewardedVideo(): Promise<{ success: boolean; reward?: RewardInfo }>;

  /**
   * Check if rewarded video ad is loaded
   */
  isRewardedVideoLoaded(): Promise<{ loaded: boolean }>;

  /**
   * Load an interstitial ad
   */
  loadInterstitial(options: { placementId: string }): Promise<void>;

  /**
   * Show an interstitial ad
   */
  showInterstitial(): Promise<{ success: boolean }>;

  /**
   * Check if interstitial ad is loaded
   */
  isInterstitialLoaded(): Promise<{ loaded: boolean }>;

  /**
   * Set test mode
   */
  setTestMode(options: { enabled: boolean }): Promise<void>;

  /**
   * Add test device
   */
  addTestDevice(options: { deviceId: string }): Promise<void>;
}

export interface RewardInfo {
  type: string;
  amount: number;
}

export interface MetaAdsEvent {
  type:
    | 'initialized'
    | 'rewardedVideoLoaded'
    | 'rewardedVideoFailedToLoad'
    | 'rewardedVideoShown'
    | 'rewardedVideoCompleted'
    | 'rewardedVideoClosed'
    | 'interstitialLoaded'
    | 'interstitialFailedToLoad'
    | 'interstitialShown'
    | 'interstitialClosed';
  data?: any;
}
