tweetsApp.service('tweetsService', function($http) {
    this.find = function(busqueda, callback, errorHandler) {
        $http.post('/tweets', '"' + busqueda + '"').then(callback, errorHandler)
    }
})