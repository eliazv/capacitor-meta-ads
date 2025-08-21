// Test script per verificare che il plugin funzioni
const { MetaAds } = require('./dist/plugin.cjs.js');

console.log('🧪 Test Plugin Meta Ads');
console.log('========================');

// Test 1: Verifica che il plugin sia importabile
console.log('✅ Plugin importato correttamente');
console.log('MetaAds object:', typeof MetaAds);

// Test 2: Verifica che i metodi esistano
const expectedMethods = [
  'initialize',
  'loadRewardedVideo', 
  'showRewardedVideo',
  'isRewardedVideoLoaded',
  'loadInterstitial',
  'showInterstitial', 
  'isInterstitialLoaded',
  'setTestMode',
  'addTestDevice'
];

console.log('\n📋 Verifica metodi disponibili:');
expectedMethods.forEach(method => {
  if (typeof MetaAds[method] === 'function') {
    console.log(`✅ ${method}`);
  } else {
    console.log(`❌ ${method} - MANCANTE!`);
  }
});

// Test 3: Verifica che i metodi restituiscano Promise
console.log('\n🔄 Test chiamate metodi (dovrebbero restituire Promise):');

try {
  const initResult = MetaAds.initialize({ appId: 'test', testMode: true });
  console.log('✅ initialize() restituisce:', typeof initResult, initResult instanceof Promise ? '(Promise)' : '');
} catch (error) {
  console.log('⚠️  initialize() error (normale su web):', error.message);
}

try {
  const loadResult = MetaAds.loadRewardedVideo({ placementId: 'test' });
  console.log('✅ loadRewardedVideo() restituisce:', typeof loadResult, loadResult instanceof Promise ? '(Promise)' : '');
} catch (error) {
  console.log('⚠️  loadRewardedVideo() error (normale su web):', error.message);
}

console.log('\n🎉 Test completato!');
console.log('\n📝 Note:');
console.log('- Gli errori su web sono normali (Meta non supporta web)');
console.log('- Il plugin dovrebbe funzionare su Android/iOS');
console.log('- Assicurati di configurare correttamente Android/iOS');
