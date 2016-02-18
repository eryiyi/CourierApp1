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
 * ʶ��ص�������
 */
public class MyRecognizerDialogLister implements RecognizerDialogListener {
	private Context context;
	public MyRecognizerDialogLister(Context context)
	{
		this.context = context;
	}
	//�Զ���Ľ���ص��������ɹ�ִ�е�һ��������ʧ��ִ�еڶ�������
	@Override
	public void onResult(RecognizerResult results, boolean isLast) {
		// TODO Auto-generated method stub
		boolean flag = isLast;
		if(!flag){//���һ���Ǿ�� ���ǲ�����
			String text = JsonParser.parseIatResult(results.getResultString());
//		System.out.println(text);
//		Toast.makeText(context,text, Toast.LENGTH_LONG).show();
			if(text.length() == 11){
				//���ù㲥��ˢ����ҳ
				Intent intent1 = new Intent(Constants.MOBILE_RECORD);
				intent1.putExtra(Constants.MOBILE_CONTENT ,text);
				context.sendBroadcast(intent1);
			}else {
				Toast.makeText(context, R.string.validate_data_error, Toast.LENGTH_LONG).show();
			}
		}

	}
	/**
	 * ʶ��ص�����.
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
