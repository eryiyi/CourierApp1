package com.example.CourierApp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.example.CourierApp.R;
import com.example.CourierApp.util.Constants;
import com.example.CourierApp.util.JsonParser;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * 识别回调监听器
 */
public class MyRecognizerDialogLister implements RecognizerDialogListener {
	private Context context;
	public MyRecognizerDialogLister(Context context)
	{
		this.context = context;
	}
	//自定义的结果回调函数，成功执行第一个方法，失败执行第二个方法
	@Override
	public void onResult(RecognizerResult results, boolean isLast) {
		// TODO Auto-generated method stub
		boolean flag = isLast;
		if(!flag){//最后一个是句号 我们不处理
			String text = JsonParser.parseIatResult(results.getResultString());
//		System.out.println(text);
//		Toast.makeText(context,text, Toast.LENGTH_LONG).show();
			if(text.length() == 11){
				//调用广播，刷新主页
				Intent intent1 = new Intent(Constants.MOBILE_RECORD);
				intent1.putExtra(Constants.MOBILE_CONTENT ,text);
				context.sendBroadcast(intent1);
			}else {
				Toast.makeText(context, R.string.validate_data_error, Toast.LENGTH_LONG).show();
			}
		}

	}
	/**
	 * 识别回调错误.
	 */
	@Override
	public void onError(SpeechError error) {
		// TODO Auto-generated method stub
		int errorCoder = error.getErrorCode();
		switch (errorCoder) {
		case 10118:
			System.out.println("user don't speak anything");
			break;
		case 10204:
			System.out.println("can't connect to internet");
			break;
		default:
			break;
		}
	}

	

}
