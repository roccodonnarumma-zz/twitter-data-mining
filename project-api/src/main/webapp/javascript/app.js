var app = angular.module("twitterApp", ['ngRoute', 'ngAnimate', 'ngResource']);

app.factory('movies', function($resource) {
  return $resource('http://localhost:8080/project-api/movies');
});

app.factory('movie', function($resource) {
  return $resource('http://localhost:8080/project-api/movie/:id');
});

app.factory('latestTweet', function($resource) {
  return $resource('http://localhost:8080/project-api/tweet/latest'); 
});

app.factory('latestMovieTweet', function($resource) {
  return $resource('http://localhost:8080/project-api/tweet/latest/:id');
});

app.factory('twitterService', function ($http, $q) {
  return {
    getData: function() {
      var tweet = {
          user: 'Rocco Project',
          thumbnail: 'https://pbs.twimg.com/profile_images/487975419845439491/FsDTfs0M_bigger.jpeg',
          handle: 'roccoproject',
          date: 'Aug 9',
          tweet: 'Nice movie',
          score: 'positive'
      };
      var defer = $q.defer();
      defer.resolve(tweet);
      return defer.promise;
    }
  };
});

app.factory('movieService', function() {
  return {
    getTop10Movies: function() {
       var movies = [];
       
       movies.push(
         {
           id: '123',
           name: 'Guardians of the Gallaxy',
           thumbnail: 'http://content8.flixster.com/movie/11/17/80/11178082_ori.jpg',
           totalTweets: 11,
           totalPositiveTweets: 9,
           totalNeutralTweets: 1,
           totalNegativeTweets: 1
         }
       );
       
       movies.push(
         {
           id: '123',
           name: 'Guardians of the Gallaxy',
           thumbnail: 'http://content8.flixster.com/movie/11/17/80/11178082_ori.jpg',
           totalTweets: 11,
           totalPositiveTweets: 9,
           totalNeutralTweets: 1,
           totalNegativeTweets: 1
         }
       );
       
       return movies;
    }
  }
});

app.config(function($routeProvider) {
 $routeProvider
    .when('/', {
        templateUrl: 'pages/now.html',
        controller: 'nowController'
    })
    .when('/top10', {
        templateUrl: 'pages/top10.html',
        controller: 'top10Controller'
    })
    .when('/movies', {
        templateUrl: 'pages/movies.html',
        controller: 'moviesController'
    })
    .when('/movie/:id', {
        templateUrl: 'pages/movie.html',
        controller: 'movieController'
    })
    .when('/admin', {
        templateUrl: 'pages/admin.html',
        controller: 'adminController'
    })
    .otherwise({
        redirectTo: '/'
    });
});

app.controller('tabController', function($scope, $location) {
  $scope.isActive = function(path) {
    return path === $location.path();
  };
});

app.controller('nowController', function($scope, $timeout, latestTweet) {
  $scope.tweets = [];
    
  $scope.loadTweets = function () {
    $timeout(function() {
      latestTweet.get(function(data) {
        var newTweets = $scope.tweets;
        if(newTweets[0] == undefined || newTweets[0].id != data.id) {
          newTweets.unshift(data);
          if(newTweets.length > 10) {
            newTweets.pop();
          }
          $scope.tweets = newTweets;
        }
      });
      $scope.loadTweets();
    }, 2000);
  };
  
  $scope.loadTweets();
});

app.controller('top10Controller', function($scope, movieService) {
  $scope.movies = movieService.getTop10Movies();
});

app.controller('moviesController', function($scope, movies) {
  movies.query(function(data) {
    $scope.movies = data;
  });
});

app.controller('movieController', function($scope, $routeParams, $timeout, latestMovieTweet, movie) {
  movie.get( { id : $routeParams.id }, function(data) {
    $scope.movie = data; 
  });
  
  $scope.tweets = [];
  
  $scope.loadTweets = function () {
    $timeout(function() {
      latestMovieTweet.get( { id : $routeParams.id }, function(data) {
        var newTweets = $scope.tweets;
        if(newTweets[0] == undefined || newTweets[0].id != data.id) {
          newTweets.unshift(data);
          if(newTweets.length > 10) {
            newTweets.pop();
          }
          $scope.tweets = newTweets;
          $scope.movie.numberOfTweets = $scope.movie.numberOfTweets + 1;
        } 
      });
      $scope.loadTweets();
    }, 2000);
  };
  
  $scope.loadTweets();
});

app.controller('adminController', function($scope, movies, movie) {
  movies.query(function(data) {
    $scope.movies = data;
  });
  
  $scope.save = function(movieObject, hashtag) {
    movie.save({ id : movieObject.id }, hashtag);
    movieObject.tracks = hashtag;
  };
});

app.animation('.repeated-item', function() {
  return {
    enter : function(element, done) {
      element.css('opacity',0);
      jQuery(element).animate({
        opacity: 1
      }, done);
  
      return function(isCancelled) {
        if(isCancelled) {
          jQuery(element).stop();
        }
      }
    },
    leave : function(element, done) {
      element.css('opacity', 1);
      jQuery(element).animate({
        opacity: 0
      }, done);

      return function(isCancelled) {
        if(isCancelled) {
          jQuery(element).stop();
        }
      }
    },
    move : function(element, done) {
      element.css('opacity', 0);
      jQuery(element).animate({
        opacity: 1
      }, done);

      return function(isCancelled) {
        if(isCancelled) {
          jQuery(element).stop();
        }
      }
    },

    addClass : function(element, className, done) {},
    removeClass : function(element, className, done) {}
  }
});
