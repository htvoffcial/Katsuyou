# AdGuard DNS 自動切替 Android

iOS の `com.apple.dnsSettings.managed` プロファイルに近い用途を Android の `VpnService` ベースで実装するプロジェクトです。

## 方針

- 通常時: AdGuard DNS を利用
- `NETGG-BYOD` 接続時: 自動停止
- Android では一般アプリから Private DNS を直接切り替えられないため `VpnService` を利用
- DNS 上流は `dns.adguard-dns.com:853` (DNS over TLS)

## 注意

このサンプルは「自動切替ポリシー」と DoT 上流への問い合わせ部分を含みます。
`VpnService` で全DNSパケットをTUNへ取り込み、元の送信元へ正しいIP/UDP応答を再注入するには、完全なパケット転送/NAT層が必要です。本リポジトリはその部分を簡略化しています。

また、Androidのバージョン・端末メーカーによってSSID取得に必要な権限や挙動が異なります。

## GitHub Actions

`.github/workflows/build-apk.yml` が `app-debug.apk` をArtifactとして公開します。

GitHub:
Actions → Build APK → Run workflow

成功後:
Artifacts → `adguard-dns-debug-apk`
