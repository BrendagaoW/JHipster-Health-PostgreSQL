(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('MetricController', MetricController);

    MetricController.$inject = ['$scope', '$state', 'Metric'];

    function MetricController ($scope, $state, Metric) {
        var vm = this;
        
        vm.metrics = [];

        loadAll();

        function loadAll() {
            Metric.query(function(result) {
                vm.metrics = result;
            });
        }
    }
})();
