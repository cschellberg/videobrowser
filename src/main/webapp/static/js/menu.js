// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
 // if (!event.target.matches('.dropbtn')) {
    var buttonParentId = event.srcElement.parentElement.id;
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      var dropdownParentId = openDropdown.parentElement.id;
      if (openDropdown.classList.contains('show') && (buttonParentId != dropdownParentId)) {
        openDropdown.classList.remove('show');
      }
    }
// } 
}



function pickView() {
	var locationUrl = window.location.search;
	var paramIndex = locationUrl.indexOf("view");
	var view = "";
	if (paramIndex >= 0) {
		locationUrl = locationUrl.substring(paramIndex);
		var viewParam = locationUrl.split("&")[0];
		var viewParamParts = viewParam.split("=");
		if (viewParamParts.length > 1) {
			view = viewParamParts[1];
		}
	}
		viewElement = document.getElementById(view);
		if ( viewElement != null ){
		viewElement.style.visibility = "visible";
		}
   paramIndex = locationUrl.indexOf("errorMessage");
	if (paramIndex >= 0) {
		var errorParam = locationUrl.split("&")[1];
		var errorParamParts = errorParam.split("=");
		if (errorParamParts.length > 1) {
			errorMessage = decodeURIComponent(errorParamParts[1]);
		    document.getElementById("errorMessage").innerHTML=errorMessage;
		}

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
	$scope.dropDown= function (index) {
	    document.getElementById('dd'+index).classList.toggle("show");
	}
	$scope.goTo= function (url) {
	    window.location.href=url;
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
	$scope.dropDown= function (index) {
	    document.getElementById('dd'+index).classList.toggle("show");
	}
	$scope.goTo= function (url) {
	    window.location.href=url;
	}
});

