zk.load("zul.wgt", function () {
	zul.wgt.InfovisDiv = zk.$extends(zul.wgt.Div, {
		_labelType : '',
		_useGradients : '',
		_nativeTextSupport: false,
		_animate: false,
		bind_: function(desktop, skipper, after){
			this.$supers('bind_', arguments);
			  var ua = navigator.userAgent,
			  iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
			  typeOfCanvas = typeof HTMLCanvasElement,
			  nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
			  textSupport = nativeCanvasSupport 
			    && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
			  //I'm setting this based on the fact that ExCanvas provides text support for IE
			  //and that as of today iPhone/iPad current text support is lame
			  this._labelType = (!nativeCanvasSupport || (textSupport && !iStuff))? 'Native' : 'HTML';
			  this._nativeTextSupport = this._labelType == 'Native';
			  this._useGradients = nativeCanvasSupport;
			  this._animate = !(iStuff || !nativeCanvasSupport);
		},
		unbind_: function () {
	        this.$supers('unbind_', arguments);
	    },
		_init: function(treeData){
			var removing = false;
		    //init Spacetree
		    //Create a new ST instance
		    var wrapper = '' + this.$n().id; //this.$n() return the root html element of this widget
		    this._st = new $jit.ST({
		        'injectInto': wrapper,
		        //add styles/shapes/colors
		        //to nodes and edges
		        
		        //set overridable=true if you want
		        //to set styles for nodes individually 
		        Node: {
		          overridable: true,
		          width: 60,
		          height: 20,
		          color: '#ccc'
		        },
		        //change the animation/transition effect
		        transition: $jit.Trans.Quart.easeOut,
		        
		        onBeforeCompute: function(node){
		        	console.log("loading " + node.name);
		        },
		        
		        onAfterCompute: function(node){
		        	console.log("done");
		        },
		
		        //This method is triggered on label
		        //creation. This means that for each node
		        //this method is triggered only once.
		        //This method is useful for adding event
		        //handlers to each node label.
		        onCreateLabel: function(label, node){
		            //add some styles to the node label
		            var style = label.style;
		            label.id = node.id;
		            style.color = '#333';
		            style.fontSize = '0.8em';
		            style.textAlign = 'center';
		            style.width = "60px";
		            style.height = "20px";
		            label.innerHTML = node.name;
		            //Delete the specified subtree 
		            //when clicking on a label.
		            //Only apply this method for nodes
		            //in the first level of the tree.
		            console.log(wrapper);
		            if(node._depth == 1) {
		                style.cursor = 'pointer';
		                label.onclick = function() {
		                    if(!removing) {
		                        removing = true;
		                        console.log("removing subtree...");  
		                        //remove the subtree
		                        var currentST = zk.Widget.$("$" + zk.Widget.$("$selectedST").getValue());
		                        currentST._st.removeSubtree(label.id, true, 'animate', {
		                            hideLabels: false,
		                            onComplete: function() {
		                              removing = false;
		                              zAu.send(new zk.Event(currentST, "onRemove", {'' : {'data' : {'nodeId': label.id}}}, {toServer:true}));
		                              console.log("subtree removed");
		                            }
		                        });
		                    }
		                };
		            };
		        },
		        //This method is triggered right before plotting a node.
		        //This method is useful for adding style 
		        //to a node before it's being rendered.
		        onBeforePlotNode: function(node) {
		            if (node._depth == 1) {
		                node.data.$color = '#f77';
		            }
		        }
		    });
		    //load json data
		//    var mynewjson = zk.Widget.$('$data').getValue(); 
		//    st.loadJSON(jq.evalJSON(mynewjson));
		    //this._st.loadJSON(jq.evalJSON(treeData));
		    //compute node positions and layout
		    //this._st.compute();
		    //optional: make a translation of the tree
		    //this._st.geom.translate(new $jit.Complex(-200, 0), "current");
		    //Emulate a click on the root node. 
		    //this._st.onClick(this._st.root);
		    //end
		    this._refresh(treeData);
		},
		_addNode: function(newTextNode) {
			var newNodeJSON = jq.evalJSON(newTextNode);
			this._st.addSubtree(newNodeJSON, "animate", {
		        hideLabels: false,
		        onComplete: function() {
		            console.log("subtree added");
				}
		    });
		},
		_refresh: function(treeData) {
		    this._st.loadJSON(jq.evalJSON(treeData));
		    //compute node positions and layout
		    this._st.compute();
		    //optional: make a translation of the tree
		    this._st.geom.translate(new $jit.Complex(-200, 0), "current");
		    //Emulate a click on the root node. 
		    this._st.onClick(this._st.root);
		    //end
		},
		_switchPosition: function(orient) {
			this._st.switchPosition(orient, "animate", {  
				 onComplete: function() {  
				   console.log('completed!');  
				 }  
			});
		},
		_switchAlignment: function(orient) {
			this._st.switchAlignment(orient, "animate", {  
				 onComplete: function() {  
				   console.log('completed!');  
				 }  
			});
		}		
	});
});
