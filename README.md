# ReadMe Testing

Verwendung von dieser Aufgabe erfolgt auf Arch Linux. Darum können/werden manche Befehle abweichen.

## Installation for Kafka via Docker

Docker starten:

```bash
sudo systemctl start docker
```

Folder erstellen und in diesen wechseln:

```bash
mkdir kafka && cd kafka
```

Danach die compose.yml Datei erstellen:

```bash
nvim compose.yml
```

Inhalt der compose.yml Datei:

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000

  kafka:
    image: confluentinc/cp-kafka:7.4.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

Wenn Inhalt von dem compose.yml Datei erstellt wurde, Docker Container starten:

```bash
cd kafka
docker-compose up -d
```

Wenn das erledigt ist kann man einmal einen Befehl nutzen um zu schauen ob Kafka läuft:

```bash
docker ps
```

Dort sollte dann in dem Fall einmal kafka und zookeeper auftauchen. Zur Information bezüglich Kafka, Kafka ist der
Arbeiter also er ist für die Speicherung und Verteilung der Daten zuständig. Zookeeper ist hingegen der Manager der
alles koordiniert.

Und um es immer zu starten (docker-compose up -d im Ordner wo die compose.yml Datei liegt):

```bash
cd kafka/
sudo systemctl start docker
docker-compose up -d 
```

Und jedesmal wenn man Fertig ist:

```bash
docker-compose down
```

Um das Programm zu testen starten wir die MessagingApplication.java

Um jetzt etwas zu senden geben wir folgenden Link in den Browser ein:

```
http://localhost:8080/warehouse/send?data=wimma
```

Um dann den report zu sehen gehen wir auf:

```
http://localhost:8080/management/report
```

Und um die empfangenen Nachrichten zu sehen, schauen wir in den Folder logs, dort sollte ein File mit dem Namen
warehouse_central.log sein. Dort sind dann die empfangenen Nachrichten zu sehen.

```bash 
cd logs/
nvim warehouse_central.log
```

## Fragestellungen

- Nennen Sie mindestens 4 Eigenschaften der Message Oriented Middleware?
    - Asynchronität: Sender Empfänger müssen nicht gleichzeitig aktiv sein. Nachricht wird zwischengespeichert.
    - Lose Kopplung: Sender und Empfänger sind unabhängig voneinander. Kommunizieren nur über Broker
    - Zuverlässigkeit: Nachrichten können auf Festplatte gespeichert werden, damit bei Systemabsturz nicht verloren
      gehen.
    - Skalierbarkeit: Es können weitere Producer / Consumer hinzugefügt werden um Last zu verteilen.
- Was versteht man unter einer transienten und synchronen Kommunikation?
    - Synchrone Kommunikation: Sender blockiert bis Empfänger Nachricht erhalten und verarbeitet hat.
    - Transiente Kommunikation: Nachrichten werden nur so lange gespeichert, wie Sender und Empfänger aktive Verbindung
      haben. Wenn Empfänger offline geht während dem Senden dann geht Nachricht verloren.
- Beschreiben Sie die Funktionsweise einer JMS Queue?
  -Queue basiert auf Point-to-Point MOdell. Nachricht wird genau einem Consumer geliefert. Gibt mehrere Empfänger,
  werden Nachrichten per Round-Robing (abwechselnd) verteilt. Nachricht bleibt in der Queue bis erfolgreichem Koncum
  oder Löschung.
- JMS Overview - Beschreiben Sie die wichtigsten JMS Klassen und deren Zusammenhang?
    - ConnectionFactory: Start-Objekt mit dem Verbindung zum Broker aufgebaut wird.
    - Connection: aktive TCP-Verbindung zum Messaging-Server
    - Session: Einseitiger Kontext zum Erzeugen von Nachrichten. Hier wird auch festgelegt, ob Transaktionen genutzt
      werden.
    - MessageProducer/MessageConsumer: Objekte die von der Session erzeugt werden um Nachrichten an ein Ziel zu senden
      oder zu lesen.
    - Destination: Zielobjekt an das die Nachricht adressiert ist.
- Beschreiben Sie die Funktionsweise eines JMS Topic?
    - Ein Topic basiert auf dem Publish-Subscribe Modell:
    - Eine Nachricht wird an alle aktiven Abonenenten gleichzeitig verteilt. Radio-Sender allle die zuhören bekommen die
      Information. Durable Subscription: Besonderheit bei der Broker NAchrichten für Abonnenten speichert, kurzzeitig
      offline sind.
- Was versteht man unter einem lose gekoppelten verteilten System? Nennen Sie ein Beispiel dazu. Warum spricht man hier
  von lose? `
    - Ein System ist lose gekoppelt wenn die einzelnen Kompnenten so weit wie es geht unabgängig voneinander
      funktionieren.
    - Zeitliche Entkopplung: Sender und Empfänger müssen nicht zur selben Zeit bereit sein.
    - Ortstransparent: Der Sender muss nicht wissen wo der Empfänger ist sonder nur den Namen der Queue/des Topics
      kennen.
    - Plattformunabhängigkeit: Sender kann in Java geschrieben sien, Empfänger in Python.
