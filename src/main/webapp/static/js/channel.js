
userApp.controller('ChannelListController', function($scope, $http) {
	$scope.listChannels = function() {
		$http.get("/vb/channel/list").then(function mySuccess(response) {
			$scope.channels = response.data;
		}, function myError(response) {
			$scope.channels = response.text;
		});
	}
	$scope.saveVideo = function saveVideo(name){
		var videoFileElement = document.getElementById("videoFile");
		var  videoChannelElement = document.getElementById("videoChannel");
		var url="/vb/admin/channel/addVideo";
	    var data="channelName="+videoChannelElement.value+"&name="+name;
		var config = {headers : {
			'Content-Type': 'application/x-www-form-urlencoded; multipart/form-data; charset=UTF-8'},
			fileName: videoFileElement.value,
            fileFormDataName: 'videoFile'
		}
		$http.post(url, data, config)
		   .then(
		       function(response){
		    	   $scope.listResponse = response.data;
		       }, 
		       function(response){
		    	   $scope.listResponse =  response;
		       }
		    );
		var viewElement = document.getElementById("channel_list");
		viewElement.style.visibility = "visible";
		viewElement = document.getElementById("add_video");
		viewElement.style.visibility = "hidden";
	}
	
	$scope.deleteVideo = function(channelName,videoName) {
		$http.delete("/vb/admin/channel/deleteVideo/"+channelName+"/"+videoName).then(function mySuccess(response) {
			if ( response.data.code ==  0){
			window.location.reload();
			}
			else {
			$scope.listResponse = response.data.error;
			}
		}, function myError(response) {
			$scope.listResponse = response;
		});

	}
	
	$scope.delete = function(channelName) {
		$http.delete("/vb/admin/channel/delete/"+channelName).then(function mySuccess(response) {
			if ( response.data.code ==  0){
			window.location.reload();
			}
			else {
			$scope.listResponse = response.data.error;
			}
		}, function myError(response) {
			$scope.listResponse = response.statusText;
		});

	}
	$scope.listVideos = function(channelName) {
		$scope.selectedChannel=channelName;
		var viewElement = document.getElementById("channel_list");
		viewElement.style.visibility = "hidden";
		viewElement = document.getElementById("list_videos");
		viewElement.style.visibility = "visible";
		$http.get("/vb/channel/"+channelName).then(function mySuccess(response) {
			$scope.channel = response.data;
		}, function myError(response) {
			$scope.channel = response.text;
		});
	}
	$scope.addVideo = function(channelName) {
		$scope.selectedChannel=channelName;
		var viewElement = document.getElementById("channel_list");
		viewElement.style.visibility = "hidden";
		viewElement = document.getElementById("add_video");
		viewElement.style.visibility = "visible";
	}
	$scope.cancelAddVideo = function(channelName) {
		var viewElement = document.getElementById("channel_list");
		viewElement.style.visibility = "visible";
		viewElement = document.getElementById("add_video");
		viewElement.style.visibility = "hidden";
	}

});

userApp.controller('ChannelSaveController', function($scope, $http) {
	$scope.save = function(channel) {
		$scope.saveResponse="";
		var url="/vb/admin/channel"
		var config = {
                headers : {
                    'Content-Type': 'application/json'
                }
            }
		var data=JSON.stringify(channel);
		$http.post(url, data, config)
		   .then(
		       function(response){
		    	   $scope.saveResponse = response.data;
		       }, 
		       function(response){
		    	   $scope.saveResponse =  response.error;
		       }
		    );
	}
});