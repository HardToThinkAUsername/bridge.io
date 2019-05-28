var content = ""
var text = "所有帖子"
var val = "0"
$(function() {
	nav_color();
	/*
	 * 注册用户前端校验
	 */
    $(".register-form").bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                message: '用户名验证失败',
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    },
                    stringLength: {
                        min: 3,
                        max: 16,
                        message: '用户名长度必须在3到16位之间'
                    },
                    threshold: 2,//有2字符以上才发送ajax请求
                    remote: {//ajax验证。server result:{"valid",true or false} 
                        url: "/user/usernameExist",
                        message: "用户名已存在,请重新输入",
                        delay: 1000,//ajax刷新的时间是1秒一次
                        type: 'POST',
                    	data: function(validator) {
	                		return {
	                		    userName : $(".username").val(),
	                		    type : "register"
	                		};
                        }
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
                        message: '用户名只能包含中文汉字、大写字母、小写字母、数字和下划线'
                    }
                }
            },
            password: {
            	validators: {
            		notEmpty: {
            			message: '密码不能为空'
            		},
            		stringLength: {
                        min: 6,
                        max: 30,
                        message: '密码长度必须在6到30位之间'
                    },
            		regexp: {
                  	    regexp: /^[a-zA-Z0-9_\.]+$/,
                     	message: '密码只能是大写字母，小写字母，数字或者下划线'
                 	}
            	}
            },
            confirmPassword: {
            	validators: {
            		identical: {
            			field: 'password',
            			message: '两次密码不一致'
            		}
            	}
            },
            phoneNum: {
            	message: '手机号无效',
             	validators: {
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '请输入11位手机号码'
                    },
                    regexp: {
                        regexp: /^1[3|5|8]{1}[0-9]{9}$/,
                        message: '请输入正确的手机号码'
                    }
                }
            },
            email: {
                validators: {
                    emailAddress: {
                        message: '邮箱地址格式有误'
                    }
                }
            },
            
            agree: {
            	validators: {
            		choice: {
            			min: 1,
            			max: 1,
            			message: '请确认已阅读用户协议'
            		
            		}
            	}
            }
        },
    }).on('success.form.bv',function(e){
    	e.preventDefault();
    	var $form = $(e.target);
    	var bv = $form.data('bootstrapValidator');
    	$.ajax({
		    url: "/user/save",
		    type: "POST",
		    async: false,
		    data: "username=" + $(".username").val()+
      		  "&password=" + $(".password").val()+
      		  "&confirmPassword=" + $(".confirmPassword").val()+
      		  "&phoneNum=" + $(".phoneNum").val()+
      		  "&email=" + $(".email").val()+
      		  "&agree=" + $(".agree").val()+
      		  "&address=" + $(".address").val()+
      		  "&type=" + "register"+
      		  "&gender=" + $(".gender:checked").val(),
		    success: function(result) {
		    	if(result.code == 1){
		    		$("#registerModal").modal('hide')
		    		alert("注册成功")
		    	}else{
		    		alert("注册失败")
		    	}
		    }
    	});
    });
    
    
    /*
	 * 用户登录前端校验
	 */
    $(".login-form").bootstrapValidator({
        message: 'This value is not valid',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            username: {
                message: '用户名验证失败',
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    },
                    threshold: 2,//有2字符以上才发送ajax请求
                    remote: {//ajax验证。server result:{"valid",true or false} 
                        url: "/user/usernameExist",
                        message: "用户名不存在",
                        delay: 1000,//ajax刷新的时间是1秒一次
                        type: 'POST',
                    	data: function(validator) {
	                		return {//提交的数据
	                		    username : $(".username").val(),
	                		    type : "login"
	                		};
                        }
                    }
                }
            },
            password: {
            	validators: {
            		notEmpty: {
            			message: '密码不能为空'
            		},
            		stringLength: {
                        min: 6,
                        max: 30,
                        message: '用户名长度必须在6到30位之间'
                    },
            		regexp: {
                  	    regexp: /^[a-zA-Z0-9_\.]+$/,
                     	message: '密码只能是大写字母，小写字母，数字或者下划线'
                 	}
            	}
            }
        },
    }).on('success.form.bv',function(e){
    	e.preventDefault();
    	var $form = $(e.target);
    	var bv = $form.data('bootstrapValidator');
    	$.ajax({
		    url: "/user/login",
		    type: "POST",
		    async: false,
		    cache: false,
		    dataType: "json",
		    data: "username=" + $(".login-form :text").val()+
      		  "&password=" + $(".login-form :password").val(),
		    success: function(result) {
		    	if(result.code == 1){
		    		$("#loginModal").modal('hide')
		    		loginChange(result.extend);
		    	}else {
		    		alert(result.msg)
				}
		    }
    	});
    });
    
    

    /**
     * 判断session中是否有user
     * 
     * */
    $.ajax({
    	url: "/user/getSessionUser",
    	cache: false,
    	async: false,
        dataType: "json",
    	success: function(result){
    		if(result.code == 1){
    			loginChange(result.extend)
    		}
    	}
    });
    
    
    /*
     * 	导航栏状态控制 
     */
    $(".index-nav").each(function(index,item) {
    	$(item).mouseover(function() {
    		if (!$(this).hasClass("active")) {
    			$(this).css("background-color", "#909090")
				.css("margin-top", "-2px")
				.css("padding-top", "2px");
			}
		}).mouseleave(function() {
			if (!$(this).hasClass("active")) {
				$(this).css("background-color","#989898")
				.css("margin-top", "0px")
				.css("padding-top", "0px");
			}
		}).click(function() {
			if(!$(this).hasClass("active")){
				$(".index-nav").each(function(index,item) {
					$(item).removeClass("active")
				})
				$(this).addClass("active")
				.css("margin-top", "0px")
				.css("padding-top", "0px");
			}
			nav_color();
		})
    });
    
});

