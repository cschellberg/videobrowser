// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
	// if (!event.target.matches('.dropbtn')) {
	var buttonParentId = event.srcElement.parentElement.id;
	var dropdowns = document.getElementsByClassName("dropdown-content");
	var i;
	for (i = 0; i < dropdowns.length; i++) {
		var openDropdown = dropdowns[i];
		var dropdownParentId = openDropdown.parentElement.id;
		if (openDropdown.classList.contains('show')
				&& (buttonParentId != dropdownParentId)) {
			openDropdown.classList.remove('show');
		}
	}
	// }
}

function getParameter(str, name) {
	var parameterChunks = str.split(/[&?]+/);
	for (ii = 0; ii < parameterChunks.length; ii++) {
		var parameterChunk = parameterChunks[ii];
		if (parameterChunk.indexOf(name) >= 0) {
			parameterParts = parameterChunk.split("=");
			if (parameterParts.length > 1) {
				return parameterParts[1];
			}
		}
	}
	return "";
}

function pickView() {
	var locationUrl = window.location.search
	pickAView(locationUrl);
}

function pickAView(urlStr) {
	var view = getParameter(urlStr, "view")
	viewElement = document.getElementById(view);
	if (viewElement != null) {
		viewElement.style.visibility = "visible";
	}
	var errorMessage = decodeURIComponent(getParameter(urlStr, "errorMessage"));
	var viewElement = document.getElementById("errorMessage")
	if (viewElement != null) {
		viewElement.innerHTML = errorMessage;
	}
}

var userApp = angular.module("userApp", []);

userApp.controller('AnonymousMenuController', function($scope, $http) {
	$scope.menuItems = function() {
		$http.get("/vb/anonymousMenu").then(function mySuccess(response) {
			$scope.menu = response.data;
		}, function myError(response) {
			$scope.menu = response.text;
		});
	}
	$scope.dropDown = function(index) {
		document.getElementById('dd' + index).classList.toggle("show");
	}
	$scope.goTo = function(url) {
		window.location.href = url;
	}
});

userApp.controller('MenuController', function($scope, $http) {
	$scope.menuItems = function() {
		$http.get("/vb/menu").then(function mySuccess(response) {
			$scope.menu = response.data;
		}, function myError(response) {
			$scope.menu = response.text;
		});
	}
	$scope.dropDown = function(index) {
		document.getElementById('dd' + index).classList.toggle("show");
	}
	$scope.goTo = function(url) {
		window.location.href = url;
	}
});
