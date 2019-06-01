/*
	构建所有帖子列表
*/
function build_post_all(result){
    var postUl = $("<ul class='post-ul col-lg-12'></ul>")
    var posts = result.extend.pageInfo.list;
    if(posts.length <= 0){
        return postUl.append($("<span class='text-muted col-lg-12' style='padding:15px;'>没有找到对应帖子!</span>"))
    }
	$.each(posts,function(index,item){
		var postBody = ""
		var postLi = $("<li class='post-li col-lg-12'></li>")
		var floorsDiv = $("<div class='floors-div col-lg-1'></div>")
		var floorsSpan = $("<span title='回复' class='floors-span'></span>")
		floorsDiv.append(floorsSpan.append(item.floors))
		var postTitleA = $("<a href='javascript: ;' onclick='post_detail("+item.id+")' title='"+item.postTitle+"'></a>").append(item.postTitle)
		if((item.postBody).length < 100){
			postBody = item.postBody;
		}else {
			postBody = (item.postBody).substring(0,100) +"..."
		}
		var postBodyp = $("<p></p>").append(postBody)
		var postDiv = $("<div class='post-div col-lg-9'></div>")
		postDiv.append(postTitleA).append(postBodyp)
		
		var userDiv = $("<div class='col-lg-2 user-div'></div>")
		var authorIcon = $("<span class='glyphicon glyphicon-user' style='color:#e2e1e1'></span>")
		var authorName = $("<span ></span>").append("&nbsp;&nbsp;"+item.user.username)
		var authorA = $("<a href='/index/toUserPage?id="+item.user.id+"' title='帖子作者: "+item.user.username+"'></a>").append(authorIcon).append(authorName)
		var authorDiv = $("<div class='col-lg-12 author-div'></div>").append(authorA)
		
		var date = new Date(item.date)
		var sameDay = isSameDay(new Date(), date)
		var sameYear = isSameYear(new Date(), date)
		var dateDiv = $("<div class='date-div col-lg-12'></div>")
		if(sameDay){// 发布日期和现在是同一天
			var dateSpan = $("<span class='date-span' title='发布时间'></span>").append(date.Format("hh:mm:ss"))
			dateDiv.append(dateSpan)
		}else if(sameYear){//是同一年
			var dateSpan = $("<span class='date-span' title='发布时间'></span>").append(date.Format("MM-dd hh:mm:ss"))
			dateDiv.append(dateSpan)
		}else{//不是同一年
			var dateSpan = $("<span class='date-span' title='发布时间'></span>").append(date.Format("yyyy-MM-dd hh:mm:ss"))
			dateDiv.append(dateSpan)
		}
		userDiv.append(authorDiv).append(dateDiv);
		postUl.append(postLi.append($("<div class='li-line'></div>").append(floorsDiv).append(postDiv).append(userDiv)));
	})
	return postUl;
}
/*
	构建资料列表
*/
function build_data(result){
	var dataUl = $("<ul class='data-ul'></ul>")
	$.each(result.extend.pageInfo.list,function(index,item){
		var dataLi = $("<li class='col-lg-12 data-li'></li>")
		var dataDiv = $("<div class='data-div col-lg-8'></div>")
		var dataA = $("<a href='javascript:;' class='col-lg-12' onclick='data_detail("+item.id+")' title='点击查看详情'>文件名: &nbsp;&nbsp;&nbsp;</a>").append(item.name)
		var description = item.description;
		if(description == ""){
			description = "暂无描述";
		}
		var dataDesc = $("<span class='col-lg-12 data-desc' title='文件描述'>描述: &nbsp;&nbsp;&nbsp;</span>").append(description)
		dataDiv.append(dataA).append(dataDesc)
		dataLi.append(dataDiv)
		dataUl.append(dataLi)
	})
	return dataUl;
}
/*
资料详情
*/
function data_detail(dataId) {
	$.ajax({
		type: 'GET',
		url: '/data/userRead',
		cache: false,
		data: 'id='+dataId,
		async: false,
		success: function(result) {
			if(result.code == 1){
				var containerContent = $(".container-content").empty();
				containerContent.append(build_data_detail(result))
			}else{
				alert(result.msg)
			}
		},
		error: function() {
			alert("加载出错,请检查网络刷新重试!");
		}
	})
}
function build_data_detail(result){
	var data = result.extend.data;
	var dataDiv = $("<div class='col-lg-12 data-area' style='padding:15px;'></div>")
	var sessionUser = result.extend.sessionUser;
	//文件名字
	var dataNameLable = $("<lable class='col-lg-2 text-info'>文件名称:</lable>")
	var dataNameSpan = $("<span class='col-lg-10 text-muted'></span>").append(data.name)
	var dataNameDiv = $("<div class='col-lg-12 data-name'></div>").append(dataNameLable).append(dataNameSpan)
	//文件描述
	var desc = data.description;
	if(desc == ""){
		desc = "暂无描述"
	}
	var dataDescLable = $("<lable class='col-lg-2 text-info'>文件描述:</lable>")
	var dataDescSpan = $("<span class='col-lg-10 text-muted'></span>").append(desc)
	var dataDescDiv = $("<div class='col-lg-12 data-desc'></div>").append(dataDescLable).append(dataDescSpan)
	//作者
	var dataAuthorLable = $("<lable class='col-lg-2 text-info'>上传人:</lable>")
	var dataAuthorSpan = $("<span class='col-lg-10 text-muted'></span>").append(data.user.username)
	var dataAuthorA = $("<a href='/index/toUserPage?id="+data.user.id+"'></a>").append(dataAuthorSpan)
	var dataAuthorDiv = $("<div class='col-lg-12 data-desc'></div>").append(dataAuthorLable).append(dataAuthorA)
	//上传日期
	var dataDateLable = $("<lable class='col-lg-2 text-info'>上传日期:</lable>")
	var dataDateSpan = $("<span class='col-lg-10 text-muted'></span>").append(dateFormat(new Date(data.date)))
	var dataDateDiv = $("<div class='col-lg-12 data-desc'></div>").append(dataDateLable).append(dataDateSpan)
	//下载次数
	var dataDownloadTimesLable = $("<lable class='col-lg-2 text-info'>下载次数:</lable>")
	var dataDownloadTimesSpan = $("<span class='col-lg-10 text-muted download-times'></span>").append(data.downloadTimes)
	var dataDownloadTimesDiv = $("<div class='col-lg-12 data-desc'></div>").append(dataDownloadTimesLable).append(dataDownloadTimesSpan)
	//下载
	var dataDownloadSpan;
	var dataDownA;
	if(sessionUser != "null"){
		dataDownloadSpan = $("<span class='text-muted'>点击下载</span>")
		dataDownA = $("<a href='"+data.address+"' style='padding:15px;' onclick='download_finish("+data.id+")' download='"+data.name+"'></a>").append(dataDownloadSpan)
	}else{
		dataDownloadSpan = $("<span class='text-muted'>登录后才能下载!</span>")
		dataDownA = $("<a href='javascript:;' style='padding:15px;'></a>").append(dataDownloadSpan)
	}
	var dataDownDiv = $("<div class='col-lg-2' style='margin-top:15px;'></div>").append(dataDownA)
	return dataDiv.append(dataNameDiv)
				  .append(dataDescDiv)
				  .append(dataAuthorDiv)
				  .append(dataDateDiv)
				  .append(dataDownloadTimesDiv)
				  .append(dataDownDiv);
}
function download_finish(dataId) {
	$.ajax({
		type: "POST",
		url: '/data/download',
		data: 'id='+dataId,
		success: function(result) {
			if(result.code ==  1){
				$(".download-times").text(result.extend.times)
			}
		}
	})
}

