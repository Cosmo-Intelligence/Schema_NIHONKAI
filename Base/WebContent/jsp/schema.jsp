<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.yokogawa.theraris.rtWeb.app.bean.SchemaInfoBean" %>
<%@ page import="com.yokogawa.theraris.rtWeb.app.bean.SchemaConfBean" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<%
SchemaInfoBean info = (SchemaInfoBean)request.getAttribute("info");
SchemaConfBean conf = (SchemaConfBean)request.getAttribute("conf");
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja">
	<head>
		<title>schema</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
		<meta http-equiv="content-script-type" content="text/javascript" />
		<meta http-equiv="Expires" content="0" />
		<style>
			#canvas {
				background: #666;
			}
			table {
				border-collapse:collapse;
				padding : 0px;
			}
			#regist {
				font-size:20px;
				font-weight:bold;
				width:155px;
				height:40px;
			}
		</style>
	</head>
	<body onLoad="init()"><form action="./regist" accept-charset="UTF-8" method="post">
	<div>
	<input type="hidden" id="photoSchemaUid" name="photoSchemaUid" value="<%=info.getPhoteSchemaUid() %>"/>
	<input type="hidden" id="kanjaID" name="kanjaID" value="<%=info.getKanjaID() %>"/>
	<input type="hidden" id="risId" name="risId" value="<%=info.getRisId() %>"/>
	<input type="hidden" id="userId" name="userId" value="<%=info.getUserId() %>"/>
	<input type="hidden" id="startPointX" name="startPointX"/>
	<input type="hidden" id="startPointY" name="startPointY"/>
	<input type="hidden" id="endPointX" name="endPointX"/>
	<input type="hidden" id="endPointY" name="endPointY"/>
		<table style="border:solid 1px;">
			<tr>
				<td style="width:80px;border:solid 1px;height:25px;">手の位置</td>
				<td style="border-bottom:solid 1px;height:25px;">
					<input type="radio" id="handPosition1" name="handPosition" value="0" <%  if("0".equals(info.getHandPosition())){ %>checked <%} %> onclick="changeImg('img0')" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="handPosition1">下げる</label>
				</td>
				<td style="border-bottom:solid 1px;">
					<input type="radio" id="handPosition2" name="handPosition" value="1" <%  if("1".equals(info.getHandPosition())){ %>checked <%} %> onclick="changeImg('img1')" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="handPosition2">胸の上</label>
				</td>
				<td style="border-bottom:solid 1px;">
					<input type="radio" id="handPosition3" name="handPosition" value="2" <%  if("2".equals(info.getHandPosition())){ %>checked <%} %> onclick="changeImg('img2')" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="handPosition3">頭の上</label>
				</td>
				<td style="width:150px;">
					<input type="radio" id="handPosition4" name="handPosition" value="3" <%  if("3".equals(info.getHandPosition())){ %>checked <%} %> onclick="changeImg('img3')" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="handPosition4">その他</label>
				</td>
			</tr>
			<tr>
				<td colspan=4 rowspan=10><canvas id="canvas" width="460" height="700"></canvas></td>
				<td  style="border:solid 1px;height:25px;">部位区分</td>
			</tr>
			<tr>
				<td style="height:25px;border-left:solid 1px;">
					<input type="radio" id="schemaBui1" name="schemaBui" value="0" <%  if("0".equals(info.getSchemaBui())){ %>checked <%} %>onclick="changeBui(this.value)" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="schemaBui1">頭部</label>
				</td>
			</tr>
			<tr>
				<td style="height:25px;border-left:solid 1px;">
					<input type="radio" id="schemaBui2" name="schemaBui" value="1" <%  if("1".equals(info.getSchemaBui())){ %>checked <%} %>onclick="changeBui(this.value)" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="schemaBui2">頚部</label>
				</td>
			</tr>
			<tr>
				<td style="height:25px;border-left:solid 1px;">
					<input type="radio" id="schemaBui3" name="schemaBui" value="2" <%  if("2".equals(info.getSchemaBui())){ %>checked <%} %>onclick="changeBui(this.value)" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="schemaBui3">胸部</label>
				</td>
			</tr>
			<tr>
				<td style="height:25px;border-left:solid 1px;">
					<input type="radio" id="schemaBui4" name="schemaBui" value="3" <%  if("3".equals(info.getSchemaBui())){ %>checked <%} %>onclick="changeBui(this.value)" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="schemaBui4">腹部</label>
				</td>
			</tr>
			<tr>
				<td style="height:25px;border-left:solid 1px;">
					<input type="radio" id="schemaBui5" name="schemaBui" value="4" <%  if("4".equals(info.getSchemaBui())){ %>checked <%} %>onclick="changeBui(this.value)" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="schemaBui5">骨盤</label>
				</td>
			</tr>
			<tr>
				<td style="height:25px;border-left:solid 1px;">
					<input type="radio" id="schemaBui6" name="schemaBui" value="5" <%  if("5".equals(info.getSchemaBui())){ %>checked <%} %>onclick="changeBui(this.value)" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="schemaBui6">四肢</label>
				</td>
			</tr>
			<tr>
				<td style="height:25px;border-left:solid 1px;">
					<input type="radio" id="schemaBui7" name="schemaBui" value="6" <%  if("6".equals(info.getSchemaBui())){ %>checked <%} %>onclick="changeBui(this.value)" <%if(info.getRef() ==1){%>disabled<%} %>/><label for="schemaBui7">その他</label>
				</td>
			</tr>
			<tr>
				<td style="height:192px;border-left:solid 1px;">
					<textarea id="schemaComment" name="schemaComment" rows=12 style="height:192px;" <%if(info.getRef() ==1){%>disabled<%} %>><%=info.getSchemaComment() %></textarea>
				</td>
			</tr>
			<tr>
				<td style="height:40px;border-left:solid 1px;text-align:center;">
					<button id="regist" name="regist"type="submit" style="<%if(info.getRef() ==1){%>display:none<%} %>" onclick="if(!check()){return false;}">登録</button>
				</td>
			</tr>
		</table>
	</div>
	</form>

		<script type="text/javascript">
			//権限設定0:操作可能、1:操作不可
			var ref = <%=info.getRef()%>;
			var canvas = document.getElementById("canvas");
			var context = canvas.getContext('2d');
			var objX, objY;
			var objWidth, objHeight;
			var x, y, relX, relY;
			var dragging = 'NO';
			var checkedBui = '0';
			var canvas_width = 460;
			var canvas_hegiht = 700;
			//画像読み込み
			var img = new Image();
			var img0 = new Image();
			var img1 = new Image();
			var img2 = new Image();
			var img3 = new Image();
			img0.src = "./getFileDataServlet?key=0";
			img1.src = "./getFileDataServlet?key=1";
			img2.src = "./getFileDataServlet?key=2";
			img3.src = "./getFileDataServlet?key=3";
			var image0OnloadFlg = 0;
			var image1OnloadFlg = 0;
			var image2OnloadFlg = 0;
			var image3OnloadFlg = 0;
			//線幅
			var lineWidth = 10;
			function init() {
				//読み込みのため待機0.5秒
				sleep(500);
				var handopos = <%=info.getHandPosition()%>;
				var roopCount = 0;
				//読み込み終わるまではループ
				//1万回動いても終わらない場合は強制終了。
				while(true){
					//読み込みのため待機0.01秒0.01*10000=100秒待っても取得できない場合はエラー
					sleep(100);
					roopCount++;
					if(image0OnloadFlg == 1
							&&image1OnloadFlg == 1
							&&image2OnloadFlg == 1
							&&image3OnloadFlg == 1){
						if(handopos == 0){
							img = img0;
						}else if(handopos == 1){
							img = img1;
						}else if(handopos == 2){
							img = img2;
						}else if(handopos == 3){
							img = img3;
						}
						context.drawImage(img,0,0,canvas_width, img.height * canvas_width / img.width);
						objX=<%=info.getStartPointX()%>;
						objY=<%=info.getStartPointY()%>;
						objWidth=<%=info.getEndPointX().subtract(info.getStartPointX())%>;
						objHeight=<%=info.getEndPointY().subtract(info.getStartPointY())%>;
						drawRect();
						break;
					}else if(roopCount > 10000){
						alert("画像が読み込めませんでした。")
						break;
					}
					/* 画像が読み込まれるのを待ってから処理を続行 */
					if(img0.complete){
						image0OnloadFlg = 1;
					}
					if(img1.complete){
						image1OnloadFlg = 1;
					}
					if(img2.complete){
						image2OnloadFlg = 1;
					}
					if(img3.complete){
						image3OnloadFlg = 1;
					}
				}
			}

			function drawRect() {
				context.clearRect(0, 0, canvas.width, canvas.height); // キャンバスをクリア
				context.drawImage(img,0,0,canvas_width, img.height * canvas_width / img.width);
				context.strokeStyle = 'rgb(255,00,000)'; //枠線の色は青
				context.lineWidth = lineWidth;
				context.strokeRect(objX, objY, objWidth, objHeight);
			}
			//引数にはミリ秒を指定します。（例：5秒の場合は5000）
			function sleep(a){
			  var dt1 = new Date().getTime();
			  var dt2 = new Date().getTime();
			  while (dt2 < dt1 + a){
			    dt2 = new Date().getTime();
			  }
			  return;
			}
			function onDown(e) {
				if (ref == 1){
					return;
					}
				// キャンバスの左上端の座標を取得
				var offsetX = canvas.getBoundingClientRect().left;
				var offsetY = canvas.getBoundingClientRect().top;
				// マウスが押された座標を取得
				x = e.clientX - offsetX;
				y = e.clientY - offsetY;
				// オブジェクト上の座標かどうかを判定
				if(objX + lineWidth <= x && (objX + objWidth - lineWidth) >= x && objY + lineWidth <= y && (objY + objHeight - lineWidth) >= y){
					dragging = 'MOVE'; // ドラッグ開始
					relX = objX - x;
					relY = objY - y;
				}else if((objX - lineWidth) < x && (objX + lineWidth) > x && (objY + lineWidth) < y && (objY + objHeight - lineWidth ) > y){
					dragging = 'RESIZE_L'; // ドラッグ開始
					relX = objX - x;
				}else if((objX + lineWidth) < x && (objX + objWidth - lineWidth) > x && (objY - lineWidth) < y && (objY + lineWidth) > y){
					dragging = 'RESIZE_T'; // ドラッグ開始
					relY = objY - y;
				}else if((objX + objWidth - lineWidth) < x && (objX + objWidth + lineWidth) > x && (objY + lineWidth) < y && (objY + objHeight - lineWidth) > y){
					dragging = 'RESIZE_R'; // ドラッグ開始
					relX = objX - x;
				}else if((objX + lineWidth) < x && (objX + objWidth - lineWidth) > x && (objY + objHeight - lineWidth) < y && (objY + objHeight + lineWidth) > y){
					dragging = 'RESIZE_B'; // ドラッグ開始
					relY = objY - y;
				}else if((objX - lineWidth) < x && (objX + lineWidth) > x && (objY - lineWidth)  < y && (objY + lineWidth) > y){
					dragging = 'RESIZE_LT'; // ドラッグ開始
					relX = objX - x;
					relY = objY - y;
				}else if((objX + objWidth - lineWidth) < x && (objX + objWidth + lineWidth) > x && (objY - lineWidth)  < y && (objY + lineWidth) > y){
					dragging = 'RESIZE_RT'; // ドラッグ開始
					relX = objX - x;
					relY = objY - y;
				}else if((objX + objWidth - lineWidth ) < x && (objX + objWidth + lineWidth) > x && (objY + objHeight - lineWidth) < y && (objY + objHeight + lineWidth) > y){
					dragging = 'RESIZE_RB'; // ドラッグ開始
					relX = objX - x;
					relY = objY - y;
				}else if((objX - lineWidth) < x && (objX + lineWidth) > x && (objY + objHeight - lineWidth) < y && (objY + objHeight + lineWidth) > y){
					dragging = 'RESIZE_LB'; // ドラッグ開始
					relX = objX - x;
					relY = objY - y;
				}
			}
			function onMove(e) {
				if (ref == 1){
					return;
					}
				// キャンバスの左上端の座標を取得
				var offsetX = canvas.getBoundingClientRect().left;
				var offsetY = canvas.getBoundingClientRect().top;
				// マウスが移動した先の座標を取得
				x = e.clientX - offsetX;
				y = e.clientY - offsetY;
				// ドラッグが開始されていればオブジェクトの座標を更新して再描画
				if (dragging == 'MOVE') {
					//十字
					canvas.style.cursor = "move";
					objX = x + relX;
					objY = y + relY;
 					drawRect();
				}else if(dragging == 'RESIZE_L'){
					//左
					canvas.style.cursor = "e-resize";
					if(!(objWidth + (objX-x)- relX < lineWidth * 2)){
						objWidth = objWidth + (objX-x)- relX;
						objX = x + relX;
						drawRect();
					}
				}else if(dragging == 'RESIZE_T'){
					//上
					canvas.style.cursor = "n-resize";
					if(!(objHeight + (objY-y)- relY < lineWidth * 2)){
						objHeight = objHeight + (objY-y)- relY;
						objY = y + relY;
						drawRect();
					}
				}else if(dragging == 'RESIZE_R'){
					//右
					canvas.style.cursor = "w-resize";
					if(!(objWidth + (x - objX + relX) < lineWidth * 2)){
						objWidth = objWidth + (x - objX + relX);
						relX = - (x - objX);
						drawRect();
					}
				}else if(dragging == 'RESIZE_B'){
					//下
					canvas.style.cursor = "s-resize";
					if(!(objHeight + (y - objY + relY) < lineWidth * 2)){
						objHeight = objHeight + (y - objY + relY);
						relY = - (y - objY);
						drawRect();
					}
				}else if(dragging == 'RESIZE_LT'){
					//左上
					canvas.style.cursor = "nw-resize";
					if(!(objWidth + (objX-x)- relX < lineWidth * 2)
					&& !(objHeight + (objY-y)- relY < lineWidth * 2)){
						objWidth = objWidth + (objX-x)- relX;
						objX = x + relX;
						objHeight = objHeight + (objY-y)- relY;
						objY = y + relY;
						drawRect();
					}
				}else if(dragging == 'RESIZE_RT'){
					//右上
					canvas.style.cursor = "ne-resize";
					if(!(objWidth + (x - objX + relX) < lineWidth * 2)
					&& !(objHeight + (objY-y)- relY < lineWidth * 2)){
						objWidth = objWidth + (x - objX + relX);
						relX = - (x - objX);
						objHeight = objHeight + (objY-y)- relY;
						objY = y + relY;
						drawRect();
					}
				}else if(dragging == 'RESIZE_RB'){
					//右下
					canvas.style.cursor = "se-resize";
					if(!(objHeight + (y - objY + relY) < lineWidth * 2)
					&& !(objWidth + (x - objX + relX) < lineWidth * 2)){
						objHeight = objHeight + (y - objY + relY);
						relY = - (y - objY);
						objWidth = objWidth + (x - objX + relX);
						relX = - (x - objX);
						drawRect();
					}
				}else if(dragging == 'RESIZE_LB'){
					//左下
					canvas.style.cursor = "sw-resize";
					if(!(objHeight + (y - objY + relY) < lineWidth * 2)
					&& !(objWidth + (objX-x)- relX < lineWidth * 2)){
						objHeight = objHeight + (y - objY + relY);
						relY = - (y - objY);
						objWidth = objWidth + (objX-x)- relX;
						objX = x + relX;
						drawRect();
					}
				 }else{
					if(objX + lineWidth <= x && (objX + objWidth - lineWidth) >= x && objY + lineWidth <= y && (objY + objHeight - lineWidth) >= y){
						//十字
						canvas.style.cursor = "move";
					}else if((objX - lineWidth) < x && (objX + lineWidth) > x && (objY + lineWidth) < y && (objY + objHeight - lineWidth ) > y){
						//左
						canvas.style.cursor = "e-resize";
					}else if((objX + lineWidth) < x && (objX + objWidth - lineWidth) > x && (objY - lineWidth) < y && (objY + lineWidth) > y){
						//上
						canvas.style.cursor = "n-resize";
					}else if((objX + objWidth - lineWidth) < x && (objX + objWidth + lineWidth) > x && (objY + lineWidth) < y && (objY + objHeight - lineWidth) > y){
						//右
						canvas.style.cursor = "w-resize";
					}else if((objX + lineWidth) < x && (objX + objWidth - lineWidth) > x && (objY + objHeight - lineWidth) < y && (objY + objHeight + lineWidth) > y){
						//下
						canvas.style.cursor = "s-resize";
					}else if((objX - lineWidth) < x && (objX + lineWidth) > x && (objY - lineWidth)  < y && (objY + lineWidth) > y){
						//左上
						canvas.style.cursor = "nw-resize";
					}else if((objX + objWidth - lineWidth) < x && (objX + objWidth + lineWidth) > x && (objY - lineWidth)  < y && (objY + lineWidth) > y){
						//右上
						canvas.style.cursor = "ne-resize";
					}else if((objX + objWidth - lineWidth ) < x && (objX + objWidth + lineWidth) > x && (objY + objHeight - lineWidth) < y && (objY + objHeight + lineWidth) > y){
						//右下
						canvas.style.cursor = "se-resize";
					}else if((objX - lineWidth) < x && (objX + lineWidth) > x && (objY + objHeight - lineWidth) < y && (objY + objHeight + lineWidth) > y){
						//左下
						canvas.style.cursor = "sw-resize";
					}else{
						//デフォルト
						canvas.style.cursor = "default";
					}
				 }
			}
			function onUp(e) {
				dragging = 'NO'; // ドラッグ終了
			}
			canvas.addEventListener('mousedown', onDown, false);
			canvas.addEventListener('mousemove', onMove, false);
			canvas.addEventListener('mouseup', onUp, false);
			function changeImg(fileName){
				if(fileName == "img0"){
					img = img0;
				}else if(fileName == "img1"){
					img = img1;
				}else if(fileName == "img2"){
					img = img2;
				}else if(fileName == "img3"){
					img = img3;
				}
				defCur();
				drawRect();

			}
			function changeBui(check){
				checkedBui = check;
				defCur();
				drawRect();
			}
			function defCur(){
				if(checkedBui == '0'){
					objX=<%=conf.getDefaultHeadStartPointX()%>;
					objY=<%=conf.getDefaultHeadStartPointY()%>;
					objWidth=<%=conf.getDefaultHeadEndPointX().subtract(conf.getDefaultHeadStartPointX())%>;
					objHeight=<%=conf.getDefaultHeadEndPointY().subtract(conf.getDefaultHeadStartPointY())%>;
				}else if(checkedBui == '1'){
					objX=<%=conf.getDefaultNeckStartPointX()%>;
					objY=<%=conf.getDefaultNeckStartPointY()%>;
					objWidth=<%=conf.getDefaultNeckEndPointX().subtract(conf.getDefaultNeckStartPointX())%>;
					objHeight=<%=conf.getDefaultNeckEndPointY().subtract(conf.getDefaultNeckStartPointY())%>;
				}else if(checkedBui == '2'){
					objX=<%=conf.getDefaultChestStartPointX()%>;
					objY=<%=conf.getDefaultChestStartPointY()%>;
					objWidth=<%=conf.getDefaultChestEndPointX().subtract(conf.getDefaultChestStartPointX())%>;
					objHeight=<%=conf.getDefaultChestEndPointY().subtract(conf.getDefaultChestStartPointY())%>;
				}else if(checkedBui == '3'){
					objX=<%=conf.getDefaultAbdomenStartPointX()%>;
					objY=<%=conf.getDefaultAbdomenStartPointY()%>;
					objWidth=<%=conf.getDefaultAbdomenEndPointX().subtract(conf.getDefaultAbdomenStartPointX())%>;
					objHeight=<%=conf.getDefaultAbdomenEndPointY().subtract(conf.getDefaultAbdomenStartPointY())%>;
				}else if(checkedBui == '4'){
					objX=<%=conf.getDefaultPelvisStartPointX()%>;
					objY=<%=conf.getDefaultPelvisStartPointY()%>;
					objWidth=<%=conf.getDefaultPelvisEndPointX().subtract(conf.getDefaultPelvisStartPointX())%>;
					objHeight=<%=conf.getDefaultPelvisEndPointY().subtract(conf.getDefaultPelvisStartPointY())%>;
				}else if(checkedBui == '5'){
					objX=<%=conf.getDefaultLimbStartPointX()%>;
					objY=<%=conf.getDefaultLimbStartPointY()%>;
					objWidth=<%=conf.getDefaultLimbEndPointX().subtract(conf.getDefaultLimbStartPointX())%>;
					objHeight=<%=conf.getDefaultLimbEndPointY().subtract(conf.getDefaultLimbStartPointY())%>;
				}else if(checkedBui == '6'){
					objX=<%=conf.getDefaultEtcStartPointX()%>;
					objY=<%=conf.getDefaultEtcStartPointY()%>;
					objWidth=<%=conf.getDefaultEtcEndPointX().subtract(conf.getDefaultEtcStartPointX())%>;
					objHeight=<%=conf.getDefaultEtcEndPointY().subtract(conf.getDefaultEtcStartPointY())%>;
				}

			}
			function check(){
				var schemaComment = document.getElementById("schemaComment");
				var startPointX = document.getElementById("startPointX");
				var startPointY = document.getElementById("startPointY");
				var endPointX = document.getElementById("endPointX");
				var endPointY = document.getElementById("endPointY");
				//コメントの必須チェック
				if(getByteLength(schemaComment.value) > 2000){
					alert("コメントは2000バイト以下で入力ください。現在：" + getByteLength(schemaComment.value)+"バイト" );
					return false;
				}
				startPointX.value = objX;
				startPointY.value = objY;
				endPointX.value = objX + objWidth;
				endPointY.value = objY + objHeight;
				return true;
			}
			function getByteLength(str){
			  str = (str==null)?"":str;
			  return encodeURI(str).replace(/%../g, "*").length;
			}
		</script>
	</body>
</html>