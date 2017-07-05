package com.mumu.study.http;

import android.os.Handler;
import android.os.Message;

public class HttpHandler extends Handler {

	private ResponseListener responseListener;

	public HttpHandler(ResponseListener responseListener){
		this.responseListener = responseListener;
	}

	@Override
	public void handleMessage(Message msg) {

		switch (msg.what) {
			case HttpExecute.REQ_SUCCESS:
				if (responseListener != null){
					ResponseModel model = (ResponseModel) msg.obj;
					responseListener.onSuccess(model.getModel());
				}
				break;
			case HttpExecute.REQ_FAILURE:
				if (responseListener != null){
					ResponseModel model = (ResponseModel) msg.obj;
					responseListener.onFailure(model.getCode(), model.getMsg());
				}
				break;

			default:
				break;
		}
	}
}