/*
 * 点击查看更多 或者类帖子的标题 导航栏发生变化
 * */
function nav_change(i){
	var text = "";
	if(i == 0){//介绍
		text = "介绍区";
	}else if(i == 1){//资料
		text = "资料区";
	}else if(i == 2){//求助
		text = "求助区";
	}else if(i == 3){//热帖
		text = "热帖区";
	}
	$(".index-nav").each(function(index,item) {
		if($(item).find("a").text() == text)
			$(item).addClass("active");
		else
			$(item).removeClass("active");
	})
	nav_color();
}

/*
 * 控制导航栏的颜色变化
 * */
function nav_color() {
	$(".index-nav").each(function(index,item) {
    	if($(item).hasClass("active")){
    		$(item).css("background-color","#c9282d");
    	}else{
    		$(item).css("background-color","#989898")
    	}
	})
}


/**
 * 跳转到用户中心
 * */
function to_person_center(e){
	$(".container-content").empty().load("/user/personCenter");
	return false;
}

/**
 * 登录后页面变化
 * */
function loginChange(msg) {
	var date = new Date();
	var hour = date.getHours();
	$("#loginBtn").css("display","none");
	$("#registerBtn").css("display","none");
	var personMsgDiv = $("<div class='person-msg' style='margin-top:5px'></div>");
	var userIcon = $("<span class='glyphicon glyphicon-user'></span>");
	var welcomMsg = $("<span class='text-info'></span>")
	if(hour > 0 && hour < 12){
		welcomMsg.append("早上好!&nbsp;&nbsp;&nbsp;");
	}else if(hour >=12 && hour <= 18){
		welcomMsg.append("下午好!&nbsp;&nbsp;&nbsp;");
	}else{
		welcomMsg.append("晚上好!&nbsp;&nbsp;&nbsp;");
	}
	var a = $("<a href='javascript: ;' onclick='to_person_center()' title='点击进入个人中心'>&nbsp;&nbsp;</a>");
	var usernameSpan = $("<span class='username-span text-muted'></span>").append(msg.user.username);
	var userRankSpan = $("<span class='text-muted'></span>").append("Lv."+msg.user.rank);
	var signInBtn = $("<button class='btn-sm btn-default sign-btn text-muted' onclick='sign_in()' title='连续签到可以获取更多的经验, 帮助你更快的升级.'>签到</button>")
	if(msg.hasSignin == 1){//已经签到
		signInBtn.removeClass("text-muted").addClass("text-info").text("已签到"+msg.user.continuousSigninTimes+"天");
	}
	a.append(usernameSpan).append(userRankSpan)
	personMsgDiv.append(welcomMsg)
				.append(userIcon)
				.append(a)
				.append(signInBtn);
	var logOutA = $("<a href='/user/logout'><span class='glyphicon glyphicon-log-out'></span><span title='退出' class='text-muted'>退出</span></a>");
	var logOutDiv = $("<div class='log-out' style='text-align:right;float:right;'></div>")
					.append(logOutA);
	if(msg.user.role.id != 0){
		var manageCenterDiv = $("<div class='manage-center' style='float:right;'><a href='/index/toManagePage'><span class='glyphicon glyphicon-cog'></span><span class='text-muted' title='管理中心'>管理中心</span></a></div>");
		$(".login-top").append(personMsgDiv)
		.append(logOutDiv)	
		.append(manageCenterDiv);
	}else{
		$(".login-top").append(personMsgDiv)	
		.append(logOutDiv);
	}
	$(".person-center-nav").css("display","block");
}
/*
 * 签到
 * */
