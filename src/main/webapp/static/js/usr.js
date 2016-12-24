
userApp.controller('UserController', function($scope, $http) {
	$scope.register = function(user) {
		$scope.saveResponse = "";
		var url = "/vb/reg"
		var config = {
			headers : {
				'Content-Type' : 'application/json'
			}
		}
		var data = JSON.stringify(user);
		$http.post(url, data, config).then(function(response) {
			$scope.saveResponse = response.data;
		}, function(response) {
			$scope.saveResponse = response.error;
		});
	}

	$scope.getLoggedInUser = function() {
		$http.get("/vb/getLoggedInUser").then(function mySuccess(response) {
			$scope.user = response.data;
		}, function myError(response) {
			alert("Error saving account because \n"+response);
		});
	}
});