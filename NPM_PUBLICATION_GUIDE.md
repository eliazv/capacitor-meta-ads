# 📦 NPM Publication Guide - capacitor-meta-ads

## Pre-Publication Checklist

### ✅ 1. Code Quality & Testing
```bash
# Run all quality checks
npm run lint
npm run build
npm run verify:web

# Check TypeScript compilation
tsc --noEmit

# Format code
npm run fmt
```

### ✅ 2. Version Management
```bash
# Update version in package.json (already done: 1.0.0)
# For future updates:
npm version patch   # 1.0.1
npm version minor   # 1.1.0  
npm version major   # 2.0.0
```

### ✅ 3. Documentation Check
- ✅ README.md complete with examples
- ✅ API documentation auto-generated
- ✅ CONTRIBUTING.md present
- ✅ TROUBLESHOOTING.md present
- ✅ EXAMPLE_USAGE.md present

### ✅ 4. Package.json Verification
```json
{
  "name": "capacitor-meta-ads",
  "version": "1.0.0",
  "description": "Capacitor plugin for Meta Audience Network ads integration",
  "main": "dist/plugin.cjs.js",
  "module": "dist/esm/index.js",
  "types": "dist/esm/index.d.ts",
  "files": [
    "android/",
    "ios/",
    "dist/",
    "*.md",
    "*.podspec",
    "Package.swift"
  ]
}
```

## Publication Steps

### 🔐 1. NPM Account Setup
```bash
# Login to NPM (first time only)
npm login

# Verify login
npm whoami
```

### 🏗️ 2. Build for Production
```bash
# Clean and build
npm run clean
npm run build

# Verify dist folder contents
ls dist/
# Should contain:
# - esm/         (TypeScript compiled to ESM)
# - plugin.js    (UMD bundle)  
# - plugin.cjs.js (CommonJS bundle)
# - docs.json    (API documentation)
```

### 📋 3. Test Package Contents
```bash
# Preview what will be published
npm pack --dry-run

# Actually create tarball (optional)
npm pack
# Creates: capacitor-meta-ads-1.0.0.tgz
```

### 🚀 4. Publish to NPM
```bash
# Publish to NPM registry
npm publish

# For scoped packages (if needed):
npm publish --access public
```

### 🏷️ 5. Tag Git Release
```bash
# Tag the release
git tag v1.0.0
git push origin v1.0.0

# Or create GitHub release through web interface
```

## Post-Publication

### 📊 1. Verify Publication
```bash
# Check package on NPM
npm view capacitor-meta-ads

# Test installation
npm install capacitor-meta-ads
```

### 📝 2. Update GitHub
- Create GitHub release with changelog
- Update repository description
- Add topics/tags: `capacitor`, `meta`, `ads`, `facebook`

### 📢 3. Announce Release
- Update project documentation
- Notify users/community
- Consider blog post or social media

## Maintenance Releases

### 🔄 Patch Release (1.0.1)
```bash
# Fix bugs, update dependencies
npm version patch
npm run build
npm publish
git push origin v1.0.1
```

### ⭐ Minor Release (1.1.0) 
```bash
# Add new features, maintain backward compatibility
npm version minor
npm run build  
npm publish
git push origin v1.1.0
```

### 💥 Major Release (2.0.0)
```bash
# Breaking changes
npm version major
npm run build
npm publish
git push origin v2.0.0
```

## Troubleshooting

### ❌ Publication Fails
```bash
# Check npm login
npm whoami

# Verify package.json
npm run build

# Check for conflicts
npm view capacitor-meta-ads
```

### 🔍 Package Size Issues
```bash
# Check package size
npm pack --dry-run

# Optimize by updating .npmignore
echo "node_modules" >> .npmignore
echo "*.log" >> .npmignore
```

### 🏷️ Version Conflicts
```bash
# Check current published version
npm view capacitor-meta-ads version

# Update version appropriately
npm version patch
```

## Quality Gates

Before publishing, ensure:

- ✅ All tests pass
- ✅ Code is formatted and linted
- ✅ Documentation is updated
- ✅ Version is bumped appropriately
- ✅ Git repository is clean
- ✅ Build artifacts are fresh

## Success! 🎉

Your plugin is now published to NPM and ready for developers to use:

```bash
npm install capacitor-meta-ads
npx cap sync
```

The plugin will be available at: https://www.npmjs.com/package/capacitor-meta-ads