function sign_in(){
	$.ajax({
		type: 'POST',
		url: '/user/signin',
		cache: false,
		success: function(result){
			alert(result.msg)
			if(result.code == 1 || result.msg == "今日已签到!"){
				$(".sign-btn").removeClass("btn-primary").addClass("btn-info").text("已连续签到"+result.extend.time+"天");
			}
		}
	});
}

/**
 *  pageNum: 分页查询的页号
 *	pageSize: 每页的数量
 *	type: 帖子的类型
 */
function get_all(pageNum,pageSize,type){
	var url;
	//根据type值确定地址
	if(type == 0){//介绍类帖子
		url = '/post/getIntroPostAll'
	}else if(type == 1){//资料
		url = '/data/getDataAll'
	}else if(type == 2){//求助类帖子
		url = '/post/getHelpPostAll'
	}else if(type == 3){//热帖
		url = '/post/getHotPostAll'
	}
	$.ajax({
		type: 'GET',
		url: url,
		cache: true,
		data: "pageNum="+pageNum+"&pageSize="+pageSize,
		success: function(result){
			var containerDiv = $("<div class='post-div'></div>")
			var pageDiv = $("<div class='paging-div'></div>")
			$(".container-content").empty().append(build_search_area(result))
			if(type == 1){
				containerDiv.append(build_data(result))//构建资料列表
			}else{
				containerDiv.append(build_post_all(result))//构建帖子列表
            }
            if(result.extend.pageInfo.list.length > 0){
                pageDiv.append(build_page_bar(result,type,1));//构建分页条
            }
			$(".container-content").append(containerDiv).append(pageDiv);
		}
	})
}

/*
模糊查询
*/
function go_search(){
	content = $(".search-content").val()
	var type = $(".search-type-btn").val();
	get_fuzzy(1,10,type);
}

/*
* 模糊查询类型
* */
function search_type_change(ele) {
	text = $(ele).find("a").text()
	val = $(ele).val();
	$(".search-type-btn").text(text).val(val)
}

/*
 * 构建搜索框
 * 
 * */