/*
	帖子的详情
*/
function post_detail(postId){
	$.ajax({
		type: 'GET',
		url: '/post/userRead',
		cache: true,
		data: 'id='+postId,
		async: false,
		success: function(result) {
			if(result.code == 1){
				var containerContent = $(".container-content").empty();
				containerContent.append(build_post_detail(result))
			}else{
				alert(result.msg)
			}
		},
		error: function() {
			alert("加载出错,请检查网络刷新重试!");
		}
	})
}



/*
 * 构建帖子详情
 * */

function build_post_detail(result) {//container-content  dateFormat
	var post = result.extend.post;//帖子
	var user = post.user;//作者
	var floors = result.extend.floors;//楼层
	var sessionUser = result.extend.sessionUser;
	var userDiv = build_user_Div(user);
	var roleSpan = $("<span class='text-info'>楼主</span>")
	var roleDiv = $("<div class='role-div col-lg-6 col-lg-offset-6'><div>").append(roleSpan)
	userDiv.find(".user-img-div").before(roleDiv)
	
	//帖子区域
	//帖子标题
	var postTitleSpan = $("<span style='line-height:35px;height:45px;line-height:45px;font-size:20px;'></span>").append(post.postTitle)
	var centuryId = post.century;
	var centuryStr = "";
	if(centuryId == 1){
		centuryStr = "18世纪前";
	}else if(centuryId == 2){
		centuryStr = "18-19世纪";
	}else if(centuryId == 3){
		centuryStr = "20世纪";
	}else if(centuryId == 4){
		centuryStr = "21世纪";
	}
	var postCenturySpan = $("<span style='padding:15px;' class='text-info'></span>").append(centuryStr)
	var postTitleA = $("<a href='javascript:;' class='col-lg-10'></a>").append(postTitleSpan).append(postCenturySpan)
	var prasingSpan = $("<span onclick='prasing_post("+post.id+",this)'>赞<span>")
	var prasingA = $("<a href='javascript:;' class='prasing-post btn btn-xs btn-default'></a>").append(prasingSpan)
	var commentSpan = $("<span>回复</span>")
	var commentA = $("<a href='#comment-area' class='comment-post btn btn-xs btn-default'></a>").append(commentSpan)
	var postTitleDiv = $("<div class='col-lg-12' style='min-height:45px;line-height:45px;border-bottom: 1px solid;'></div>").append(postTitleA).append(prasingA).append("&nbsp;&nbsp;").append(commentA)
	//帖子内容
	var postBodySpan = $("<span></span>").append(post.postBody)
	var postBodyDiv = $("<div class='col-lg-12'></div>").append(postBodySpan)
	
	var floorNumSpan = $("<span class='text-info'>楼主</span>")
	var floorNumDiv = $("<div class='col-lg-3'></div>").append(floorNumSpan);
	var realeaseDateSpan = $("<span></span>").append((new Date(post.date)).Format("yyyy-MM-dd hh:mm:ss"))
	var realeaseDateDiv = $("<div class='col-lg-9'></div>").append(realeaseDateSpan)
	var postBottomDiv1 = $("<div class='col-lg-4 col-lg-offset-9'></div>").append(floorNumDiv).append(realeaseDateDiv)
	var postBottomDiv2 = $("<div class='col-lg-12'></div>").append(postBottomDiv1)
	var postDiv = $("<div class='post-area col-lg-10' style='padding:0;'></div>").append($("<div class='col-lg-12' style='min-height:150px;'></div>").append(postTitleDiv).append(postBodyDiv)).append(postBottomDiv2)
	var postAuthorDiv = $("<div class='col-lg-12 post-author-area' style='border-bottom:2px #eceff2 solid;min-height:180px;'></div>").append(userDiv).append(postDiv)
	
	//楼层信息
	var floorMsgSpan = $("<span class='text-info col-lg-2'></span>").append("本帖共&nbsp;<color class='floors-num' style='color:red'>"+post.floors+"</color>&nbsp;楼")
	//被赞的次数
	var prasiedSpan = $("<span class='text-info col-lg-2'></span>").append("共&nbsp;<color style='color:red' class='pra-num'>"+post.praisedTimes+"</color>&nbsp;次赞")
	var postMsgDiv = $("<div class='col-lg-12' style='height:45px;line-height:45px;background:#eceff2;'></div>").append(floorMsgSpan).append(prasiedSpan)
	
	var contentDiv = $("<div class='content row'></div>").append(postMsgDiv).append(postAuthorDiv)
    
	var floorsDiv = build_post_floors(floors,post.id,sessionUser);
	var commentDiv = build_comment_area(post.id);
	return contentDiv.append(floorsDiv).append(commentDiv);
}

