var exec = require('cordova/exec');

window.UsageStatistics = {
    getUsageStatistics: function(success, error, interval) {
      	console.log("getUsageStatistics() :: " + interval);
	  	var array = [interval];
    	exec(success, error, "MyUsageStatsManager", "getUsageStatistics", array);
    },
    
	openPermissionSettings: function(success, error) {
		console.log("openPermissionSettings() :: ");
    	exec(success, error, "MyUsageStatsManager", "openPermissionSettings", null);
	}
};