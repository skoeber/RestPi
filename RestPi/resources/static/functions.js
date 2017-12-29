/**
 * 
 */
var switchLight = function (pinId) {
	var xhr = new XMLHttpRequest();
	xhr.open('PUT', '/api/gpio/pin/' + pinId + '/interval?frequency=500&repeats=2', false);
	xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	xhr.send();
};