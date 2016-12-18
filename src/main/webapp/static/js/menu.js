// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropbtn')) {

    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
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
	if (view == "user_list" || view == "user_save") {
		viewElement = document.getElementById(view);
		viewElement.style.visibility = "visible";
	}
}

var userApp = angular.module("userApp", []);

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

