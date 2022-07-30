# Json-Scrubber
# This is a tool for scrubbing sensitive information from a json object. It accepts a ScrubRequest that requres a json element, a list of keywords you want to be scrubbed, and a value to put in place of the scrubbed value. It uses ExecutorService to execute the methods on multiple threads if the character count of the json object is greater than 25 million, otherwise it uses an iterative approach. The overhead of creating new threads is too costly when the size of the json is small-medium.
On massive json files, it makes a good bit of difference, so the entry point checks the size of the json object before deciding which method to execute.

Endpoint - https://7ext5vpeak.execute-api.us-east-1.amazonaws.com/Production/jsonscrubber

Example input:
```json

{
    "replacementValue": "*****",
    "keywords": ["id", "topping"],
    "jsonElement": {
	"id": "0001",
	"type": "donut",
	"name": "Cake",
	"ppu": 0.55,
	"batters":
		{
			"batter":
				[
					{ "id": "1001", "type": "Regular" },
					{ "id": "1002", "type": "Chocolate" },
					{ "id": "1003", "type": "Blueberry" },
					{ "id": "1004", "type": "Devil's Food" }
				]
		},
	"topping":
		[
			{ "id": "5001", "type": "None" },
			{ "id": "5002", "type": "Glazed" },
			{ "id": "5005", "type": "Sugar" },
			{ "id": "5007", "type": "Powdered Sugar" },
			{ "id": "5006", "type": "Chocolate with Sprinkles" },
			{ "id": "5003", "type": "Chocolate" },
			{ "id": "5004", "type": "Maple" }
		]
}
}

```


Output from the example above:
```json

{
    "id": "*****",
    "type": "donut",
    "name": "Cake",
    "ppu": 0.55,
    "batters": {
        "batter": [
            {
                "id": "*****",
                "type": "Regular"
            },
            {
                "id": "*****",
                "type": "Chocolate"
            },
            {
                "id": "*****",
                "type": "Blueberry"
            },
            {
                "id": "*****",
                "type": "Devil's Food"
            }
        ]
    },
    "topping": [
        {
            "id": "*****",
            "type": "*****"
        },
        {
            "id": "*****",
            "type": "*****"
        },
        {
            "id": "*****",
            "type": "*****"
        },
        {
            "id": "*****",
            "type": "*****"
        },
        {
            "id": "*****",
            "type": "*****"
        },
        {
            "id": "*****",
            "type": "*****"
        },
        {
            "id": "*****",
            "type": "*****"
        }
    ]
}

```
