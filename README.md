# 📱 Auftragsverwaltung-App (Reparatur-Management)

Eine moderne Android-App zur Verwaltung von Reparaturaufträgen, entwickelt mit Kotlin, Jetpack Compose und Material Design 3.

## 🚀 Funktionen

### 🧍 Kundenverwaltung
- Kundennamen und Telefonnummern eingeben
- Automatisch generierte eindeutige Kunden-ID
- Kundenarchiv mit Suchfunktion
- Bearbeitung von Telefonnummern
- uvm.

### 🛠️ Reparaturleistungen
- Auswahl verschiedener Reparaturarten
- Automatische Preisberechnung
- Verknüpfung mit Kundenprofilen
- Speicherung von Aufträgen
- uvm.

## 🧪 Screenshots
Ansicht aller Aufträge  
![Auftragsscreen](https://github.com/user-attachments/assets/8a0e3105-639d-4684-a5c9-e34b2ffdf240)  
Eingabe von Aufträgen  
![Auftrag Eingabe](https://github.com/user-attachments/assets/484d62e9-137f-4bac-af3a-5ef55352529e)  
Abschließen des Auftrags mit Versendung automatisch generierter SMS  
![Abschließen des Auftrags per SMS](https://github.com/user-attachments/assets/88b67c55-cc63-4c4f-8ca5-481b5ec73eb4)  
Archiv mit Suchleiste um alle vergangenen Kunden zu finden  
![Archiv mit allen Kunden](https://github.com/user-attachments/assets/c5b75d1f-d5aa-4461-8f35-8c2c780ff97e)  
Übersicht der Historie von Kunden  
![Auflistung aller eingegagnenen Reparaturen](https://github.com/user-attachments/assets/c6a1d38e-2d44-4b24-9a1b-b13f4eb9d738)  


## ⚙️ Tech Stack

| Komponente        | Beschreibung                        |
|------------------|-------------------------------------|
| **Sprache**       | Kotlin                              |
| **UI**            | Jetpack Compose                     |
| **Design**        | Material Design 3                   |
| **Build-System**  | Gradle (Kotlin DSL)                 |

## 📦 Projektstruktur (Auszug)

```bash
├── app/                      # Android App-Quellcode
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/duhan/auftrag/
│           └── res/
├── build.gradle.kts          # Projekt-Build-Konfiguration
├── settings.gradle.kts       # Gradle-Settings
└── README.md                 # Diese Datei

```
### Geplante Features
- Responsives Design
- Option zur Modifikation der SMS-Benachrichtigungen
- UI Anpassungen
- App-Namen und Banner anpassen :)
