var app = angular.module("twitterApp", ['ngRoute', 'ngAnimate']);

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
    getMovies: function() {
      var movies = [];
      movies.push(
        {
          id: '',
          name: 'Guardians of the Galaxy',
          thumbnail: 'http://content8.flixster.com/movie/11/17/80/11178082_ori.jpg',
          score: 'positive'
        }
      );
      return movies;
    },
    getMovie: function(movieId) {
      return {
        id: movieId,
        name: 'Guardians of the Galaxy',
        description: 'From Marvel, the studio that brought you the global blockbuster franchises of Iron Man, Thor, Captain America and The Avengers, comes a new team-the Guardians of the Galaxy. An action-packed, epic space adventure, Marvel\'s \"Guardians of the Galaxy\" expands the Marvel Cinematic Universe into the cosmos, where brash adventurer Peter Quill finds himself the object of an unrelenting bounty hunt after stealing a mysterious orb coveted by Ronan, a powerful villain with ambitions that threaten the entire universe. To evade the ever-persistent Ronan, Quill is forced into an uneasy truce with a quartet of disparate misfits-Rocket, a gun-toting raccoon, Groot, a tree-like humanoid, the deadly and enigmatic Gamora and the revenge-driven Drax the Destroyer. But when Quill discovers the true power of the orb and the menace it poses to the cosmos, he must do his best to rally his ragtag rivals for a last, desperate stand-with the galaxy\'s fate in the balance. (C) Walt Disney',
        thumbnail: 'http://content8.flixster.com/movie/11/17/80/11178082_ori.jpg',
        releaseDate: '2014-08-01',
        rating: 'PG-13',
        imdb: '2015381',
        actors: [
          {
            name: 'Chris Pratt',
            characters: 'Peter Quill/Star-Lord',
            thumbnail: 'http://content6.flixster.com/rtactor/39/90/39904_pro.jpg'
          },
          {
            name: 'Lee Pace',
            characters: 'Ronan the Accuser',
            thumbnail: 'http://content8.flixster.com/photo/12/48/64/12486490_ori.jpg'
          }
        ],
        numberOfTweets: 11,
        score: 'positive'
      }
    },
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
    .otherwise({
        redirectTo: '/'
    });
});

app.controller('tabController', function($scope, $location) {
  $scope.isActive = function(path) {
    return path === $location.path();
  };
});

app.controller('nowController', function($scope, $timeout, twitterService) {
  $scope.tweets = [];
    
  $scope.loadTweets = function () {
    $timeout(function() {
      twitterService.getData().then(function(data) {
        var newTweets = $scope.tweets;
        newTweets.unshift(data);
        if(newTweets.length > 10) {
          newTweets.pop();
        }
        $scope.tweets = newTweets;
      });
      $scope.loadTweets();
    }, 2000);
  };
  
  $scope.loadTweets();
});

app.controller('top10Controller', function($scope, movieService) {
  $scope.movies = movieService.getTop10Movies();
});

app.controller('moviesController', function($scope, movieService) {
  $scope.movies = movieService.getMovies();
});

app.controller('movieController', function($scope, $routeParams, $timeout, twitterService, movieService) {
  $scope.movie = movieService.getMovie($routeParams.id); 
  
  $scope.tweets = [];
  
  $scope.loadTweets = function () {
    $timeout(function() {
      twitterService.getData().then(function(data) {
        var newTweets = $scope.tweets;
        newTweets.unshift(data);
        if(newTweets.length > 10) {
          newTweets.pop();
        }
        $scope.tweets = newTweets;
        $scope.movie.numberOfTweets = $scope.movie.numberOfTweets + 1;
      });
      $scope.loadTweets();
    }, 2000);
  };
  
  $scope.loadTweets();
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
