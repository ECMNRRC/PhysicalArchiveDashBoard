define([
    "dojo/_base/declare",
    "dojo/dom",
    "dojo/dom-construct",
    "ecm/model/ResultSet",
    "dojo/json",
    "dojo/i18n!./nls/localization",
    "physicalArchiveDashboardPluginDojo/Toaster"
], function (declare, dom, domConstruct, ResultSet, json, lcl, Toaster) {

    return declare("physicalArchiveDashboardPluginDojo.RequestsDashboard", null, {
        _chartInstance: null,
        _chartBorrowingInstance: null,
        _lcl: lcl,
        _parent: null,

        constructor: function (args) {
            this.toaster = new Toaster();
            this._parent = args.parent;
        },

        // Method to get data for view requests
        GetViewRequestsData: function () {
            var params = {
                method: "GetViewRequestsData"
            };

            var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService", params);
            var resultSet = new ResultSet(response);

            var results = [];
            if (!resultSet.result.startsWith("ERROR")) {
                results = JSON.parse(resultSet.result, true);
            } else {
                this._handleError(resultSet);
            }
            return results;
        },

        // Show the view requests chart and hide other sections
        getViewRequestsData: function () {
            // Hide other sections 
            // if (this._parent.borrowingRequestsContainer) {
            //     this._parent.borrowingRequestsContainer.style.display = 'none';
            // }
            // if (this._parent.borrowingRequestsChartContainer) {
            //     this._parent.borrowingRequestsChartContainer.style.display = 'none';
            // }

            // Show view requests section
            if (this._parent.totalRequestsContainer) {
                this._parent.totalRequestsContainer.style.display = 'block';
            }
            if (this._parent.viewRequestsChartContainer) {
                this._parent.viewRequestsChartContainer.style.display = 'block';
            }

            // Call the method to render the view requests chart
            this.renderViewRequestsChart();
        },

        // Render the chart for view requests
        renderViewRequestsChart: function () {
            if (this._chartInstance) {
                this._chartInstance.destroy();
            }

            var dataResponse = this.GetViewRequestsData();

            if (!dataResponse || dataResponse.length === 0) {
                console.error("No data received for view requests.");
                return;
            }

            var totalRequests = 0;
            var electronicRequestsCount = 0;
            var paperRequestsCount = 0;
            var acceptedRequestsCount = 0;
            var rejectedRequestsCount = 0;
            var completedRequestsCount = 0;

            dataResponse.forEach(item => {
                totalRequests += item.ViewRequestTypeCount;

                if (item.ViewType === "الكترونى") {
                    electronicRequestsCount += item.ViewRequestTypeCount;
                } else if (item.ViewType === "ورقى") {
                    paperRequestsCount += item.ViewRequestTypeCount;
                }

                if (item.Status === "موافقه") {
                    acceptedRequestsCount += item.ViewRequestTypeCount;
                } else if (item.Status === "رفض") {
                    rejectedRequestsCount += item.ViewRequestTypeCount;
                } else if (item.Status === "منتهي") {
                    completedRequestsCount += item.ViewRequestTypeCount;
                }
            });

            // Calculate percentages relative to the total
            var electronicPercentage = totalRequests > 0 ? ((electronicRequestsCount / totalRequests) * 100).toFixed(2) : 0;
            var paperPercentage = totalRequests > 0 ? ((paperRequestsCount / totalRequests) * 100).toFixed(2) : 0;
            var acceptedPercentage = totalRequests > 0 ? ((acceptedRequestsCount / totalRequests) * 100).toFixed(2) : 0;
            var rejectedPercentage = totalRequests > 0 ? ((rejectedRequestsCount / totalRequests) * 100).toFixed(2) : 0;
            var completedPercentage = totalRequests > 0 ? ((completedRequestsCount / totalRequests) * 100).toFixed(2) : 0;

            // Prepare the data for the chart
            var seriesData = [
                {
                    name: this._lcl.ELECTRONIC_REQUESTS + ' (' + electronicPercentage + '%)',
                    data: [electronicRequestsCount]
                },
                {
                    name: this._lcl.PAPER_REQUESTS + ' (' + paperPercentage + '%)',
                    data: [paperRequestsCount]
                },
                {
                    name: this._lcl.ACCEPTED_REQUESTS + ' (' + acceptedPercentage + '%)',
                    data: [acceptedRequestsCount]
                },
                {
                    name: this._lcl.REJECTED_REQUESTS + ' (' + rejectedPercentage + '%)',
                    data: [rejectedRequestsCount]
                },
                {
                    name: this._lcl.COMPLETED_REQUESTS + ' (' + completedPercentage + '%)',
                    data: [completedRequestsCount]
                }
            ];
            // Chart options for displaying the view requests in vertical columns
  		    var chartOptions = {
  		        series: seriesData,
  		        chart: {
  		            type: 'bar', // Bar chart
  		            height: 400,
  		            stacked: false // Not stacked to show as separate columns
  		        },
  		        plotOptions: {
  		            bar: {
  		                horizontal: false, // Ensure vertical columns
  		                columnWidth: '50%', // Adjust width of columns
  		            }
  		        },
  		        xaxis: {
  		            categories: [this._lcl.VIEW_REQUESTS_TITLE] // Just one category representing all requests
  		        },
  		        title: {
  		            text: this._lcl.VIEW_REQUESTS_TITLE, // Localized title
  		            align: 'center'
  		        },
  		        tooltip: {
  		            y: {
  		                formatter: function(value, { seriesIndex, dataPointIndex, w }) {
  		                    let percentage = '';
  		                    switch (seriesIndex) {
  		                        case 0: percentage = electronicPercentage; break;
  		                        case 1: percentage = paperPercentage; break;
  		                        case 2: percentage = acceptedPercentage; break;
  		                        case 3: percentage = rejectedPercentage; break;
  		                        case 4: percentage = completedPercentage; break;
  		                    }
  		                    return value + ' ' + this._lcl.REQUESTS ;
  		                }.bind(this)
  		            }
  		        }
  		    };

            this._chartInstance = new ApexCharts(this._parent.viewRequestsChartContainer, chartOptions);
            this._chartInstance.render();

            // Update request counts and percentages in the DOM
            this._updateRequestCounts(totalRequests, electronicRequestsCount, paperRequestsCount, acceptedRequestsCount, rejectedRequestsCount, completedRequestsCount, electronicPercentage, paperPercentage, acceptedPercentage, rejectedPercentage, completedPercentage);
        },

        // Update counts and percentages for view requests
        _updateRequestCounts: function (total, electronic, paper, accepted, rejected, completed, electronicPercentage, paperPercentage, acceptedPercentage, rejectedPercentage, completedPercentage) {
            dom.byId("totalRequestsCount").innerHTML = total;
            dom.byId("electronicRequestsCount").innerHTML = electronic;
            dom.byId("electronicPercentage").innerHTML = electronicPercentage + "%";
            dom.byId("paperRequestsCount").innerHTML = paper;
            dom.byId("paperPercentage").innerHTML = paperPercentage + "%";
            dom.byId("acceptedRequestsCount").innerHTML = accepted;
            dom.byId("acceptedPercentage").innerHTML = acceptedPercentage + "%";
            dom.byId("rejectedRequestsCount").innerHTML = rejected;
            dom.byId("rejectedPercentage").innerHTML = rejectedPercentage + "%";
            dom.byId("completedRequestsCount").innerHTML = completed;
            dom.byId("completedPercentage").innerHTML = completedPercentage + "%";
        },

        // Method to get data for borrowing requests
        GetBorrowingRequestsData: function () {
            var params = {
                method: "GetBorrowingRequestsData"
            };

            var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService", params);
            var resultSet = new ResultSet(response);

            var results = [];
            if (!resultSet.result.startsWith("ERROR")) {
                results = JSON.parse(resultSet.result, true);
            } else {
                this._handleError(resultSet);
            }
            return results;
        },

        // Show the borrowing requests chart and hide other sections
        getBorrowingRequestsData: function () {
            // Hide other sections
            // if (this._parent.firstChartContainerElectronicAndArchiveDoc) {
            //     this._parent.firstChartContainerElectronicAndArchiveDoc.style.display = 'none';
            // }
            // if (this._parent.totalCountContainer) {
            //     this._parent.totalCountContainer.style.display = 'none';
            // }
            // if (this._parent.totalRequestsContainer) {
            //     this._parent.totalRequestsContainer.style.display = 'none';
            // }
            // if (this._parent.viewRequestsChartContainer) {
            //     this._parent.viewRequestsChartContainer.style.display = 'none';
            // }

            // Show borrowing requests section
            if (this._parent.borrowingRequestsContainer) {
                this._parent.borrowingRequestsContainer.style.display = 'block';
            } else {
                console.error("Borrowing Requests Container not found");
            }
            if (this._parent.borrowingRequestsChartContainer) {
                this._parent.borrowingRequestsChartContainer.style.display = 'block';
            }
    // Update the total document count in the totalCountContainer div
    document.getElementById('totalRequestsLabel').innerHTML = this._lcl.TOTAL_VIEW_REQUESTS ;

    // Update the total document count in the totalCountContainer div
   document.getElementById('totalBorrowingLabel').innerHTML = this._lcl.TOTAL_BORROWING_REQUESTS;

            // Call the method to render the borrowing requests chart
            this.renderBorrowingRequestsChart();
        },

        // Render the chart for borrowing requests
        renderBorrowingRequestsChart: function () {
            if (this._chartBorrowingInstance) {
                this._chartBorrowingInstance.destroy();
            }

            var dataResponse = this.GetBorrowingRequestsData();

            if (!dataResponse || dataResponse.length === 0) {
                console.error("No data received for borrowing requests.");
                return;
            }

            var totalRequests = 0;
            var acceptedRequestsCount = 0;
            var rejectedRequestsCount = 0;
            var completedRequestsCount = 0;

            dataResponse.forEach(item => {
                totalRequests += item.CORR_STATUS_COUNT;

                if (item.Status === "موافقه") {
                    acceptedRequestsCount += item.CORR_STATUS_COUNT;
                } else if (item.Status === "رفض") {
                    rejectedRequestsCount += item.CORR_STATUS_COUNT;
                } else if (item.Status === "منتهي") {
                    completedRequestsCount += item.CORR_STATUS_COUNT;
                }
            });

            // Calculate percentages
            var acceptedPercentage = totalRequests > 0 ? ((acceptedRequestsCount / totalRequests) * 100).toFixed(2) : 0;
            var rejectedPercentage = totalRequests > 0 ? ((rejectedRequestsCount / totalRequests) * 100).toFixed(2) : 0;
            var completedPercentage = totalRequests > 0 ? ((completedRequestsCount / totalRequests) * 100).toFixed(2) : 0;

            // Prepare the data for the chart
            var seriesData = [
                {
                    name: this._lcl.ACCEPTED_REQUESTS + ' (' + acceptedPercentage + '%)',
                    data: [acceptedRequestsCount]
                },
                {
                    name: this._lcl.REJECTED_REQUESTS + ' (' + rejectedPercentage + '%)',
                    data: [rejectedRequestsCount]
                },
                {
                    name: this._lcl.COMPLETED_REQUESTS + ' (' + completedPercentage + '%)',
                    data: [completedRequestsCount]
                }
            ];

            
  		    // Chart options for rendering borrowing requests with vertical columns and percentages in the labels
  		    var chartOptions = {
  		        series: seriesData,
  		        chart: {
  		            type: 'bar',
  		            height: 400,
  		            stacked: false, // Not stacking bars
  		        },
  		        plotOptions: {
  		            bar: {
  		                horizontal: false, // Set to false to make vertical bars
  		                columnWidth: '50%', // Adjust the width of the columns
  		            }
  		        },
  		        xaxis: {
  		            categories: [this._lcl.TOTAL_BORROWING_REQUESTS] // Only one category for total requests
  		        },
  		        title: {
  		            text: this._lcl.BORROWING_REQUESTS_TITLE,
  		            align: 'center'
  		        },
  		        tooltip: {
  		            y: {
  		                formatter: function (value, { seriesIndex, dataPointIndex, w }) {
  		                    let percentage = '';
  		                    switch (seriesIndex) {
  		                        case 0: percentage = acceptedPercentage; break;
  		                        case 1: percentage = rejectedPercentage; break;
  		                        case 2: percentage = completedPercentage; break;
  		                    }
  		                    return value + ' ' + this._lcl.REQUESTS ;
  		                }.bind(this)
  		            }
  		        }
  		    };

            this._chartBorrowingInstance = new ApexCharts(this._parent.borrowingRequestsChartContainer, chartOptions);
            this._chartBorrowingInstance.render();

            // Update borrowing request counts and percentages in the DOM
            this._updateBorrowingRequestCounts(totalRequests, acceptedRequestsCount, rejectedRequestsCount, completedRequestsCount, acceptedPercentage, rejectedPercentage, completedPercentage);
        },

        // Update counts and percentages for borrowing requests
        _updateBorrowingRequestCounts: function (total, accepted, rejected, completed, acceptedPercentage, rejectedPercentage, completedPercentage) {
            dom.byId("totalBorrowingRequestsCount").innerHTML = total;
            dom.byId("acceptedBorrowingRequestsCount").innerHTML = accepted;
            dom.byId("acceptedBorrowingPercentage").innerHTML = acceptedPercentage + "%";
            dom.byId("rejectedBorrowingRequestsCount").innerHTML = rejected;
            dom.byId("rejectedBorrowingPercentage").innerHTML = rejectedPercentage + "%";
            dom.byId("completedBorrowingRequestsCount").innerHTML = completed;
            dom.byId("completedBorrowingPercentage").innerHTML = completedPercentage + "%";
        },

        // Handle errors
        _handleError: function (resultSet) {
            if (resultSet.result.includes("(ACCESS DENIED)")) {
                this.toaster.redToaster(this._lcl.ACCESS_DENIED);
            } else {
                this.toaster.redToaster(this._lcl.FAILED_TO_FETCH_DATA);
            }
            console.error("Failed to load data!", resultSet);
        }
    });
});
