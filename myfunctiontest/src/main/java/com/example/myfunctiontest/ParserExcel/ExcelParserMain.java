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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private Map<String,_4sProvince> map_4sProvinces;
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
                        //readExcel(filename);
                        readnewExcel(filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    //新解析4sinfo.xls  excel表  开始
    public void readnewExcel(String FileName) throws Exception {//weichai4S.xls
        //获取AssetManager对象
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
        map_4sProvinces = new HashMap<>();
        for (int everyRow = 0;everyRow < rowCount;everyRow++){
            String pName = sheet.getCell(0,everyRow).getContents();
            String cName = sheet.getCell(1,everyRow).getContents();
            _4sProvince newProvince = new _4sProvince();
            if (!pMap.containsKey(pName)){//不存在该省
                pMap.put(pName,pName);
                cMap.put(cName,cName);
                List<_4sCity> _4scitylist = new ArrayList<>();
                _4sCity m_4sCity = new _4sCity();
                List<_4sInfo> _4sinfolist = new ArrayList<>();
                _4sInfo m_4sInfo = new _4sInfo();
                newProvince.setPname(pName);
                m_4sCity.setCityname(cName);
                m_4sInfo.set_4sname(sheet.getCell(3,everyRow).getContents());
                m_4sInfo.set_4saddress(sheet.getCell(4,everyRow).getContents());
                m_4sInfo.set_4slatitude(sheet.getCell(13,everyRow).getContents());
                m_4sInfo.set_4slongitude(sheet.getCell(12,everyRow).getContents());
                _4sinfolist.add(m_4sInfo);
                m_4sCity.set_4sinfolist(_4sinfolist);
                _4scitylist.add(m_4sCity);
                newProvince.set_4scitylist(_4scitylist);
            }else if (!cMap.containsKey(cName)){//存在该省,不存在该市
                cMap.put(cName,cName);
                _4sProvince m_province = map_4sProvinces.get(pName);
                List<_4sCity> _4scitylist = map_4sProvinces.get(pName).get_4scitylist();
                _4sCity m_4sCity = new _4sCity();
                List<_4sInfo> _4sinfolist = new ArrayList<>();
                _4sInfo m_4sInfo = new _4sInfo();
                m_4sCity.setCityname(cName);
                m_4sInfo.set_4sname(sheet.getCell(3,everyRow).getContents());
                m_4sInfo.set_4saddress(sheet.getCell(4,everyRow).getContents());
                m_4sInfo.set_4slatitude(sheet.getCell(13,everyRow).getContents());
                m_4sInfo.set_4slongitude(sheet.getCell(12,everyRow).getContents());
                _4sinfolist.add(m_4sInfo);
                m_4sCity.set_4sinfolist(_4sinfolist);
                _4scitylist.add(m_4sCity);
                m_province.set_4scitylist(_4scitylist);
                newProvince = m_province;
            }else{//存在该省,存在该市
                _4sProvince m_province = map_4sProvinces.get(pName);
                List<_4sCity> e_citylist = map_4sProvinces.get(pName).get_4scitylist();
                _4sCity m_4sCity = new _4sCity();
                List<_4sInfo> m_4sinfolist = new ArrayList<>();
                for (int i = 0;i<e_citylist.size();i++){
                    if (e_citylist.get(i).getCityname().contains(cName)){
                        m_4sinfolist = e_citylist.get(i).get_4sinfolist();
                        e_citylist.remove(i);
                    }
                }
                _4sInfo m_info = new _4sInfo();
                m_info.set_4sname(sheet.getCell(3,everyRow).getContents());
                m_info.set_4saddress(sheet.getCell(4,everyRow).getContents());
                m_info.set_4slongitude(sheet.getCell(12,everyRow).getContents());
                m_info.set_4slatitude(sheet.getCell(13,everyRow).getContents());
                m_4sinfolist.add(m_info);
                m_4sCity.setCityname(cName);
                m_4sCity.set_4sinfolist(m_4sinfolist);
                e_citylist.add(m_4sCity);
                m_province.set_4scitylist(e_citylist);
                newProvince = m_province;
            }
            //将数据添加到集合
            //TODO add provinces
            map_4sProvinces.put(pName,newProvince);
        }
Log.i("province---",map_4sProvinces.size()+"");
        Set set = map_4sProvinces.entrySet();
        Iterator i =set.iterator();
        while (i.hasNext()){
            Map.Entry entry = (Map.Entry) i.next();
            List<_4sCity> yjslist = map_4sProvinces.get(entry.getKey()).get_4scitylist();
            Log.i("provinces---",yjslist+"");
        }
       /* for (int j = 0;j<map_4sProvinces.size();j++){
            Log.i("provinces---",map_4sProvinces.);
          //  List<_4sCity> yjslist = map_4sProvinces.get(j).get_4scitylist();
           // Log.i("provinces---",yjslist+"");
        }*/
        Log.i("province----",map_4sProvinces+"");
    }


    //新解析4sinfo.xls  excel表  结束


}
