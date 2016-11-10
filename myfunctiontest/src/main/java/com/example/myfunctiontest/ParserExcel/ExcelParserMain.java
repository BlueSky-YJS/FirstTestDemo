package com.example.myfunctiontest.ParserExcel;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myfunctiontest.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * author: Administrator
 * created on: 2016/11/10 11:20
 * description:
 */

public class ExcelParserMain extends AppCompatActivity {
    private Button btn;
    //private Map<String, List<_4sInfo>> map4s;
    private List<Map<String, List<_4sInfo>>> map4scity;
    private Map<String, List<Map<String, List<_4sInfo>>>> map4sprovince;
    private Button parseStartBtn;
    private Map<String,String> pMap;
    private Map<String,String> cMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parseexcle_layout);
        pMap = new HashMap<String, String>();
        cMap = new HashMap<String, String>();
       // map4s = new HashMap<String, List<_4sInfo>>();
        map4sprovince = new HashMap<String, List<Map<String,List<_4sInfo>>>>();
        parseStartBtn = (Button) findViewById(R.id.startParse);
        parseStartBtn.setOnClickListener(new myParseClickListener());
    }


    class myParseClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            final String filename = "assets/4SInfo.xls";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        readExcel(filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    //excel开始
    public void readExcel(String FileName) throws Exception{//weichai4S.xls
        //获取AssetManager对象
        Map<String, List<String>> citymap = new HashMap<String, List<String>>();
       // AssetManager assetManager = this.getAssets();
        //打开Excel文件，返回输入流对象
       // InputStream inputStream = assetManager.open(FileName);
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FileName);
        Workbook workbook = null;
        workbook = Workbook.getWorkbook(inputStream);
        //得到第一张表
        Sheet sheet = workbook.getSheet(0);
        //列数
        int columnCount = sheet.getColumns();
        //行数
        int rowCount = sheet.getRows();
        //单元格
        Cell cell = null;
        Cell cellp = null;
        List<String> list = new ArrayList<String>();
        int p=0;
        for (int everyRow = 0;everyRow < rowCount;everyRow++) {
            for(int everyColumn = 0;everyColumn < columnCount;everyColumn++){
                cell = sheet.getCell(0, everyRow);
                cellp = sheet.getCell(1, everyRow);
                String pName = sheet.getCell(0,everyRow).getContents();
                if(!pMap.containsKey(pName)){//不存在该省
                    pMap.put(pName,"true");
                    Map<String, List<_4sInfo>> map4s = new HashMap<String, List<_4sInfo>>();
                    String cName = sheet.getCell(1,everyRow).getContents();
                    if(!cMap.containsKey(cName)){//不存在该市
                        cMap.put(cName,"true");
                        List<_4sInfo> _4slist = new ArrayList<>();
                        _4sInfo _4s = new _4sInfo();
                        _4s.set_4sname(sheet.getCell(3,everyRow).getContents());
                        _4s.set_4saddress(sheet.getCell(4,everyRow).getContents());
                        _4s.set_4slongitude(sheet.getCell(12,everyRow).getContents());
                        _4s.set_4slatitude(sheet.getCell(13,everyRow).getContents());
                        _4slist.add(_4s);
                        map4s.put(cName,_4slist);
                    }
                    List<Map<String,List<_4sInfo>>> p_4slist = new ArrayList<>();
                    p_4slist.add(map4s);
                    map4sprovince.put(pName,p_4slist);
                }else {//存在该省份
                    String cName = sheet.getCell(1,everyRow).getContents();
                    Map<String, List<_4sInfo>> c_map4s = new HashMap<>();
                    if(!cMap.containsKey(cName)){//不存在该市
                        cMap.put(cName,"true");
                        Map<String, List<_4sInfo>> map4s = new HashMap<String, List<_4sInfo>>();
                        List<_4sInfo> _4slist = new ArrayList<>();
                        _4sInfo _4s = new _4sInfo();
                        _4s.set_4sname(sheet.getCell(3,everyRow).getContents());
                        _4s.set_4saddress(sheet.getCell(4,everyRow).getContents());
                        _4s.set_4slongitude(sheet.getCell(12,everyRow).getContents());
                        _4s.set_4slatitude(sheet.getCell(13,everyRow).getContents());
                        _4slist.add(_4s);
                        map4s.put(cName,_4slist);
                    }else {//存在该城市
                        List<Map<String,List<_4sInfo>>> mList = map4sprovince.get(pName);
                        c_map4s.clear();
                        for (int i=0;i<mList.size();i++){
                            if (mList.get(i).containsKey(cName)){
                                c_map4s = mList.get(i);
                            }
                        }
                        _4sInfo c_4s = new _4sInfo();
                        c_4s.set_4sname(sheet.getCell(3,everyRow).getContents());
                        c_4s.set_4saddress(sheet.getCell(4,everyRow).getContents());
                        c_4s.set_4slongitude(sheet.getCell(12,everyRow).getContents());
                        c_4s.set_4slatitude(sheet.getCell(13,everyRow).getContents());
                        List<_4sInfo> c_list = c_map4s.get(cName);
                        c_list.add(c_4s);
                        c_map4s.put(cName,c_list);
                    }
                    List<Map<String,List<_4sInfo>>> p_4slist = new ArrayList<>();
                    p_4slist.add(c_map4s);
                    map4sprovince.put(pName,p_4slist);
                }
                String contents =  cell.getContents();
                Log.i("string--", cell.getContents());
            }
        }
        Log.i("success--", "chuanjianwancheng"+map4sprovince);
			    /*for (int everyRow = 0;everyRow < rowCount;everyRow++) {
			    	cell = sheet.getCell(0, everyRow);
			    	String pname = sheet.getCell(0, p).getContents();
			    	String pname2 = sheet.getCell(0, everyRow).getContents();
			    	String cname = sheet.getCell(1, p).getContents();
			    	String cname2 = sheet.getCell(1, everyRow).getContents();
			    	if(pname.equalsIgnoreCase(pname2)){

				    		cellp = sheet.getCell(1, everyRow);
				    		if(!cname.equalsIgnoreCase(cname2)){
				    			list.add(cname);
				    		}
				    		String ceshi = cname;
			    			Log.i("ceshi---",cname );

			    	}
			    	if(!pname.equalsIgnoreCase(pname2)&&!cname.equalsIgnoreCase(cname2)){
			    		p = everyRow;
			    	}

			    	citymap.put(pname, list);
			    }*/

    }
    //excel结束


}
