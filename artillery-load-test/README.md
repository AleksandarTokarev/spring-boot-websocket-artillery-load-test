# Artillery Load Testing Service
This is a service that uses the tool https://www.artillery.io/ in order to execute load tests.

## How to create Docker Image and Run it
1. `docker build -t artillery-ws-load-test:tag .` (build the current directory with a tag as you like)
2. `docker run artillery-ws-load-test:1.0.0` (the tag that you have built previously)  
NOTE: Windows will not work out of the box as there is a limitation on the host network   https://stackoverflow.com/a/63210708/3421066  
   a) You need to enable host networking following the link:https://stackoverflow.com/a/78957561/3421066  
b) `docker run --net=host  artillery-ws-load-test:1.0.0` run it with this command

# Links
https://www.artillery.io/docs/get-started/get-artillery  
https://www.artillery.io/docs/reference/engines/websocket  
https://websocket.org/tools/websocket-echo-server  
https://www.artillery.io/docs/reference/cli/report  
https://www.artillery.io/docs/reference/reported-metrics#metrics-reported-by-artillery  
https://github.com/artilleryio/artillery/issues/2517 (metrics fix for websocket received messages and rate)  
https://www.artillery.io/docs/reference/test-script#scenario-weights

## How to run locally?
Install NodeJS & NPM  
After that   
`npm install -g artillery@latest`  
If you want to just run it use the command below:  
`artillery run spring-websocket-test.yml`  
If you want to run it and save results use the command below:  
`artillery run --output test-run-report.json spring-websocket-test.yml`  
After that you can use the JSON to generate HTML:  
`artillery report test-run-report.json`  
How to do debugging  
`DEBUG=ws artillery run --output test-run-report.json spring-websocket-test.yml`  
Analyze results on the cloud (https://app.artillery.io/)   
`artillery run spring-websocket-test.yml --record --key {{yourArtilleryCloudApiKey}}`