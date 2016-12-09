(function () {
    'use strict';

    angular
        .module('21PointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'app',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'app/home/home.html',
                        controller: 'HomeController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('home');
                        return $translate.refresh();
                    }]
                }
            })
            .state('points.add', {
                parent: 'home',
                url: '/points/add',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/points/points-dialog.html',
                        controller: 'PointsDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    date: null,
                                    exercise: null,
                                    meals: null,
                                    alcohol: null,
                                    notes: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('home', null, {reload: true});
                    }, function () {
                        $state.go('home');
                    });
                }]
            })
            .state('blood.add', {
                parent: 'home',
                url: '/blood/add',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/blood/blood-dialog.html',
                        controller: 'BloodDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    date: null,
                                    systolic: null,
                                    diastolic: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('home', null, {reload: true});
                    }, function () {
                        $state.go('home');
                    });
                }]
            })
            .state('weight.add', {
                parent: 'home',
                url: '/weight/add',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/weight/weight-dialog.html',
                        controller: 'WeightDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    date: null,
                                    weight: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('home', null, {reload: true});
                    }, function () {
                        $state.go('home');
                    });
                }]
            })
        ;
    }
})();
