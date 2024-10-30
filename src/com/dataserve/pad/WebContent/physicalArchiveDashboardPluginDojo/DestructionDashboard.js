define([
    "dojo/_base/declare",
    "dojo/dom",
    "dojo/dom-construct",
    "ecm/model/ResultSet",
    "dojo/json",
    "dojo/i18n!./nls/localization",
    "physicalArchiveDashboardPluginDojo/Toaster",
	"ecm/model/Desktop",

], function (declare, dom, domConstruct, ResultSet, json, lcl, Toaster,Desktop) {

    return declare("physicalArchiveDashboardPluginDojo.DestructionDashboard", null, {
        _chartInstance: null,
        _lcl: lcl,
        _parent: null,

        constructor: function (args) {
            this.toaster = new Toaster();
            this._parent = args.parent;
        },
        
        GetTotalDocsInSystem: function () {
            var toaster = new Toaster();
            var params = {
                method: "GetTotalDocsInSystem"
            };

            // Call the backend service
            var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService", params);
            var resultSet = new ResultSet(response);

            var results = [];

            if (!resultSet.result.startsWith("ERROR")) {
                // Parse the result into JSON format
                results = json.parse(resultSet.result, true);
            } else {
                // Handle errors
                if (resultSet.result.includes("(ACCESS DENIED)")) {
                    toaster.redToaster(this._lcl.ACCESS_DENIED);
                } else {
                    toaster.redToaster(this._lcl.FAILED_TO_FETCH_DATA);
                }
                console.log("Failed to load data!");
                console.log(resultSet);
            }

            return results;
        },
        
        getArchiveCenterTransferdFiles: function() {
        	var toaster = new Toaster();
        	params = {
 					method: "GetArchiveCenterTransferdFiles",
 					userId: Desktop.userId
 			};
             
 			var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService",params);
 			var resultSet = new ResultSet(response);
 			
 			message = new ecm.widget.dialog.MessageDialog();
			var results = [];
			if(!resultSet.result.startsWith("ERROR")){
				results = json.parse(resultSet.result, true);
			} else {
				if (resultSet.result.includes("(ACCESS DENIED)")) {	
					toaster.redToaster(lcl.ACCESS_DENIED);

				} else {
					toaster.redToaster(lcl.FAILED_TO_FETCH_DATA);

				}
				console.log("Failed to load data!");
				console.log(resultSet);

			}
			return results;
        },
        
        getArchiveCenterTransferdFilesTable: function () {
            const data = this.getArchiveCenterTransferdFiles();
            const totalDocsInsys = this.GetTotalDocsInSystem();

            if (!totalDocsInsys) {
                console.error('No data received for electronic and archived documents.');
                return;
            }

            const TOTAL_DOCS = totalDocsInsys.TotalCount;
            const totalTransferredDocs = data.reduce((sum, dept) => sum + dept.documentCount, 0);

            const locale = ecm.model.desktop.valueFormatter.locale;
            const isRTL = locale === 'ar';

            const container = this._parent.getArchiveCenterTransferdFiles;
            if (container) {
            	container.style.display = 'block';
            }
            domConstruct.empty(container); 

            const table = domConstruct.create("table", { 
                class: "document-table", 
                style: `width:100%; border-collapse: collapse; direction: ${isRTL ? 'rtl' : 'ltr'};` 
            }, container);

            const thead = domConstruct.create("thead", {}, table);
            const headerRow = domConstruct.create("tr", {}, thead);

            [this._lcl.DEPARTMENT_NAME, this._lcl.DOCUMENT_COUNT_LABEL, this._lcl.PERCENTAGE_LABEL].forEach((title) => {
                domConstruct.create("th", { 
                    innerHTML: title, 
                    style: "border: 1px solid #ddd; padding: 8px; background-color: #f2f2f2;" 
                }, headerRow);
            });

            const tbody = domConstruct.create("tbody", {}, table);

            data.forEach((dept) => {
                const percentage = ((dept.documentCount / TOTAL_DOCS) * 100).toFixed(2);
                const row = domConstruct.create("tr", {}, tbody);

                domConstruct.create("td", { innerHTML: dept.deptArName, style: "border: 1px solid #ddd; padding: 8px;" }, row);
                domConstruct.create("td", { innerHTML: dept.documentCount, style: "border: 1px solid #ddd; padding: 8px;" }, row);
                domConstruct.create("td", { innerHTML: `${percentage}%`, style: "border: 1px solid #ddd; padding: 8px;" }, row);
            });

            const totalPercentage = ((totalTransferredDocs / TOTAL_DOCS) * 100).toFixed(2);
            const summaryRow = domConstruct.create("tr", { style: "font-weight: bold; background-color: #f2f2f2;" }, tbody);

            domConstruct.create("td", { innerHTML: this._lcl.TOTAL_TRANSFARED_DOCUMENT, style: "border: 1px solid #ddd; padding: 8px;" }, summaryRow);
            domConstruct.create("td", { innerHTML: totalTransferredDocs, style: "border: 1px solid #ddd; padding: 8px;" }, summaryRow);
            domConstruct.create("td", { innerHTML: `${totalPercentage}%`, style: "border: 1px solid #ddd; padding: 8px;" }, summaryRow);
        },
        
        getNationalCenterTransferdFiles: function() {
        	var toaster = new Toaster();
        	params = {
 					method: "GetNationalCenterTransferdFiles",
 					userId: Desktop.userId
 			};
             
 			var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService",params);
 			var resultSet = new ResultSet(response);
 			
 			message = new ecm.widget.dialog.MessageDialog();
			var results = [];
			if(!resultSet.result.startsWith("ERROR")){
				results = json.parse(resultSet.result, true);
			} else {
				if (resultSet.result.includes("(ACCESS DENIED)")) {	
					toaster.redToaster(lcl.ACCESS_DENIED);

				} else {
					toaster.redToaster(lcl.FAILED_TO_FETCH_DATA);

				}
				console.log("Failed to load data!");
				console.log(resultSet);

			}
			return results;
        },
        
        getNationalCenterTransferdFilesTable: function () {        	
            const data = this.getNationalCenterTransferdFiles();
            const totalDocsInsys = this.GetTotalDocsInSystem();

            if (!totalDocsInsys) {
                console.error('No data received for electronic and archived documents.');
                return;
            }

            const TOTAL_DOCS = totalDocsInsys.TotalCount;
            const totalTransferredDocs = data.reduce((sum, dept) => sum + dept.documentCount, 0);

            const locale = ecm.model.desktop.valueFormatter.locale;
            const isRTL = locale === 'ar';

            const container = this._parent.getNationalCenterTransferdFiles;
            if (container) {
            	container.style.display = 'block';
            }
            domConstruct.empty(container); 

            const table = domConstruct.create("table", { 
                class: "document-table", 
                style: `width:100%; border-collapse: collapse; direction: ${isRTL ? 'rtl' : 'ltr'};` 
            }, container);

            const thead = domConstruct.create("thead", {}, table);
            const headerRow = domConstruct.create("tr", {}, thead);

            [this._lcl.DEPARTMENT_NAME, this._lcl.DOCUMENT_COUNT_LABEL, this._lcl.PERCENTAGE_LABEL].forEach((title) => {
                domConstruct.create("th", { 
                    innerHTML: title, 
                    style: "border: 1px solid #ddd; padding: 8px; background-color: #f2f2f2;" 
                }, headerRow);
            });

            const tbody = domConstruct.create("tbody", {}, table);

            data.forEach((dept) => {
                const percentage = ((dept.documentCount / TOTAL_DOCS) * 100).toFixed(2);
                const row = domConstruct.create("tr", {}, tbody);

                domConstruct.create("td", { innerHTML: dept.deptArName, style: "border: 1px solid #ddd; padding: 8px;" }, row);
                domConstruct.create("td", { innerHTML: dept.documentCount, style: "border: 1px solid #ddd; padding: 8px;" }, row);
                domConstruct.create("td", { innerHTML: `${percentage}%`, style: "border: 1px solid #ddd; padding: 8px;" }, row);
            });

            const totalPercentage = ((totalTransferredDocs / TOTAL_DOCS) * 100).toFixed(2);
            const summaryRow = domConstruct.create("tr", { style: "font-weight: bold; background-color: #f2f2f2;" }, tbody);

            domConstruct.create("td", { innerHTML: this._lcl.TOTAL_TRANSFARED_DOCUMENT, style: "border: 1px solid #ddd; padding: 8px;" }, summaryRow);
            domConstruct.create("td", { innerHTML: totalTransferredDocs, style: "border: 1px solid #ddd; padding: 8px;" }, summaryRow);
            domConstruct.create("td", { innerHTML: `${totalPercentage}%`, style: "border: 1px solid #ddd; padding: 8px;" }, summaryRow);
        },






    });
});
