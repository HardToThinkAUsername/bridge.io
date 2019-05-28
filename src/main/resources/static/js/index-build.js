/**
 * 首页初始化
 */


/*
	构建热帖列表
*/
$.ajax({
	type: "GET",
	url: "post/getHotPostInit",
	cache: false,
	data: 'pageSize=3&pageNum=0',
	success: function(result){
		var posts = result.extend.posts; 
		if(result.code == 1){
			build_post_li(posts,$(".hot-post-ul"),0);
		}
	}
})
/*
	构建资料列表
*/
$.ajax({
	type: "GET",
	url: "/data/getDataInit",
	cache: true,
	success: function(result){
		var datas = result.extend.datas.list;
		$.each(datas,function(index,item){
			var dataTitleDiv;
			var dataDisplayLi = $("<li></li>");
			var dataDisplayDiv = $("<div class='col-lg-12 data-display'></div>");
			if(item.description == "")
				dataTitleDiv = $("<div class='data-title'><p><a href='javascript: ;' onclick='data_detail("+item.id+")'>"+item.name+"</a></p></div>")
			else 
				dataTitleDiv = $("<div class='data-title'><p><a href='javascript: ;' onclick='data_detail("+item.id+")'>"+item.description+"</a></p></div>")
			var dataDateDiv = $("<div class='data-date'><p class='date-p'>"+dateFormat(new Date(item.date))+"</p></div>")
			dataDisplayDiv.append($("<div class='col-lg-8'></div>").append(dataTitleDiv).append(dataDateDiv));
			dataDisplayLi.append(dataDisplayDiv)
			if(index < 3){
				$(".data-area-left").find("ul").append(dataDisplayLi)
			}else {
				$(".data-area-right").find("ul").append(dataDisplayLi)
			}
		})
	}
})

/*
	构建求助中心列表
*/
$.ajax({
	type: 'GET',
	url: '/post/getHelpPostInit',
	cache: true,
	success: function(result){
		var posts = result.extend.helpPost;
		build_post_li(posts,$(".help-post-ul"),1)
	}
})

/*
	构建介绍中心列表
*/
$.ajax({
	type: 'GET',
	url: '/post/getIntroPostInit',
	cache: true,
	success: function(result){
		var posts = result.extend.introPost;
		build_post_li(posts,$(".intro-post-ul"),2)
	}
})

/*
	构建热帖和求助帖 子列表
	posts: 帖子集
	ele: 列表ul的JQuery对象
	type: 0: 热帖   1:求助   2:介绍
*/
function build_post_li(posts,ele,type){
	var postBody = "";
	if(type == 2){//介绍类
		$.each(posts,function(key,value){
			var hotPotLi = $("<li class='col-lg-12' style='margin-left:-55px;'></li>")
			var textAreaDiv = $("<div class='col-lg-9 col-lg-offset-2 text-area'></div>")
			var postTitleDiv = $("<div class='post-title-area'><p class='post-title'><span>"+value.postTitle+"</span></p></div>");
			
			var postAuthorDiv = $("<div class='post-author-msg' style='margin-bottom:15px;'></div>")
			var postAuthorLabel = $("<label>发帖人: </label> <span class='post-author'>"+value.user.username+"</span> <br />")
			var postDateLabel = $("<label> 发帖时间: </label> <span class='post-date'>"+dateFormat(new Date(value.date))+"</span>")
			postAuthorDiv.append(postAuthorLabel).append(postDateLabel);
			
			postBody = value.postBody.replace("\"","'");
			var postBodyA = $("<a href='javascript: ;' onclick='post_detail("+value.id+","+type+")'></a>");
			if(value.postBody.length > 100){
				postBody = value.postBody.substring(0,100)+"...";
			}else{
				postBody = value.postBody;
			}
			postBodyA.append(postBody);
			textAreaDiv.append(postTitleDiv).append(postAuthorDiv).append(postBodyA);
			if(key.indexOf("http") >= 0){
				var imgAreaDiv = $("<div class='col-lg-9 col-lg-offset-2 img-area' style='float: left;'><a href='javascript: ;' onclick='post_detail("+value.id+","+type+")'><img src='"+key+"'></a></div>")
				hotPotLi.append(imgAreaDiv)
			}else {
				var imgAreaDiv = $("<div class='col-lg-9 col-lg-offset-2 img-area' style='float: left;'><a href='javascript: ;' onclick='post_detail("+value.id+","+type+")'><img src='http://localhost:8080/resources/bridge.jpg'></a></div>")
				hotPotLi.append(imgAreaDiv)
			}
			hotPotLi.append(textAreaDiv);
			ele.append(hotPotLi);
		})
	}else {//热帖类  求助类
		$.each(posts,function(key,value){
			var hotPotLi = $("<li class='col-lg-12' style='margin-left:-55px;'></li>")
			var textAreaDiv = $("<div class='col-lg-6 text-area'></div>")
			var postTitleDiv = $("<div class='post-title-area'><p class='post-title'><span>"+value.postTitle+"</span></p></div>");
			
			var postAuthorDiv = $("<div class='post-author-msg' style='margin-bottom:15px;'></div>")
			var postAuthorLabel = $("<label>发帖人: </label> <span class='post-author'>"+value.user.username+"</span> <br />")
			var postDateLabel = $("<label> 发帖时间: </label> <span class='post-date'>"+dateFormat(new Date(value.date))+"</span>")
			postAuthorDiv.append(postAuthorLabel).append(postDateLabel);
			
			postBody = value.postBody.replace("\"","'");
			var postBodyA = $("<a href='javascript: ;' onclick='post_detail("+value.id+","+type+")'></a>");
			if(value.postBody.length > 100){
				postBody = value.postBody.substring(0,100)+"...";
			}else{
				postBody = value.postBody;
			}
			postBodyA.append(postBody);
			textAreaDiv.append(postTitleDiv).append(postAuthorDiv).append(postBodyA);
			
			if(key.indexOf("http") >= 0){
				var imgAreaDiv = $("<div class='col-lg-6 img-area' style='float: left;'><a href='javascript: ;' onclick='post_detail("+value.id+","+type+")'><img src='"+key+"'></a></div>")
				hotPotLi.append(imgAreaDiv)
			}else {
				var imgAreaDiv = $("<div class='col-lg-6 img-area' style='float: left;'><a href='javascript: ;' onclick='post_detail("+value.id+","+type+")'><img src='http://localhost:8080/resources/bridge.jpg'></a></div>")
				hotPotLi.append(imgAreaDiv)
			}
			hotPotLi.append(textAreaDiv);
			ele.append(hotPotLi);
		})
	}
}


/*
	时间格式转换
*/

function dateFormat(date){
	var datetime = date.getFullYear()
        + "年"// "年"
        + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
                + (date.getMonth() + 1))
        + "月"// "月"
        + (date.getDate() < 10 ? "0" + date.getDate() : date
                .getDate())
        + "日";//"日"
 	return datetime;
}