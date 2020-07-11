sap.ui.define([
	'sap/ui/core/mvc/Controller',
	'sap/ui/model/json/JSONModel',
    'sap/ui/core/Fragment'
], function(Controller, JSONModel, Fragment) {
	'use strict';
	return Controller.extend('sap.fys.slots.controller.Sample', {
		ok: function() {
			if (!this._dialog) {
				this._dialog = sap.ui.xmlfragment('sap.fys.slots.fragments.Sample', this);
                this.getView().addDependent(this._dialog);
			}
			this._dialog.open();
		},
		close: function() {
			this._dialog && this._dialog.close();
		}
	});
});