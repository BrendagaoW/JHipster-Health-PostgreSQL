(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('GoalController', GoalController);

    GoalController.$inject = ['$scope', '$state', 'Goal'];

    function GoalController ($scope, $state, Goal) {
        var vm = this;
        
        vm.goals = [];

        loadAll();

        function loadAll() {
            Goal.query(function(result) {
                vm.goals = result;
            });
        }
    }
})();
