var labelType, useGradients, nativeTextSupport, animate;

(function() {
  var ua = navigator.userAgent,
      iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
      typeOfCanvas = typeof HTMLCanvasElement,
      nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
      textSupport = nativeCanvasSupport 
        && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
  //I'm setting this based on the fact that ExCanvas provides text support for IE
  //and that as of today iPhone/iPad current text support is lame
  labelType = (!nativeCanvasSupport || (textSupport && !iStuff))? 'Native' : 'HTML';
  nativeTextSupport = labelType == 'Native';
  useGradients = nativeCanvasSupport;
  animate = !(iStuff || !nativeCanvasSupport);
})();

var st;

function addNode(newTextNode) {
	var newNodeJSON = jq.evalJSON(newTextNode);
	st.addSubtree(newNodeJSON, "animate", {
        hideLabels: false,
        onComplete: function() {
            console.log("subtree added");
		}
    });
}
function refresh(treeData) {
    st.loadJSON(jq.evalJSON(treeData));
    //compute node positions and layout
    st.compute();
    //optional: make a translation of the tree
    st.geom.translate(new $jit.Complex(-200, 0), "current");
    //Emulate a click on the root node. 
    st.onClick(st.root);
    //end
}

function switchPosition(orient) {
	st.switchPosition(orient, "animate", {  
		 onComplete: function() {  
		   console.log('completed!');  
		 }  
	});
}

function switchAlignment(orient) {
	st.switchAlignment(orient, "animate", {  
		 onComplete: function() {  
		   console.log('completed!');  
		 }  
	});
}

function init(treeData){

	var removing = false;
    //init Spacetree
    //Create a new ST instance
    var wrapper = '' + zk.Widget.$("$infovis").$n().id;
    st = new $jit.ST({
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
            if(node._depth == 1) {
                style.cursor = 'pointer';
                label.onclick = function() {
                    if(!removing) {
                        removing = true;
                        console.log("removing subtree...");  
                        //remove the subtree
                        st.removeSubtree(label.id, true, 'animate', {
                            hideLabels: false,
                            onComplete: function() {
                              removing = false;
                              zAu.send(new zk.Event(zk.Widget.$('$infovis'), "onRemove", {'' : {'data' : {'nodeId': label.id}}}, {toServer:true}));
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
    st.loadJSON(jq.evalJSON(treeData));
    //compute node positions and layout
    st.compute();
    //optional: make a translation of the tree
    st.geom.translate(new $jit.Complex(-200, 0), "current");
    //Emulate a click on the root node. 
    st.onClick(st.root);
    //end
}