# Hermes

[![Build Status](https://travis-ci.org/Almaz-KG/Hermes.svg?branch=master)](https://travis-ci.org/Almaz-KG/Hermes)

Hermes is an open source software platform for trade backtesting and researching. Hermes can be used to develop algorithmic trading strategies or researching stock data correlation and other trade-based features.

Hermes requires JDK 8 or newer.

Features
-
Soon will be described

Resources
-
Hermes contains the following resources:
  - [**ETF Day1 and H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several ETF history csv files
  - [**Forex Day1 and H1 currency data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several Forex currency history csv files
  - [**Indexes D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several Index history csv files
  - [**Futures D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several Futures history csv files
  - [**Energy futures D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several energy futures (BRN, WT, NG, etc)  history csv files
  - [**Agriculture futures D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several Agriculture Futures (COFFEE, COCOA, etc) history csv files
  - [**Metals D1 & H1 spot data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains gold & silver spot history csv files
  - [**E-Coins D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several digital couns (Bitcoin, Litecoin) history csv files
  - [**ETF prices, D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several ETF price history csv files
  - [**RU stocks D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several Russian stock history csv files
  - [**US stocks D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several USA stock history csv files
  - [**DE stocks D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several German stock history csv files
  - [**FR stocks D1 & H1 data**](hermesdataprovider/src/main/resources/qoute_data/quotes/D1) contains several French stock history csv files

Build
-----
Hermes is a typical maven project, so follow these steps

1. Import project as a maven project in your favorite IDE
2. Create your strategy implementaion (or use strategy samples)
3. Run BackTestRunner with your trading strategy 
4. Get backtest result
5. Build and save report

Examples
-------
#### Day range research example
Let's try to build some research. Simple assumption that: every weekday has a different bar size. I mean the difference between OPEN_PRICE and CLOSE_PRICE or between HIGH_PRICE and LOW_PRICE. Also, we wanna get some numerical value of every weekday size, so follow these steps for get result:

* You need to get some quote historical data - so write this code and you get historical data

```java
  List<QuoteHistory> quoteHistories = 
          ResourceManager.getQuoteHistories(Quotes.RU_Stocks, TimeFrame.Day1);
``` 
* Now, you should for every quote history run research method

```java
  List<ResearchResult> results = new ArrayList<>();

  for (QuoteHistory quoteHistory : quoteHistories) {
      DayRangeResearch research = new DayRangeResearch(quoteHistory);
      ResearchResult researchResult = research.research();
      results.add(researchResult);
  }
```
* To save the research result write this code

```java
  Path path = Paths.get(BackTestRunner.DEFAULT_OUTPUT_REPORT_DIRECTORY + "barDayRangeResearch.html");
  String template = "HtmlDayRangeResearchReportTemplate.vm";
  
  ReportBuilder reportBuilder = new HtmlResearchReportBuilder(results, template);
  reportBuilder.build();
  reportBuilder.save(path.toFile());
```
Full example code here [runDayRangeResearch](src/main/java/examples/ResearchRunner.java) 

#### And result of our researching as image:
![Alt text](https://cloud.githubusercontent.com/assets/2547372/10561045/22a974ba-7526-11e5-8530-64e5432f6932.png "Research report")

Other examples
------
Will be described soon

How to
------
See [**wiki**](https://github.com/Almaz-KG/Germes/wiki).



