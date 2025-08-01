import { registerPlugin } from '@capacitor/core';

import type { MetaAdsPlugin } from './definitions';

const MetaAds = registerPlugin<MetaAdsPlugin>('MetaAds', {
  web: () => import('./web').then((m) => new m.MetaAdsWeb()),
});

export * from './definitions';
export { MetaAds };
