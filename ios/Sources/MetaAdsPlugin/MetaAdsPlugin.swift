import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(MetaAdsPlugin)
public class MetaAdsPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "MetaAdsPlugin"
    public let jsName = "MetaAds"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "initialize", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "loadRewardedVideo", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "showRewardedVideo", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "isRewardedVideoLoaded", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "loadInterstitial", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "showInterstitial", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "isInterstitialLoaded", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "setTestMode", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "addTestDevice", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = MetaAds()

    @objc func initialize(_ call: CAPPluginCall) {
        guard let appId = call.getString("appId") else {
            call.reject("App ID is required")
            return
        }
        
        let testMode = call.getBool("testMode") ?? false
        
        implementation.initialize(appId: appId, testMode: testMode) { success, error in
            if success {
                call.resolve()
            } else {
                call.reject(error ?? "Unknown initialization error")
            }
        }
    }
    
    @objc func loadRewardedVideo(_ call: CAPPluginCall) {
        guard let placementId = call.getString("placementId") else {
            call.reject("Placement ID is required")
            return
        }
        
        implementation.loadRewardedVideo(placementId: placementId) { success, error in
            if success {
                call.resolve()
            } else {
                call.reject(error ?? "Unknown error loading rewarded video")
            }
        }
    }
    
    @objc func showRewardedVideo(_ call: CAPPluginCall) {
        implementation.showRewardedVideo { success, reward in
            var result: [String: Any] = ["success": success]
            if let reward = reward {
                result["reward"] = reward
            }
            call.resolve(result)
        }
    }
    
    @objc func isRewardedVideoLoaded(_ call: CAPPluginCall) {
        call.resolve(["loaded": false])
    }
    
    @objc func loadInterstitial(_ call: CAPPluginCall) {
        guard let placementId = call.getString("placementId") else {
            call.reject("Placement ID is required")
            return
        }
        
        implementation.loadInterstitial(placementId: placementId) { success, error in
            if success {
                call.resolve()
            } else {
                call.reject(error ?? "Unknown error loading interstitial")
            }
        }
    }
    
    @objc func showInterstitial(_ call: CAPPluginCall) {
        implementation.showInterstitial { success in
            call.resolve(["success": success])
        }
    }
    
    @objc func isInterstitialLoaded(_ call: CAPPluginCall) {
        call.resolve(["loaded": false])
    }
    
    @objc func setTestMode(_ call: CAPPluginCall) {
        let enabled = call.getBool("enabled") ?? false
        implementation.setTestMode(enabled: enabled)
        call.resolve()
    }
    
    @objc func addTestDevice(_ call: CAPPluginCall) {
        if let deviceId = call.getString("deviceId") {
            implementation.addTestDevice(deviceId: deviceId)
        }
        call.resolve()
    }
}
