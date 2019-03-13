<%@page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,conn.ConnSql" errorPage="" %>
<html>
	<head>
		<title>�����Ƽ�ϵͳ</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">

		<link rel="stylesheet" type="text/css"
			href="../resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="../resources/css/common.css" />

		<script type="text/javascript" src="../resources/js/ext-base.js"></script>
		<script type="text/javascript" src="../resources/js/ext-all.js"></script>
<script type="text/javascript" src="../js/jquery-1.7.2.js"></script>
		<style type="text/css">
			
			.x-panel-body p {
				margin: 5px;
			}
			
			.x-column-layout-ct .x-panel {
				margin-bottom: 5px;
			}
			
			.x-column-layout-ct .x-panel-dd-spacer {
				margin-bottom: 5px;
			}
			
				
			.user {
				background-image: url(../resources/icons/user.gif) !important;
			}	
			.unit {
				background-image: url(../resources/icons/application_view_list.png) !important;
			}
			.settings {
				background-image: url(../resources/icons/cog.png) !important;
			}
			.tabs {
			    background-image:url(../resources/icons/tabs.gif) !important;
			}
			.munuList{
				list-style: square;
				padding-left:30px;
				margin-top:10px;
				color: #000000;
				font-size:12px;
				text-decoration : underline; 
				cursor:hand;
			}	
			
			#main-panel td {
           		padding:5px;
        	}
		</style>

	</head>
	<body>
		<script type="text/javascript">

       		//create main tabpanel
       		var tabPanel=new Ext.TabPanel({
            	id:"tabPanel",
                   region:'center',
                   deferredRender:false,
                   activeTab:0,
                   iconCls: 'tabs',
                   items:[{
                       contentEl:'center',
                       title: '��ҳ',
                       autoScroll:true
                   }]
          	});
	    	Ext.onReady(function(){
		       Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		       
		       
		       //init main page framework
		       var viewport = new Ext.Viewport({
		            layout:'border',
		            items:[{
				        region: 'north',
				        html: '<div class="layout-title"><font size="5">�����Ƽ�ϵͳ</div>',
				        autoHeight: true,
				        border: false,
				        layout:"absolute",
				        margins: '5 5 5 10'
				    },{
		                region:'west',
		                id:'west-panel',
		                title:'�˵���',
		                split:true,
		                width: 200,
		                minSize: 175,
		                maxSize: 400,
		                collapsible: true,
		                margins:'0 0 5 5',
		                cmargins:'0 5 5 5',
		                layout:'accordion',
		                layoutConfig:{
		                    animate:true
		                },
		                items: [{
		                    title:'�鿴����',
		                    html: Ext.getDom('userMenus1').innerHTML,
		                    autoScroll:true,
		                    border:false,
		                    iconCls:'user'
		                },{
		                    title:'������Ϣ',
		                    html: Ext.getDom('infoMenus').innerHTML,
		                    autoScroll:true,
		                    border:false,
		                    iconCls:'info'
		                },{
		                    title:'�����Ķ�',
		                    html: Ext.getDom('pxglMenus').innerHTML,
		                    border:false,
		                    autoScroll:true,
		                    iconCls:'pxgl'
		                },{
		                    title:'�޸�����',
		                    html: Ext.getDom('jcglMenus').innerHTML,
		                    border:false,
		                    autoScroll:true,
		                    iconCls:'jcgl'
		                },{
		                    title:'�˳���¼',
		                    html: Ext.getDom('ryddMenus').innerHTML,
		                    border:false,
		                    autoScroll:true,
		                    iconCls:'rydd'
		                }]
		            },tabPanel]
		        });
		        
		        //init main page tables
		        var panel = new Ext.Panel({
	                id:'main-panel',
	                baseCls:'x-plain',
	                renderTo: Ext.get("center"),
	                layout:'table',
	                layoutConfig: {columns:2},
	                defaults: {frame:true, width:395, height: 300},
	                items:[{
	                    title:'ϵͳ��ʾ��Ϣ',
	                    colspan:2,
	                    collapsible:true,
	                    width:800,
	                    contentEl:'afficheDiv'
	                }]
	            });
		        //clear temp elements.
		       	Ext.getDom("menus").innerHTML="";
		    });
		    
		    function onClickMenuItem(el){ 
		    updateTab($(el).attr('id'),el.innerHTML,$(el).attr('href'));
		    }
		    
		    function addTab(id,tabTitle, targetUrl){
		        tabPanel.add({
		        	id:id,
				    title: tabTitle,
				    iconCls: 'tabs',
				    autoLoad: {url: "./tabFrame.jsp?url="+targetUrl, callback: this.initSearch, scope: this},
				    closable:true
				}).show();
		    }
		    
		    function updateTab(id,title, url) {
		    	var tab = tabPanel.getItem(id);
		    	if(tab){
		    		tabPanel.remove(tab);
		    	}
		    	tab = addTab(id,title,url);
		    	tabPanel.setActiveTab(tab);
		    }
		    
		    
		   
		    
		  
		       
		</script>

		<div id="menus">
			<!-- html part -->
			<div id="userMenus1">
				<ul class="munuList">
						<li><span id="person1" href="../admin/gg/lookGG.jsp" onclick="onClickMenuItem(this)"><font size="3">�鿴����</font></span></li>
				</ul>
			</div>
			
			<div id="infoMenus">
				<ul class="munuList">
						<li><span id="perinfo" href="../admin/yonghu/lookinfo.jsp" onclick="onClickMenuItem(this)"><font size="3">�鿴��Ϣ</font></span></li>
              		<li><span id="perinfo" href="../admin/yonghu/updateinfo.jsp" onclick="onClickMenuItem(this)"><font size="3">�޸���Ϣ</font></span></li>
				
				</ul>
			</div>
			
			<div id="pxglMenus">
				<ul class="munuList">
					<li>
						<span id="unit3" href="../admin/kemu/searchTJClass.jsp" onclick="onClickMenuItem(this)"><font size="3">�鿴�Ƽ�</span>
					</li>
					<li>
						<span id="unit3" href="../admin/kemu/searchALLClass.jsp" onclick="onClickMenuItem(this)"><font size="3">�������</span>
					</li>
				</ul>
			</div>
			
			<div id="jcglMenus">
				<ul class="munuList">
					<li>
						<span id="unit5" href="./../updatePsd.jsp" onclick="onClickMenuItem(this)"><font size="3">�޸�����</span>
					</li>
					
				</ul>
			</div>
			<div id="ryddMenus">
				<ul class="munuList"> 
					<li>
						<span id="unit7" onclick="javascript:window.location.href='./../index.html'"><font size="3">�˳���¼</span>
					</li>
				</ul>
			</div>
		</div>
		
		<div id="center">
	        
	  	</div>
	  	
	  	<div id="afficheDiv">
	  		<p>�𾴵� <font color=green><B></B></font> �û�,��ӭ��ʹ�������Ƽ�ϵͳ.</p>
	        <p>���ǽ��Ƽ�����������õ�����</p>
	  	</div>
	  
	</body>
</html>
