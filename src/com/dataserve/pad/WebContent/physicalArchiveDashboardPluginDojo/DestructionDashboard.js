define([
    "dojo/_base/declare",
    "dojo/dom",
    "dojo/dom-construct",
    "ecm/model/ResultSet",
    "dojo/json",
    "dojo/i18n!./nls/localization",
    "physicalArchiveDashboardPluginDojo/Toaster"
], function (declare, dom, domConstruct, ResultSet, json, lcl, Toaster) {

    return declare("physicalArchiveDashboardPluginDojo.DestructionDashboard", null, {
        _chartInstance: null,
        _lcl: lcl,
        _parent: null,

        constructor: function (args) {
            this.toaster = new Toaster();
            this._parent = args.parent;
        },


    });
});
