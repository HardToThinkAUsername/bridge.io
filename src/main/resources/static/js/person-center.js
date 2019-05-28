$(function() {

	/**
	 * 控制发帖, 发文件模板框隐藏的方式
	 */
	
	$('#postingModal').modal({
		keyboard: false,//关闭按ESC键隐藏模态框
		backdrop: false,//关闭点页面背景隐藏模态框
		show: false//关闭加载页面后自动显示模态框
	})
	/*
	 * 点击查询我的帖子
	 * */
	$(".my-post").click(function() {
		search_my_post();
		getMyPostCount()
	})
	/*
	 * 点击查询我的文件
	 */
	$(".my-data").click(function() {
		search_my_data();
		getMyDataCount();
	})
	
	/*
	 * 点击查看我的私信
	 */
	
	$(".my-secret-msg").click(function() {
		var result = getMySecretMsg();
		var ul = build_my_secretMsg(result);
		var containerDiv = $("<div class='my-secretmsg-div'></div>").append(ul)
		$(".person-post-area").empty().append(containerDiv)
	})
	
	/**
	 * 点击查看我的关注
	 */
	$(".my-concern").click(function() {
		var result = getMyConcern();
		var ul = build_my_concern(result);
		var containerDiv = $("<div class='my-concern-div'></div>").append(ul)
		$(".person-post-area").empty().append(containerDiv) 
	})
	
	
	/**
	 * 发布帖子
	 */
	
	$(".release-btn").click(function() {
		var postType = -1;
		var postTypeText = $(".postTypeSelBtn").text();
		var postTitleText = $(".postTitle").val();
		var postBodyText = $(".w-e-text").html()
		var keywords = ""
		var century = 0;
		$(".keyword").each(function(index,item) {
			if($(item).prop("checked")){//选中
				keywords = keywords + $(item).val()+","
			}
		})
		$(".century").each(function(index,item) {
			if($(item).prop("checked")){//选中
				century = $(item).val()
			}
		})
		if(century == 0){
			alert("请选择桥梁年代!");
			return ;
		}
		if(postTypeText != "求助帖子" && postTypeText != "介绍帖子"){
			alert("请选择帖子类型!") 
		}else if(postTitleText == "" || $.trim(postTitleText) == ""){
			alert("帖子主题不能为空")
		}else if(postBodyText == "" || $.trim(postBodyText) == ""){
			alert("帖子内容不能为空")
		}else if(keywords == ""){
			alert("请选择桥梁类型")
		}else{
			if(postTypeText == "求助帖子"){
				postType = 0;
			}else if(postTypeText == "介绍帖子"){
				postType = 1;
			}
			$.ajax({
				type: "POST",
				url: "/post/releasePost",
				data: "postType=" + postType
					+ "&postTitle=" + postTitleText
					+ "&postBody=" + encodeURIComponent(postBodyText)
					+ "&keywords=" + keywords
					+ "&century="+century,
				cache: false,
	            processData: false,
				success: function(result) {
					if(result.code == 1){
						$("#postingModal").modal('hide');
						search_my_post();
						getMyPostCount()
					}
					alert(result.msg)
				}
				
			})
		}
	});
	
	$(".data-type-ul").find("li").each(function(index,item){
 		$(item).click(function(){
	 		$("#dropdownMenu2").empty().append($(this).text());
 		})
 	})
 	$('#dataModal').modal({
		keyboard: false,//关闭按ESC键隐藏模态框
		backdrop: false,//关闭点页面背景隐藏模态框
		show: false//关闭加载页面后自动显示模态框
	})
	
	var dataType = 0;
	$(".data-type-ul").find("li").each(function(index,item){
		$(item).click(function(){
			var text = $(this).find("a").text();
			if(text == "word文档"){
				dataType = 1;
			}else if(text == "文本文档"){
				dataType = 2;
			}else if(text == "PPT"){
				dataType = 3;
			}else if(text == "图片"){
				dataType = 4;
			}else if(text == "压缩文件"){
				dataType = 5;
			}
		})
	})
	$(".broswer").click(function(){
		if(dataType == 0){// 如果没有选择文档类型
			alert("请先选择你要上传的文件类型!");
		}else if(dataType == 1){
			$("#data").prop('accept','application/msword,application/pdf,application/msexcel').click()
		}else if(dataType == 2){
			$("#data").prop('accept','text/plain').click()
		}else if(dataType == 3){
			$("#data").prop('accept','application/vnd.ms-powerpoint').click()
		}else if(dataType == 4){
			$("#data").prop('accept','image/x-png,image/jpeg').click()
		}else if(dataType == 5){
			$("#data").prop('accept','application/zip').click()
		}
	})
	$("#data").change(function(){
		$(".broswer").text(getFileName($(this).val()))
	})
	
	/*
	 * 上传文件
	 */
	$(".data-upload").click(function(){
		var formData = new FormData();
		if(dataType == 0){
			alert("请选择上传文件类型!");
			return false;
		}
		formData.append("type", dataType)
		formData.append("data", document.getElementById("data").files[0]);
		formData.append("description", $(".description").val());
		$.ajax({
			type: 'POST',
			url: '/data/upload',
			cache: false,//关闭缓存
			async: true,//一步
			data: formData,
			contentType: false,//必须false才会自动加上正确的Content-Type
			processData : false,//关闭数据格式转换
			success: function(result){
				if(result.code == 1){
					$("#dataModal").modal('hide');
					search_my_data();
					getMyDataCount();
				}
				alert(result.msg);										
			}
		})
 	});
	/**
	 * 更换头像按钮事件
	 */
	
	$(".profile-phonto").click(function() {
		$(".change-profile-photo").click();
	});
	/**
	 * 修改个人信息按钮事件
	 */
	$(".update-btn").click(function() {
		if($(".update-btn").text() == "修改"){
			$(".list-group-item").find("input").each(function(index, item) {
				$(item).prop("disabled", "")
				.css("border","solid 1px")
				.css("padding-left","10px");
			})
			$(".gender-li").css("display","block");
			$(".update-btn").text("保存修改");
		}else if($(".update-btn").text() == "保存修改"){
			var bootstrapValidator = $("form").data('bootstrapValidator');
			bootstrapValidator.validate();
			//bootstrapValidator.isValid();
		}
	})
	$(".user-msg-form").bootstrapValidator({
		message : 'This value is not valid',
		feedbackIcons : {
			valid : 'glyphicon glyphicon-ok',
			invalid : 'glyphicon glyphicon-remove',
			validating : 'glyphicon glyphicon-refresh'
		},
		fields : {
			username : {
				container : '.username-help-block',
				message : '用户名验证失败',
				validators : {
					notEmpty : {
						message : '用户名不能为空'
					},
					stringLength : {
						min : 3,
						max : 16,
						message : '用户名长度必须在3到16位之间'
					},
					threshold : 2,// 有2字符以上才发送ajax请求
					remote : {// ajax验证。server result:{"valid",true or false}
						url : "/user/usernameExist",
						message : "用户名已存在, 请重新输入",
						delay : 100,// ajax刷新的时间是1秒一次
						type : 'POST',
						data : function(validator) {
							return {
								username : $(".username").val(),
								oldUsername : $(".hide-username").val(),
								type : "update"
							};
						}
					},
					regexp : {
						regexp : /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
						message : '用户名只能包含中文汉字、大写字母、小写字母、数字和下划线'
					}
				}
			},
			email: {
				container : '.email-help-block',
                validators: {
                    emailAddress: {
                        message: '邮箱地址格式有误'
                    }
                }
            },
			password : {
				container : '.password-help-block',
				validators : {
					notEmpty : {
						message : '密码不能为空'
					},
					stringLength : {
						min : 6,
						max : 30,
						message : '用户名长度必须在6到30位之间'
					},
					regexp : {
						regexp : /^[a-zA-Z0-9_\.]+$/,
						message : '密码只能是大写字母，小写字母，数字或者下划线'
					}
				}
			},
			phoneNum : {
				container : '.phoneNum-help-block',
				message : '手机号无效',
				validators : {
					stringLength : {
						min : 11,
						max : 11,
						message : '请输入11位手机号码'
					},
					regexp : {
						regexp : /^1[3|5|8]{1}[0-9]{9}$/,
						message : '请输入正确的手机号码'
					}
				}
			}

		},
	}).on('success.form.bv',function(e) {
		e.preventDefault();
		var $form = $(e.target);
		var bv = $form.data('bootstrapValidator');
		$.ajax({
			url : "/user/save",
			type : "POST",
			async : false,
			data :  "username=" + $(".username").val() + "&password="
			+ $(".password").val()
			+ "&phoneNum=" + $(".phoneNum").val()
			+ "&id=" + $(".hide-id").val()
			+ "&role=" +$(".hide-role").val()
			+ "&type=update"
			+ "&gender=" + $(".gender:checked").val(),
			success : function(result) {
				if (result.code == 1) {
					alert("修改成功 !")
					$(".update-btn").text("修改");
					$(".gender-li").css("display","none");
					$(".glyphicon ").removeClass("glyphicon-ok");
					$(".list-group-item").find("input").each(function(index, item) {
						$(item).prop("disabled", "disabled")
						.css("border","none")
						.css("background-color","none")
						.css("padding-left","0");
                    })
                    $(".username-span").text($(".username").val())
					
				} else if (result.code == 0) {
					alert("修改失败 !")
				}
			}
		});
	});
	
})

