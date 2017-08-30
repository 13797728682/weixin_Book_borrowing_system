package cn.sdut.bms_manager.register;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.sdut.bms_manager.MainActivity;
import cn.sdut.bms_manager.R;
import cn.sdut.bms_manager.tools.StreamTools;
import cn.sdut.bms_manager.tools.SysApplication;

public class RegisterThree extends Activity {

	private String name;
	private String employnum;
	private String phone;
	private Button btn_register;
	private EditText et_pwd;
	private EditText et_pwdtrue;
	private String returndata;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registerthree);
		SysApplication.getInstance().addActivity(this);
		Intent intent=getIntent();
		name = intent.getStringExtra("name");
		employnum = intent.getStringExtra("employnum");
		phone = intent.getStringExtra("phone");
		btn_register = (Button)findViewById(R.id.btn_register);
		et_pwd = (EditText)findViewById(R.id.et_pwd);
		et_pwdtrue = (EditText)findViewById(R.id.et_pwdtrue);
		btn_register.setOnClickListener(new OnClickListener() {
			
			private String pwd;
			private String pwd_true;

			@Override
			public void onClick(View v) {
				pwd = et_pwd.getText().toString().trim();
				pwd_true = et_pwdtrue.getText().toString().trim();
			    if(pwd.equals("")){
			    Toast.makeText(RegisterThree.this,"����������", Toast.LENGTH_LONG).show();
		}else if(pwd.length()<6){
			 Toast.makeText(RegisterThree.this,"���볤�Ȳ��ܵ���6λ", Toast.LENGTH_LONG).show();
		}else if(pwd.equals(pwd_true)){
			new Thread() {
				

				public void run() {
					String path=null;
					try {
						path = "https://www.kyssky.party:8443/bms_manage/RegisterForInsertStaff?name="
								+ URLEncoder.encode(name, "utf-8")+"&employnum="+employnum+"&phone="+phone+"&password="+pwd;
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					  System.out.println(path.toString());
					URL url;
					try {
						url = new URL(path);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setConnectTimeout(60000);
						int code = conn.getResponseCode();
						if (code == 200) {
							InputStream in = conn.getInputStream();
							returndata = StreamTools.readStream(in);
							System.out.println(returndata);
							JSONObject json = JSONObject.parseObject(returndata);
							String statuscode = (String) json.get("statuscode");
							if (statuscode.equals("1001")) {
								// ��תҳ��
							    runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										AlertDialog.Builder builder = new Builder(RegisterThree.this);
										builder.setTitle("��ʾ");
										builder.setMessage("ע��ɹ�,���Ĺ���Ա���Ϊ"+employnum);
										    builder.setNegativeButton("ȷ��",new DialogInterface.OnClickListener() {
												
												public void onClick(DialogInterface dialog, int which) {
													// TODO Auto-generated method stub
													Intent intent =new Intent(RegisterThree.this,MainActivity.class);
													startActivity(intent);
													SysApplication.getInstance().exit();
												}

				
											});
										builder.show();
									}
								});
								/*Intent intent=new Intent(RegisterTwo.this,RegisterThree.class);
								 intent.putExtra("phone",phone);
								 intent.putExtra("employnum", employnum);
								 intent.putExtra("name", name);
								 startActivity(intent);*/
							} else {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method
										// stub
										AlertDialog.Builder builder = new Builder(RegisterThree.this);
										builder.setTitle("��ʾ");
										builder.setMessage("ע��ʧ�ܣ�");
										builder.setPositiveButton("ȷ��", null);
										builder.show();
									}
								});
							}

						} else {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(RegisterThree.this, "����������ʧ��", Toast.LENGTH_LONG).show();
								}
							});
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				};
			}.start();
			
			
		}else{
			 Toast.makeText(RegisterThree.this,"ȷ���������������벻һ��", Toast.LENGTH_LONG).show();
		}
			}
		});
	}
	
	
}
