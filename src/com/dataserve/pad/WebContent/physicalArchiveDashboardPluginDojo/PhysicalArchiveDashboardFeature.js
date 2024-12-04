define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    "dojo/html",
    "dojo/dom",
    "dojo/dom-class",
    "dojo/dom-construct",
    "ecm/widget/dialog/BaseDialog",
    "ecm/model/ResultSet",
    "dojo/json",
    "dojo/i18n!./nls/localization",
    "ecm/widget/layout/_LaunchBarPane",
    "dojo/text!./templates/PhysicalArchiveDashboardFeature.html",
    "physicalArchiveDashboardPluginDojo/Toaster",
    "physicalArchiveDashboardPluginDojo/RequestsDashboard",
    "physicalArchiveDashboardPluginDojo/DocumentsDashboard",
    "physicalArchiveDashboardPluginDojo/DestructionDashboard",
    "dijit/form/Select",

 

],
function(
    declare,
    lang,
    html,
    dom,
    domClass,
    domConstruct,
    BaseDialog,
    ResultSet,
    json,
    lcl,
    _LaunchBarPane,
    template,
    Toaster,
    RequestsDashboard,
    DocumentsDashboard,
    DestructionDashboard,
    Select
    
) {
    /**
     * @name physicalArchiveDashboardPluginDojo.PhysicalArchiveDashboardFeature
     * @class 
     * @augments ecm.widget.layout._LaunchBarPane
     */
    return declare("physicalArchiveDashboardPluginDojo.PhysicalArchiveDashboardFeature", [
        _LaunchBarPane
    ], {
        /** @lends physicalArchiveDashboardPluginDojo.PhysicalArchiveDashboardFeature.prototype */

        templateString: template,
        _lcl: lcl,
        _chartInstance:null,
        filterClassCharInst:null,
        filterDepartmentId:null,

        // Set to true if widget template contains DOJO widgets.
        widgetsInTemplate: false,

        postCreate: function() {
        	    	
			var params = {};
			params.parent=this;
			this.requestsDashboard = new RequestsDashboard(params);
			this.documentsDashboard = new DocumentsDashboard(params);
			this.destructionDashboard = new DestructionDashboard(params);
			debugger
            this.logEntry("postCreate");
            this.inherited(arguments);
			this.addDepSelect()
//			this.addDepartmentFilter(); // Initialize department filter
            
            this.firstChartRendered = false;
            this.secondChartRendered = false;
            this.userClassificationChartRendered = false;
            this.operationsChartRendered = false;

            this.logExit("postCreate");
        },


        loadContent: function() {
            this.logEntry("loadContent");
            
            if (!this.isLoaded) {

                
                this.isLoaded = true;
                this.needReset = false;
            }
            
            this.logExit("loadContent");
        },

		
        getDestructionsTables:function(){

			this.destructionDashboard.getArchiveCenterTransferReadyFilesTable(this.filterDepartmentId);
     
			this.destructionDashboard.getArchiveCenterTransferdFilesTable(this.filterDepartmentId);
		
			this.destructionDashboard.getNationalCenterTransferReadyFilesTable(this.filterDepartmentId);
			
			this.destructionDashboard.getNationalCenterTransferdFilesTable(this.filterDepartmentId);
			
			this.destructionDashboard.getDestoredFilesTable(this.filterDepartmentId);
			
			this.destructionDashboard.getAllReadyDestroyFilesTable(this.filterDepartmentId);

        },
        


        
        
        getElectronicAndArchiveDoc: function () {
            var dataResponse = this.documentsDashboard.getElectronicAndArchiveDoc();
            var dataResponse = this.documentsDashboard.renderChartsKeywordDoc();
			this.documentsDashboard.getCalculatedMigratedDocumentsStatsTable(this.filterDepartmentId);
			this.documentsDashboard.getArchiveDocDepartmentable(this.filterDepartmentId);
			this.documentsDashboard.getConfidentialDocClassificationTable(this.filterDepartmentId);
			this.documentsDashboard.getArchiveDocClassificationtable(this.filterDepartmentId);
        },
        
      
        
		 getViewRequestsData: function () {
            var dataResponse = this.requestsDashboard.getViewRequestsData();
            var dataResponse = this.requestsDashboard.getBorrowingRequestsData();
        },

    
		renderSecondChart: function(dataRes) {
		    const dataResponse = dataRes;
		    let operationsByDepartment = {};
		    dataResponse.forEach(item => {
		        const depName = ecm.model.desktop.valueFormatter.locale === 'en' ? item.depNameEn : item.depNameAr;
		        const operationName = ecm.model.desktop.valueFormatter.locale === 'en' ? item.operationNameEn : item.operationNameAr;

		        if (!operationsByDepartment[depName]) {
		            operationsByDepartment[depName] = {};
		        }
		        if (!operationsByDepartment[depName][operationName]) {
		            operationsByDepartment[depName][operationName] = 0;
		        }
		        operationsByDepartment[depName][operationName]++;
		    });

		    let series = [];
		    let departments = Object.keys(operationsByDepartment);
		    let operations = {};
		    departments.forEach(dep => {
		        Object.keys(operationsByDepartment[dep]).forEach(op => {
		            if (!operations[op]) {
		                operations[op] = {
		                    name: op,
		                    data: []
		                };
		            }
		            operations[op].data.push(operationsByDepartment[dep][op]);
		        });
		    });

		    let colors = ['#008FFB', '#00E396', '#FEB019', '#FF4560', '#775DD0'];
		    let colorIndex = 0;
		    for (let op in operations) {
		        operations[op].color = colors[colorIndex % colors.length];
		        series.push(operations[op]);
		        colorIndex++;
		    }

		    const options = {
		        series: series,
		        chart: {
		            type: 'bar',
		            height: 350,
		            stacked: false
		        },
		        colors: series.map(s => s.color),
		        plotOptions: {
		            bar: {
		                horizontal: false,
		                columnWidth: '70%',
		                endingShape: 'rounded'
		            },
		        },
		        dataLabels: {
		            enabled: false
		        },
		        xaxis: {
		            categories: departments
		        },
		        yaxis: {
		            title: {
		                text: "Count"
		            }
		        },
		        title: {
		            text: this._lcl.THIRD_CHART_TITLE,
		            align: 'center',
		        },
		        tooltip: {
		            y: {
		                formatter: function(value) {
		                    return `Count: ${value}`;
		                }
		            }
		        }
		    };

		    if (this.secondChartRendered) {
		        if (this.chart) {
		            this.chart.destroy();
		        }
		    }

		    this.chart = new ApexCharts(this.secondChartContainer, options);
		    this.chart.render();
		    this.secondChartRendered = true;
		},


	  	getDocFilterByClass: function(dataObj){
	  		debugger
			var toaster = new Toaster();
  			params = {
  					method: "GetDocFilterByClass",
  					dataObj:JSON.stringify(dataObj)
					};
	  			
	 		var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService",params);
	 		var resultSet = new ResultSet(response);
	       
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
				
			this.groups = JSON.parse(JSON.stringify(results));
			return results;
	  		},
	  		
		  	getOperationToDep: function(dataObj){
		  		debugger
				var toaster = new Toaster();
	  			params = {
	  					method: "GetOperationToDep",
	  					dataObj:JSON.stringify(dataObj)

						};
		  			
		 		var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService",params);
		 		var resultSet = new ResultSet(response);
		       
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
					
				this.groups = JSON.parse(JSON.stringify(results));
				debugger
				return results;
		  		},	
		  		
		  		
		  		
			getOperationForCLass: function(dataObj){
			  		debugger
					var toaster = new Toaster();
		  			params = {
		  					method: "GetOperationForClass",
		  					dataObj: JSON.stringify(dataObj)

							};
			  			
			 		var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService",params);
			 		var resultSet = new ResultSet(response);
			       
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
						
					this.groups = JSON.parse(JSON.stringify(results));
					debugger
					return results;
			  		},	
	  		
		  		
		  		processDataForUserClassificationChart: function(dataResponse) {
		  		    let users = {};
		  		    let departmentClassifications = {};
		  		    let departments = {}; 
		  		    let series = [];

		  		    dataResponse.forEach(item => {
		  		    	const dep = ecm.model.desktop.valueFormatter.locale === 'en' ? item.depNameEn:item.depNameAr
						const cls = ecm.model.desktop.valueFormatter.locale === 'en' ? item.classNameEn:item.classNameAr
		  		        const user = ecm.model.desktop.valueFormatter.locale === 'en' ? item.userNameEn:item.userNameAr;
		  		        const count = item.fileCount;

		  		        departments[dep] = true;
		  		        users[user] = users[user] || {};
		  		        departmentClassifications[dep] = departmentClassifications[dep] || new Set();

		  		        departmentClassifications[dep].add(cls);

		  		        users[user][`${dep} - ${cls}`] = (users[user][`${dep} - ${cls}`] || 0) + count;
		  		    });

		  		    Object.keys(users).forEach(user => {
		  		        let userSeries = {
		  		            name: user,
		  		            data: []
		  		        };

		  		        Object.keys(departments).forEach(dep => {
		  		            departmentClassifications[dep].forEach(cls => {
		  		                let classificationKey = `${dep} - ${cls}`;
		  		                userSeries.data.push(users[user][classificationKey] || 0);
		  		            });
		  		        });

		  		        series.push(userSeries);
		  		    });

		  		    let categories = [];
		  		    Object.keys(departments).forEach(dep => {
		  		        departmentClassifications[dep].forEach(cls => {
		  		            categories.push(`${dep} - ${cls}`);
		  		        });
		  		    });

		  		    return {
		  		        series: series,
		  		        categories: categories
		  		    };
		  		},


		  		
		  		
		  		renderUserClassificationChart: function(dataRes) {
		  		    const dataResponse = dataRes;
		  		    let processedData = this.processDataForUserClassificationChart(dataResponse);

		  		    const options = {
		  		        series: processedData.series,
		  		        chart: {
		  		            type: 'bar',
		  		            height: 600, 
		  		            stacked: true
		  		        },
		  		        plotOptions: {
		  		            bar: {
		  		                horizontal: false,
		  		                columnWidth: '70%',
		  		                dataLabels: {
		  		                    position: 'top',
		  		                }
		  		            }
		  		        },
		  		        dataLabels: {
		  		            enabled: true
		  		        },
		  		        xaxis: {
		  		            categories: processedData.categories,
		  		            labels: {
		  		                rotate: -45,
		  		                trim: false,
		  		                minHeight: 120,
		  		                style: {
		  		                    cssClass: 'apexcharts-xaxis-label',
		  		                },
		  		            },
		  		            axisTicks: {
		  		                show: true
		  		            }
		  		        },
		  		        yaxis: {
		  		            title: {
		  		                text: 'Document Count'
		  		            }
		  		        },
		  		        tooltip: {
		  		            shared: true,
		  		            intersect: false,
		  		            y: {
		  		                formatter: function(val) {
		  		                    return val + " documents";
		  		                }
		  		            }
		  		        },
		  		        title: {
		  		            text: this._lcl.FIRST_CHART_TITLE,
		  		            align: 'center',
		  		        },
		  		        legend: {
		  		            position: 'right',
		  		            offsetY: 10
		  		        },
		  		        grid: {
		  		            padding: {
		  		                bottom: 30  
		  		            }
		  		        }
		  		    };
		  		    
		  		    if (this.userClassificationChartRendered) {
		  		        this.userClassificationChart.destroy();
		  		    }
		  		    
		  		    this.userClassificationChart = new ApexCharts(this.thirdChartContainer, options);
		  		    this.userClassificationChart.render();
		  		    
		  		    this.userClassificationChartRendered = true;
		  		},

		  		
		  		
		  		renderOperationsChart: function(dataRes) {
		  		    if (this.operationsChart) {
		  		        this.operationsChart.destroy();
		  		    }

		  		    const dataResponse = dataRes;

		  		    let operationsByDeptClass = {};
		  		    let departmentSet = new Set();
		  		    let classificationSet = new Set();

		  		    dataResponse.forEach(item => {
		  		        var department = ecm.model.desktop.valueFormatter.locale === 'en' ? item.depNameEn : item.depNameAr;
		  		        var classification = ecm.model.desktop.valueFormatter.locale === 'en' ? item.classNameEn : item.classNameAr;

		  		        departmentSet.add(department);
		  		        classificationSet.add(classification);

		  		        const deptClassKey = `${department}-${classification}`;

		  		        if (!operationsByDeptClass[deptClassKey]) {
		  		            operationsByDeptClass[deptClassKey] = 0;
		  		        }
		  		        operationsByDeptClass[deptClassKey] += 1;
		  		    });

		  		    let series = Array.from(classificationSet).map(classification => {
		  		        return {
		  		            name: classification,
		  		            data: Array.from(departmentSet).map(department => {
		  		                const deptClassKey = `${department}-${classification}`;
		  		                return operationsByDeptClass[deptClassKey] || 0;
		  		            })
		  		        };
		  		    });

		  		    let categories = Array.from(departmentSet);

		  		    const options = {
		  		        series: series,
		  		        chart: {
		  		            type: 'bar',
		  		            height: 600,
		  		            stacked: true
		  		        },
		  		        plotOptions: {
		  		            bar: {
		  		                horizontal: false,
		  		                columnWidth: '45%',
		  		                barHeight: '75%'
		  		            }
		  		        },
		  		        xaxis: {
		  		            categories: categories,
		  		            tickPlacement: 'on'
		  		        },
		  		        yaxis: {
		  		            title: {
		  		                text: 'Number of Operations'
		  		            }
		  		        },
		  		        tooltip: {
		  		            y: {
		  		                formatter: function(val) {
		  		                    return val + " operations";
		  		                }
		  		            }
		  		        },
		  		        title: {
		  		            text: this._lcl.FOURTH_CHART_TITLE,
		  		            align: 'center',
		  		        },
		  		        legend: {
		  		            position: 'right',
		  		            offsetY: 10
		  		        },
		  		        grid: {
		  		            padding: {
		  		                bottom: 30  
		  		            }
		  		        }
		  		    };

		  		    this.operationsChart = new ApexCharts(this.fourthChartContainer, options);
		  		    this.operationsChart.render();
		  		},

		  		
		  		
			  	getOperationForUser: function(dataObj){
			  		debugger
					var toaster = new Toaster();
		  			params = {
		  					method: "GetOperationForUser",
		  					dataObj:JSON.stringify(dataObj)
							};
			  			
			 		var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService",params);
			 		var resultSet = new ResultSet(response);
			       
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
						
					this.groups = JSON.parse(JSON.stringify(results));
					return results;
			  		},
			  		
			  		

			  		renderOperationsForUserChart: function(dataRes) {
			  		    const dataResponse = dataRes;

			  		    let operationsByDept = {};

			  		    dataResponse.forEach(item => {
			  		    	let dep = ecm.model.desktop.valueFormatter.locale === 'en' ? item.depNameEn:item.depNameAr;
			  		    	let opr = ecm.model.desktop.valueFormatter.locale === 'en' ? item.operationNameEn:item.operationNameAr;
			  		    	let user = ecm.model.desktop.valueFormatter.locale === 'en' ? item.userNameEn:item.userNameAr;
			  		        let deptOperation = `${dep} - ${opr}`;
			  		        if (!operationsByDept[dep]) {
			  		            operationsByDept[dep] = {};
			  		        }
			  		        if (!operationsByDept[dep][deptOperation]) {
			  		            operationsByDept[dep][deptOperation] = {};
			  		        }
			  		        operationsByDept[dep][deptOperation][user] = item.operationCount;
			  		    });

			  		    let departments = Object.keys(operationsByDept).sort();
			  		    let categories = [];
			  		    departments.forEach(dept => {
			  		        let operations = Object.keys(operationsByDept[dept]).sort();
			  		        operations.forEach(op => {
			  		            categories.push(op);
			  		        });
			  		    });

			  		    let series = [];
			  		    dataResponse.forEach(item => {
			  		    	let dep = ecm.model.desktop.valueFormatter.locale === 'en' ? item.depNameEn:item.depNameAr;
			  		    	let opr = ecm.model.desktop.valueFormatter.locale === 'en' ? item.operationNameEn:item.operationNameAr;
			  		    	let user = ecm.model.desktop.valueFormatter.locale === 'en' ? item.userNameEn:item.userNameAr;
			  		        let operation = `${dep} - ${opr}`;
			  		        let userIndex = series.findIndex(s => s.name === user);
			  		        if (userIndex === -1) {
			  		            series.push({
			  		                name: user,
			  		                data: categories.map(cat => (cat === operation ? item.operationCount : 0))
			  		            });
			  		        } else {
			  		            series[userIndex].data[categories.indexOf(operation)] += item.operationCount;
			  		        }
			  		    });

			  		    const options = {
			  		        series: series,
			  		        chart: {
			  		            type: 'bar',
			  		            height: 400,
			  		            stacked: true
			  		        },
			  		        plotOptions: {
			  		            bar: {
			  		                horizontal: false,
			  		                columnWidth: '45%',
			  		                barHeight: '75%'
			  		            }
			  		        },
			  		        title: {
			  		            text: this._lcl.FIFTH_CHART_TITLE,
			  		            align: 'center',
			  		        },
			  		        xaxis: {
			  		            categories: categories
			  		        },
			  		        tooltip: {
			  		            y: {
			  		                formatter: function (value, { seriesIndex, dataPointIndex, w }) {
			  		                    let user = w.config.series[seriesIndex].name;
			  		                    let [dept, operation] = w.config.xaxis.categories[dataPointIndex].split(' - ');
			  		                    return `User: ${user}<br>Department: ${dept}<br>Operation: ${operation}<br>Count: ${value}`;
			  		                }
			  		            }
			  		        },
			  		    };

			  		    if (this.fifthChart) {
			  		        this.fifthChart.destroy();
			  		    }

			  		    this.fifthChart = new ApexCharts(this.fifthChartContainer, options);
			  		    this.fifthChart.render();
			  		},
			  		
			  		
			  		filterDataByDate: function(){

			              this.renderCharts();
			  		},
			  		
					
	  		
				  	getDocbyDate: function(){
				  		debugger
						var toaster = new Toaster();
			  			params = {
			  					method: "GetDocByDate",
								};
				  			
				 		var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService",params);
				 		var resultSet = new ResultSet(response);
				       
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
							
						this.groups = JSON.parse(JSON.stringify(results));
						return results;
				  		},
				  		
				  		
				  		getDocByFilteredDate: function(dateTo, dateFrom){
				  			debugger
				  			var toaster = new Toaster();
				  			params = {
				  					method: "GetDocByFilteredDate",
				  					dateTo:dateTo,
				  					dateFrom:dateFrom
				  			};
				  			
				  			var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService",params);
				  			var resultSet = new ResultSet(response);
				  			
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
				  			
				  			this.groups = JSON.parse(JSON.stringify(results));
				  			return results;
				  		},
				  		
				  		processDataForStackedBarChart: function(dataResponse) {
				  		    let dataByWeekAndClass = {};

				  		    dataResponse.forEach(function(item) {
				  		        const weekYearKey = `Week ${item.weekNum}, ${item.year}`;
				  		        const className = ecm.model.desktop.valueFormatter.locale === 'en' ? item.classNameEn : item.classNameAr;
				  		        
				  		        if (!dataByWeekAndClass[weekYearKey]) {
				  		            dataByWeekAndClass[weekYearKey] = {};
				  		        }
				  		        
				  		        if (!dataByWeekAndClass[weekYearKey][className]) {
				  		            dataByWeekAndClass[weekYearKey][className] = 0;
				  		        }
				  		        
				  		        dataByWeekAndClass[weekYearKey][className] += item.classCount;
				  		    });

				  		    let categories = Object.keys(dataByWeekAndClass).sort();
				  		    let seriesData = {};

				  		    for (let weekYearKey in dataByWeekAndClass) {
				  		        for (let className in dataByWeekAndClass[weekYearKey]) {
				  		            if (!seriesData[className]) {
				  		                seriesData[className] = {
				  		                    name: className,
				  		                    data: Array(categories.length).fill(0) // Initialize with zeros
				  		                };
				  		            }
				  		            const index = categories.indexOf(weekYearKey);
				  		            seriesData[className].data[index] = dataByWeekAndClass[weekYearKey][className];
				  		        }
				  		    }

				  		    let series = Object.keys(seriesData).map(key => seriesData[key]);

				  		    return { categories, series };
				  		},
				  		
				  		renderCharts: function() {
				  			
				  		    if (this._chartInstance) {
				  		        this._chartInstance.destroy();
				  		    }
				  		    
				  			var dataResponse;
				  			var dateTo = this.dateTo.value
				  			var dateFrom = this.dateFrom.value
				  			if(!dateTo& !dateFrom){
					  		    var dataResponse = this.getDocbyDate();

				  			}
				  			else{
					  		    var dataResponse = this.getDocByFilteredDate(dateTo, dateFrom);

				  			}

				  		    var processedChartData = this.processDataForStackedBarChart(dataResponse);

				  		    var chartOptions = {
				  		        series: processedChartData.series,
				  		        chart: {
				  		            type: 'bar',
				  		            height: 500,
				  		            stacked: true,
				  		            toolbar: {
				  		                show: true
				  		            },
				  		            zoom: {
				  		                enabled: true
				  		            }
				  		        },
				  		        responsive: [{
				  		            breakpoint: 480,
				  		            options: {
				  		                legend: {
				  		                    position: 'bottom',
				  		                    offsetX: -10,
				  		                    offsetY: 0
				  		                }
				  		            }
				  		        }],
				  		        plotOptions: {
				  		            bar: {
				  		                horizontal: false,
				  		            },
				  		        },
				  		        xaxis: {
				  		            categories: processedChartData.categories,
				  		            title: {
				  		                text: 'Weeks'
				  		            }
				  		        },
				  		        yaxis: {
				  		            title: {
				  		                text: 'Number of Documents'
				  		            }
				  		        },
				  		        legend: {
				  		            position: 'right',
				  		            offsetY: 50
				  		        },
				  		        fill: {
				  		            opacity: 1
				  		        },
				  		        tooltip: {
				  		            y: {
				  		                formatter: function(val) {
				  		                    return val + " documents";
				  		                }
				  		            }
				  		        },
				  		        title: {
				  		            text: 'Weekly Document Count by Class',
				  		            align: 'center'
				  		        }
				  		    };

				  		    this._chartInstance = new ApexCharts(this.sixthChartContainer, chartOptions);
				  		    this._chartInstance.render();
				  		},
				  		
				  		
				  		
				  		getFilterData : function() {
				  			debugger
							params = {
								method : "GetFilterData",
							};

					  			
					 		var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService",params);
					 		var resultSet = new ResultSet(response);
					       
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
								
							this.groups = JSON.parse(JSON.stringify(results));
							return results;

						},
						
						addDepSelect: function() {
						    var departments = this.getFilterData();
						    var deps = this.GetDepartments();
						    var classes = this.getClassesData();
						    var opt = this.GetOperation();
						    this.filterContainer.style.display = 'flex';
						    this.filterContainer.style.flexDirection = 'row';
						    this.filterContainer.style.alignItems = 'center';
						    this.filterContainer.style.flexWrap = 'nowrap';
						    this.filterContainer.style.width = '100%';
						    this.filterContainer.style.justifyContent = 'space-between';

						    this.selectWidgets = {};

						    var createFilterItem = function(labelText, selectId, optionFiller, selectData) {
						        var container = domConstruct.create("div", {
						            style: 'display: flex; flex-direction: row; align-items: center; margin-right: 5px; flex: 1 1 auto;'
						        }, this.filterContainer.domNode);

						        domConstruct.create("label", {
						            innerHTML: labelText,
						            for: selectId,
						            style: 'margin-right: 5px; width: 100px;'
						        }, container);

						        var select = new Select({
						            id: selectId,
						            options: optionFiller(selectData),
						            style: 'width: 300px;',
						            value: "" 
						        });
						        select.placeAt(container);

						        this.selectWidgets[selectId] = select;

						        this.filterContainer.domNode.appendChild(container);
						    }.bind(this);

						    createFilterItem(lcl.DEPARTMENTS, 'departmentSelect', this.deptsSelectFiller, deps);
						    createFilterItem(lcl.EMP, 'empSelect', this.empSelectFiller, []); 
						    createFilterItem(lcl.CLASSIFICATION, 'classSelect', this.classSelectFiller, classes);
						    createFilterItem(lcl.OPERATION, 'operationSelect', this.operationSelectFiller, opt);

						    this.selectWidgets['departmentSelect'].on('change', function() {
						        console.log("Department changed");
						        this.updateEmployeeSelect();
						    }.bind(this));

						    this.selectWidgets['empSelect'].on('change', function() {
						        console.log("Employee selected: ", this.selectWidgets['empSelect'].get('value'));
						    }.bind(this));
						},

						updateEmployeeSelect: function() {
						    var dep = this.getSelectValueAndOption("departmentSelect");
						    var employees = this.getEmployeesByDepartment(dep.value.toString()); // Fetch employees based on department
						    var empSelect = this.selectWidgets['empSelect'];

						    empSelect.set('options', this.empSelectFiller([]));

						    empSelect.set('options', this.empSelectFiller(employees));

						    empSelect.set('value', '');
						},

						getEmployeesByDepartment: function(departmentId) {
							var toaster = new Toaster();
						    try {
						        var params = {
						            method: "GetUsersByDepartmentId",
						            depId: departmentId
						        };

						        var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService", params);

						        var resultSet = new ResultSet(response);

						        var results = [];
						        if (!resultSet.result.startsWith("ERROR")) {
						            results = this.fullStructure = json.parse(resultSet.result, true);
						            if (results.length === 0) {
						                toaster.redToaster(lcl.USER_DEPARTMENT_UNDEFINED);
						                
						               
						            }
						        } else {
						            if (resultSet.result.includes("(ACCESS DENIED)")) {
						                toaster.redToaster(lcl.ACCESS_DENIED);
						                console.log(resultSet.result);
						            } else {
						                toaster.redToaster(lcl.FAILED_TO_FETCH_DATA);
						            }
						           
						            
						        }
						        return results;
						    } catch (error) {
						        console.error("An error occurred while fetching departments:", error);
						        return [];
						    }
						},

						



						

						getSelectValueAndOption: function(selectId) {
						    var selectWidget = this.selectWidgets[selectId];
						    if (selectWidget) {
						        var options = selectWidget.get('options');
						        var selectedOption = options.find(option => option.selected === true);
						        if (selectedOption) {
						            return { value: selectedOption.value, label: selectedOption.label };
						        } else {
						            console.error('No selected option found for select widget with id ' + selectId);
						            return null;
						        }
						    } else {
						        console.error('Select widget with id ' + selectId + ' not found.');
						        return null;
						    }
						},


						filterDataBtn: function(){
							var dep = this.getSelectValueAndOption("departmentSelect")
							var emp = this.getSelectValueAndOption("empSelect")
							var clas = this.getSelectValueAndOption("classSelect")
							var op = this.getSelectValueAndOption("operationSelect")
							
							console.log(dep.value+ " "+ dep.label)
							console.log(emp.value+ " "+ emp.label)
							console.log(clas.value+ " "+ clas.label)
							console.log(op.value+ " "+ op.label)
							var dataObj = {departmentId:dep.value.toString(),
											employeeId:emp.value,
											classificationId:clas.value,
											operationId:op.value
								};


			                    this.renderUserClassificationChart(this.getDocFilterByClass(dataObj));
			                    this.renderSecondChart(this.getOperationToDep(dataObj));
			                    this.renderOperationsChart(this.getOperationForCLass(dataObj));
			                    this.renderOperationsForUserChart(this.getOperationForUser(dataObj));


			                    


//							}
							console.log( "data Obj "+ dataObj)
							console.log( "data Obj ", dataObj)



						},


				

						deptsSelectFiller: function(departments) {
						    var result = [{label: " ", value: " "}]; 
						    var addedDepartments = new Set();

						    for (var department of departments) {
						        var label = ecm.model.desktop.valueFormatter.locale === 'en' ? department.nameEn : department.nameAr;
						        if (!addedDepartments.has(label)) {
						            result.push({label: label, value: department.id});
						            addedDepartments.add(label);
						        }
						    }

						    console.log("Department Options: ", result); 

						    return result;
						},

						empSelectFiller: function(employees) {
						    var result = [{label: " ", value: " "}]; 
						    var addedEmp = new Set();

						    for (var emp of employees) {
						        var label = ecm.model.desktop.valueFormatter.locale === 'en' ? emp.nameEn : emp.nameAr;
						        if (!addedEmp.has(label)) {
						            result.push({label: label, value: emp.UsernameLDAP});
						            addedEmp.add(label);
						        }
						    }

						    console.log("Employee Options: ", result); 

						    return result;
						},
						classSelectFiller: function(classes) {
						    var result = [{label: " ", value: " "}]; 
						    var addedClass = new Set();

						    for (var cls of classes) {
						        var label = ecm.model.desktop.valueFormatter.locale === 'en' ? cls.nameEn : cls.nameAr;
						        if (!addedClass.has(label)) {
						            result.push({label: label, value: cls.sympolicName});
						            addedClass.add(label);
						        }
						    }

						    console.log("Class Options: ", result); 

						    return result;
						},

						operationSelectFiller: function(opts) {
						    var result = [{label: " ", value: " "}]; 
						    var addedOperation = new Set();

						    for (var opt of opts) {
						        var label = ecm.model.desktop.valueFormatter.locale === 'en' ? opt.opNameEn : opt.opNameAr;
						        if (!addedOperation.has(label)) {
						            result.push({label: label, value: opt.opId});
						            addedOperation.add(label);
						        }
						    }

						    console.log("Operation Options: ", result); 

						    return result;
						},

						
						getClassesData: function() {
							var toaster = new Toaster();
						    try {
						        debugger;
						        var params = {
						            method: "GetClassificationsByUser",
						            userId: ecm.model.desktop.userId
						        };

						        var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService", params);

						        var resultSet = new ResultSet(response);

						        var results = [];
						        if (!resultSet.result.startsWith("ERROR")) {
						            results = this.fullStructure = json.parse(resultSet.result, true);
						        } else {
						            if (resultSet.result.includes("(ACCESS DENIED)")) {
						                toaster.redToaster(lcl.ACCESS_DENIED);
						            } else {
						                toaster.redToaster(lcl.FAILED_TO_FETCH_DATA);
						            }
						            console.log("Failed to load data!");
						            console.log(resultSet);
						        }

						        this.classes = JSON.parse(JSON.stringify(results));
						        return results;
						    } catch (error) {
						        console.error("An error occurred while fetching classes data:", error);
						        toaster.redToaster(lcl.FAILED_TO_FETCH_DATA);
						        return [];
						    }
						},

						
						
						GetDepartments: function() {
							var toaster = new Toaster();
						    try {
						        debugger;
						        var params = {
						            method: "GetUserDepartments",
						            userId: ecm.model.desktop.userId
						        };

						        var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService", params);

						        var resultSet = new ResultSet(response);

						        var results = [];
						        if (!resultSet.result.startsWith("ERROR")) {
						            results = this.fullStructure = json.parse(resultSet.result, true);
						            if (results.length === 0) {
						                toaster.redToaster(lcl.USER_DEPARTMENT_UNDEFINED);
						                
						               
						            }
						        } else {
						            if (resultSet.result.includes("(ACCESS DENIED)")) {
						                toaster.redToaster(lcl.ACCESS_DENIED);
						                console.log(resultSet.result);
						            } else {
						                toaster.redToaster(lcl.FAILED_TO_FETCH_DATA);
						            }
						           
						            
						        }
						        return results;
						    } catch (error) {
						        console.error("An error occurred while fetching departments:", error);
						        toaster.redToaster(lcl.FAILED_TO_FETCH_DATA);
						       
						        
						        return [];
						    }
						},

						
						GetOperation: function() {
							var toaster = new Toaster();
						    try {
						        debugger;
						        var params = {
						            method: "GetAllOperation",
						        };

						        var response = ecm.model.Request.invokeSynchronousPluginService("PhysicalArchiveDashboardPlugin", "PhysicalArchiveDashBoardService", params);

						        var resultSet = new ResultSet(response);

						        var results = [];
						        if (!resultSet.result.startsWith("ERROR")) {
						            results = this.fullStructure = json.parse(resultSet.result, true);
						            if (results.length === 0) {
						                toaster.redToaster(lcl.USER_DEPARTMENT_UNDEFINED);
						                
						               
						            }
						        } else {
						            if (resultSet.result.includes("(ACCESS DENIED)")) {
						                toaster.redToaster(lcl.ACCESS_DENIED);
						                console.log(resultSet.result);
						            } else {
						                toaster.redToaster(lcl.FAILED_TO_FETCH_DATA);
						            }
						           
						            
						        }
						        return results;
						    } catch (error) {
						        console.error("An error occurred while fetching operations:", error);
						        toaster.redToaster(lcl.FAILED_TO_FETCH_DATA);
						       
						        
						        return [];
						    }
						}









	});
});
