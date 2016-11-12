(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('GoalDetailController', GoalDetailController);

    GoalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Goal', 'User', 'Metric'];

    function GoalDetailController($scope, $rootScope, $stateParams, previousState, entity, Goal, User, Metric) {
        var vm = this;

        vm.goal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('21PointsApp:goalUpdate', function(event, result) {
            vm.goal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
