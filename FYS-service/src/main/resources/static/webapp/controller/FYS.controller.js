sap.ui.define([
	'sap/ui/core/mvc/Controller',
    "sap/ui/core/Fragment",
    "sap/ui/model/json/JSONModel",
    "sap/ui/core/mvc/XMLView"
], function(Controller, Fragment, JSONModel, XMLView) {
    'use strict';
    return Controller.extend('sap.fys.slots.controller.FYS', {
        onInit: function() {
            setInterval(this.getData.bind(this), 3000);
        },
        navigate : function() {
            //TODO
        },
        onSearch: function (oEvent) {
            var key1 = this.getView().byId("reservedType").getSelectedKey();
            var key2 = this.getView().byId("vehicleSize").getSelectedKey();
            var ids = new Map();
            var oModel = this.getOwnerComponent().getModel("Parking");
            if(key1 && key2) {
                oModel.getData().forEach(obj => {
                    if(obj.reservedType === key1 && obj.sizeOfSlot === key2) {
                        if (obj.state === "VACANT") {
                            ids.set(obj.id.toString(),obj.id);
                        }

                    }
                });
            } else if(key1) {
                oModel.getData().forEach(obj => {
                    if(obj.reservedType === key1 && obj.state === "VACANT") {
                        ids.set(obj.id.toString(),obj.id);
                    }
                });
            } else if(key2) {
                oModel.getData().forEach(obj => {
                    if( obj.sizeOfSlot === key2 && obj.state === "VACANT") {
                        ids.set(obj.id.toString(),obj.id);
                    }
                });
            } else{
                oModel.getData().forEach(obj => {
                    if( obj.state === "VACANT") {
                        ids.set(obj.id.toString(),obj.id);
                    }
                });
            }

            var oFirstRowItems = this.getView().byId("gridList1").getItems();
            var oSecondRowItems = this.getView().byId("gridList2").getItems();
            oFirstRowItems.forEach(obj => {
                let oIconCntrl = obj.getContent()[0].getItems()[0];
                if (ids.get(oIconCntrl.getAlt())) {
                    oIconCntrl.setBackgroundColor("green");
                    ids.delete(oIconCntrl.getAlt());
                } else {
                    oIconCntrl.setBackgroundColor("grey");
                }
            });

            oSecondRowItems.forEach(obj => {
                let oIconCntrl = obj.getContent()[0].getItems()[0];
                if (ids.get(oIconCntrl.getAlt())) {
                    oIconCntrl.setBackgroundColor("green");
                    ids.delete(oIconCntrl.getAlt());
                } else {
                    oIconCntrl.setBackgroundColor("grey");
                }
            });
        },
        getData: function(){
            var oModel = this.getOwnerComponent().getModel("Parking");
            var aData = jQuery.ajax({
                type : "GET",
                contentType : "application/json",
                url : "http://localhost:8080/slots",
                dataType : "json",
                async: true,
                success : function(data,textStatus, jqXHR) {
                    oModel.setData(data);
                }
            });
        }
    });
});