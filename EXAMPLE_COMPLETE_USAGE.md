# 🎯 Esempio Completo di Utilizzo - Capacitor Meta Ads

Questo esempio mostra come integrare completamente il plugin Meta Ads in una vera app Capacitor con codice funzionante e testato.

## 1. Installazione

```bash
# Installa il plugin
npm install capacitor-meta-ads
npx cap sync
```

## 2. Configurazione Android

### android/app/src/main/AndroidManifest.xml

```xml
<application>
    <!-- Meta App ID - SOSTITUISCI CON IL TUO -->
    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="YOUR_META_APP_ID" />

    <!-- Attività richieste da Meta Audience Network -->
    <activity
        android:name="com.facebook.ads.AudienceNetworkActivity"
        android:configChanges="keyboardHidden|orientation|screenSize"
        android:exported="false"
        android:theme="@android:style/Theme.Translucent.NoTitleBar" />

    <!-- Content Provider richiesto -->
    <provider
        android:name="com.facebook.ads.AudienceNetworkContentProvider"
        android:authorities="${applicationId}.AudienceNetworkContentProvider"
        android:exported="false" />
</application>
```

## 3. Configurazione iOS

### ios/App/App/Info.plist

```xml
<key>FacebookAppID</key>
<string>YOUR_META_APP_ID</string>
<key>FacebookClientToken</key>
<string>YOUR_CLIENT_TOKEN</string>
```

## 4. Codice TypeScript Completo

```typescript
import { MetaAds } from 'capacitor-meta-ads';

export class MetaAdsService {
  private isInitialized = false;

  // Placement IDs di test - SOSTITUISCI CON I TUOI IN PRODUZIONE
  private readonly TEST_REWARDED_ID = 'VID_HD_16_9_46S_APP_INSTALL#YOUR_PLACEMENT_ID';
  private readonly TEST_INTERSTITIAL_ID = 'IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID';

  async initialize() {
    try {
      await MetaAds.initialize({
        appId: 'YOUR_META_APP_ID', // SOSTITUISCI CON IL TUO
        testMode: true, // Imposta false in produzione
      });

      // Aggiungi dispositivo di test (opzionale)
      await MetaAds.addTestDevice({
        deviceId: 'YOUR_TEST_DEVICE_ID',
      });

      this.isInitialized = true;
      console.log('✅ Meta Ads inizializzato con successo');
    } catch (error) {
      console.error('❌ Errore inizializzazione Meta Ads:', error);
      throw error;
    }
  }

  async showRewardedVideo(): Promise<boolean> {
    if (!this.isInitialized) {
      throw new Error('Meta Ads non inizializzato');
    }

    try {
      // 1. Carica l'annuncio
      console.log('📱 Caricamento rewarded video...');
      await MetaAds.loadRewardedVideo({
        placementId: this.TEST_REWARDED_ID,
      });

      // 2. Verifica che sia caricato
      const { loaded } = await MetaAds.isRewardedVideoLoaded();
      if (!loaded) {
        throw new Error('Rewarded video non caricato');
      }

      // 3. Mostra l'annuncio
      console.log('🎬 Mostrando rewarded video...');
      const result = await MetaAds.showRewardedVideo();

      if (result.success && result.reward) {
        console.log('🎉 Ricompensa ottenuta:', result.reward);
        // Qui dai la ricompensa all'utente
        this.giveRewardToUser(result.reward);
        return true;
      } else {
        console.log('❌ Annuncio chiuso senza ricompensa');
        return false;
      }
    } catch (error) {
      console.error('❌ Errore rewarded video:', error);
      throw error;
    }
  }

  async showInterstitial(): Promise<boolean> {
    if (!this.isInitialized) {
      throw new Error('Meta Ads non inizializzato');
    }

    try {
      // 1. Carica l'annuncio
      console.log('📱 Caricamento interstitial...');
      await MetaAds.loadInterstitial({
        placementId: this.TEST_INTERSTITIAL_ID,
      });

      // 2. Verifica che sia caricato
      const { loaded } = await MetaAds.isInterstitialLoaded();
      if (!loaded) {
        throw new Error('Interstitial non caricato');
      }

      // 3. Mostra l'annuncio
      console.log('🎬 Mostrando interstitial...');
      const result = await MetaAds.showInterstitial();

      if (result.success) {
        console.log('✅ Interstitial mostrato con successo');
        return true;
      } else {
        console.log('❌ Errore mostrando interstitial');
        return false;
      }
    } catch (error) {
      console.error('❌ Errore interstitial:', error);
      throw error;
    }
  }

  private giveRewardToUser(reward: any) {
    // Implementa qui la logica per dare la ricompensa
    console.log(`🎁 Ricompensa: ${reward.amount} ${reward.type}`);
    // Esempio: aggiungi monete, vite, etc.
  }
}

// Utilizzo nell'app
export async function initializeAds() {
  const adsService = new MetaAdsService();
  await adsService.initialize();
  return adsService;
}
```

## 5. Esempio di Utilizzo in un Componente

```typescript
// In un componente Angular/React/Vue
export class GameComponent {
  private adsService: MetaAdsService;

  async ngOnInit() {
    this.adsService = await initializeAds();
  }

  async onWatchAdForReward() {
    try {
      const rewarded = await this.adsService.showRewardedVideo();
      if (rewarded) {
        this.showMessage('Ricompensa ottenuta!');
      } else {
        this.showMessage('Completa il video per ottenere la ricompensa');
      }
    } catch (error) {
      this.showMessage("Errore caricando l'annuncio");
    }
  }

  async onShowInterstitial() {
    try {
      await this.adsService.showInterstitial();
    } catch (error) {
      console.error('Errore interstitial:', error);
    }
  }
}
```

## 6. Checklist Pre-Produzione

Prima di pubblicare l'app:

- [ ] Sostituisci `YOUR_META_APP_ID` con il tuo vero App ID
- [ ] Sostituisci i placement ID di test con quelli reali
- [ ] Imposta `testMode: false`
- [ ] Rimuovi `addTestDevice`
- [ ] Testa su dispositivi reali
- [ ] Verifica che l'app sia approvata da Meta
- [ ] Controlla le policy di Meta Audience Network

## 7. Troubleshooting

### Errore "Meta Audience Network not initialized"

- Assicurati di chiamare `initialize()` prima di altri metodi
- Verifica che l'App ID sia corretto

### Errore "No ads available"

- Usa modalità test durante sviluppo
- Verifica che i placement ID siano corretti
- Controlla la connessione internet

### Errore "Placement ID is required"

- Verifica che i placement ID non siano vuoti o null

## 8. Link Utili

- [Meta Business Manager](https://business.facebook.com/)
- [Meta Audience Network Documentation](https://developers.facebook.com/docs/audience-network/)
- [Plugin Troubleshooting Guide](./TROUBLESHOOTING.md)