/*
 * 赞帖子
 */

function prasing_post(postId,ele) {
	$.ajax({
		type: 'POST',
		url: '/postUser/prasingPost',
		data: 'id='+postId,
		cache: false,
		success: function(result) {
			if(result.code == 1){
				if(result.extend.num == 2){//点赞成功
					var num = parseInt($(".pra-num").text()) + 1
					$(".pra-num").text(num)
				}
				$(ele).text("已赞")
			}
		}
	})
}

/*
 * 构建回复区域
 */
function build_comment_area(postId) {
	var commentDiv = $("<div class='comment-area col-lg-12 col-lg-offset-1' id='comment-area' style='padding:15px;'></div>")
	var editor = $("<div class='editor col-lg-10' id='comment-editor'></div>")
	
	var tipSpan = $("<span class='text-danger col-lg-3'>tip: 最多不能超过100个字</span>")
	var submitBtn = $("<button class='btn btn-sm btn-default col-lg-offset-8' onclick='comment_post("+postId+")'>发表</button>")
	var submitDiv = $("<div class='submit-div col-lg-10'></div>").append(tipSpan).append(submitBtn)
    rich_text_editor(editor);
    var commentTipSpan = $("<span class='col-lg-12'>发表评论</span>")
	return commentDiv.append(commentTipSpan).append(editor).append(submitDiv);
}
/*
 * 回复帖子
 */
