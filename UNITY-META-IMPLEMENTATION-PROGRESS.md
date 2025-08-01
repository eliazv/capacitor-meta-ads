# 🚀 Unity Ads + Meta Audience Network - Piano di Implementazione

## 📋 Panoramica Generale

**Obiettivo**: Integrare Unity Ads e Meta Audience Network in app React + Capacitor per massimizzare guadagni tramite mediazione AdMob.

**Complessità**: ⭐⭐⭐ (Media-Alta) - Richiede creazione plugin nativi personalizzati
**Tempo stimato**: 4-6 ore per Android, +2-3 ore per iOS
**Priorità**: Meta Audience Network → Unity Ads

---

## 🎯 Cosa Farò Io (Sviluppo)

### ✅ FASE 1: Meta Audience Network (Android)

- [ ] **1.1** Creare plugin Capacitor `capacitor-meta-ads`
- [ ] **1.2** Aggiungere SDK Meta alle dipendenze Android
- [ ] **1.3** Implementare inizializzazione nativa
- [ ] **1.4** Implementare Rewarded Video Ads
- [ ] **1.5** Implementare Interstitial Ads
- [ ] **1.6** Esporre metodi JavaScript
- [ ] **1.7** Test e debug Android

### ⏳ FASE 2: Unity Ads (Android)

- [ ] **2.1** Creare plugin Capacitor `capacitor-unity-ads`
- [ ] **2.2** Aggiungere SDK Unity alle dipendenze Android
- [ ] **2.3** Implementare inizializzazione nativa
- [ ] **2.4** Implementare Rewarded Video Ads
- [ ] **2.5** Implementare Interstitial Ads
- [ ] **2.6** Esporre metodi JavaScript
- [ ] **2.7** Test e debug Android

### 🍎 FASE 3: iOS (Entrambi i network)

- [ ] **3.1** Configurare Meta SDK per iOS
- [ ] **3.2** Configurare Unity SDK per iOS
- [ ] **3.3** Implementare codice nativo iOS
- [ ] **3.4** Test su iOS

---

## 🛠️ Cosa Devi Fare Tu (Configurazione)

### 📱 Configurazione AdMob/Meta/Unity

