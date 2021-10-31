package com.thknight.yoaadhaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.lingala.zip4j.ZipFile;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class
MainActivity extends AppCompatActivity {
    private String name,dob,dist,gender,father,street,post,pincode,state, subdist,vill,photo;

    EditText uid,capValue,otp;
    RequestQueue queue;
    String cap,txid;
    String uid_no;
    ImageView iv,ppic;
    String uidv,capv,otpTxid,txxxx, ekycxml,filename;
    TextView textView;
    Button submit,send_otp,reload,see,update;
    DBHandler db = new DBHandler(MainActivity.this);

    Consumer consumer;


    public MainActivity() {

    }
    public  void callOtp(Context context, String uid, String txid, String captchaValue, RequestQueue queue) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uidNumber", uid);
        params.put("captchaTxnId", txid);
        params.put("captchaValue", captchaValue);
        params.put("transactionId","MYAADHAAR:59142477-3f57-465d-8b9a-75b28fe48725");

        JsonObjectRequest req=new JsonObjectRequest(Request.Method.POST, Constants.generateOtp, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse( JSONObject response ) {
                try {
                    if(response.getString("status").equals("Success")){

                        txxxx = response.getString("txnId");

                        Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(context, "otp error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse( VolleyError error ) {

            }
        });
        queue.add(req);


    }


    private void generateOTP() {
        uidv=uid.getText().toString();
        capv=capValue.getText().toString();
        callOtp(MainActivity.this,uidv,txid,capv,queue);


    }

    public void callEkyc(View v) {


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("txnNumber",txxxx);
        params.put("otp", otp.getText().toString());
        params.put("shareCode", "1234");
        params.put("uid", uid.getText().toString());
        System.out.println("Value" + txxxx + otp.getText().toString() + uid.getText().toString());

        JsonObjectRequest req=new JsonObjectRequest(Request.Method.POST, Constants.ekycOffline, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse( JSONObject response ) {
                try {
                    if(response.getString("status").equals("Success")){

                        doProcess(response.getString("eKycXML"),response.getString("fileName"));

                        // Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("ResponseCode>>>>>",response.toString());

                    }
                    else{
                        Toast.makeText(MainActivity.this, "otp error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse( VolleyError error ) {

            }
        });
        queue.add(req);

    }
    private void setCaptcha(String cap_value) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.alert_dark_frame);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        //String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        imageBytes = Base64.decode(cap_value, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        iv.setImageBitmap(decodedImage);

    }

    private void callCaptcha() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constants.langCode, "AbCdEfGh123456");
        params.put(Constants.captchaLength, "AbCdEfGh123456");
        params.put(Constants.captchaType, "AbCdEfGh123456");

        JsonObjectRequest req=new JsonObjectRequest(Request.Method.POST, Constants.captchaUrl, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse( JSONObject response ) {
                try {
                    if(response.getString("status").equals("Success")){
                        cap=response.getString("captchaBase64String");
                        txid=response.getString("captchaTxnId");
                        Log.d(">>>>>>>>>",txid);

                        setCaptcha(cap);
                        Toast.makeText(MainActivity.this, "captcha OK", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse( VolleyError error ) {

            }
        });
        queue.add(req);



    }
    public void callOtp1( View view ) {
        generateOTP();
        //callEkyc();
    }



    private void doProcess( String eKycXML,String filename ) {


        //  file = getFilesDir();


        // data save
        try {
            FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE);
            byte[] decoder = Base64.decode(eKycXML,1);
            fos.write(decoder);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//     data save


        // open zip


        File path=getFilesDir();
        String fpath=path+"/"+filename;
        String n=path+"/dataUIDAI/";
        String withXml=filename.replaceAll("zip","xml");
        String ExtractFilePath=n+"/"+withXml;
        Log.d(">>>>>",path.getPath());
        try {
            new ZipFile(fpath, "1234".toCharArray()).extractAll(path.getPath()+"/dataUIDAI");
            photo=parseString(ExtractFilePath,"OfflinePaperlessKyc","Pht");
            name =parseStringByTag(ExtractFilePath,"Poi","name");
//            name = "kapil";
            dob =parseStringByTag(ExtractFilePath,"Poi","dob");
            father =parseStringByTag(ExtractFilePath,"Poa","careof");
            dist =parseStringByTag(ExtractFilePath,"Poa","dist");
            street =parseStringByTag(ExtractFilePath,"Poa","street");
            post =parseStringByTag(ExtractFilePath,"Poa","po");
            state =parseStringByTag(ExtractFilePath,"Poa","state");
            subdist = parseStringByTag(ExtractFilePath, "Poa", "subdist");
            vill = parseStringByTag(ExtractFilePath, "Poa", "vtc");
            pincode = parseStringByTag(ExtractFilePath, "Poa", "pc");

            consumer = new Consumer(name,father,dob,pincode,dist,street,post,state,subdist,vill);
            consumer.setName(name);
            consumer.setDist(dist);
            consumer.setDob(dob);
            consumer.setFather(father);
            consumer.setStreet(street);
            consumer.setPost(post);
            consumer.setState(state);
            consumer.setSubdist(subdist);
            consumer.setVill(vill);
            consumer.setPincode(pincode);




            Toast.makeText(MainActivity.this, dob, Toast.LENGTH_SHORT).show();
            setPic(photo);
            String text = name + father + dist + street + post + state + subdist +vill + pincode;
            textView.setText(text);
            DBHandler db = new DBHandler(MainActivity.this);
            try {
                Log.d("<<UID>> ",uid.getText().toString());

                String uid_value = uid.getText().toString();
                String name = consumer.getName();
                String father = consumer.getFather();
                String dob = consumer.getDob();
                String pincode = consumer.getPincode();
                String dist = consumer.getDist();
                String street = consumer.getStreet();
                String state = consumer.getState();
                String subdist = consumer.getSubdist();
                String vill = consumer.getVill();
                System.out.println("main" + uid_value + name + father + dob + pincode + dist + street + post + state + subdist + vill);

                boolean isInserted = db.addNewAddress(MainActivity.this,uid_value,name,father,dob,pincode,dist,street,post,state,subdist,vill);
                Log.d("<<Inserted>>",String.valueOf(isInserted));



            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }





            Log.d("<><><><",photo);
        }
        catch (IOException e){
            Log.d("DEBUG---",e.toString());



        } catch (SAXException e) {
            Log.d("DEBUG---",e.toString());

            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            Log.d("DEBUG---",e.toString());

        }


    }

    private void setPic( String dob ) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.alert_dark_frame);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        //String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        imageBytes = Base64.decode(dob, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        ppic.setImageBitmap(decodedImage);


    }

    public static String parseString(String filename, String tag, String subNode) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(filename);
        String text = null;
//an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//an instance of builder to parse the specified xml file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
//        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        NodeList nodeList = doc.getElementsByTagName(tag);
// nodeList is not iterable, so we are using for loop
        for (int itr = 0; itr < nodeList.getLength(); itr++)
        {
            Node node = nodeList.item(itr);
            System.out.println("\nNode Name :" + node.getNodeName());

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                text = element.getElementsByTagName(subNode).item(0).getTextContent();
                System.out.println(text);
//                text = element.getElementsByTagName(subNode).item(0).getTextContent();


            }
        }






        return text + " ";
    }

    public static String parseStringByTag(String filename, String tag, String subNode) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(filename);
        String text = null;
