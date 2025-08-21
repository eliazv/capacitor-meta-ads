# 🚀 Guida Completa - Capacitor Meta Ads Plugin

Questa guida ti accompagnerà passo-passo nell'integrazione degli annunci Meta (Facebook) Audience Network nella tua app Capacitor.

## 📋 Prerequisiti

### 1. Account Meta Business Manager
- Account [Meta Business Manager](https://business.facebook.com/) attivo
- App registrata su Meta Audience Network
- Placement IDs per rewarded video e interstitial
- App approvata da Meta (per produzione)

### 2. Ambiente di Sviluppo
- **Capacitor**: 6.x o superiore
- **Android**: API Level 21+ (Android 5.0+)
- **iOS**: 15.0+ 
- **Node.js**: 16+ 

## 🔧 Installazione

### Passo 1: Installa il Plugin
```bash
npm install capacitor-meta-ads
npx cap sync
```

### Passo 2: Configura Android

#### android/app/src/main/AndroidManifest.xml
```xml
<application>
    <!-- Meta App ID - SOSTITUISCI CON IL TUO -->
    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="TUO_META_APP_ID" />
        
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

### Passo 3: Configura iOS

#### ios/App/App/Info.plist
```xml
<key>FacebookAppID</key>
<string>TUO_META_APP_ID</string>
<key>FacebookClientToken</key>
<string>TUO_CLIENT_TOKEN</string>
```

## 💻 Implementazione Codice

### Servizio Base
```typescript
import { MetaAds } from 'capacitor-meta-ads';

export class MetaAdsService {
  private isInitialized = false;
  
  // IDs di test - SOSTITUISCI CON I TUOI IN PRODUZIONE
  private readonly REWARDED_PLACEMENT_ID = 'VID_HD_16_9_46S_APP_INSTALL#TUO_PLACEMENT_ID';
  private readonly INTERSTITIAL_PLACEMENT_ID = 'IMG_16_9_APP_INSTALL#TUO_PLACEMENT_ID';
  
  async initialize() {
    try {
      await MetaAds.initialize({
        appId: 'TUO_META_APP_ID', // SOSTITUISCI
        testMode: true // false in produzione
      });
      
      // Aggiungi dispositivo di test (opzionale)
      await MetaAds.addTestDevice({ 
        deviceId: 'TUO_DEVICE_ID_TEST' 
      });
      
      this.isInitialized = true;
      console.log('✅ Meta Ads inizializzato');
    } catch (error) {
      console.error('❌ Errore inizializzazione:', error);
      throw error;
    }
  }
  
  async showRewardedVideo(): Promise<boolean> {
    if (!this.isInitialized) {
      throw new Error('Meta Ads non inizializzato');
    }
    
    try {
      // 1. Carica annuncio
      await MetaAds.loadRewardedVideo({
        placementId: this.REWARDED_PLACEMENT_ID
      });
      
      // 2. Verifica caricamento
      const { loaded } = await MetaAds.isRewardedVideoLoaded();
      if (!loaded) {
        throw new Error('Annuncio non disponibile');
      }
      
      // 3. Mostra annuncio
      const result = await MetaAds.showRewardedVideo();
      
      if (result.success && result.reward) {
        console.log('🎉 Ricompensa:', result.reward);
        this.handleReward(result.reward);
        return true;
      }
      
      return false;
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
      await MetaAds.loadInterstitial({
        placementId: this.INTERSTITIAL_PLACEMENT_ID
      });
      
      const { loaded } = await MetaAds.isInterstitialLoaded();
      if (!loaded) {
        throw new Error('Annuncio non disponibile');
      }
      
      const result = await MetaAds.showInterstitial();
      return result.success;
    } catch (error) {
      console.error('❌ Errore interstitial:', error);
      throw error;
    }
  }
  
  private handleReward(reward: any) {
    // Implementa qui la logica per la ricompensa
    console.log(`🎁 Ricompensa: ${reward.amount} ${reward.type}`);
    // Esempio: aggiungi monete, vite, punti, etc.
  }
}
```

### Utilizzo in Componenti

#### Angular
```typescript
import { Component, OnInit } from '@angular/core';
import { MetaAdsService } from './meta-ads.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html'
})
export class GameComponent implements OnInit {
  private adsService = new MetaAdsService();
  
  async ngOnInit() {
    try {
      await this.adsService.initialize();
    } catch (error) {
      console.error('Errore inizializzazione ads:', error);
    }
  }
  
