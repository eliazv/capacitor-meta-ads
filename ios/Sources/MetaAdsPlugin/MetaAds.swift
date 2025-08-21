import Foundation
import FBAudienceNetwork

@objc public class MetaAds: NSObject {
    private var rewardedVideoAd: FBRewardedVideoAd?
    private var interstitialAd: FBInterstitialAd?
    private var isInitialized = false
    private var testDevices: [String] = []
    
    // Callback typealias
    public typealias InitializationCallback = (Bool, String?) -> Void
    public typealias AdLoadCallback = (Bool, String?) -> Void
    public typealias RewardedVideoCallback = (Bool, [String: Any]?) -> Void
    public typealias InterstitialCallback = (Bool) -> Void
    
    @objc public func initialize(appId: String, testMode: Bool, callback: @escaping InitializationCallback) {
        DispatchQueue.main.async {
            // Set test mode if enabled
            if testMode {
                FBAdSettings.setTestMode(true)
                print("MetaAds: Test mode enabled")
            }
            
            // Add test devices if any
            if !self.testDevices.isEmpty {
                FBAdSettings.addTestDevices(self.testDevices)
                print("MetaAds: Added \(self.testDevices.count) test devices")
            }
            
            // Initialize Audience Network
            FBAudienceNetworkAds.initialize(with: nil) { result in
                if result.success {
                    self.isInitialized = true
                    print("MetaAds: Meta Audience Network initialized successfully")
                    callback(true, nil)
                } else {
                    let error = result.message ?? "Unknown initialization error"
                    print("MetaAds: Failed to initialize Meta Audience Network: \(error)")
                    callback(false, "Initialization failed: \(error)")
                }
            }
        }
    }
    
    @objc public func loadRewardedVideo(placementId: String, callback: @escaping AdLoadCallback) {
        guard isInitialized else {
            callback(false, "Meta Audience Network not initialized")
            return
        }
        
        DispatchQueue.main.async {
            print("MetaAds: Loading rewarded video with placement ID: \(placementId)")
            
            self.rewardedVideoAd = FBRewardedVideoAd(placementID: placementId)
            
            guard let rewardedVideoAd = self.rewardedVideoAd else {
                callback(false, "Failed to create rewarded video ad")
                return
            }
            
            rewardedVideoAd.delegate = RewardedVideoAdDelegate(callback: callback)
            rewardedVideoAd.load()
        }
    }
    
    @objc public func showRewardedVideo(callback: @escaping RewardedVideoCallback) {
        guard let rewardedVideoAd = rewardedVideoAd, rewardedVideoAd.isAdValid else {
            callback(false, nil)
            return
        }
        
        DispatchQueue.main.async {
            print("MetaAds: Showing rewarded video")
            
            if let rootViewController = self.getRootViewController() {
                let showDelegate = RewardedVideoShowDelegate(callback: callback)
                self.rewardedVideoAd?.delegate = showDelegate
                rewardedVideoAd.show(fromRootViewController: rootViewController)
            } else {
                callback(false, nil)
            }
        }
    }
    
    @objc public func isRewardedVideoLoaded() -> Bool {
        return rewardedVideoAd?.isAdValid ?? false
    }
    
    @objc public func loadInterstitial(placementId: String, callback: @escaping AdLoadCallback) {
        guard isInitialized else {
            callback(false, "Meta Audience Network not initialized")
            return
        }
        
        DispatchQueue.main.async {
            print("MetaAds: Loading interstitial with placement ID: \(placementId)")
            
            self.interstitialAd = FBInterstitialAd(placementID: placementId)
            
            guard let interstitialAd = self.interstitialAd else {
                callback(false, "Failed to create interstitial ad")
                return
            }
            
            interstitialAd.delegate = InterstitialAdDelegate(callback: callback)
            interstitialAd.load()
        }
    }
    
    @objc public func showInterstitial(callback: @escaping InterstitialCallback) {
        guard let interstitialAd = interstitialAd, interstitialAd.isAdValid else {
            callback(false)
            return
        }
        
        DispatchQueue.main.async {
            print("MetaAds: Showing interstitial")
            
            if let rootViewController = self.getRootViewController() {
                let showDelegate = InterstitialShowDelegate(callback: callback)
                self.interstitialAd?.delegate = showDelegate
                interstitialAd.show(fromRootViewController: rootViewController)
            } else {
                callback(false)
            }
        }
    }
    
    @objc public func isInterstitialLoaded() -> Bool {
        return interstitialAd?.isAdValid ?? false
    }
    
    @objc public func setTestMode(enabled: Bool) {
        print("MetaAds: Setting test mode: \(enabled)")
        FBAdSettings.setTestMode(enabled)
    }
    