//an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//an instance of builder to parse the specified xml file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
//        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        NodeList nodeList = doc.getElementsByTagName(tag);
// nodeList is not iterable, so we are using for loop
        for (int itr = 0; itr < nodeList.getLength(); itr++)
        {
            Node node = nodeList.item(itr);
            System.out.println("\nNode Name :" + node.getNodeName());

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                text = element.getAttribute(subNode);
                System.out.println(text);
//                text = element.getElementsByTagName(subNode).item(0).getTextContent();


            }
        }






        return text + " ";
    }





    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uid=findViewById(R.id.et_uid);
        iv=findViewById(R.id.iv_cap);
        ppic=findViewById(R.id.iv_pic);
        capValue=findViewById(R.id.et_captcha);
        otp=findViewById(R.id.et_eotp);
        queue= Volley.newRequestQueue(MainActivity.this);
        textView = findViewById(R.id.textView);
        submit = findViewById(R.id.bt_sub);
        see = findViewById(R.id.see);
        send_otp = findViewById(R.id.bt_opt);
        reload = findViewById(R.id.reload);
        callCaptcha();
        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLoadNewActivity = new Intent(MainActivity.this, AddressUpdate.class);
                startActivity(intentLoadNewActivity);




            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCaptcha();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEkyc(view);


            }
        });


        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent(MainActivity.this,AddressUpdate.class);

                try{
                    uid_no = uid.getText().toString();
                    StringBuilder name = db.showAddress(uid_no,2);
                    StringBuilder father = db.showAddress(uid_no,3);
                    StringBuilder dob = db.showAddress(uid_no,4);
                    StringBuilder street = db.showAddress(uid_no,5);
                    StringBuilder subdist = db.showAddress(uid_no,6);
                    StringBuilder post = db.showAddress(uid_no,7);
                    StringBuilder city = db.showAddress(uid_no,8);
                    StringBuilder zipcode = db.showAddress(uid_no,9);

                    intent.putExtra("name",name.toString());
                    intent.putExtra("father",father.toString());
                    intent.putExtra("dob",dob.toString());
                    intent.putExtra("street",street.toString());
                    intent.putExtra("subdist",subdist.toString());
                    intent.putExtra("post",post.toString());
                    intent.putExtra("city",city.toString());
                    intent.putExtra("zipcode",zipcode.toString());

                    startActivity(intent);





                }
                catch (Exception e){
                    e.printStackTrace();
                }




            }
        });




        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateOTP();
            }
        });









    }






}
