// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorMetaAds",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "CapacitorMetaAds",
            targets: ["MetaAdsPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "6.0.0")
    ],
    targets: [
        .target(
            name: "MetaAdsPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/MetaAdsPlugin"),
        .testTarget(
            name: "MetaAdsPluginTests",
            dependencies: ["MetaAdsPlugin"],
            path: "ios/Tests/MetaAdsPluginTests")
    ]
)