function build_search_area(result) {
	//搜索1
	var searchAreaDiv = $("<div class='search-area col-lg-12'></div>")
	var typeBtn = $("<button type='button' class='btn btn-default dropdown-toggle search-type-btn' value='0' data-toggle='dropdown'>所有帖子<span class='caret'></span></button>").text(text).val(val);
	var li1 = $("<li class='search-type-menu-item' value='1' onclick='search_type_change(this)'><a href='javascript:;'>介绍类帖子</a></li>")
	var li2 = $("<li class='search-type-menu-item' value='2' onclick='search_type_change(this)'><a href='javascript:;'>资料下载</a></li>")
	var li3 = $("<li class='search-type-menu-item' value='3' onclick='search_type_change(this)'><a href='javascript:;'>求助类帖子</a></li>")
	var li4 = $("<li class='search-type-menu-item' value='4' onclick='search_type_change(this)'><a href='javascript:;'>热帖</a></li>")
	var ul = $("<ul class='dropdown-menu'></ul>").append(li1).append(li2).append(li3).append(li4)
	var inputGroupBtnDiv = $("<div class='input-group-btn'></div>").append(typeBtn).append(ul)
	var inputText = $("<input type='text' class='form-control search-content' placeholder='请输入关键字搜索....'>").val(content)
	var searchBtn = $("<button class='btn btn-primary search-go-btn' type='button' onclick='go_search()'>搜索</button>")
	var searchSpan = $("<span class='input-group-btn'></span>").append(searchBtn)
	var inputGroupDiv = $("<div class='input-group'></div>").append(inputGroupBtnDiv).append(inputText).append(searchSpan)
	var inputGroupFatherDiv = $("<div class='col-lg-5'></div>").append(inputGroupDiv)
	//搜索2
	var option1 = $("<option selected='selected'>请选择桥梁类型类型</option>")
	var option2 = $("<option>铁路桥</option>")
	var option3 = $("<option>公路桥</option>")
	var option4 = $("<option>人行桥</option>")
	var option5 = $("<option>高架桥</option>")
	var option6 = $("<option>木桥</option>")
	var option7 = $("<option>钢桥</option>")
	var option8 = $("<option>钢筋混凝土桥</option>")
	var bridgeType = result.extend.bridgeType;
	if(bridgeType == "铁路桥"){
		option2.prop("selected","selected");
	}else if (bridgeType == "公路桥") {
		option3.prop("selected","selected");
	}else if (bridgeType == "人行桥") {
		option4.prop("selected","selected");
	}else if (bridgeType == "高架桥") {
		option5.prop("selected","selected");
	}else if (bridgeType == "木桥") {
		option6.prop("selected","selected");
	}else if (bridgeType == "钢桥") {
		option7.prop("selected","selected");
	}else if (bridgeType == "钢筋混凝土桥") {
		option8.prop("selected","selected");
	}
	var select = $("<select class='select' style='font-size: 15px;'></select>")
	select.append(option1).append(option2).append(option3).append(option4).append(option5).append(option6).append(option7).append(option8)
	//var btn = $("<button type='button' class='bridge-type-search btn btn-primary btn-sm' onclick='bridge_type_change()' style='margin-left: 10px;'>搜索</button>")
	var selectDiv = $("<div class='col-lg-3' style='height: 35px;line-height: 35px;padding: 3px;text-align:center;'></div>").append(select)//.append(btn)
	//搜索3
	var o1 = $("<option selected='selected'>请选择桥梁年代</option>")
	var o2 = $("<option>18世纪前</option>")
	var o3 = $("<option>18-19世纪</option>")
	var o4 = $("<option>20世纪</option>")
	var o5 = $("<option>21世纪</option>")
	var century = result.extend.century;
	if(century == 1){
		o2.prop("selected","selected");
	}else if (century == 2) {
		o3.prop("selected","selected");
	}else if (century == 3) {
		o4.prop("selected","selected");
	}else if (century == 4) {
		o5.prop("selected","selected");
	}
	var select2 = $("<select class='century-select' style='font-size: 15px;'>")
	select2.append(o1).append(o2).append(o3).append(o4).append(o5)
	var btn2 = $("<button type='button' class='bridge-century-search btn btn-primary btn-sm' onclick='bridge_century_change()' style='margin-left: 10px;'>搜索</button>")
	var select2Div = $("<div class='col-lg-3' style='height: 35px;line-height: 35px;padding: 3px;text-align:center;'>").append(select2).append(btn2)
	searchAreaDiv.append(inputGroupFatherDiv).append(selectDiv).append(select2Div)
	return searchAreaDiv;
}

/*
 * 	模糊查询
 * */
function get_fuzzy(pageNum,pageSize,type){
	var url = "" 	
	if(type == 2){//资料
		url = "/data/fuzzyQuery";
	}else{//帖子
		url = "/post/fuzzyQuery";
	}
	$.ajax({
		type: "GET",
		url: url,
		data: "content="+content+"&type="+type+"&pageNum="+pageNum+"&pageSize="+pageSize,
		cache: false,
		success: function(result){
			if(result.code == 1){
				var containerDiv = $("<div></div>")
				var pageDiv = $("<div class='paging-div'></div>")
				$(".container-content").empty().append(build_search_area(result))
				if(type == 2){//资料
					containerDiv.append(data_fuzzy_build(result))
				}else{//帖子
					containerDiv.append(build_post_all(result))
                }
                if(result.extend.pageInfo.list.length > 0){
                    pageDiv.append(build_page_bar(result,type,0));//构建分页条
                }
				$(".container-content").append(containerDiv).append(pageDiv);
			}else{
				alert(result.msg)
			}
		}
	})
}
/*
 * 模糊查询
 * */
