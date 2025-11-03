package com.yokogawa.theraris.rtWeb.app.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yokogawa.theraris.logger.AppLogger;
import com.yokogawa.theraris.rtWeb.app.bean.PhotoSchemaInfoBean;
import com.yokogawa.theraris.rtWeb.app.bean.SchemaConfBean;
import com.yokogawa.theraris.rtWeb.app.bean.SchemaInfoBean;
import com.yokogawa.theraris.rtWeb.app.common.constants.MyConsts;
import com.yokogawa.theraris.rtWeb.app.common.constants.Parameters;
import com.yokogawa.theraris.rtWeb.app.dbaccess.rtris.PhotoSchemaInfoAccess;
import com.yokogawa.theraris.rtWeb.core.Configuration;
import com.yokogawa.theraris.rtWeb.core.DbConnectionFactory;

public class RtWebServiceLogic {
	public void search(HttpServletRequest request,HttpServletResponse res){
		SchemaInfoBean info = new SchemaInfoBean();
		SchemaConfBean conf = new SchemaConfBean();
		// UID
		String parmUid = "";
		// 選択部位コード
		String parmBuiCode = "";
		// 患者Id
		String parmKanjaId = "";
		// 位置コード
		String parmHandPos = "";
		// 参照フラグ
		int parmRefFlag = 0;
		// 呼び出し元画面からの遷移
		if (request.getParameter(Parameters.PARM_UID) != null) {
			parmUid = request.getParameter(Parameters.PARM_UID).trim();
			AppLogger.getInstance().log(AppLogger.FINE,Parameters.PARM_UID + " = " + parmUid + "'");
		}
		// 呼び出し元画面からの遷移
		if (request.getParameter(Parameters.PARM_BUI) != null) {
			parmBuiCode = request.getParameter(Parameters.PARM_BUI).trim();
			AppLogger.getInstance().log(AppLogger.FINE,Parameters.PARM_BUI + " = " + parmBuiCode + "'");
		}
		// 呼び出し元画面からの遷移
		if (request.getParameter(Parameters.PARM_KANJA_ID) != null) {
			parmKanjaId = request.getParameter(Parameters.PARM_KANJA_ID).trim();
			AppLogger.getInstance().log(AppLogger.FINE,Parameters.PARM_KANJA_ID + " = " + parmKanjaId + "'");
		}
		// 呼び出し元画面からの遷移
		if (request.getParameter(Parameters.PARM_HANDPOS) != null) {
			parmHandPos = request.getParameter(Parameters.PARM_HANDPOS).trim();
			AppLogger.getInstance().log(AppLogger.FINE,Parameters.PARM_HANDPOS + " = " + parmHandPos + "'");
		}
		// 呼び出し元画面からの遷移
		if (request.getParameter(Parameters.PARM_REF_FLAG) != null) {
			parmRefFlag = Integer.parseInt(request.getParameter(Parameters.PARM_REF_FLAG).trim());
			AppLogger.getInstance().log(AppLogger.FINE,Parameters.PARM_REF_FLAG + " = " + parmRefFlag + "'");
		}
		conf = this.getConf();
		info = this.getInfo(parmUid,parmBuiCode,parmKanjaId,parmHandPos,parmRefFlag);
		request.setAttribute("info", info);
		request.setAttribute("conf", conf);
	}

