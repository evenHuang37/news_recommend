var sm2;
var grid;
var win;
var form;
			
Ext.onReady(function(){
	initData();
});
			
function initData(){
	unitController.getRecords(function(data){
		if(data!=null){
			var list=[];
			for(var i=0;i<data.length;i++){ 
				list[i]=[data[i].id,data[i].name,data[i].description,data[i].parentId];
			}
			Ext.grid.dummyData=list;
			initGrid();
		}
	});
}

function getInsertForm(){
	return new Ext.FormPanel({
		labelWidth: 75,
	  	url:'../unit.hr',
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        width: 340,
        defaults: {width: 230},
        defaultType: 'textfield',
		standardSubmit:true,
        items: [{
                fieldLabel: '机构名称',
                name: 'name',
                allowBlank:false
            },{
                fieldLabel: '描述',
                name: 'description'
            },{
                fieldLabel: '所属机构',
                name: 'parentId'
            }
        ]
    });
}

function getUpdateForm(){
	return new Ext.FormPanel({
        labelWidth: 75, 
        url:'../unit.hr',
        frame:true,
        bodyStyle:'padding:5px 5px 0', 
        width: 340,
        defaults: {width: 230},
        defaultType: 'textfield',
		standardSubmit:true,
        items: [{
                fieldLabel: 'id',
                name: 'id',
                readOnly:true
            },{
                fieldLabel: '机构名称',
                name: 'name',
                allowBlank:false
            },{
                fieldLabel: '描述',
                name: 'description'
            },{
                fieldLabel: '所属机构',
                name: 'parentId'
            }
        ]
    });
}

function initGrid(){
    Ext.QuickTips.init();
    
    var xg = Ext.grid;
	
    // shared reader
    var reader = new Ext.data.ArrayReader({}, [
       {name: 'id'},
       {name: 'name'},
       {name: 'description'},
       {name: 'parentId'}
    ]);

    ////////////////////////////////////////////////////////////////////////////////////////
    // form 
    ////////////////////////////////////////////////////////////////////////////////////////
	if(!form){
	    form=getInsertForm();
	}
    ////////////////////////////////////////////////////////////////////////////////////////
    // win 
    ////////////////////////////////////////////////////////////////////////////////////////
	if(!win){
		win = new Ext.Window({
		    el:'add-window',
		    layout:'fit',
		    width:380,
		    height:195,
		    closeAction:'hide',
		    plain: true,
		    items:form,
		    buttons: [{
		        text:'Submit',
		        handler:function(){
		        	form.getForm().submit({
		        	    method:'post',
			        	failure:function(retForm,retAction){
			        		if(retAction.result&&retAction.result=='1'){
			        			Ext.MessageBox.alert('提示', '保存数据成功！');
			        			win.hide();
			        			grid.destroy();
			        			initData();
			        		}else if(retAction.result&&retAction.result=='error'){ 
			        			Ext.MessageBox.alert('提示', '保存数据失败！');
			        		}else {
			        			Ext.MessageBox.alert('提示', '保存数据出错！');
			        		}
			        	},
			        	waitMsg:'Saving Data...'
		        	});
		        }
		    },{
		    	text:'reset',
		        handler:function(){
		        	form.getForm().reset();
		        }
		    },{
		        text: 'Close',
		        handler: function(){
		            win.hide();
		        }
		    }]
		});
	}
	//////////////////////////////////////////////////////////////////////////////////
    // Grid 
    ////////////////////////////////////////////////////////////////////////////////////////
    sm2= new xg.CheckboxSelectionModel();
    grid = new xg.GridPanel({
        id:'button-grid',
        store: new Ext.data.Store({
            reader: reader,
            data: xg.dummyData
        }),
        cm: new xg.ColumnModel([
            sm2,
            {id:'id',header: "id", width: 30, sortable: true, dataIndex: 'id'},
            {header: "name", width: 40, sortable: true, dataIndex: 'name'},
            {header: "description", width: 50, sortable: true, dataIndex: 'description'},
            {header: "parentId", width: 30, sortable: true, dataIndex: 'parentId'}
        ]),
        sm: sm2,
		stripeRows: true,
		
        viewConfig: {
            forceFit:true
        },

        // inline toolbars
        tbar:[{
            text:'添加',
            tooltip:'添加一条机构信息',
            iconCls:'add',
            onClick:function(){
            	//update form to insert form
            	form=getInsertForm();
            	var items=new Ext.util.MixedCollection();
            	items.add("form",form);
            	win.items=items;
	        	win.show(this);
            	form.getForm().reset();
            }
        }, '-', {
            text:'修改',
            tooltip:'修改一条机构信息',
            iconCls:'option',
            onClick:function(){
            	if(sm2.getCount()==1){ 
            		//update form to insert form
	            	form=getUpdateForm();
	            	var items=new Ext.util.MixedCollection();
	            	items.add("form",form);
	            	win.items=items;
	        		win.show(this);
            	    form.getForm().reset();
            	    form.getForm().loadRecord(sm2.getSelected());
            	}else{ 
            		Ext.MessageBox.alert('提示', '请选择一条记录！');
            	}
            }
        },'-',{
            text:'删除',
            tooltip:'删除选择的机构信息',
            iconCls:'remove',
            onClick:function(){
            	if(sm2.hasSelection()){ 
            	    var list=sm2.getSelections();
            	    var rList=[];
		        	for(var i=0; i<list.length;i++){ 
		            	rList[i]=list[i].data["id"];
		      		}
		      		unitController.removeRecords(rList,function(data){
		      			if(data>0){ 
		      				Ext.MessageBox.alert('提示', "删除"+data+'条数据成功!');
		      				grid.destroy();
		      				initData();
		      			}else{
		      				Ext.MessageBox.alert('提示', "删除数据失败!");
		      			}
		      		});
            	}else{ 
            		Ext.MessageBox.alert('提示', "请至少选择一条记录!");
            	}
            }
        }],

        width:800,
        frame:true,
        title:'机构信息列表',
        iconCls:'icon-grid',
        renderTo: document.body
    });
};