/*
 * 更换头像
 * 
 * */
function changePhoto() {
    var f = $(":file").val();
    f = f.toLowerCase();
    var strRegex = ".bmp|jpg|jpeg|png$";
    var re=new RegExp(strRegex);
    if (re.test(f.toLowerCase())){
    	//FormData对象，
        //可以把form中所有表单元素的name与value组成一个queryString，提交到后台。
        //在使用Ajax提交时，使用FormData对象可以减少拼接queryString的工作量
    	
        var formData = new FormData();
        formData.append("file",$(":file")[0].files[0]);
        $.ajax({
            url: '/user/changeProfilePhoto',
            type: 'post',
            data: formData,
            cache: false,
            processData: false,
            contentType: false,
            success:function(data){
                if(data.code == 0){
                    alert(data.msg);
                }
                //根据上传成功并返回压缩后的图片url，更新img标签src属性
                if(data.code==1){
                   $("#avatar").attr("src",data.extend.path)
                }
            },
            error:function(e){
                console.log(e);
            }
        });
    }
    else{
        alert("请选择正确格式的图片");
         file = $(".profile-photo");
         file.after(file.clone());
         file.remove();
        return false;
    }
    return true;
}

/*
 * 获取所选文件的名称
 */
function getFileName(text){
	var pos = text.lastIndexOf("\\");
	return text.substring(pos+1);
}
/*
 * 选择发帖类型
 * */
