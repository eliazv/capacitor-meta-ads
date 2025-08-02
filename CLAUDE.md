# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Capacitor plugin for Meta Audience Network ads integration (`capacitor-meta-ads`). The plugin provides support for rewarded video and interstitial ads with comprehensive Android implementation and planned iOS support.

## Essential Commands

### Development Commands
- `npm run build` - Build plugin, compile TypeScript to ESM, and generate documentation
- `npm run clean` - Remove dist directory
- `npm run watch` - Watch TypeScript files for changes
- `npm run docgen` - Generate API documentation in README.md and dist/docs.json

### Code Quality Commands
- `npm run lint` - Run ESLint on TypeScript files and check formatting
- `npm run fmt` - Auto-fix ESLint issues and format code with Prettier
- `npm run eslint` - Run ESLint only
- `npm run prettier` - Format code with Prettier
- `npm run swiftlint` - Run SwiftLint on iOS code

### Platform Verification
- `npm run verify` - Build and validate all platforms (web, Android, iOS)
- `npm run verify:android` - Test Android build with `cd android && ./gradlew clean build test`
- `npm run verify:ios` - Test iOS build with xcodebuild
- `npm run verify:web` - Verify web build

### Publishing
- `npm run prepublishOnly` - Prepare plugin for publishing (runs automatically before npm publish)

## Code Architecture

### Core Structure
- **src/definitions.ts**: TypeScript interfaces defining the plugin API (`MetaAdsPlugin`, `RewardInfo`, `MetaAdsEvent`)
- **src/index.ts**: Main plugin export using Capacitor's `registerPlugin`
- **src/web.ts**: Web implementation (mock/no-op for Meta Audience Network)

### Platform Implementations
- **Android**: Full native implementation in `android/src/main/java/com/eliazavatta/plugins/metaads/`
  - `MetaAdsPlugin.java`: Capacitor plugin bridge with `@PluginMethod` annotations
  - `MetaAds.java`: Native Android implementation using Meta Audience Network SDK
- **iOS**: Basic structure in `ios/Sources/MetaAdsPlugin/` (currently incomplete/placeholder)

### Plugin API Design
The plugin follows Capacitor patterns with:
- Async/Promise-based methods for ad loading and showing
- Callback interfaces for handling ad events and state changes
- Error handling through Promise rejection
- State checking methods (isRewardedVideoLoaded, isInterstitialLoaded)

### Build System
- **TypeScript**: Compiles to ESM format in `dist/esm/`
- **Rollup**: Bundles into IIFE (`dist/plugin.js`) and CommonJS (`dist/plugin.cjs.js`) formats
- **Capacitor**: Native platform builds use gradle (Android) and xcodebuild (iOS)

### Key Features Implemented
- Meta Audience Network SDK initialization with test mode support
- Rewarded video ads with reward callback system
- Interstitial ads with success/failure callbacks
- Test device management for development
- Comprehensive error handling and logging

### Android-Specific Notes
- Uses Meta Audience Network SDK 6.17.0+
- Requires Meta App ID configuration in AndroidManifest.xml
- Implements proper ad lifecycle management and memory cleanup
- Supports Meta's test placement IDs for development

### Development Patterns
- Uses Ionic/Capacitor code style (ESLint config: `@ionic/eslint-config/recommended`)
- Prettier formatting with `@ionic/prettier-config`
- SwiftLint for iOS code consistency
- Comprehensive JSDoc comments for API documentation generation