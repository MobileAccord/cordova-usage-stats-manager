var exec = require('cordova/exec');

window.getUsageStatistics = function(interval, success, error) {
	console.log("getUsageStatistics() :: " + interval);
	var array = [interval];
    exec(success, error, "MyUsageStatsManager", "getUsageStatistics", array);
};