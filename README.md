# json-to-markdown-cli
Convert a json structure to markdown
At the moment this is a work in Progress. There is no dedicated installation process right now.
Preliminary Installation 
Build the project via gradle: 
- ```cd <projectFlder>```
- ```./gradlew clean build```
- unzip the created archive
- ```unzip ./build/distributions/json-to-markdown-0.0.1.zip```
- ``` cd ./build/distributions/json-to-markdown-0.0.1/bin````
- run the converter (a config file for the metro-retro json can be found in ```build/distributions/json-to-markdown-0.0.1/config```)
- ```./json-to-markdown -p <path-to-the-json-file> -c <path-to-the-config-file> -o <path-to-the-output-file>
