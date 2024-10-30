define([
    "dojo/_base/declare",
    "dojo/dom",
    "dojo/dom-construct",
    "ecm/model/ResultSet",
    "dojo/json",
    "dojo/i18n!./nls/localization",
    "physicalArchiveDashboardPluginDojo/Toaster"
], function (declare, dom, domConstruct, ResultSet, json, lcl, Toaster) {

    return declare("physicalArchiveDashboardPluginDojo.DocumentsDashboard", null, {
        _chartInstance: null,
        _keywordChartInstance: null,  // For keyword chart instance
        _lcl: lcl,
        _parent: null,

        constructor: function (args) {
            this.toaster = new Toaster();
            this._parent = args.parent;  // Properly assign parent
        },

        // Get data for electronic and archive documents
        getElectronicAndArchiveDoc: function () {
            // Hide other sections (View Requests and Borrowing Requests)
            if (this._parent.secondChartContainerKeywordDoc) {
                this._parent.secondChartContainerKeywordDoc.style.display = 'none';
            }
            if (this._parent.totalKeywordDoc) {
            	this._parent.totalKeywordDoc.style.display = 'none';
            }

            // Show the electronic/archive document section
            if (this._parent.firstChartContainerElectronicAndArchiveDoc) {
                this._parent.firstChartContainerElectronicAndArchiveDoc.style.display = 'block';
            }
            if (this._parent.totalCountContainer) {
                this._parent.totalCountContainer.style.display = 'block';
            }


            // Call the method to render the electronic and archive documents chart
            this.renderChartsElectronicAndArchiveDoc();
        },

        // Render the chart for electronic and archive documents
        renderChartsElectronicAndArchiveDoc: function () {
            if (this._chartInstance) {
                this._chartInstance.destroy();
            }

            // Get the response for electronic and archived documents
            var dataResponse = this.GetElectronicAndArchiveDocCount();

            if (!dataResponse) {
                console.error('No data received for electronic and archived documents.');
                return;
            }

            // Extract the counts from the response
            var totalCount = dataResponse.TotalCount || 0;
            var archivedCount = dataResponse.ArchivedCount || 0;
            var electronicCount = dataResponse.ElectronicCount || 0;

            // Calculate percentages
            var archivedPercentage = totalCount > 0 ? ((archivedCount / totalCount) * 100).toFixed(2) : 0;
            var electronicPercentage = totalCount > 0 ? ((electronicCount / totalCount) * 100).toFixed(2) : 0;

            // Update the total document count in the totalCountContainer div
            document.getElementById('totalCountContainer').innerHTML = this._lcl.TOTAL_DOCS + ': ' + totalCount;

            // Chart options for displaying the archived and electronic document counts
            var chartOptions = {
                series: [archivedCount, electronicCount], // The data for the pie chart
                labels: [
                    this._lcl.ARCHIVED_DOCS + ': ' + this._lcl.DOCUMENT_COUNT_LABEL + ' (' + archivedCount + ') ' + this._lcl.PERCENTAGE_LABEL + ' (' + archivedPercentage + '%)',
                    this._lcl.ELECTRONIC_DOCS + ': ' + this._lcl.DOCUMENT_COUNT_LABEL + ' (' + electronicCount + ') ' + this._lcl.PERCENTAGE_LABEL + ' (' + electronicPercentage + '%)'
                ],
                chart: {
                    type: 'pie',
                    height: 400
                },
                title: {
                    text: this._lcl.DOCS_DISTRIBUTION_TITLE, // Title for the chart
                    align: 'center'
                },
                tooltip: {
                    y: {
                        formatter: function (value) {
                            return value;
                        }
                    }
                }
            };

            // Render the chart in the firstChartContainerElectronicAndArchiveDoc div
            this._chartInstance = new ApexCharts(this._parent.firstChartContainerElectronicAndArchiveDoc, chartOptions);
            this._chartInstance.render();
        },

        // Render the chart for keyword documents
        renderChartsKeywordDoc: function () {

        	  // Hide other sections (View Requests and Borrowing Requests)
        	 if (this._parent.firstChartContainerElectronicAndArchiveDoc) {
                 this._parent.firstChartContainerElectronicAndArchiveDoc.style.display = 'none';
             }
             if (this._parent.totalCountContainer) {
                 this._parent.totalCountContainer.style.display = 'none';
             }
           

            // Show the electronic/archive document section
             if (this._parent.secondChartContainerKeywordDoc) {
                 this._parent.secondChartContainerKeywordDoc.style.display = 'block';
             }
             
             if (this._parent.totalKeywordDoc) {
                 this._parent.totalKeywordDoc.style.display = 'block';
             }
             
       	  if (this._chartInstance) {
              this._chartInstance.destroy();
          }
           
            const secondChartContainer = this._parent.secondChartContainerKeywordDoc;
            var dataResponse = this.GetKeywordDoc();
            if (!dataResponse) {
                console.error('No data received for keyword documents.');
                return;
            }

            // Extract the counts
            var totalCount = dataResponse.TotalCount || 0;
            var keywordDocCount = dataResponse.keywordDocCount || 0;
            var keywordDocPercentage = totalCount > 0 ? ((keywordDocCount / totalCount) * 100).toFixed(2) : 0;
            var nonKeywordDocCount = totalCount - keywordDocCount;
            var nonKeywordDocPercentage = totalCount > 0 ? ((nonKeywordDocCount / totalCount) * 100).toFixed(2) : 0;

            document.getElementById('totalKeywordDoc').innerHTML = this._lcl.TOTAL_KEYWORD_DOCS + ': ' + keywordDocCount;

            // Chart options for keyword document data
            var chartOptions = {
                series: [keywordDocCount, nonKeywordDocCount],
                labels: [
                    this._lcl.KEYWORD_DOCS + ': ' + this._lcl.DOCUMENT_COUNT_LABEL + ' (' + keywordDocCount + ')' + ' ' + this._lcl.PERCENTAGE_LABEL + ' (' + keywordDocPercentage + '%)',
                    this._lcl.NON_KEYWORD_DOCS + ': ' + this._lcl.DOCUMENT_COUNT_LABEL + ' (' + nonKeywordDocCount + ')' + ' ' + this._lcl.PERCENTAGE_LABEL + ' (' + nonKeywordDocPercentage + '%)',
                    this._lcl.TOTAL_DOCS + ': ' + this._lcl.DOCUMENT_COUNT_LABEL + ' (' + totalCount + ')'
                ],
                chart: {
                    type: 'pie',
                    height: 400
                },
                title: {
                    text: this._lcl.KEYWORD_DOCS_TITLE,
                    align: 'center'
                },
                tooltip: {
                    y: {
                        formatter: function (value) {
                            return value;
                        }.bind(this)
                    }
                }
            };

            // Render the chart in the secondChartContainerKeywordDoc div
            this._chartInstance = new ApexCharts(secondChartContainer, chartOptions);
            this._chartInstance.render();
        },

        // Get data from the backend for keyword documents
        GetKeywordDoc: function () {
            var toaster = new Toaster();
            var params = {
                method: "GetKeywordDoc"
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

        // Get data from the backend for electronic and archived documents
        GetElectronicAndArchiveDocCount: function () {
            var toaster = new Toaster();
            var params = {
                method: "GetElectronicAndArchiveDoc"
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
        }
    });
});