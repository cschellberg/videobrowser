
userApp.controller('UserController', function($scope, $http) {
	$scope.register = function(channel) {
		$scope.saveResponse = "";
		var url = "/vb/reg"
		var config = {
			headers : {
				'Content-Type' : 'application/json'
			}
		}
		var data = JSON.stringify(channel);
		$http.post(url, data, config).then(function(response) {
			$scope.saveResponse = response.data;
		}, function(response) {
			$scope.saveResponse = response.error;
		});
	}

});