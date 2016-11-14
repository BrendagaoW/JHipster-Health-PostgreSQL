(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('BloodDetailController', BloodDetailController);

    BloodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Blood', 'User'];

    function BloodDetailController($scope, $rootScope, $stateParams, previousState, entity, Blood, User) {
        var vm = this;

        vm.blood = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('21PointsApp:bloodUpdate', function(event, result) {
            vm.blood = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