	public void reload(HttpServletRequest request,HttpServletResponse res){
		SchemaInfoBean info = new SchemaInfoBean();
		SchemaConfBean conf = new SchemaConfBean();
		// UID
		String uid = "";
		// 選択部位コード
		String buiCode = "";
		// 患者Id
		String kanjaId = "";
		// 位置コード
		String handPos = "";
		// 参照フラグ
		int parmRefFlag = 0;
		if( request.getParameter("photoSchemaUid") !=null){
			uid =request.getParameter("photoSchemaUid");
		}
		if( request.getParameter("kanjaID") !=null){
			kanjaId = request.getParameter("kanjaID");
		}
		conf = this.getConf();
		info = this.getInfo(uid,buiCode,kanjaId,handPos,parmRefFlag);
		request.setAttribute("info", info);
		request.setAttribute("conf", conf);
	}
	private SchemaConfBean getConf(){
		SchemaConfBean conf = new SchemaConfBean();
		conf.setDefaultHeadStartPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_HEAD).getStartX());
		conf.setDefaultHeadStartPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_HEAD).getStartY());
		conf.setDefaultHeadEndPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_HEAD).getEndX());
		conf.setDefaultHeadEndPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_HEAD).getEndY());
		conf.setDefaultNeckStartPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_NECK).getStartX());
		conf.setDefaultNeckStartPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_NECK).getStartY());
		conf.setDefaultNeckEndPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_NECK).getEndX());
		conf.setDefaultNeckEndPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_NECK).getEndY());
		conf.setDefaultChestStartPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_CHEST).getStartX());
		conf.setDefaultChestStartPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_CHEST).getStartY());
		conf.setDefaultChestEndPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_CHEST).getEndX());
		conf.setDefaultChestEndPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_CHEST).getEndY());
		conf.setDefaultAbdomenStartPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_ABDOMEN).getStartX());
		conf.setDefaultAbdomenStartPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_ABDOMEN).getStartY());
		conf.setDefaultAbdomenEndPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_ABDOMEN).getEndX());
		conf.setDefaultAbdomenEndPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_ABDOMEN).getEndY());
		conf.setDefaultPelvisStartPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUIPOS_PELYIS).getStartX());
		conf.setDefaultPelvisStartPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUIPOS_PELYIS).getStartY());
		conf.setDefaultPelvisEndPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUIPOS_PELYIS).getEndX());
		conf.setDefaultPelvisEndPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUIPOS_PELYIS).getEndY());
		conf.setDefaultLimbStartPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_LIMB).getStartX());
		conf.setDefaultLimbStartPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_LIMB).getStartY());
		conf.setDefaultLimbEndPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_LIMB).getEndX());
		conf.setDefaultLimbEndPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_LIMB).getEndY());
		conf.setDefaultEtcStartPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_ETC).getStartX());
		conf.setDefaultEtcStartPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_ETC).getStartY());
		conf.setDefaultEtcEndPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_ETC).getEndX());
		conf.setDefaultEtcEndPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(MyConsts.BUI_POS_ETC).getEndY());

		return conf;
	}
	private SchemaInfoBean getInfo(String parmUid,String parmBuiCode,String parmKanjaId,String parmHandPos,int paramRef){
		SchemaInfoBean info = new SchemaInfoBean();
		Connection con = DbConnectionFactory.getInstance().getRTRISDBConnection();
		try{
			//シェーマIDの取得
			info.setPhoteSchemaUid(parmUid);
			//患者IDの取得
			info.setKanjaID(parmKanjaId);
			//データの取得
			PhotoSchemaInfoAccess photoSchemaInfoAccess= new PhotoSchemaInfoAccess();
			PhotoSchemaInfoBean photeSchemaBean = photoSchemaInfoAccess.select(con,info.getPhoteSchemaUid());
			if(photeSchemaBean != null && photeSchemaBean.getPhoteSchemaUid() != null
					&&  !(MyConsts.STRING_EMPTY.equals(photeSchemaBean.getPhoteSchemaUid()))){
				//データがあった場合
				//各データを設定する。
				info.setPhoteSchemaUid(photeSchemaBean.getPhoteSchemaUid());
				info.setHandPosition(photeSchemaBean.getHandPosition());
				info.setKanjaID(photeSchemaBean.getKanjaID());
				info.setRisId(photeSchemaBean.getRisId());
				info.setSchemaBui(photeSchemaBean.getSchemaBui());
				info.setSchemaComment(photeSchemaBean.getSchemaComment());
				info.setSchemaImageName(photeSchemaBean.getSchemaImageName());
				info.setStartPointX(photeSchemaBean.getStartPointX());
				info.setStartPointY(photeSchemaBean.getStartPointY());
				info.setEndPointX(photeSchemaBean.getEndPointX());
				info.setEndPointY(photeSchemaBean.getEndPointY());
				info.setEntryDate(photeSchemaBean.getEntryDate());
				info.setUpdDate(photeSchemaBean.getUpdDate());
			}else{
				//データがなかった場合
				//コンフィグデータを設定する。
				//手の位置
				if(parmHandPos != null && !(MyConsts.STRING_EMPTY.equals(parmHandPos))){
					info.setHandPosition(this.getConfig(MyConsts.HAND_POS_MAP, parmHandPos));
				}else{
					info.setHandPosition(this.getConfig(MyConsts.DEFAULT_MAP, MyConsts.DEFAULT_MAP_HAND_POS));
				}
				//部位
				if(parmHandPos != null && !(MyConsts.STRING_EMPTY.equals(parmBuiCode))){
					info.setSchemaBui(this.getConfig(MyConsts.BUI_MAP, parmBuiCode));
				}else{
					info.setSchemaBui(this.getConfig(MyConsts.DEFAULT_MAP, MyConsts.DEFAULT_MAP_BUI));
				}
				info.setStartPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(info.getSchemaBui()).getStartX());
				info.setStartPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(info.getSchemaBui()).getStartY());
				info.setEndPointX(Configuration.getInstance().getDefaultPosConfBeanMap().get(info.getSchemaBui()).getEndX());
				info.setEndPointY(Configuration.getInstance().getDefaultPosConfBeanMap().get(info.getSchemaBui()).getEndY());
			}
			//デフォルト値の設定

			info.setRef(paramRef);
		}catch(Exception e){
			AppLogger.getInstance().fatal("", e);
		}finally{
			//クローズ処理
			DbConnectionFactory.getInstance().returnRTRISDBConnection(con);
		}
		return info;
	}
	private String getConfig(String paramName,String paramKey){
		String returnString = null;
		if(MyConsts.HAND_POS_MAP.equals(paramName)){
			returnString = Configuration.getInstance().getHandPosMap().get(paramKey).getId();
		}else if(MyConsts.BUI_MAP.equals(paramName)){
			returnString = Configuration.getInstance().getBuiMap().get(paramKey).getId();
		}else if(MyConsts.DEFAULT_MAP.equals(paramName)){
			returnString = Configuration.getInstance().getDefaultMap().get(paramKey).getName();
		}
		return returnString;
	}
	public void check(HttpServletRequest request,HttpServletResponse res){
		PhotoSchemaInfoBean info = new PhotoSchemaInfoBean();
		if( request.getParameter("photoSchemaUid") !=null){
			info.setPhoteSchemaUid(request.getParameter("photoSchemaUid"));
		}
		if( request.getParameter("handPosition") !=null){
			info.setHandPosition(request.getParameter("handPosition"));
		}
		if( request.getParameter("kanjaID") !=null){
			info.setKanjaID(request.getParameter("kanjaID"));
		}
		if( request.getParameter("risId") !=null){
			info.setRisId(request.getParameter("risId"));
		}
		if( request.getParameter("schemaBui") !=null){
			info.setSchemaBui(request.getParameter("schemaBui"));
		}
		if( request.getParameter("schemaComment") !=null){
			try{
				info.setSchemaComment(new String(request.getParameter("schemaComment").getBytes("ISO-8859-1"), "UTF-8"));
			}catch(Exception e){
			}
		}
		if( request.getParameter("schemaImageName") !=null){
			info.setSchemaImageName(request.getParameter("schemaImageName"));
		}
		if( request.getParameter("startPointX")!=null){
			info.setStartPointX(new BigDecimal(request.getParameter("startPointX")));
		}
		if( request.getParameter("startPointY") !=null){
			info.setStartPointY(new BigDecimal(request.getParameter("startPointY")));
		}
		if( request.getParameter("endPointX") !=null){
			info.setEndPointX(new BigDecimal(request.getParameter("endPointX")));
		}
		if( request.getParameter("endPointY")!=null){
			info.setEndPointY(new BigDecimal(request.getParameter("endPointY")));
		}
		//コネクションの作成
		Connection con = DbConnectionFactory.getInstance().getRTRISDBConnection();
		try{

			//データの取得
			PhotoSchemaInfoAccess photoSchemaInfoAccess= new PhotoSchemaInfoAccess();
			PhotoSchemaInfoBean photeSchemaBean = photoSchemaInfoAccess.select(con,info.getPhoteSchemaUid());
			//ファイル名の取得
			info.setSchemaImageName(Configuration.getInstance().getSchemaImgNameMap().get(info.getHandPosition()).getName());
			if(photeSchemaBean != null && photeSchemaBean.getPhoteSchemaUid() != null
					&&  !(MyConsts.STRING_EMPTY.equals(photeSchemaBean.getPhoteSchemaUid()))){
				photoSchemaInfoAccess.update(con,info);
			}else{
				photoSchemaInfoAccess.insert(con,info);
			}
			//全てが正常の場合はコミット
			con.commit();
			//rrisCon.commit();
		}catch(Exception e){
			AppLogger.getInstance().fatal("", e);
			try {
				if(con != null){
					con.rollback();
				}
			} catch (SQLException e2) {
				AppLogger.getInstance().fatal("", e2);
			}
		}finally{
			//クローズ処理
			DbConnectionFactory.getInstance().returnRTRISDBConnection(con);
		}

	}
	public void getRest(HttpServletRequest request,HttpServletResponse res){
		// Key（位置コード）
		String parmKey= "";
		// 呼び出し元画面からの遷移
		if (request.getParameter("key") != null) {
			parmKey = request.getParameter("key").trim();
			AppLogger.getInstance().log(AppLogger.FINE,"key = " + parmKey + "'");
		}
		//指定されたファイルを取得。
		String videoListPath = MyConsts.STRING_EMPTY;
		videoListPath = Configuration.getInstance().getSchemaImgPath();
		String fileName =  Configuration.getInstance().getSchemaImgNameMap().get(parmKey).getName();
		//ファイルのバイナリを取得する。
		File videoFile = new File(videoListPath + "/" + fileName) ;
		OutputStream os = null;
		try{
			//pwオブジェクト
			res.setContentType(MyConsts.IMG_HEADER);
			// 取得したIDに紐づく画像データを取得する
			BufferedImage img = ImageIO.read(videoFile);
			os = res.getOutputStream();
			ImageIO.write(img, MyConsts.IMG_EXTENTION, os);
			os.flush();
		}catch(Exception e){
				AppLogger.getInstance().fatal("", e);
		}finally{
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
						AppLogger.getInstance().fatal("", e);
				}
			}
		}
	}
}