    @objc public func addTestDevice(deviceId: String) {
        print("MetaAds: Adding test device: \(deviceId)")
        testDevices.append(deviceId)
        if isInitialized {
            FBAdSettings.addTestDevice(deviceId)
        }
    }

    // MARK: - Helper Methods

    private func getRootViewController() -> UIViewController? {
        if #available(iOS 13.0, *) {
            // Use the new scene-based approach for iOS 13+
            guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
                  let window = windowScene.windows.first else {
                return nil
            }
            return window.rootViewController
        } else {
            // Fallback for iOS 12 and earlier
            return UIApplication.shared.keyWindow?.rootViewController
        }
    }
}

// MARK: - Delegate Classes

private class RewardedVideoAdDelegate: NSObject, FBRewardedVideoAdDelegate {
    private let callback: MetaAds.AdLoadCallback
    
    init(callback: @escaping MetaAds.AdLoadCallback) {
        self.callback = callback
    }
    
    func rewardedVideoAdDidLoad(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("MetaAds: Rewarded video loaded successfully")
        callback(true, nil)
    }
    
    func rewardedVideoAd(_ rewardedVideoAd: FBRewardedVideoAd, didFailWithError error: Error) {
        print("MetaAds: Rewarded video failed to load: \(error.localizedDescription)")
        callback(false, "Failed to load rewarded video: \(error.localizedDescription)")
    }
    
    func rewardedVideoAdDidClick(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("MetaAds: Rewarded video clicked")
    }
    
    func rewardedVideoAdWillLogImpression(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("MetaAds: Rewarded video impression logged")
    }
}

private class RewardedVideoShowDelegate: NSObject, FBRewardedVideoAdDelegate {
    private let callback: MetaAds.RewardedVideoCallback
    
    init(callback: @escaping MetaAds.RewardedVideoCallback) {
        self.callback = callback
    }
    
    func rewardedVideoAdDidLoad(_ rewardedVideoAd: FBRewardedVideoAd) {
        // Not called during show
    }
    
    func rewardedVideoAd(_ rewardedVideoAd: FBRewardedVideoAd, didFailWithError error: Error) {
        print("MetaAds: Error showing rewarded video: \(error.localizedDescription)")
        callback(false, nil)
    }
    
    func rewardedVideoAdDidClick(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("MetaAds: Rewarded video clicked during show")
    }
    
    func rewardedVideoAdWillLogImpression(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("MetaAds: Rewarded video shown")
    }
    
    func rewardedVideoAdVideoComplete(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("MetaAds: Rewarded video completed - reward earned")
        let reward = ["type": "coins", "amount": 1] as [String: Any]
        callback(true, reward)
    }
    
    func rewardedVideoAdDidClose(_ rewardedVideoAd: FBRewardedVideoAd) {
        print("MetaAds: Rewarded video closed")
        callback(false, nil)
    }
}

private class InterstitialAdDelegate: NSObject, FBInterstitialAdDelegate {
    private let callback: MetaAds.AdLoadCallback
    
    init(callback: @escaping MetaAds.AdLoadCallback) {
        self.callback = callback
    }
    
    func interstitialAdDidLoad(_ interstitialAd: FBInterstitialAd) {
        print("MetaAds: Interstitial loaded successfully")
        callback(true, nil)
    }
    
    func interstitialAd(_ interstitialAd: FBInterstitialAd, didFailWithError error: Error) {
        print("MetaAds: Interstitial failed to load: \(error.localizedDescription)")
        callback(false, "Failed to load interstitial: \(error.localizedDescription)")
    }
    
    func interstitialAdDidClick(_ interstitialAd: FBInterstitialAd) {
        print("MetaAds: Interstitial clicked")
    }
    
    func interstitialAdWillLogImpression(_ interstitialAd: FBInterstitialAd) {
        print("MetaAds: Interstitial impression logged")
    }
}

private class InterstitialShowDelegate: NSObject, FBInterstitialAdDelegate {
    private let callback: MetaAds.InterstitialCallback
    
    init(callback: @escaping MetaAds.InterstitialCallback) {
        self.callback = callback
    }
    
    func interstitialAdDidLoad(_ interstitialAd: FBInterstitialAd) {
        // Not called during show
    }
    
    func interstitialAd(_ interstitialAd: FBInterstitialAd, didFailWithError error: Error) {
        print("MetaAds: Error showing interstitial: \(error.localizedDescription)")
        callback(false)
    }
    
    func interstitialAdDidClick(_ interstitialAd: FBInterstitialAd) {
        print("MetaAds: Interstitial clicked during show")
    }
    
    func interstitialAdWillLogImpression(_ interstitialAd: FBInterstitialAd) {
        print("MetaAds: Interstitial shown")
        callback(true)
    }
    
    func interstitialAdDidClose(_ interstitialAd: FBInterstitialAd) {
        print("MetaAds: Interstitial closed")
    }
}