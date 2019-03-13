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
				        html: '<div class="layout-title">�����Ƽ�ϵͳ</div>',
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
		                    title:'�������',
		                    html: Ext.getDom('userMenus1').innerHTML,
		                    autoScroll:true,
		                    border:false,
		                    iconCls:'user'
		                },{
		                    title:'�û�����',
		                    html: Ext.getDom('userMenus').innerHTML,
		                    autoScroll:true,
		                    border:false,
		                    iconCls:'user'
		                },{
		                    title:'�����Ķ�',
		                    html: Ext.getDom('pxglMenus').innerHTML,
		                    border:false,
		                    autoScroll:true,
		                    iconCls:'pxgl'
		                },{
		                    title:'���ù���',
		                    html: Ext.getDom('zcpdMenus1').innerHTML,
		                    border:false,
		                    autoScroll:true,
		                    iconCls:'zcpd'
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
						<li><span id="person1" href="./gg/updateGG.jsp" onclick="onClickMenuItem(this)"><font size="3">���¹���</span></li>
						<li><span id="person1" href="../admin/gg/lookGG.jsp" onclick="onClickMenuItem(this)"><font size="3">�鿴����</font></span></li>
						
				</ul>
			</div>
			<div id="userMenus">
				<ul class="munuList">
						<!--<li><span id="person1" href="./yonghu/register.jsp" onclick="onClickMenuItem(this)">����û�</span></li>
						--><li><span id="person2" href="./yonghu/searchUser.jsp" onclick="onClickMenuItem(this)"><font size="3">�鿴�û�</span></li>
						<li><span id="person3" href="./yonghu/deleteUser.jsp" onclick="onClickMenuItem(this)"><font size="3">ɾ���û�</span></li>
				</ul>
			</div>
			<div id="pxglMenus">
				<ul class="munuList">
					<li>
						<span id="unit3" href="./kemu/searchTJClass.jsp" onclick="onClickMenuItem(this)"><font size="3">�鿴�Ƽ�</span>
					</li>
					<li>
						<span id="unit3" href="./kemu/searchALLClass.jsp" onclick="onClickMenuItem(this)"><font size="3">�������</span>
					</li>
				</ul>
			</div>
			<div id="zcpdMenus1">
				<ul class="munuList">
				
					<li>
						<span id="unit3" href="./hobby/searchClass.jsp" onclick="onClickMenuItem(this)"><font size="3">�鿴����</span>
					</li>
					<li>
						<span id="unit4" href="./hobby/addClass.jsp" onclick="onClickMenuItem(this)"><font size="3">��Ӱ���</span>
					</li>
					<li>
						<span id="unit4" href="./hobby/deleteClass.jsp" onclick="onClickMenuItem(this)"><font size="3">ɾ������</span>
					</li>
					<li>
						<span id="unit4" href="./hobby/updateClass.jsp" onclick="onClickMenuItem(this)"><font size="3">�޸İ���</span>
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
	  		<p>�𾴵� <font color=green><B></B></font> ����Ա,��ӭ��ʹ�������Ƽ�ϵͳ.</p>
	        <p>���ϵͳ���й���</p>
	  	</div>
	  
	</body>
</html>
