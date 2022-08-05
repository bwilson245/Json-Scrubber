# Json-Scrubber
This is a tool for scrubbing sensitive information from a json object. The request body accepts a ScrubRequest that requires a json element, a list of keywords you want to be scrubbed, and a value to put in place of the scrubbed value. It uses ExecutorService to execute the methods on multiple threads. It returns a Scrub result that contains the scrubbed json element and a Statistics object to show the time it took to scrub the object, the total number of elements, total number of objects, total number of arrays, total number of primitives, and the total number of scrubbed elements.

Endpoint - PUT https://7ext5vpeak.execute-api.us-east-1.amazonaws.com/Production/jsonscrubber

Example input:
```json

{
    "replacementValue": "*****",
    "keywords": 
        [
            "key1",
            "key2",
            "key5",
            "key8"
        ],
    "jsonElement": {
        "key1": "value1",
        "key2": null,
        "key3": "value3",
        "key4": {
            "key5": "value5",
            "key6": "value6",
            "key7": "value7",
            "key8": [
                    "value9",
                    "value10"
            ]
        }
    }
}

```


Output from the example above:
```json

{
    "jsonElement": {
        "key1": "*****",
        "key2": "*****",
        "key3": "value3",
        "key4": {
            "key5": "*****",
            "key6": "value6",
            "key7": "value7",
            "key8": [
                "*****",
                "*****"
            ]
        }
    },
    "statistics": {
        "processTimeInMicroSeconds": 87.0,
        "totalElements": 10,
        "totalObjects": 1,
        "totalArrays": 0,
        "totalPrimitives": 7,
        "totalNull": 1,
        "totalScrubbedElements": 5
    }
}

```
