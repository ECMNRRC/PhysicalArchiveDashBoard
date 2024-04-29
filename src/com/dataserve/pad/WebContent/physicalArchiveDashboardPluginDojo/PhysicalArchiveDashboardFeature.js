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
    Toaster    
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

        // Set to true if widget template contains DOJO widgets.
        widgetsInTemplate: false,

        postCreate: function() {
            this.logEntry("postCreate");
            this.inherited(arguments);
            this.getDocbyDate()
            this.getOperationForUser()
            
            this.firstChartRendered = false;
            this.secondChartRendered = false;
            this.userClassificationChartRendered = false;
            this.operationsChartRendered = false;

            this.logExit("postCreate");
        },


        loadContent: function() {
            this.logEntry("loadContent");
            
            if (!this.isLoaded) {
                setTimeout(lang.hitch(this, function() {
                    this.renderFirstChart();
                    this.renderSecondChart();
                    this.renderUserClassificationChart();
                    this.renderOperationsChart();
                    this.renderOperationsForUserChart();
                }), 0);
                
                this.isLoaded = true;
                this.needReset = false;
            }
            
            this.logExit("loadContent");
        },

		

		renderFirstChart: function() {
			if (!this.firstChartRendered) {
		    const dataResponse = this.getDocFilterByClass(); 

		    let departmentCounts = {};
		    let userCounts = {};
		    let classificationCounts = {};
		    let departmentLabels = [];
		    let userLabels = [];
		    let classificationLabels = [];

		    dataResponse.forEach(item => {
		    	var depName = ecm.model.desktop.valueFormatter.locale === 'en' ? item.depNameEn:item.depNameAr
		    	var userName = ecm.model.desktop.valueFormatter.locale === 'en' ? item.userNameEn:item.userNameAr
		    	var className = ecm.model.desktop.valueFormatter.locale === 'en' ? item.classNameEn:item.classNameAr
		        if (!departmentCounts[depName]) {
		            departmentCounts[depName] = 0;
		            departmentLabels.push(depName);
		        }
		        departmentCounts[depName] += item.fileCount;

		        const userKey = `${userName} (${depName})`;
		        if (!userCounts[userKey]) {
		            userCounts[userKey] = 0;
		            userLabels.push(item.userNameEn);
		        }
		        userCounts[userKey] += item.fileCount;

		        const classKey = `${className} (${userKey})`;
		        if (!classificationCounts[classKey]) {
		            classificationCounts[classKey] = 0;
		            classificationLabels.push(className); 
		        }
		        classificationCounts[classKey] += item.fileCount;
		    });

		    let departmentSeries = Object.values(departmentCounts);
		    let userSeries = Object.values(userCounts);
		    let classificationSeries = Object.values(classificationCounts);
		    let combinedLabels = departmentLabels.concat(userLabels, classificationLabels);
		    let combinedSeries = departmentSeries.concat(userSeries, classificationSeries);

		    const options = {
		        series: combinedSeries,
		        labels: combinedLabels,
		        chart: {
		            type: 'donut',
		            height: 350
		        },
		        legend: {
		            show: true,
		            position: 'right',
		            formatter: function(val, opts) {
		                const seriesIndex = opts.seriesIndex;
		                let label = combinedLabels[seriesIndex];
		                return label;
		            }
		        },
		        plotOptions: {
		            pie: {
		                donut: {
		                    labels: {
		                        show: true,
		                        total: {
		                            showAlways: true,
		                            show: true,
		                            label: 'Total Documents',
		                            formatter: function(w) {
		                                return w.globals.seriesTotals.reduce((a, b) => a + b, 0).toString();
		                            }
		                        }
		                    }
		                }
		            }
		        },
		        tooltip: {
		            y: {
		                formatter: function(value, { seriesIndex, dataPointIndex, w }) {
		                    let label = combinedLabels[dataPointIndex];
		                    return `${label}: ${value}`;
		                }
		            }
		        }
		    };

		    var chart = new ApexCharts(this.firstChartContainer, options);
		    chart.render();
		    this.firstChartRendered = true;
            }
		},
		
		

		renderSecondChart: function() {
			if(!this.secondChartRendered) {
		    const dataResponse = this.getOperationToDep(); 
		    
		    let operationsByDepartment = {};
		    dataResponse.forEach(item => {
  		    	const depName = ecm.model.desktop.valueFormatter.locale === 'en' ? item.depNameEn:item.depNameAr
  		    	const operationName = ecm.model.desktop.valueFormatter.locale === 'en' ? item.operationNameEn:item.operationNameAr

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

		    var chart = new ApexCharts(this.secondChartContainer, options);
		    chart.render();
		    this.secondChartRendered = true;
          } 
		},

	  	getDocFilterByClass: function(){
	  		debugger
			var toaster = new Toaster();
  			params = {
  					method: "GetDocFilterByClass",
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
	  		
		  	getOperationToDep: function(){
		  		debugger
				var toaster = new Toaster();
	  			params = {
	  					method: "GetOperationToDep",
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
		  		
		  		
		  		
			getOperationForCLass: function(){
			  		debugger
					var toaster = new Toaster();
		  			params = {
		  					method: "GetOperationForClass",
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

		  		renderUserClassificationChart: function() {
		  			if (!this.userClassificationChartRendered) {
		  		    const dataResponse = this.getDocFilterByClass();
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
		  		    
		  		    var chart = new ApexCharts(this.thirdChartContainer, options);
		  		    chart.render();
		  		  this.userClassificationChartRendered = true;
		          }
		  		},
		  		
		  		
		  		
		  		renderOperationsChart: function() {
		  			if (!this.operationsChartRendered) {
		  		    const dataResponse = this.getOperationForCLass();

		  		    let operationsByDeptClass = {};
		  		    let departmentSet = new Set();
		  		    let classificationSet = new Set();

		  		    dataResponse.forEach(item => {
				    	var department = ecm.model.desktop.valueFormatter.locale === 'en' ? item.depNameEn:item.depNameAr
						var classification = ecm.model.desktop.valueFormatter.locale === 'en' ? item.classNameEn:item.classNameAr

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
		  		                columnWidth: '45%', // Narrower bars
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

		  		    var chart = new ApexCharts(this.fourthChartContainer, options);
		  		    chart.render();
		  		  this.operationsChartRendered = true;
		            }
		  		},
		  		
		  		
			  	getOperationForUser: function(){
			  		debugger
					var toaster = new Toaster();
		  			params = {
		  					method: "GetOperationForUser",
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
			  		
			  		

			  		renderOperationsForUserChart: function() {
			  		    const dataResponse = this.getOperationForUser();

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
			  		            text: this._lcl.FOURTH_CHART_TITLE,
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

			  		    var chart = new ApexCharts(this.fifthChartContainer, options);
			  		    chart.render();
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

				  		    // Organize data by week and class
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

				  		    // Convert the organized data into series format for ApexCharts
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

				  		    // Process the data to fit the ApexCharts series format
				  		    var processedChartData = this.processDataForStackedBarChart(dataResponse);

				  		    // Define the chart options
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

			  		
			  		







	});
});