function posting_type_change(e){
	$(".postTypeSelBtn").empty().append($(e).text());
}
/*
查询我的文件
*/
function search_my_data() {
	$.ajax({
		type: "GET",
		url: "/data/getDataByUserId",
		data: "id="+userId,
		cache: true,
		async: false,
		success: function(result){
			if(result.code == 1){
				var containerDiv = $("<div class='my-data-div'></div>")
				if(result.extend.list.length == 0){
					containerDiv.append($("<span class='text-muted'>暂未上传资料</span>"))
				}else if(result.extend.list.length > 0){
					containerDiv.append(build_my_data_ul(result))
				}
				$(".person-post-area").empty().append(containerDiv)
			}else{
				alert(result.msg)
			}
		}
	})
}



/*
查询我的帖子
*/
function search_my_post(){
	$.ajax({
		type: 'GET',
		url: '/post/getPostByUserId',
		data: "id="+userId,
		cache: true,
		async: false,
		success: function(result){
			if(result.code == 1){
				var containerDiv = $("<div class='my-post-div'></div>")
				containerDiv.append(build_my_post_ul(result))
				$(".person-post-area").empty().append(containerDiv)
			}else{
				alert(result.msg)
			}
		}
	})
}

/*
构建帖子列表
*/
function build_my_post_ul(result){
	var postUl = $("<ul class='my-post-ul'></ul>")
	$.each(result.extend.list,function(index,item){
		var postA = $("<a href='javascript:;' onclick='post_detail("+item.id+")'  title='点击查看详情'></a>").append(item.postTitle);
		var postTitleDiv = $("<div class='post-title-div col-lg-12'></div>").append(postA)
		var postBody = item.postBody;
		if(postBody.length > 100){
			postBody = postBody.substring(0,100)+"..."
		}
		var postBodySpan = $("<span style='padding:15px;'></span>").append(postBody)
		var postBodyDiv = $("<div class='post-body-div col-lg-12' style='margin-top:15px;'></div>").append(postBodySpan)
		var postDiv = $("<div class='col-lg-9'></div>").append(postTitleDiv).append(postBodySpan)
		var delBtn = $("<button class='btn btn-danger' onclick='del_my_post("+item.id+",this)'>删除</button>")
		var delDiv = $("<div class='del-div col-lg-2'></div>").append(delBtn)
		var postLi = $("<li class='my-post-li col-lg-12 text-link' style='padding:15px;'></li>").append(postDiv).append(delDiv)
		postUl.append(postLi)
	})
	return postUl;
}

/*
	构建文件列表
*/

function build_my_data_ul(result){
	var dataUl = $("<ul class='my-data-ul'></ul>")
	$.each(result.extend.list,function(index,item){
		var dataLi = $("<li class='col-lg-12 data-li'></li>")
		var dataDiv = $("<div class='data-div col-lg-8'></div>")
		var dataA = $("<a href='javascript:;' class='col-lg-12' onclick='data_detail("+item.id+")' title='点击查看详情'>文件名: &nbsp;&nbsp;&nbsp;</a>").append(item.name)
		var description = item.description;
		if(description == ""){
			description = "暂无描述";
		}
		var dataDesc = $("<span class='col-lg-12 data-desc' title='文件描述'>描述: &nbsp;&nbsp;&nbsp;</span>").append(description)
		dataDiv.append(dataA).append(dataDesc)
		var delBtn = $("<button class='btn btn-danger' onclick='del_my_data("+item.id+",this)'>删除</button>")
		var delDiv = $("<div class='del-div col-lg-2'></div>").append(delBtn)
		dataLi.append(dataDiv).append(delDiv)
		dataUl.append(dataLi)
	})
	return dataUl;
}
/*
 * 构建私信列表
 * */