function comment_post(postId){
	var text = $(".w-e-text").text()
	$.ajax({
		type: 'POST',
		url: '/floor/commentPost',
		data: 'id='+postId+"&comment="+text,
		success: function(result) {
			if(result.code == 1){
				post_detail(postId)
				var t = $(".floors-ul").find("li:first-child").offset().top;
				$(window).scrollTop(t);//滚动到锚点位置
			}
			alert(result.msg)
		}
	})
}

/*
 * 赞楼层
 * */
function prasied_floor(floorId,ele) {
	$.ajax({
		type:'POST',
		url: '/floor/prasiedFloor',
		data: "floorId="+floorId,
		success: function(result) {
			if(result.code == 1){
				$(ele).find("color").text(result.extend.times)
				$(ele).text("已赞")
			}else
				alert(result.msg)
				
		}
	})
}

/*
 * 删除楼层
 */
function remove_floor(floorId,postId,ele) {
	if(confirm("是否删除?")){
		$.ajax({
			type:'POST',
			url: '/floor/deleteById',
			data: "floorId="+floorId+"&postId="+postId,
			success: function(result) {
				if(result.code == 1){
					post_detail(postId)
				}
				alert(result.msg)
			}
		})
	}
}
/*
 * 构建楼层
 */
function build_post_floors(floors,postId,sessionUser) {
	var floorsDiv = $("<div class='floor-area col-lg-12'></div>")
	var floorsUl = $("<ul class='floors-ul'></ul>")
	$.each(floors, function(index,item) {
		var user = item.user;
		var userDiv = build_user_Div(user);//构建用户区域
		//内容区域
		var floorContentSpan = $("<span class='text-muted'></span>").append(item.content)
		var floorContentA = $("<a href='javascript:;'></a>").append(floorContentSpan)
		var floorContentDiv = $("<div class='center-block'></div>").append(floorContentA)
		var floorDiv = $("<div class='col-lg-10 bg-info' style='padding:15px;height:140px;'></div>").append(floorContentDiv)
		//信息区域
		var floorNumSpan = $("<span class='text-info col-lg-1 col-lg-offset-7'></span>").append(index+1+" 楼")
		var floorDateSpan = $("<span class='text-info'></span>").append((new Date(item.date)).Format("yyyy-MM-dd hh:mm:ss")).append("&nbsp;&nbsp;")
		var prasiedSpan = $("<span class='btn btn-xs btn-default' onclick='prasied_floor("+item.id+",this)'>赞(<color style='color:red;'>"+item.praisedTimes+"</color>)</span>")
		var replySpan = $("<span class='btn btn-xs btn-default reply-span' onclick='show_reply_floor("+item.id+",this)'>评论(<color style='color:red;' class='reply-num'>"+item.replyedTimes+"</color>)</span>")
		var removeSpan = $("<span class='btn btn-xs btn-default' onclick='remove_floor("+item.id+","+postId+",this)'>删除</span>")
		var floorMsgDiv = $("<div class='col-lg-10 floor-opt'></div>").append(floorNumSpan).append(floorDateSpan).append(prasiedSpan).append(replySpan)
		if(sessionUser != "null"){
			if(sessionUser.id == item.user.id){
				floorMsgDiv.append(removeSpan)
			}
		}
		var floorLi = $("<li class='col-lg-12' style='border-bottom:2px #eceff2 solid;padding:15px;min-height:180px;'></li>");
		//隐藏评论区域
		var hideReplyInput = $("<input type='text' val='' style='width:85%' placeholder='评论长度不能超过100个字符'/>")
		var hideReplyBtn = $("<button type='button' class='btn btn-xs btn-default' onclick='reply_floor("+item.id+",this)'>评论</button>")
		var hideReplyDiv = $("<div class='col-lg-10 col-lg-offset-2 hide-reply-area' style='display:none;padding:15px;'></div>").append(hideReplyInput).append(hideReplyBtn)
		var commentDiv = $("<div class='reply-area col-lg-10 col-lg-offset-2' style='padding:0;'></div>")
		floorLi.append(userDiv).append(floorDiv).append(floorMsgDiv).append(hideReplyDiv).append(commentDiv);
		floorsUl.append(floorLi)
	})
	var floorsTipSpan = $("<span class='col-lg-12'>评论区</span>")
	return floorsDiv.append(floorsTipSpan).append(floorsUl);
}

