json-array-flattener
====================
This a java utility to flatten a nested/complex/multi-arrayed json into simple flattened json ( 1 to n)

## Maven Repo

```xml
<dependency>
	<groupId>org.github.json</groupId>
	<artifactId>json-array-flattener</artifactId>
	<version>0.1.0</version>
</dependency>
```

Since v0.1.0, Java 8 required


## Input:
```json
{"data":[{"value":[{"mfg":"Audi","info":{"color":"white","length":3932,"abs":true,"engineSN":"ddws32","cc":3500},"infotime":1505296056837}],"brand":"X"},{"value":[{"mfg":"BMW","info":{"color":"white","length":4010,"abs":true,"engineSN":"322de","cc":3600},"infotime":1505296657333},{"mfg":"jaguar","info":{"color":"red","length":4512,"engineSN":"7766h","cc":3500},"infotime":1505296657333}],"brand":"Y"}]}
```
 
## Output:
```json
{"data":{"value":{"infotime":"1505296056837","info":{"cc":"3500","abs":"true","color":"white","length":"3932","engineSN":"ddws32"},"mfg":"Audi"},"brand":"X"}}
{"data":{"value":{"infotime":"1505296657333","info":{"cc":"3600","abs":"true","color":"white","length":"4010","engineSN":"322de"},"mfg":"BMW"},"brand":"Y"}}
{"data":{"value":{"infotime":"1505296657333","info":{"cc":"3500","color":"red","length":"4512","engineSN":"7766h"},"mfg":"jaguar"},"brand":"Y"}}
```

## Usage:
```java
//read the json file
String content = app.readJsonFile("D:\\Projects\\json\\3b774348-9579-48ae-8798-d47fe225f684.json");

//call JsonArrayFlattener.getFlattenedJson(complexArrayJson) to split and flatten it.
List<String> jsonList = JsonArrayFlattener.getFlattenedJson(complexArrayJson);
```