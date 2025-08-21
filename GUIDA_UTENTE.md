# 🚀 Guida Semplice - Capacitor Meta Ads Plugin

Guida rapida per integrare gli annunci Meta nella tua app Capacitor.

## 📋 Prima di Iniziare

Devi avere:
- Account [Meta Business Manager](https://business.facebook.com/)
- App registrata su Meta Audience Network
- Meta App ID e Placement IDs

## 🔧 Installazione

### 1. Installa il Plugin
```bash
npm install capacitor-meta-ads
npx cap sync
```

### 2. Configura Android
Aggiungi nel file `android/app/src/main/AndroidManifest.xml` dentro `<application>`:

```xml
<!-- Sostituisci TUO_META_APP_ID con il tuo vero App ID -->
<meta-data
    android:name="com.facebook.sdk.ApplicationId"
    android:value="TUO_META_APP_ID" />
    
<activity
    android:name="com.facebook.ads.AudienceNetworkActivity"
    android:configChanges="keyboardHidden|orientation|screenSize"
    android:exported="false"
    android:theme="@android:style/Theme.Translucent.NoTitleBar" />
    
<provider
    android:name="com.facebook.ads.AudienceNetworkContentProvider"
    android:authorities="${applicationId}.AudienceNetworkContentProvider"
    android:exported="false" />
```

### 3. Configura iOS
Aggiungi nel file `ios/App/App/Info.plist`:

```xml
<key>FacebookAppID</key>
<string>TUO_META_APP_ID</string>
<key>FacebookClientToken</key>
<string>TUO_CLIENT_TOKEN</string>
```

## 💻 Utilizzo Base

### 1. Importa il Plugin
```typescript
import { MetaAds } from 'capacitor-meta-ads';
```

### 2. Inizializza
```typescript
await MetaAds.initialize({
  appId: 'TUO_META_APP_ID',
  testMode: true // false in produzione
});
```

### 3. Mostra Rewarded Video
```typescript
// Carica
await MetaAds.loadRewardedVideo({
  placementId: 'TUO_PLACEMENT_ID'
});

// Mostra
const result = await MetaAds.showRewardedVideo();
if (result.success) {
  console.log('Ricompensa ottenuta!');
}
```

### 4. Mostra Interstitial
```typescript
// Carica
await MetaAds.loadInterstitial({
  placementId: 'TUO_PLACEMENT_ID'
});

// Mostra
const result = await MetaAds.showInterstitial();
```

## 🧪 Test

Durante lo sviluppo usa sempre:
- `testMode: true`
- Placement IDs di test Meta
- Aggiungi il tuo dispositivo come test device

## 🚀 Produzione

Prima di pubblicare:
- [ ] Sostituisci con veri App ID e Placement IDs
- [ ] Imposta `testMode: false`
- [ ] Testa su dispositivi reali
- [ ] Verifica approvazione app da Meta

## ❗ Problemi Comuni

**"Meta Audience Network not initialized"**
→ Chiama sempre `initialize()` prima

**"No ads available"**
→ Usa modalità test durante sviluppo

**"Placement ID is required"**
→ Verifica che i placement IDs non siano vuoti

## 📚 Link Utili

- [Meta Business Manager](https://business.facebook.com/)
- [Meta Audience Network Docs](https://developers.facebook.com/docs/audience-network/)
- [Plugin GitHub](https://github.com/eliazv/capacitor-meta-ads)

---

**🎯 Fatto! Il tuo plugin Meta Ads è pronto!**