  async onWatchAdForReward() {
    try {
      const rewarded = await this.adsService.showRewardedVideo();
      if (rewarded) {
        this.showMessage('Ricompensa ottenuta!');
      }
    } catch (error) {
      this.showMessage('Annuncio non disponibile');
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

#### React
```tsx
import React, { useEffect, useState } from 'react';
import { MetaAdsService } from './MetaAdsService';

const GameComponent: React.FC = () => {
  const [adsService] = useState(new MetaAdsService());
  const [isInitialized, setIsInitialized] = useState(false);
  
  useEffect(() => {
    const initAds = async () => {
      try {
        await adsService.initialize();
        setIsInitialized(true);
      } catch (error) {
        console.error('Errore inizializzazione ads:', error);
      }
    };
    
    initAds();
  }, []);
  
  const handleWatchAd = async () => {
    if (!isInitialized) return;
    
    try {
      const rewarded = await adsService.showRewardedVideo();
      if (rewarded) {
        alert('Ricompensa ottenuta!');
      }
    } catch (error) {
      alert('Annuncio non disponibile');
    }
  };
  
  return (
    <div>
      <button onClick={handleWatchAd} disabled={!isInitialized}>
        Guarda Annuncio per Ricompensa
      </button>
    </div>
  );
};
```

## 🧪 Test e Debug

### Modalità Test
Durante lo sviluppo, usa sempre la modalità test:

```typescript
await MetaAds.initialize({
  appId: 'TUO_APP_ID',
  testMode: true // IMPORTANTE: true durante sviluppo
});
```

### Placement IDs di Test
Meta fornisce placement IDs di test:
- **Rewarded Video**: `VID_HD_16_9_46S_APP_INSTALL#TUO_PLACEMENT_ID`
- **Interstitial**: `IMG_16_9_APP_INSTALL#TUO_PLACEMENT_ID`

### Dispositivi di Test
Aggiungi il tuo dispositivo come test device:

```typescript
await MetaAds.addTestDevice({ 
  deviceId: 'TUO_DEVICE_ID' 
});
```

**Come trovare il Device ID:**
- **Android**: Controlla i log di Android Studio
- **iOS**: Controlla i log di Xcode

## 🚀 Produzione

### Checklist Pre-Pubblicazione
- [ ] Sostituisci `TUO_META_APP_ID` con l'App ID reale
- [ ] Sostituisci i placement IDs di test con quelli reali
- [ ] Imposta `testMode: false`
- [ ] Rimuovi `addTestDevice`
- [ ] Testa su dispositivi reali
- [ ] Verifica approvazione app da Meta
- [ ] Controlla policy Meta Audience Network

### Configurazione Produzione
```typescript
await MetaAds.initialize({
  appId: 'IL_TUO_VERO_APP_ID',
  testMode: false // IMPORTANTE: false in produzione
});
```

## ❗ Troubleshooting

### Errori Comuni

#### "Meta Audience Network not initialized"
**Soluzione**: Chiama sempre `initialize()` prima di altri metodi

#### "No ads available"
**Soluzioni**:
- Usa modalità test durante sviluppo
- Verifica connessione internet
- Controlla placement IDs
- Verifica approvazione app Meta

#### "Placement ID is required"
**Soluzione**: Verifica che i placement IDs non siano vuoti

#### Android: "SDK location not found"
**Soluzione**: Configura Android SDK nel tuo ambiente di sviluppo

#### iOS: Build errors
**Soluzioni**:
- Verifica iOS deployment target (15.0+)
- Esegui `pod install` nella cartella ios
- Pulisci build cache Xcode

### Log di Debug
Abilita i log per debug:

```typescript
// I log sono automaticamente abilitati in modalità test
console.log('Meta Ads Debug: abilitato in testMode');
```

## 📚 Risorse Utili

- [Meta Business Manager](https://business.facebook.com/)
- [Meta Audience Network Docs](https://developers.facebook.com/docs/audience-network/)
- [Capacitor Documentation](https://capacitorjs.com/docs)
- [Plugin GitHub Repository](https://github.com/eliazv/capacitor-meta-ads)

## 🆘 Supporto

Se hai problemi:
1. Controlla questa guida
2. Verifica il file `TROUBLESHOOTING.md`
3. Controlla i log di debug
4. Apri un issue su GitHub

---

**🎯 Ora il tuo plugin Meta Ads è pronto per generare revenue dalla tua app Capacitor!**
