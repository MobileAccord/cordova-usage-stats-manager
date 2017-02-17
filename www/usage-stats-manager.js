var exec = require('cordova/exec');

window.UsageStatistics = {
    getUsageStatistics: function(interval, success, error) {
      	console.log("getUsageStatistics() :: " + interval);
	  	var array = [interval];
    	exec(success, error, "MyUsageStatsManager", "getUsageStatistics", array);
    },
    
	launchUsageStatsManagerPermissionSettings: function(success, error) {
		console.log("launchUsageStatsManagerPermissionSettings() :: ");
    	exec(success, error, "MyUsageStatsManager", "launchUsageStatsManagerPermissionSettings", null);
	}
};