/*
 * 评论楼层
 */
function reply_floor(floorId,ele) {
	var text = $(ele).prev("input:text").val();
	$.ajax({
		type: "POST",
		url: "/comment/commentFloor",
		data: "comment="+text+"&floorId="+floorId,
		success: function(result) {
			if(result.code == 1){
				show_reply_floor(floorId,ele);
				var color = $(ele).parent().parent().find(".floor-opt").find(".reply-span").find("color");
				var num = parseInt(color.text())
				num = num + 1;
				color.text(num)
			}
			alert(result.msg)
		}
	})
}
/*
 * 显示评论楼层输入框
 */
function show_reply_floor(floorId,ele) {
	$.ajax({
		type: "GET",
		url: "/comment/getCommentByFloorId",
		data: "floorId="+floorId,
		success: function(result) {
			if(result.code == 1){
				var commentUl = build_floor_comment(result,floorId)
				$(ele).parent().parent().find(".reply-area").empty().append(commentUl)
			}
		}
	})
	$(ele).parent().next(".hide-reply-area").css("display","block");
}
/*
 * 构建楼层的评论
 */
function build_floor_comment(result,floorId) {
	var commentUl = $("<ul style='padding:0;'></ul>")
	var sessionUser = result.extend.sessionUser;
	$.each(result.extend.comments,function(index,item){
		var user = item.user
		//用户头像
		var userProfileImg = $("<img alt='' title='点击查看用户详情' width='100%' src='"+user.userImage.path+"'/>");
		var userProfileA = $("<a href='/index/toUserPage?id="+user.id+"'></a>").append(userProfileImg)
		var userProfileDiv = $("<div class='user-img-div col-lg-1'></div>").append(userProfileA)
		//用户名
		var usernameSpan = $("<span></span>").append(user.username)
		var usernameA = $("<a href='/index/toUserPage?id="+user.id+"' title='点击查看用户详情'></a>").append(usernameSpan)
		var usernameDiv = $("<div class='col-lg-12'></div>").append(usernameA)
		//评论内容
		var commentSpan = $("<span class='text-muted'></span>").append(item.content)
		var commentDiv = $("<div class='col-lg-12'></div>").append(commentSpan)
		var contentDiv = $("<div class='col-lg-9' style='padding:0;'></div>").append(usernameDiv).append(commentDiv)
		var commentLi = $("<li class='col-lg-12' style='padding:0;'></li>").append(userProfileDiv).append(contentDiv)
		if(sessionUser.id == user.id){
			//删除按钮
			var removeBtn = $("<button class='btn btn-xs btn-default' onclick='remove_reply("+item.id+","+floorId+",this)'>删除</button>")
			var removeDiv = $("<div class='col-lg-2'></div>").append(removeBtn)
			commentLi.append(removeDiv)
		}
		commentUl.append(commentLi)
	})
	return commentUl;
}

/*
 *删除对floor的回复 
 */

function remove_reply(commentId,floorId,ele) {
	if(confirm("是否删除?")){
		$.ajax({
			type:'POST',
			url: '/comment/deleteById',
			data: "commentId="+commentId+"&floorId="+floorId,
			success: function(result) {
				if(result.code == 1){
					var color = $(ele).parent().parent().parent().parent().parent().find(".floor-opt").find(".reply-span").find("color");
					var num = parseInt(color.text())
					num = num - 1;
					color.text(num)
					$(ele).parent().parent().remove();										
				}
				alert(result.msg)
			}
		})
	}
}
/*
 * 构建用户区域
 */
