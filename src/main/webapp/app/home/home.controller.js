(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Points', 'Preferences', 'Blood', 'Chart', 'Weight'];

    function HomeController ($scope, Principal, LoginService, $state, Points, Preferences, Blood, Chart, Weight) {
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
            });

            configBloodChart();

            configWeightChart();

            $scope.todayDate = new Date();
        }
        function register () {
            $state.go('register');
        }

        function configWeightChart() {
            Weight.last30Days(function(weightReadings) {
                $scope.weightReadings = weightReadings;
                if (weightReadings.readings.length) {
                    $scope.weightOptions = angular.copy(Chart.getChartConfig());
                    $scope.weightOptions.title.text = weightReadings.period;
                    $scope.weightOptions.chart.yAxis.axisLabel = "Weight";
                    $scope.weightOptions.chart.type = "scatterChart";
                    var weights = [];
                    weightReadings.readings.forEach(function (item) {
                        weights.push({
                            x: new Date(item.date),
                            y: item.weight
                        });
                    });
                    $scope.weightData = [{
                        values: weights,
                        key: 'Weight',
                        color: 'darkviolet'
                    }]; }
            });
        }

        function configBloodChart() {
            Blood.last30Days(function(bpReadings) {
                $scope.bpReadings = bpReadings;
                if (bpReadings.readings.length) {
                    $scope.bpOptions = angular.copy(Chart.getChartConfig());
                    $scope.bpOptions.title.text = bpReadings.period;
                    $scope.bpOptions.chart.yAxis.axisLabel = "Blood Pressure";
                    var systolics, diastolics;
                    systolics = [];
                    diastolics = [];
                    bpReadings.readings.forEach(function (item) {
                        systolics.push({
                            x: new Date(item.date),
                            y: item.systolic
                        });
                        diastolics.push({
                            x: new Date(item.date),
                            y: item.diastolic
                        });
                    });
                    $scope.bpData = [{
                        values: systolics,
                        key: 'Systolic',
                        color: '#673ab7'
                    }, {
                        values: diastolics,
                        key: 'Diastolic',
                        color: '#03a9f4'
                    }]; }
            });
        }
    }
})();
