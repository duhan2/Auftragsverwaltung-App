# Reparatur-Management App

## Projektübersicht

Diese Android-App dient zur Verwaltung von Reparaturaufträgen. Sie ermöglicht die Erfassung von Kundendaten, die Auswahl von Reparaturleistungen, die Kalkulation von Preisen und die Speicherung von Auftragsdetails. Die App ist mit Jetpack Compose und Material Design 3 für eine moderne und intuitive Benutzeroberfläche entwickelt worden.

## Hauptfunktionen

*   **Kundenverwaltung:**
    *   Erfassen von Kundennamen und Telefonnummern.
    *   Automatische Generierung eindeutiger Kunden-IDs.
    *   Archivierung von Kundendaten für schnellen Zugriff.
    *   Suchfunktion im Kundenarchiv.
    *   Möglichkeit zur Aktualisierung von Kundendaten (z.B. Telefonnummer).
*   **Reparatur- und Auftragsmanagement:**
    *   Auswahl von Reparaturleistungen aus einer kategorisierten Liste.
    *   Festlegen der Anzahl für jede ausgewählte Reparatur.
    *   Berechnung des Gesamtpreises basierend auf ausgewählten Reparaturen und deren Anzahl.
    *   Möglichkeit, individuelle "Extras" (Sonderleistungen) mit Preis hinzuzufügen.
    *   Erfassung des Eingangsdatums für jeden Auftrag.
    *   Hinzufügen von Notizen zu spezifischen Aufträgen.
    *   Speicherung der erstellten Reparaturaufträge.
    *   Übersichtliche Darstellung der ausgewählten Reparaturen pro Auftrag.
*   **Benutzeroberfläche:**
    *   Entwickelt mit Jetpack Compose für eine deklarative und moderne UI.
    *   Nutzung von Material Design 3 Komponenten für ein ansprechendes Erscheinungsbild.
    *   Intuitive Navigation zwischen den verschiedenen Ansichten der App.

## Technische Details

*   **Programmiersprache:** Kotlin
*   **UI-Framework:** Jetpack Compose
*   **Design-System:** Material Design 3
*   **Architektur :** MVVM (Model-View-ViewModel) – basierend auf der Verwendung von `ViewModel`-Klassen.
*   **Datenhaltung:** Room

## Screens / Ansichten

Die App besteht voraussichtlich aus folgenden Hauptansichten:

1.  **Home-Screen (Startansicht):**
    *   Wahrscheinlich eine Übersicht über bestehende Aufträge oder ein Dashboard.
    *   Möglichkeit, neue Aufträge zu erstellen.
2.  **Edit-Screen (Auftragsbearbeitung):**
    *   Formular zur Eingabe/Bearbeitung von Kundendaten (Name, Telefonnummer).
    *   Anzeige und Auswahl des Eingangsdatums.
    *   Dropdown-Menü zur Auswahl von Kunden aus dem Archiv.
    *   Bereich zur Anzeige der ausgewählten Reparaturen und deren Kosten.
    *   Möglichkeit, zum "Auswahl-Screen" zu navigieren, um Reparaturen hinzuzufügen/zu ändern.
    *   Eingabefelder für "Extras" und Notizen.
    *   Button zum Speichern/Bestätigen des Auftrags.
3.  **Auswahl-Screen (Reparaturauswahl):**
    *   Anzeige von Reparaturkategorien als `stickyHeader`.
    *   Auflistung der verfügbaren Reparaturen innerhalb jeder Kategorie.
    *   Interaktive Elemente (Buttons) zum Erhöhen oder Verringern der Anzahl für jede Reparatur.
    *   Floating Action Button (FAB) zur Bestätigung der ausgewählten Reparaturen und Rückkehr zum "Edit-Screen".

## Code-Struktur (basierend auf den Snippets)

*   **`Composable` Funktionen:** Jeder Screen und viele UI-Komponenten sind als `@Composable` Funktionen implementiert.
*   **`ViewModel` Klassen:** (z.B. `KundeViewModel`, `ArchivViewModel`) kapseln die UI-bezogene Datenlogik und stellen Daten für die Composables bereit.
*   **Datenklassen:** (z.B. `Kunde`, `Reparatur`, `Archiv`, `Kategorie`, `ReparaturChanges`) definieren die Struktur der verwendeten Datenobjekte.
*   **Navigation:** Die Navigation zwischen den Screens wird vermutlich über einen `NavController` von Jetpack Navigation Compose gesteuert.

## Einrichtung und Start (Allgemein)

1.  Klone das Repository.
2.  Öffne das Projekt in Android Studio.
3.  Lasse Gradle die Abhängigkeiten synchronisieren.
4.  Starte die App auf einem Emulator oder einem physischen Gerät.

## Zukünftige Erweiterungen (Mögliche Ideen)

*   Exportfunktionen für Auftragsdaten (z.B. als PDF oder CSV).
*   Cloud-Synchronisation der Daten.


**Hinweis:** Diese README wurde basierend auf den zur Verfügung gestellten Code-Snippets und deren Analyse erstellt. Einige Details, insbesondere zur genauen Implementierung der Datenpersistenz oder spezifischen Business-Logiken, könnten bei vollständiger Einsicht in das Projekt abweichen oder detaillierter sein.
