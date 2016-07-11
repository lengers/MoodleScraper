# MoodleScraper

## About

This tool downloads all forum threads from a given moodle overview site. Built for the WWI SCB and SCi courses of the DHBW Mannheim to collect all questions created for the operating system test.

## Usage

The project is intended to be used as a command line utility. Example:
```
$ java -jar MoodleScraper.jar s012345 ******** "http://moodle.dhbw-mannheim.de/course/view.php?id=3291"
```

To display a help message, pass the ```h``` flag:
```
$ java -jar MoodleScraper.jar -h
A program to extract forum topics from the Moodle platform.

Usage: MoodleScraper-jar [-h] [USERNAME] [PASSWORD] [URL]

Options:
    -h   Display this help

```