function data_fuzzy_build(result){
    var datas = result.extend.pageInfo.list
    var postUl = $("<ul class='data-ul col-lg-12'></ul>")
    if(datas.length <= 0){
        return postUl.append($("<span class='col-lg-12 text-muted' style='padding:15px;'>没有找到对应资料</span>")) 
    }
	$.each(datas,function(index,item){
		var postLi = $("<li style='padding:15px;' class='data-li col-lg-12'></li>")
		var dataNameA = $("<a href='javascript:;'></a>").append("文件名:&nbsp;&nbsp;"+item.name)
		var dataNameDiv = $("<div class='data-name-div col-lg-12'></div>").append(dataNameA)
		var dataDescSpan = $("<span></span>").append("描述:&nbsp;&nbsp;"+item.description)
		var dataDescDiv = $("<div class='data-desc-div col-lg-12'></div>").append(dataDescSpan)
		var dataDiv = $("<div class='data-div col-lg-8'></div>").append(dataNameDiv).append(dataDescDiv)
		postLi.append(dataDiv)
		postUl.append(postLi)
	})
	return postUl;
}
/*
根据桥梁类型查询
*/
function bridge_type_change(){
	var type = ""
	$(".select").find("option").each(function(index,item){
		if($(item).prop("selected")){
			type = $(item).text();
		}
	})
	search_by_keyword(1,10,type);
}

function search_by_keyword(pageNum,pageSize,bridgeType) {
	content = $(".search-content").val()
	var postType = $(".search-type-btn").val();
	$.get("/post/getPostByKeyWord",{pageNum:1,bridgeType:bridgeType,content:content,postType:postType},function(result){
		if(result.code == 1){
			var containerDiv = $("<div col-lg-12></div>")
			var pageDiv = $("<div class='paging-div'></div>")
			$(".container-content").empty().append(build_search_area(result))
			containerDiv.append(build_post_all(result))
	        if(result.extend.pageInfo.list.length > 0){
	        	var ele = build_page_bar(result,bridgeType,2)
	            pageDiv.append(ele);//构建分页条
	        }
			$(".container-content").append(containerDiv).append(pageDiv);
		}else
			alert(result.msg)
	})
}

function search_by_century(pageNum,pageSize,century) {
	//查询1
	var postType = $(".search-type-btn").val();
	content = $(".search-content").val()
	//查询2
	var bridgeType = ""
	$(".select").find("option").each(function(index,item){
		if($(item).prop("selected")){
			bridgeType = $(item).text();
		}
	})
	$.get("/post/getPostByCentury",{pageNum:pageNum,bridgeType:bridgeType,content:content,postType:postType,century:century},function(result){
		if(result.code == 1){
			var containerDiv = $("<div col-lg-12></div>")
			var pageDiv = $("<div class='paging-div'></div>")
			$(".container-content").empty().append(build_search_area(result))
			containerDiv.append(build_post_all(result))
	        if(result.extend.pageInfo.list.length > 0){
	        	var ele = build_page_bar(result,century,3)
	            pageDiv.append(ele);//构建分页条
	        }
			$(".container-content").append(containerDiv).append(pageDiv);
		}else
			alert(result.msg)
	})
}
function bridge_century_change() {
	var centuryDesc = "";
	var century = 0;
	$(".century-select").find("option").each(function(index,item) {
		if($(item).prop("selected")){
			centuryDesc = $(item).text();
		}
	})
	if(centuryDesc == "18世纪前"){
		century = 1;
	}else if(centuryDesc == "18-19世纪"){
		century = 2;
	}else if(centuryDesc == "20世纪"){
		century = 3;
	}else if(centuryDesc == "21世纪"){
		century = 4;
	}
	search_by_century(1,10,century);
}