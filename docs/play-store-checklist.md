# Play-Store-Checkliste – Osi Player

Stand: 24. Oktober 2026. Diese Liste ergänzt die technischen Änderungen im
Repo. Alles, was mit dem Play Console zu tun hat, muss im Browser erledigt
werden.

## 1. Vor dem ersten Upload prüfen

- [ ] **Paketname** `com.osiplayer.app` ist weltweit eindeutig. Falls nicht:
      `applicationId` in `app/build.gradle.kts` ändern (vor dem ersten Upload,
      danach ist der Name dauerhaft gebunden).
- [ ] **Signing-Keystore** vorhanden und gesichert
      (`~/.android-keystores/osiplayer-upload.jks` + `keystore.properties`).
      Keystore und Passwort niemals ins Repo committen.
- [ ] **AdMob-App-ID und App-Open-Ad-Unit** in `local.properties` eintragen:
      ```
      admob.appId=ca-app-pub-XXXXXXXX~YYYYYYYY
      admob.appOpenUnitId=ca-app-pub-XXXXXXXX/ZZZZZZZZ
      ```
      Ohne Eintrag laufen die Google-Test-IDs (keine Einnahmen).
- [ ] **Datenschutz-URL** in `local.properties` eintragen:
      `privacyPolicyUrl=https://deine-domain.de/osi-player/privacy`
      Die URL muss öffentlich erreichbar sein (GitHub Pages, eigene Website).
- [ ] **Impressum / Kontakt** in `docs/privacy-policy.md` ausfüllen und hosten.
- [ ] **App-Icon 512×512** (`docs/store/app-icon-512.png`), **TV-Banner
      320×180** (`docs/store/tv-banner-320x180.png`), Feature-Grafik
      1024×500 und Screenshots (Phone + TV) vorbereiten.
- [ ] **Release-AAB** bauen: `./gradlew bundleRelease`
      und auf einem Gerät/Emulator installieren und testen.

## 2. Play Console – App anlegen

- [ ] Entwicklerkonto verifizieren (bei neuen persönlichen Konten:
      12 Tester / 14 Tage geschlossener Test vor Produktion – aktuelle
      Regel im Console prüfen).
- [ ] App erstellen, Sprache Deutsch, App-Name „Osi Player“, Kategorie
      „Video Players & Editors“.
- [ ] **App-Zugang** (App access): Demo-Modus als primären Weg angeben:
      Login → „Demo ansehen – ohne Konto“. Zusätzlich Testkonto für den
      echten Provider in die Review-Notes (nicht in den Code).
- [ ] **Werbung**: „Enthält Werbung“ = Ja, AdMob angeben.
- [ ] **Datenschutz-URL** eintragen.
- [ ] **Data Safety** ausfüllen. Die App überträgt Zugangsdaten an den vom
      Nutzer gewählten IPTV-Server (nicht an den Entwickler), speichert
      Favoriten/Verlauf lokal und nutzt AdMob. Angaben:
      - Personal info → User IDs / Passwords: für App-Funktionalität,
        Übertragung an den gewählten Anbieter, **nicht** verschlüsselt,
        wenn der Anbieter nur HTTP anbietet.
      - Device or other IDs: durch AdMob, Zweck Werbung, verschlüsselt.
      - Keine Analyse-/Crash-Daten.
      Im Zweifel die Angaben anhand des tatsächlichen Verhaltens und der
      Google-Hilfe prüfen.
- [ ] **Content Rating** ausfüllen (App ist ein Player; Nutzerinhalte
      können jugendgefährdend sein → Altersfreigabe entsprechend wählen).
- [ ] **Zielgruppe**: nicht „Kinder“.
- [ ] **News-App**: Nein. **Gesundheit/Finanzen**: Nein.

## 3. Android TV

- [ ] TV-Banner im Manifest (`android:banner`) und im Store-Eintrag.
- [ ] TV-Screenshots (1920×1080) und TV-Banner (320×180) hochladen.
- [ ] Leanback-Launcher ist vorhanden (`LEANBACK_LAUNCHER`).
- [ ] Auf einem Android-TV-Gerät/Emulator testen: D-Pad-Fokus, Playback,
      Zurück-Navigation.

## 4. Datenschutz / Recht

- [ ] `docs/privacy-policy.md` ausfüllen und als öffentliche Webseite hosten.
- [ ] In-App: Einstellungen → Rechtliches prüfen.
- [ ] Disclaimer „Osi Player stellt keine Inhalte bereit“ ist sichtbar.
- [ ] Keine hardcodierten Zugangsdaten mehr im Code (siehe Grep unten).
- [ ] `allowBackup=false` und Data-Extraction-Rules aktiv.
- [ ] Network Security Config dokumentiert Cleartext (nur für
      nutzerkonfigurierte IPTV-Server).

## 5. Werbung / Consent

- [ ] AdMob-Konto und App anlegen; App-ID + Ad-Unit-ID eintragen.
- [ ] In AdMob eine **UMP-Consent-Nachricht** für die EEA konfigurieren
      und die Datenschutz-URL hinterlegen.
- [ ] Test mit `BuildConfig.DEBUG` (Debug-Geografie EEA) durchführen:
      Consent-Dialog erscheint, danach App-Open-Ad.
- [ ] Prüfen: Ohne Consent wird keine Anzeige angefordert.
- [ ] Prüfen: Bei fehlender Anzeige startet die App sofort normal.
- [ ] Keine Interstitial-Anzeige vor dem Splash (Play-Verbot).

## 6. Release-Checks

- [ ] `./gradlew test`
- [ ] `./gradlew lintRelease`
- [ ] `./gradlew bundleRelease`
- [ ] Kein `usesCleartextTraffic="true"` mehr im Manifest.
- [ ] Keine Test-IDs im Release? (Test-IDs sind funktional, aber ohne
      Einnahmen; vor Produktion ersetzen.)
- [ ] Keine nativen 16-KB-Probleme: AAB auf `lib/` prüfen; die App nutzt
      keine eigenen `.so`-Dateien.
- [ ] VersionCode/VersionName erhöhen bei jedem Upload.

## 7. Nützliche Befehle

```bash
# Zugangsdaten-Reste finden (z. B. alte Test-Accounts)
grep -rn 'test_account' app/src || true

# Release bauen
./gradlew clean bundleRelease

# Signatur prüfen
jarsigner -verify -verbose -certs app/build/outputs/bundle/release/app-release.aab | tail
```
