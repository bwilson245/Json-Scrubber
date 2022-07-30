# Json-Scrubber
# This is a tool for scrubbing sensitive information from a json object. It accepts a ScrubRequest that requres a json element, a list of keywords you want to be scrubbed, and a value to put in place of the scrubbed value. It uses ExecutorService to execute the methods on multiple threads if the character count of the json object is greater than 25 million, otherwise it uses an iterative approach. The overhead of creating new threads is too costly when the size of the json is small-medium.
On massive json files, it makes a good bit of difference, so the entry point checks the size of the json object before deciding which method to execute.

Endpoint - https://7ext5vpeak.execute-api.us-east-1.amazonaws.com/Production/jsonscrubber
