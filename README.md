# Realm Clicker

## Purpose

Click rapidly for a given period of time. I created this to save my hand / mouse
from being destroyed while playing [Realm Grinder](http://www.kongregate.com/games/DivineGames/realm-grinder)
but it can be used for anything which required rapid clicking for a given period.

This project is obviously a quick work and isn't exactly polished. Avoid setting it for long periods as
it doesn't have an easy stop mechanism yet (it might get one if I can be bothered to add one)

## Building

Use [Maven](https://maven.apache.org/)

    mvn clean package

## Usage

    Usage: java -jar realmgrinder-1.0.jar [OPTIONS]

    -i <value> | --initial-delay <value>
          Time to wait before we start clicking; default 2 seconds
    -r <value> | --repeat-delay <value>
          Time to wait between clicks; default 30 milliseconds
    -p <value> | --print-delay <value>
          Time to wait between messages indicating remaining duration; default 1s. 0 means off
    -d <value> | --duration <value>
          Click repeatedly for this length of time; default 20 seconds

For example, to wait 1s and then click 50 times per second for 25 seconds, indicating progress every second:

    java -jar realmgrinder-1.0.jar \
      --initial-delay 1s
      --repeat-delay 20ms
      --print-delay 1s
      --duration 25s
