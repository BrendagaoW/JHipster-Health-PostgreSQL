(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Points', 'Preferences'];

    function HomeController ($scope, Principal, LoginService, $state, Points, Preferences) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });

            Points.thisWeek(function (data) {
                $scope.pointsThisWeek = data;
                $scope.pointsPercentage = (data.points / 21) * 100;
            });

            Preferences.userPreferences(function (data) {
                $scope.preferences = data;
            })
        }
        function register () {
            $state.go('register');
        }
    }
})();
