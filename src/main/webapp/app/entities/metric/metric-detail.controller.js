(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('MetricDetailController', MetricDetailController);

    MetricDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Metric', 'Goal', 'Entry'];

    function MetricDetailController($scope, $rootScope, $stateParams, previousState, entity, Metric, Goal, Entry) {
        var vm = this;

        vm.metric = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('21PointsApp:metricUpdate', function(event, result) {
            vm.metric = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
