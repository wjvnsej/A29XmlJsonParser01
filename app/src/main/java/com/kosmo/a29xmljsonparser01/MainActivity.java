package com.kosmo.a29xmljsonparser01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "KOSMO61";
    //버튼선언
    Button btnJson1, btnJson2, btnXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버튼 위젯 가져오기
        btnJson1 = (Button)findViewById(R.id.btn_json1);
        btnJson2 = (Button)findViewById(R.id.btn_json2);
        btnXml = (Button)findViewById(R.id.btn_xml);

        //JSON파싱1
        btnJson1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJsonData1();
            }
        });

        //JSON파싱2
        btnJson2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJsonData2();
            }
        });

        //XML파싱
        btnXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getXmlData();
            }
        });

    }//// onCreate End

    //JSON파싱1
    private void getJsonData1(){
        //안드로이드에서 JSON을 파싱할 때는 반드시 예외처리를 해야한다.

        /*
        Parsing 의 대상은 JSON 문자열은 전체는 객체, number 키값의 value는 배열로 구성됨.
         */
        String jsonStr = "{'number' : [1,2,3,4,5]}";
        try {
            //JSONObject : JSON 객체를 파싱할 때 사용하는 클래스
            JSONObject jsonObject = new JSONObject(jsonStr);

            /*
            JSONArray : JSON 배열을 파싱할 때 사용하는 클래스
                number 키값의 value 가 배열이므로 getJSONArray() 메소드 사용.
            */
            JSONArray jsonArray = jsonObject.getJSONArray("number");

            for(int i = 0; i < jsonArray.length(); i++) {
                /*
                getInt(인덱스) : JSON 배열에서 정수를 가져올 때 사용
                 */
                int tempNum = jsonArray.getInt(i);
                Log.i("KOSMO61", "JSON1 파싱데이터 : " + tempNum);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }//// getJsonData1 End

    //JSON파싱2
    private void getJsonData2() {

        String jsonStr = "{'color':{'top':'red','right':'blue','bottom':'green','left':'black'}}";
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            //color 키 값의 value도 객체이므로 getJSONObject() 메소드 사용
            JSONObject color = jsonObject.getJSONObject("color");

            //getString(키값) : 키 값에 해당하는 밸류를 가져옴.
            String top = color.getString("top");
            String right = color.getString("right");
            String bottom = color.getString("bottom");
            String left = color.getString("left");

            String jsonPrint =
                    String.format("top : %s, right : %s, bottom : %s, left : %s", top, right, bottom, left);
            Log.i(TAG, "JSON2 파싱데이터 : " + jsonPrint);

            //has(키값) : JSON객체 안에 해당 키값이 있는지 확인
            if(color.has("left")){
                Log.i(TAG, "key:left 있음");
            }
            else {
                Log.i(TAG, "key:left 없음");
            }

            if(color.has("css")){
                Log.i(TAG, "key:css 있음");
            }
            else {
                Log.i(TAG, "key:css 없음");
            }
        }
        catch (Exception e) { }
    }//// getJsonData2 End

    //XML파싱
    /*
    XmlPullParser 클래스를 이용한 XML 파싱
    사용법 :
        next() : XML 탐색을 위한 메소드로 XML 요소를 식별하면 탐색을
            중지하고 식별된 요소를 리턴한다.
    상수설명
        START_DOCUMENT : XML 문서의 시작으로 파싱의 시작을 알림
        START_TAG : XML 시작태그를 의미함
        TEXT : XML 의 시작태그와 종료태그 사이의 텍스트를 의미. 즉 데이터.
        END_TAG 종료태그를 의미함.
        END_DOCUMENT : XML 문서의 끝을 알림.

    태그는 xxx.getName()을 통해 파싱하고, 데이터는 xxx.getText()를 통해 파싱한다.
     */
    private void getXmlData() {

        try {
            //파싱한 값을 저장하기 위한 컬렉션과 파싱처리에 사용 할 변수 생성
            ArrayList<String> xNumber = new ArrayList<String>();
            ArrayList<String> xActor = new ArrayList<String>();
            ArrayList<String> xWord = new ArrayList<String>();

            //XML 처리에 사용 할 변수 선언
            int event = 0;
            String currentTag = null;

            /*
            Arrays.asList()
                : 인자로 주어진 배열을 List 컬렉션으로 변환해주는 메소드.
                단, 이렇게 변환된 컬렉션에는 원소를 새롭게 추가 할 수는 없으나
                컬렉션에서 제공되는 모든 메소드를 사용 할 수 있는 장점이 있다.
             */
            List<String> tagList = Arrays.asList(new String[]{"number","actor","word"});

            //XML parser 선언 : 리소스 폴더의 word.xml 파일을 가져온다.
            XmlPullParser parser = getResources().getXml(R.xml.word);

            //XML 문서의 노드를 하나씩 읽으면서 문서의 끝까지 반복한다.
            while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {

                switch (event) {
                    //탐색된 요소가 시작태그이면..
                    case XmlPullParser.START_TAG:
                        //시작태그를 변수에 저장한다.
                        currentTag = parser.getName();
                        break;
                    //해당요소가 텍스트(데이터)이면..
                    case XmlPullParser.TEXT:
                        if(currentTag != null && tagList.contains(currentTag)) {

                            //데이터를 가져와서 변수에 저장한다.
                            String value = parser.getText();
                            Log.i(TAG, "value = " + value);

                            //각 태그명에 해당하는 값을 컬렉션에 저장한다.
                            if(currentTag.equals("number")) { //일련번호
                                xNumber.add(value);
                            }
                            else if(currentTag.equals("actor")) { //배우이름
                                xActor.add(value);
                            }
                            else if(currentTag.equals("word")) { //명대사
                                xWord.add(value);
                            }
                        }
                        break;
                    //현재 탐색된 요소가 종료태그이면..
                    case XmlPullParser.END_TAG:
                        //null 값으로 초기화.
                        currentTag = null;
                        break;
                    default:
                        break;

                }//// switch문
            }//// while문

            //컬렉션에 저장된 모든 내용을 로그로 출력한다.
            for(int i = 0; i < xNumber.size(); i++) {
                Log.i(TAG, "number = " + xNumber.get(i));
                Log.i(TAG, "actor = " + xActor.get(i));
                Log.i(TAG, "word = " + xWord.get(i));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}




