function build_user_Div(user) {
	//用户信息区域
	//用户头像
	var userProfileImg = $("<img alt='' title='点击查看用户详情' width='100%' src='"+user.userImage.path+"'/>");
	var userProfileA = $("<a href='/index/toUserPage?id="+user.id+"'></a>").append(userProfileImg)
	var userProfileDiv = $("<div class='user-img-div col-lg-10'></div>").append(userProfileA)
	//用户姓名
	var userNameSpan = $("<span title='点击查看用户详情'></span>").append(user.username);
	var userA = $("<a href='/index/toUserPage?id="+user.id+"'></a>").append(userNameSpan)
	var userNameDiv = $("<div class='col-lg-12'></div>").append(userA)
	//会员等级
	var rank = user.rank;
	if(rank == 1){
		rank = "初级粉丝&nbsp;Lv"+rank;
	}else if(rank == 2){
		rank = "初级粉丝&nbsp;Lv"+rank;
	}else if(rank == 3){
		rank = "初级粉丝&nbsp;Lv"+rank;
	}else if(rank == 4){
		rank = "正式会员&nbsp;Lv"+rank;
	}else if(rank == 5){
		rank = "中级会员&nbsp;Lv"+rank;
	}else if(rank == 6){
		rank = "高级会员&nbsp;Lv"+rank;
	}
	var userRankSpan = $("<span title='等级' class='text-info'></span>").append(rank);
	var userRankDiv = $("<div class='col-lg-12'></div>").append(userRankSpan);
	
	var userDiv = $("<div class='user-area col-lg-2' style='background:#fbfbfd;padding:0;text-align:center;height:150px;'></div>").append(userProfileDiv).append(userNameDiv).append(userRankDiv)
	
	return userDiv;
}

/*
	判断两个日期是不是同一天
*/
function isSameDay(date1,date2){
	var year1 = date1.getFullYear();//第一个日期的年份
	var month1 = date1.getMonth();//第一个日期的月份
	var day1 = date1.getDay();//第一个日期的天数
	
	var year2 = date2.getFullYear();//第二个日期的年份
	var month2 = date2.getMonth();//第二个日期的月份
	var day2 = date2.getDay();//第二个日期的天数
	if(year1 == year2 && month1 == month2 && day1 == day2)
		return true;
	else 
		return false;
}
/*
	判断两个日期是不是同一年
*/
function isSameYear(date1,date2){//判断两个日期是不是同一年
	var year1 = date1.getFullYear();//第一个日期的年份
	var year2 = date1.getFullYear();//第二个日期的年份
	if(year1 == year2)
		return true;
	else 
		return false;
}
/*
 * 日期格式转换
 */
