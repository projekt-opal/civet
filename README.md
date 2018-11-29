# Civet

OPAL metadata quality component

Project website: http://projekt-opal.de



## Quick start

```
String endpoint = "http://opalpro.cs.upb.de:8890/sparql";
String dataset = "http://europeandataportal.projekt-opal.de/dataset/0021";

CivetApi civetApi = new CivetApi().setSparqlQueryEndpoint(endpoint);
Map<String, Float> scores = civetApi.compute(new URI(dataset), civetApi.getAllMetricIds());
for (Entry<String, Float> score : scores.entrySet()) {
	System.out.println(score.getValue() + " " + score.getKey());
}

```



## Credits

- DICE - Data Science Group  
  Paderborn University  
  [dice.cs.uni-paderborn.de](http://dice.cs.uni-paderborn.de/)

- AKSW - Agile Knowledge Engineering and Semantic Web  
  University of Leipzig  
  [aksw.org](http://aksw.org/)
