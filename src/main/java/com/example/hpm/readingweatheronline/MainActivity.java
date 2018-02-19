package com.example.hpm.readingweatheronline;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText,textView;
    Button button;
    String myUrl;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(EditText) findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals(""))
                editText.setError("Please enter a valid city");
                myUrl="http://api.openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&mode=xml&appid=eab9d035425eb97d3c69bf7a0edfc4c9";
                new Download().execute();
            }
        });

    }
    class Download extends AsyncTask<Void,Void,Void>{
        InputStream stream;
        String id,name,lon,lat,country, sunRise, sunSet,tempVal,tempMin,tempMax,
                humValue,humUnit,preValue,preUnit,windSpeed,windName,dirValue,
                dirCode,dirName,cloudVal,cloudName,visibilityVal,precipitation,
                weatherNum,weatherVal,weatherIcon,lastUpdateVal,riseDate,riseTime,setDate,setTime;


        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url=new URL(myUrl);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
               stream=connection.getInputStream();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(MainActivity.this);
            dialog.setTitle("Please wait");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            XmlPullParser parser= Xml.newPullParser();
            try {
                parser.setInput(stream,"utf-8");
                int event=parser.getEventType();
                while(event!=XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        String tag = parser.getName();
                        switch (tag) {
                            case "city":
                                id = parser.getAttributeValue(0);
                                name = parser.getAttributeValue(1);

                                break;
                            case "coord":
                                lon = parser.getAttributeValue(0);
                                lat = parser.getAttributeValue(1);
                                break;
                            case "country":
                                country = parser.nextText();
                                break;
                            case "sun":
                                sunRise =parser.getAttributeValue(0);
                                String s[]=sunRise.split("T");
                                riseDate=s[0];
                                riseTime=s[1];
                                sunSet =parser.getAttributeValue(1);
                                String s2[]=sunSet.split("T");
                                setDate=s2[0];
                                setTime=s2[1];

                                break;
                            case "temperature":
                                double temp= Double.parseDouble((parser.getAttributeValue(0)));
                                double temp2=temp-273.15;
                                tempVal= String.valueOf(temp2);
                                double temp3= Double.parseDouble((parser.getAttributeValue(1)));
                                double temp4=temp3-273.15;
                                tempMin=String.valueOf(temp4);
                                double temp5= Double.parseDouble((parser.getAttributeValue(2)));
                                double temp6=temp5-273.15;
                                tempMax=String.valueOf(temp6);
                                break;
                            case "humidity":
                                humValue=parser.getAttributeValue(0);
                                humUnit=parser.getAttributeValue(1);
                                break;

                            case "pressure":
                                preValue=parser.getAttributeValue(0);
                                preUnit=parser.getAttributeValue(1);
                                break;
                            case "speed":
                                windSpeed=parser.getAttributeValue(0);
                                windName=parser.getAttributeValue(1);
                                break;
                            case "direction":
                                dirValue=parser.getAttributeValue(0);
                                dirCode=parser.getAttributeValue(1);
                                dirName=parser.getAttributeValue(2);
                                break;
                            case "clouds":
                                cloudVal=parser.getAttributeValue(0);
                                cloudName=parser.getAttributeValue(1);
                                break;
                            case "visibility":
                                visibilityVal=parser.getAttributeValue(0);
                                break;
                            case "precipitation":
                                precipitation=parser.getAttributeValue(0);
                                break;
                            case "weather":
                                weatherNum=parser.getAttributeValue(0);
                                weatherVal=parser.getAttributeValue(1);
                                weatherIcon=parser.getAttributeValue(2);
                                break;
                            case "lastupdate":
                                lastUpdateVal=parser.getAttributeValue(0);
                                textView.setText(" City : " + name + "\n longitude :" + lon + " latitude : " + lat + "\n Country: "
                                        + country+" \n SunRise on : "+ riseDate+" at :"+riseTime+"\n SunSet on : "+setDate+"  at : "
                                        +setTime +"\nFeels like  "+tempVal+"*C"+"\nMinimum Temperature: "+tempMin+"*C"
                                        +"\nMaximum Temperature: "+tempMax+"*C"+"\nPressure : "+preValue
                                        +" "+preUnit+"\nHumidity :"+humValue+humUnit+"\nWind speed :"+windSpeed+"m/s  , "+windName+ "\nWind Direction :"
                                        +dirValue+"degree"+dirCode +"("+dirName+")"+"\nClouds :"+cloudVal+", "+cloudName
                                        +"\nVisibility : "+visibilityVal+"\nPrecipitation :"+precipitation+"\nWeather :"
                                        +weatherVal+"\nlast Updated on : "+lastUpdateVal);
                                           break;


                        }

                    }


                    event = parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        }
    }

}
