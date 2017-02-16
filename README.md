# cordova-usage-stats-manager
Cordova Android plugin for accessing UsageStatsManager API

# Usage
```javascript
var success = function(success){
	console.log("Sucess :: " + success);
};

var error = function(error){
	console.log("Error :: " + error);
};

getUsageStatistics("Daily", success, error);