Date.prototype.Format = function (fmt) { // author: meizz
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
/*
	构建分页条
	type: 查询帖子的类型 
	queryType: 0: 模糊查询 1:查询全部
*/
function build_page_bar(result,type,queryType){
	var pageNav = $("<nav></nav>")
	var pageUl = $("<ul class='pagination page-ul'></ul>")
	var navigatepageNums = result.extend.pageInfo.navigatepageNums;//所有导航页号
	var pageNum = result.extend.pageInfo.pageNum//当前页
	var pageSize = result.extend.pageInfo.pageSize//每页的数量
	var pages =result.extend.pageInfo.pages//总页数
	var functionName = ""
	if(queryType == 0){//模糊
		functionName = "get_fuzzy";
	}else if(queryType == 1){//所有
		functionName = "get_all";
	}else if(queryType == 2){//桥梁类型
		functionName = "search_by_keyword"
	}else if (queryType == 3) {//年代
		functionName = "search_by_century";
	}
	if(pageNum == 1){//如果当前页是第一页
		//首页
		pageUl.append($("<li class='disabled'><a href='javascript:;' onclick='"+functionName+"(1,10,"+type+")'><span>首页</span></a></li>"))
		//上一页
		pageUl.append($("<li class='disabled'><a href='javascript:;' onclick='"+functionName+"("+(pageNum-1)+",10,"+type+")'><span aria-hidden='true'>&laquo;</span></a></li>"))
	}else{
		//首页
		pageUl.append($("<li><a href='javascript:;' aria-label='Previous' onclick='"+functionName+"(1,10,"+type+")'><span aria-hidden='true'>首页</span></a></li>"))
		//上一页
		pageUl.append($("<li><a href='javascript:;' aria-label='Previous' onclick='"+functionName+"("+(pageNum-1)+",10,"+type+")'><span aria-hidden='true'>&laquo;</span></a></li>"))
	}
	if(pages > 5){//如果总页数大于5
		if(pageNum <= 3 && pageNum > 0){//如果当前页号小于3
			$.each(navigatepageNums,function(index,item){
				var pageLi;
				if(item <= 5){//只显示前五页
					if(item == pageNum){//如果是当前页
						pageLi = $("<li class='active'><a href='javascript:;' onclick='"+functionName+"("+item+",10,"+type+")'>"+item+" </a></li>")
					}else{
						pageLi = $("<li><a href='javascript:;' onclick='"+functionName+"("+item+",10,"+type+")'>"+item+" </a></li>")
					}
					pageUl.append(pageLi)
				}
			})
			pageUl.append($("<li><span class='text-info'>...</span></li>"))
			
		}else if(pageNum > 3 && pageNum < (pages-2)){//如果当前页号大于等于3并且小于等于pages-2
			pageUl.append($("<li><span class='text-info'>...</span></li>"))
			$.each(navigatepageNums,function(index,item){
				var pageLi;
				if(item >= (pageNum-2) && item <= (pageNum+2)){//只显示前后各两页和当前页
					if(item == pageNum){//如果是当前页
						pageLi = $("<li class='active'><a href='javascript:;' onclick='"+functionName+"("+item+",10,"+type+")'>"+item+" </a></li>")
					}else{
						pageLi = $("<li><a href='javascript:;' onclick='"+functionName+"("+item+",10,"+type+")'>"+item+" </a></li>")
					}
					pageUl.append(pageLi)
				}
			})
			pageUl.append($("<li><span class='text-info'>...</span></li>"))
		}else if(pageNum >= (pages-2)){//如果当前页号大于等于 pages-2
			pageUl.append($("<li><span class='text-info'>...</span></li>"))
			$.each(navigatepageNums,function(index,item){
				var pageLi;
				if((pages-item) < 5){//只显示后五页
					if(item == pageNum){//如果是当前页
						pageLi = $("<li class='active'><a href='javascript:;' onclick='"+functionName+"("+item+",10,"+type+")'>"+item+" </a></li>")
					}else{
						pageLi = $("<li><a href='javascript:;' onclick='"+functionName+"("+item+",10,"+type+")'>"+item+" </a></li>")
					}
					pageUl.append(pageLi)
				}
			})
		}
		
	}else if(pages <= 5 && pages > 0){//总页数大于0小于5
		$.each(navigatepageNums,function(index,item){
			var pageLi;
			if(item == pageNum){//如果是当前页
				pageLi = $("<li class='active'><a href='javascript:;' onclick='"+functionName+"("+item+",10,"+type+")'>"+item+" </a></li>")
			}else{
				pageLi = $("<li><a href='javascript:;' onclick='"+functionName+"("+item+",10,"+type+")'>"+item+" </a></li>")
			}
			pageUl.append(pageLi)
		})
	}
	if(pageNum == pages){//如果是最后一页
		//下一页
		pageUl.append($("<li class='disabled'><a href='javascript:;' onclick='"+functionName+"("+(pageNum+1)+",10,"+type+")'><span aria-hidden='true'>&raquo;</span></a></li>"))
		//尾页
		pageUl.append($("<li class='disabled'><a href='javascript:;' onclick='"+functionName+"("+pages+",10,"+type+")'><span aria-hidden='true'>尾页</span></a></li>"))
	}else{
		//下一页
		pageUl.append($("<li><a href='javascript:;' aria-label='Next' onclick='"+functionName+"("+(pageNum+1)+",10,"+type+")'><span aria-hidden='true'>&raquo;</span></a></li>"))
		//尾页
		pageUl.append($("<li><a href='javascript:;' onclick='"+functionName+"("+pages+",10,"+type+")'><span aria-hidden='true'>尾页</span></a></li>"))
	}
	return pageNav.append(pageUl);
}

function rich_text_editor(ele) {
	var E = window.wangEditor
	var editor = new wangEditor(ele.get(0))
	// 自定义菜单配置
	editor.customConfig.menus = [
	    'bold',  // 粗体
	    'fontSize',  // 字号
	    'fontName',  // 字体
	    'underline',  // 下划线
	    'justify'  // 对齐方式
	    //'emoticon',  // 表情
	]
	editor.create();
}