function build_my_secretMsg(result) {
	var ul = $("<ul><ul>")
	$.each(result.extend.secretMsgs,function(index,item){
		var li = $("<li class='col-lg-12' style='padding:15px;border-bottom: 1px dotted #e4e6eb;'></li>")
		var senderA = $("<a class='col-lg-8' href='/index/toUserPage?id="+item.sender.id+"'></a>").text("发送人: "+item.sender.username)
		var dataSpan = $("<span class='col-lg-2'></span>").text(dateFormat(new Date(item.date)))
		var msgSpan = $("<span class='col-lg-12'></span>").text(item.msg)
		li.append(msgSpan).append(senderA).append(dataSpan)
		ul.append(li)
	})
	return ul;
}
/*
 * 构建关注列表
 */
function build_my_concern(result){
	var ul = $("<ul><ul>")
	$.each(result.extend.concerns,function(index,item){
		if(item.user.id != item.concernUser.id){//不是自己
			var li = $("<li class='col-lg-12' style='padding:15px;border-bottom: 1px dotted #e4e6eb;'></li>")
			var usernameSpan = $("<span class='text-info'></span>").append("用户名: " +item.concernUser.username)
			var usernameA = $("<a class='col-lg-12' href='/index/toUserPage?id="+item.concernUser.id+"' style='padding:0;'></a>").append(usernameSpan)
			var text = ""
				var rank = item.concernUser.rank;
			if(rank == 1 || rank == 2 || rank == 3){
				text = "初级粉丝: Lv"+rank;
			}else if(rank == 4 || rank == 5 || rank == 6){
				text = "正式会员: Lv"+rank;
			}
			var userRankSpan = $("<span class='text-muted'></span>").append("用户等级: "+text)
			li.append(usernameA).append(userRankSpan)
			ul.append(li)
		}
	})
	return ul;
}


/*
删除帖子
*/
function del_my_post(postId,ele) {
	if(confirm("是否删除?")){
		$.ajax({
			type: 'POST',
			url: '/post/deleteById',
			data: 'id='+postId,
			async: true,
			cache: false,
			success:function(result){
				if(result.code == 1){
					$(ele).parent().parent().remove()
					getMyPostCount()
				}
				alert(result.msg)
			}
		})
	}
}
/*
	删除文件
*/
function del_my_data(dataId,ele) {
	if(confirm("是否删除?")){
		$.ajax({
			type: 'POST',
			url: '/data/deleteById',
			data: 'id='+dataId,
			async: true,
			cache: false,
			success:function(result){
				if(result.code == 1){
					$(ele).parent().parent().remove()
					getMyDataCount()
				}
				alert(result.msg)
			}
		})
	}
}

/*
获取我发的帖子的数量
*/
function getMyPostCount() {
	$.ajax({
		type: 'GET',
		url: '/post/getPostCountByUserId',
		data: 'id='+$(".hide-id").val(),
		cache: true,
		async: false,
		success: function(result){
			$(".my-post").find("a").empty().append("我的帖子("+result.extend.count+")")
		}
	})
}
/*
获取我上传的文件的数量
*/
function getMyDataCount() {
	$.ajax({
		type: 'GET',
		url: '/data/getDataCountByUserId',
		data: 'id='+$(".hide-id").val(),
		cache: true,
		async: false,
		success: function(result){
			if(result.code == 0){
				$(".my-data").find("a").empty().append("我的资料(0)")
			}else{
				$(".my-data").find("a").empty().append("我的资料("+result.extend.count+")")
			}
		}
	})
}

/*
获取我的私信
*/
function getMySecretMsg() {
	var msg;
	$.ajax({
		type: 'GET',
		url: '/secret/getMySecretMsg',
		data: "userId="+$(".hide-id").val(),
		async: false,
		success: function(result) {
			if(result.code == 0){
				$(".my-secret-msg").find("a").text("我的私信(0)")
			}else{
				$(".my-secret-msg").find("a").text("我的私信("+result.extend.secretMsgs.length+")")
			}
			msg = result
		}
	})
	return msg;
}

/*
 * 获取我的关注
 * 
 */

function getMyConcern() {
	var msg;
	$.ajax({
		type: 'GET',
		url: '/userConcern/getMyConcern',
		data: "userId="+$(".hide-id").val(),
		async: false,
		success: function(result) {
			if(result.code == 0){
				$(".my-concern").find("a").text("我的关注(0)")
			}else{
				$(".my-concern").find("a").text("我的关注("+result.extend.concerns.length+")")
			}
			msg = result
		}
	})
	return msg;
}