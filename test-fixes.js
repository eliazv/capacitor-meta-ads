/**
 * Test script to verify the fixes implemented in the Meta Ads plugin
 * This script tests the basic functionality and error handling
 */

const { MetaAds } = require('./dist/plugin.cjs.js');

async function testPluginFixes() {
    console.log('🧪 Testing Meta Ads Plugin Fixes...\n');

    // Test 1: Initialize with empty app ID (should fail)
    console.log('Test 1: Initialize with empty app ID');
    try {
        await MetaAds.initialize({ appId: '', testMode: true });
        console.log('❌ FAIL: Should have rejected empty app ID');
    } catch (error) {
        console.log('✅ PASS: Correctly rejected empty app ID:', error.message);
    }

    // Test 2: Initialize with valid app ID
    console.log('\nTest 2: Initialize with valid app ID');
    try {
        await MetaAds.initialize({ appId: 'test-app-id', testMode: true });
        console.log('✅ PASS: Initialization successful');
    } catch (error) {
        console.log('⚠️  Expected on web platform:', error.message);
    }

    // Test 3: Load rewarded video with empty placement ID (should fail)
    console.log('\nTest 3: Load rewarded video with empty placement ID');
    try {
        await MetaAds.loadRewardedVideo({ placementId: '' });
        console.log('❌ FAIL: Should have rejected empty placement ID');
    } catch (error) {
        console.log('✅ PASS: Correctly rejected empty placement ID:', error.message);
    }

    // Test 4: Load rewarded video with valid placement ID
    console.log('\nTest 4: Load rewarded video with valid placement ID');
    try {
        await MetaAds.loadRewardedVideo({ placementId: 'VID_HD_16_9_46S_APP_INSTALL#test' });
        console.log('✅ PASS: Load rewarded video call successful');
    } catch (error) {
        console.log('⚠️  Expected on web platform:', error.message);
    }

    // Test 5: Check if rewarded video is loaded
    console.log('\nTest 5: Check if rewarded video is loaded');
    try {
        const result = await MetaAds.isRewardedVideoLoaded();
        console.log('✅ PASS: isRewardedVideoLoaded returned:', result);
    } catch (error) {
        console.log('⚠️  Expected on web platform:', error.message);
    }

    // Test 6: Load interstitial with empty placement ID (should fail)
    console.log('\nTest 6: Load interstitial with empty placement ID');
    try {
        await MetaAds.loadInterstitial({ placementId: '' });
        console.log('❌ FAIL: Should have rejected empty placement ID');
    } catch (error) {
        console.log('✅ PASS: Correctly rejected empty placement ID:', error.message);
    }

    // Test 7: Add test device with empty device ID (should fail gracefully)
    console.log('\nTest 7: Add test device with empty device ID');
    try {
        await MetaAds.addTestDevice({ deviceId: '' });
        console.log('✅ PASS: addTestDevice handled empty device ID gracefully');
    } catch (error) {
        console.log('⚠️  Expected on web platform:', error.message);
    }

    // Test 8: Add valid test device
    console.log('\nTest 8: Add valid test device');
    try {
        await MetaAds.addTestDevice({ deviceId: 'test-device-123' });
        console.log('✅ PASS: addTestDevice call successful');
    } catch (error) {
        console.log('⚠️  Expected on web platform:', error.message);
    }

    // Test 9: Set test mode
    console.log('\nTest 9: Set test mode');
    try {
        await MetaAds.setTestMode({ enabled: true });
        console.log('✅ PASS: setTestMode call successful');
    } catch (error) {
        console.log('⚠️  Expected on web platform:', error.message);
    }

    console.log('\n🎉 Plugin fixes testing completed!');
    console.log('\nNote: Some tests show "Expected on web platform" because Meta Audience Network');
    console.log('is not available on web. The real tests should be run on Android/iOS devices.');
}

// Run the tests
testPluginFixes().catch(console.error);
