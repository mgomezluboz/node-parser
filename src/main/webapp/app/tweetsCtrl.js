function TweetsCtrl(tweetsService, $scope) {
    var self = this;

    self.busqueda = "";

    self.errors = [];

    function notificarError(mensaje) {
        self.errors.push(mensaje);
        self.getUsuario();
        $timeout(function () {
            while (self.errors.length > 0)
                self.errors.pop();
        }, 3000);
    };

    self.getTweets = function() {
        tweetsService.find(self.busqueda, function(response) {
            $scope.tweets = response.data;
        }, self.notificarError);
    };

}

tweetsApp.controller("TweetsCtrl", TweetsCtrl);