1. **Meta Audience Network**:

   - Vai su [Facebook Business](https://business.facebook.com/)
   - Crea/configura app in Meta Audience Network
   - Ottieni `App ID` Meta
   - Aggiungi app alla mediazione AdMob

2. **Unity Ads**:

   - Vai su [Unity Dashboard](https://dashboard.unity3d.com/)
   - Crea progetto Unity Ads
   - Ottieni `Game ID` Unity
   - Aggiungi Unity alla mediazione AdMob

3. **AdMob Mediazione**:
   - Configura gruppi di mediazione
   - Imposta eCPM per Meta e Unity
   - Testa configurazione mediazione

### 🔧 Setup Tecnico (Dopo che creo i plugin)

1. **Installazione Plugin**:

   ```bash
   npm install ./plugins/capacitor-meta-ads
   npm install ./plugins/capacitor-unity-ads
   npx cap sync android
   ```

2. **Configurazione App**:
   - Aggiungere App ID nei file di configurazione
   - Importare plugin in TypeScript
   - Implementare chiamate agli annunci

### 📋 File di Configurazione da Modificare

- `android/app/build.gradle` (aggiunto automaticamente dai plugin)
- `capacitor.config.ts` (configurazione plugin)
- File TypeScript dell'app (import e utilizzo)

---

## 🎮 Come Funzionerà

### JavaScript/TypeScript Usage

```typescript
import { MetaAds } from "capacitor-meta-ads";
import { UnityAds } from "capacitor-unity-ads";

// Meta Rewarded
await MetaAds.loadRewardedVideo({ placementId: "YOUR_PLACEMENT_ID" });
await MetaAds.showRewardedVideo();

// Unity Interstitial
await UnityAds.loadInterstitial({ placementId: "YOUR_PLACEMENT_ID" });
await UnityAds.showInterstitial();
```

### Integrazione con AdMob Mediazione

- I plugin gestiranno gli annunci nativamente
- AdMob sceglierà automaticamente la rete con eCPM più alto
- Fallback automatico se una rete non ha annunci disponibili

---

## ⚠️ Considerazioni Importanti

### Complessità

- **Media-Alta**: Richiede conoscenza di sviluppo nativo Android/iOS
- **Plugin personalizzati**: Non esistono plugin Capacitor ufficiali
- **Testing**: Necessario testare su dispositivi reali per annunci

### Rischi

- Possibili conflitti tra SDK diversi
- Configurazione mediazione complessa
- Policy compliance (GDPR, COPPA, etc.)

### Benefici

- **+30-50% guadagni** rispetto a solo AdMob
- Competizione tra reti = eCPM più alti
- Fallback automatico se una rete fallisce

---

## 📊 Metriche di Successo

- [ ] Plugin compilano senza errori
- [ ] Annunci Meta si caricano e mostrano
- [ ] Annunci Unity si caricano e mostrano
- [ ] Mediazione AdMob funziona correttamente
- [ ] eCPM aumenta rispetto a solo AdMob
- [ ] Nessun crash o memory leak

---

## 🚀 Prossimi Passi

1. **Ora**: Inizio sviluppo plugin Meta Audience Network
2. **Tu**: Prepara App ID Meta e Unity per test
3. **Dopo Meta**: Sviluppo plugin Unity Ads
4. **Test**: Verifica funzionamento su dispositivo Android
5. **iOS**: Estensione a iOS se tutto funziona su Android

---

---

## 🔍 VALUTAZIONE PLUGIN ESISTENTI

### Plugin Unity Ads: `@openanime/capacitor-plugin-unityads`

**✅ PRO:**

- Plugin dedicato per Unity Ads
- Supporta eventi (initialized, adLoaded, adShown, etc.)
- API semplice e chiara
- TypeScript support

**❌ CONTRO:**

- ⚠️ **BETA VERSION** (0.0.1) - Solo interstitial, no rewarded
- ⚠️ **2 anni senza aggiornamenti** (ultima pubblicazione 2022)
- ⚠️ **iOS ancora in sviluppo**
- ⚠️ **Solo 1 download settimanale** - scarso utilizzo
- Repository link non funzionante (google.com.git)

**🔴 VERDETTO**: NON RACCOMANDATO - Plugin abbandonato e incompleto

### Plugin Meta Audience: `@lingash/capacitor-meta-audience-network`

**✅ PRO:**

- Plugin recente (6 mesi fa)
- Supporta Banner + Interstitial
- API semplice e funzionale
- Repository GitHub attivo
- Test devices support
- TypeScript support

**❌ CONTRO:**

- ⚠️ **Solo Android** (no iOS)
- ⚠️ **0 download settimanali** - nuovo/poco utilizzato
- ⚠️ **No Rewarded Ads** - solo Banner e Interstitial
- ⚠️ **No eventi/callback** - meno controllo

**🟡 VERDETTO**: UTILIZZABILE ma con limitazioni

---

## 🎯 RACCOMANDAZIONE FINALE

**OPZIONE A - Plugin Esistente (Parziale):**

- Usare `@lingash/capacitor-meta-audience-network` per Meta
- Creare plugin Unity Ads personalizzato (quello esistente è abbandonato)
- ⏱️ Tempo: ~2-3 ore

**OPZIONE B - Plugin Personalizzati (Completa):**

- Creare entrambi i plugin da zero
- Controllo totale su funzionalità e eventi
- Supporto completo Rewarded + Interstitial
- ⏱️ Tempo: ~4-5 ore

**🚀 RACCOMANDAZIONE**: **OPZIONE B** - Plugin personalizzati

- Maggiore controllo e flessibilità
- Supporto completo per tutti i tipi di annunci
- Possibilità di aggiungere iOS in futuro
- Codice ottimizzato per AdMob mediation

---

**Status**: 🟡 Valutazione Completata - Pronto per sviluppo
**Ultimo aggiornamento**: 01/08/2025
