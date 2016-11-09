(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('RegionMySuffixController', RegionMySuffixController);

    RegionMySuffixController.$inject = ['$scope', '$state', 'Region'];

    function RegionMySuffixController ($scope, $state, Region) {
        var vm = this;
        
        vm.regions = [];

        loadAll();

        function loadAll() {
            Region.query(function(result) {
                vm.regions = result;
            });
        }
    }
})();
