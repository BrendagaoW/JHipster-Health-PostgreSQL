(function() {
    'use strict';
    angular
        .module('21PointsApp')
        .factory('Blood', Blood);

    Blood.$inject = ['$resource', 'DateUtils'];

    function Blood ($resource, DateUtils) {
        var resourceUrl =  'api/blood/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertLocalDateFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date = DateUtils.convertLocalDateToServer(copy.date);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date = DateUtils.convertLocalDateToServer(copy.date);
                    return angular.toJson(copy);
                }
            },
            'last30Days': {
                method: 'GET',
                isArray: false,
                url: 'api/blood/bp-by-days/30'
            }
        });
    }
})();
