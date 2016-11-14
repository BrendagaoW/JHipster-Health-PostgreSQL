(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('blood', {
            parent: 'entity',
            url: '/blood',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: '21PointsApp.blood.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/blood/blood.html',
                    controller: 'BloodController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('blood');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('blood-detail', {
            parent: 'entity',
            url: '/blood/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: '21PointsApp.blood.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/blood/blood-detail.html',
                    controller: 'BloodDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('blood');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Blood', function($stateParams, Blood) {
                    return Blood.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'blood',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('blood-detail.edit', {
            parent: 'blood-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/blood/blood-dialog.html',
                    controller: 'BloodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Blood', function(Blood) {
                            return Blood.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('blood.new', {
            parent: 'blood',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
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
                }).result.then(function() {
                    $state.go('blood', null, { reload: 'blood' });
                }, function() {
                    $state.go('blood');
                });
            }]
        })
        .state('blood.edit', {
            parent: 'blood',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/blood/blood-dialog.html',
                    controller: 'BloodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Blood', function(Blood) {
                            return Blood.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('blood', null, { reload: 'blood' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('blood.delete', {
            parent: 'blood',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/blood/blood-delete-dialog.html',
                    controller: 'BloodDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Blood', function(Blood) {
                            return Blood.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('blood', null, { reload: 'blood' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
