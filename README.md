# cordova-usage-stats-manager
Cordova Android plugin for accessing UsageStatsManager API

# Usage
To access to device usage history and statistics
```javascript
var success = function(success){
	console.log("Sucess :: " + success);
};

var error = function(error){
	console.log("Error :: " + error);
};

UsageStatistics.getUsageStatistics("Daily", success, error);
```
If you need to prompt the user for permission
```javascript
var success = function(success){
	console.log("Sucess :: " + success);
};

var error = function(error){
	console.log("Error :: " + error);
};

UsageStatistics.launchUsageStatsManagerPermissionSettings(success, error);

```
