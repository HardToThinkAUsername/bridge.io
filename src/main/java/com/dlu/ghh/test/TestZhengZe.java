package com.dlu.ghh.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public class TestZhengZe {
	
	@Test
	public void test() {
		String string = "<div label-module=\"para\" style=\"overflow-wrap: break-word; margin-bottom: 15px; text-indent: 2em; line-height: 24px; zoom: 1; font-family: arial, 宋体, sans-serif;\"><div label-module=\"para\" style=\"overflow-wrap: break-word; margin-bottom: 15px; text-indent: 2em; line-height: 24px; zoom: 1;\"><a target=\"_blank\" href=\"https://baike.baidu.com/item/%E6%B4%9B%E9%98%B3%E6%A1%A5\" style=\"color: rgb(19, 110, 194);\">洛阳桥</a>：原名“<a target=\"_blank\" href=\"https://baike.baidu.com/item/%E4%B8%87%E5%AE%89%E6%A1%A5\" style=\"color: rgb(19, 110, 194);\">万安桥</a>”。我国现存最早的跨海梁式大石桥，位于福建省泉州东郊的洛阳江上，是世界桥梁筏形基础的开端，为全国重点文物保护单位。</div><div style=\"position: relative; font-size: 13.3333px; text-indent: 0px;\"><a name=\"2_2\" style=\"color: rgb(19, 110, 194); position: absolute; top: -50px;\"></a><a name=\"sub581885_2_2\" style=\"color: rgb(19, 110, 194); position: absolute; top: -50px;\"></a><a name=\"洛阳桥历史\" style=\"color: rgb(19, 110, 194); position: absolute; top: -50px;\"></a><a name=\"2-2\" style=\"color: rgb(19, 110, 194); position: absolute; top: -50px;\"></a></div><div label-module=\"para-title\" style=\"clear: both; zoom: 1; margin-top: 20px; margin-bottom: 12px; line-height: 20px; font-size: 18px; font-family: &quot;Microsoft YaHei&quot;, SimHei, Verdana; text-indent: 0px;\"><h3 style=\"margin-top: 0px; margin-bottom: 0px; font-size: 18px; color: rgb(51, 51, 51);\">洛阳桥历史</h3></div><div label-module=\"para\" style=\"overflow-wrap: break-word; margin-bottom: 15px; text-indent: 2em; line-height: 24px; zoom: 1;\">洛阳桥，宋代泉州太守<a target=\"_blank\" href=\"https://baike.baidu.com/item/%E8%94%A1%E8%A5%84\" style=\"color: rgb(19, 110, 194);\">蔡襄</a>主持建桥工程。从北宋皇祐四年（公元1053年）至嘉祐四年（公元1059年），前后历七年之久，耗银一千四百万两，建成了这座跨江接海的大石桥。据史料记载，初建时桥长三百六十丈，宽一丈五尺，武士造像分立两旁。造桥工程规模巨大。结构工艺技术高超，名震四海。</div><div label-module=\"para\" style=\"overflow-wrap: break-word; margin-bottom: 15px; text-indent: 2em; line-height: 24px; zoom: 1;\">建桥九百余年以来，先后修复十七次。大修有宋绍兴八年（公元1138年），飓风、桥坏。郡守<a target=\"_blank\" href=\"https://baike.baidu.com/item/%E8%B5%B5%E6%80%9D%E8%AF%9A\" style=\"color: rgb(19, 110, 194);\">赵思诚</a>修复；明宣德间（公元1426年－1435年）桥址下沉，潮至，桥梁俱没。知府冯桢命郡人李俊育（即<a target=\"_blank\" href=\"https://baike.baidu.com/item/%E6%9D%8E%E4%BA%94\" style=\"color: rgb(19, 110, 194);\">李五</a>）僧正淳，增高三尺；万历三十二年（公元1604年），地大震，桥梁倒塌，基址低陷，知府<a target=\"_blank\" href=\"https://baike.baidu.com/item/%E5%A7%9C%E5%BF%97%E7%A4%BC\" style=\"color: rgb(19, 110, 194);\">姜志礼</a>修复；清雍正八年（公元1730年）秋，桥崩，知县王之琦修复；民国二十一年（公元1932年）蔡延锴军长改建为钢筋混凝土公路桥，桥面增高2米。抗日战争时期受到严重破坏；1993年3月-1996年10月，国家拨出600多万元专款，实施洛阳桥保护修复工程。现桥长731.29米、宽4.5米、高7.3米，有44座船形桥墩、645个扶栏、104只<a target=\"_blank\" href=\"https://baike.baidu.com/item/%E7%9F%B3%E7%8B%AE\" style=\"color: rgb(19, 110, 194);\">石狮</a>、1座石亭、7座石塔。</div><div label-module=\"para\" style=\"overflow-wrap: break-word; margin-bottom: 15px; text-indent: 2em; line-height: 24px; zoom: 1;\">桥之中亭附近历代碑刻林立，有“万古安澜”等宋代<a target=\"_blank\" href=\"https://baike.baidu.com/item/%E6%91%A9%E5%B2%A9%E7%9F%B3%E5%88%BB\" style=\"color: rgb(19, 110, 194);\">摩岩石刻</a>；桥北有<a target=\"_blank\" href=\"https://baike.baidu.com/item/%E6%98%AD%E6%83%A0%E5%BA%99\" style=\"color: rgb(19, 110, 194);\">昭惠庙</a>、真身庵遗址；桥南有蔡襄祠，著名的蔡襄《万安桥记》宋碑，即立于祠内，被誉为书法、记文、雕刻“三绝”。</div><div label-module=\"para\" style=\"overflow-wrap: break-word; margin-bottom: 15px; text-indent: 2em; line-height: 24px; zoom: 1;\">洛阳桥的建造，是对世界桥梁科学的一大贡献。由于当时洛阳<a target=\"_blank\" href=\"https://baike.baidu.com/item/%E6%B1%9F%E6%BD%AE\" style=\"color: rgb(19, 110, 194);\">江潮</a>狂水急,“水阔五里”、“深不可址”,桥基屡被摧毁。造桥工匠创造了一种直到近代才被人们认识的新型桥基——筏形基础,就是沿着桥的中轴线抛置大量石块,形成一条连结江底的矮石堤,然后在上面建造船形墩。</div><div label-module=\"para\" style=\"overflow-wrap: break-word; margin-bottom: 15px; text-indent: 2em; line-height: 24px; zoom: 1;\">同时采用“激浪涨舟,浮运架梁”的妙法,把一条条重达数吨的大石板架在桥面上。他们又在桥下养殖大量牡蛎,把桥基涵和桥墩石胶合凝结成牢固的整体。这就是造桥史上最别出心裁的“种蛎固基法”,也是世界上第一个把生物学运用于桥梁工程的创举。洛阳桥的建成,不仅使洛阳江天堑变成通途,对泉州海外交通事业的发展也起着重大的作用。<img src=\"http://localhost:8080/resources/post/11/luoyangbridge.jpg\" style=\"text-indent: 2em; max-width: 100%;\"></div></div>";
		int indexOf1 = string.indexOf("<img src=");
		int indexOf2 = string.indexOf("style=", indexOf1);
		System.out.println(new String(string.toCharArray(),indexOf1+10,(indexOf2-indexOf1-12)));
		
	}
	@Test
	public  void test2(){
		
		String regex="<span class=span1>￥(\\d+[.]\\d+)</span>";
		Pattern pattern=Pattern.compile(regex);
		String input="<li>本网价：<span class=span1>￥42.70</span></li></ul><ul><li>市 场 价：<s>￥49.00</s></li><li>折 扣：88折<span class=span3> 节省：￥6.30</span></li>";
		Matcher matcher=pattern.matcher(input);
		// 找 42.70
		while(matcher.find()){
			System.out.println(matcher.group(1));
		}
		regex="<s>￥(\\d+[.]\\d+)</s>";
		pattern=Pattern.compile(regex);
		matcher=pattern.matcher(input);
		// 找 49.00
		while(matcher.find()){
		System.out.println(matcher.group(1));
		}
		System.out.println();
	}
	
	@Test
	public void test4() {
		StringBuffer text = new StringBuffer("");
		String string = "<div class='hot-post-title-area'><p class='hot-post-title'>45645<span>你好啊</span></p></div>";
		Integer indexOf1 = -1;
		Integer indexOf2 = -1;
		while(true) {
			indexOf1 = string.indexOf(">");
			if(indexOf1 != -1 && indexOf1 != string.length()-1) {
				string = string.substring(indexOf1+1);
				indexOf2 = string.indexOf("<");
				text.append(string.substring(0, indexOf2));
			}else {
				break;
			}
		}
		System.out.println(text.toString());
	}
	@Test
	public void test3() {
		System.out.println("'http://localhost:8080/resources/bridge.jpg 'value='"+new Date()+"'");
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),Locale.getDefault(Locale.Category.FORMAT));
		System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
	}
	
	@Test
	public void test5() throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = simpleDateFormat.parse("1998-11-06");
		System.out.println(date);
	}
	
}
