package com.example.administrator.sjassistant.view;

import java.io.File;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.sjassistant.R;
import com.example.administrator.sjassistant.util.AbsolutePathUtil;
import com.example.administrator.sjassistant.util.FileUtil;
import com.example.administrator.sjassistant.util.ToastUtil;


/**
 * 
 * @Description 自定义PopupWindow，选择图片
 */
public class ChoosePhotoWindow implements OnClickListener {

	private Context context;
	private View view;
	private PopupWindow choosePhotoWindow;
	private Button btn_cancel;
	private TextView  btn_toCamera, btn_toPhotos;

	private final int REQUESTCODE_CAMERA = 222;// 照相机
	private final int REQUESTCODE_IMAGE = 221;// 本地相册
	private final int CROP_IMAGE = 223;

	private int imgWidth = 0, imgHeight = 0, imgQuality = 0;

	private String path;// 图片路径
	private String tempPath;
	private Uri imageUri;

	public ChoosePhotoWindow() {
		super();
		// TODO Auto-generated constructor stub
	}


	private void init() {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.choosephoto_layout, null);
		btn_toCamera = (TextView) view.findViewById(R.id.realAuth_toCamera_btn);
		btn_toPhotos = (TextView) view.findViewById(R.id.realAuth_toPhotos_btn);
		btn_cancel = (Button) view.findViewById(R.id.realAuth_cancel_btn);
		choosePhotoWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		btn_toCamera.setOnClickListener(this);
		btn_toPhotos.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		choosePhotoWindow.setAnimationStyle(R.style.AnimBottom);
		
	}

	public ChoosePhotoWindow(Context context) {
		this.context = context;
		init();
	}

	/**
	 * BitMap Cut By Quality
	 * 
	 * @param context
	 * @param quality
	 *            [0~100]
	 */
	public ChoosePhotoWindow(Context context, int quality) {
		this.context = context;
		init();
		this.imgQuality = quality;
	}

	/**
	 * BitMap Cut By Width,Height
	 * 
	 * @param context
	 * @param width
	 * @param height
	 */
	public ChoosePhotoWindow(Context context, int width, int height) {
		this.context = context;
		init();
		this.imgWidth = width;
		this.imgHeight = height;
	}

	/**
	 * BitMap Cut By Width,Height,Quality
	 * 
	 * @param context
	 * @param width
	 * @param height
	 * @param quality
	 *            [0~100]
	 */
	public ChoosePhotoWindow(Context context, int width, int height, int quality) {
		this.context = context;
		init();
		this.imgWidth = width;
		this.imgHeight = height;
	}

	/**
	 * set Visibility
	 * 
	 * @param resourceId
	 * @return
	 */
	public View findViewById(int resourceId) {
		return view.findViewById(resourceId);
	}

	/**
	 * getPopupWindow
	 * 
	 * @return
	 */
	public PopupWindow getPopupWindow() {
		return choosePhotoWindow;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (FileUtil.avaiableMedia() == false) {
			Toast.makeText(context, "没有sd卡", Toast.LENGTH_LONG).show();
			closeChoosePhotoWindow();
			return;
		}
		if (btn_toCamera == v) {
			toCamera();
		} else if (btn_toPhotos == v) {
			toPhotos();
		}
		closeChoosePhotoWindow();
	}

	/**
	 * popup is showing
	 */
	public boolean isShowing() {
		return choosePhotoWindow.isShowing();
	}

	/**
	 * 显示选择照片的窗口
	 * 
	 * @param view
	 *            the view on which to pin the popup window
	 */
	public void showChoosePhotoWindow(View view) {
		if (choosePhotoWindow != null) {
			choosePhotoWindow.showAsDropDown(view, LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
		}
	}

	public void showChoosePhotoWindow2(View view) {
        if (choosePhotoWindow != null) {
            choosePhotoWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

	/**
	 * 关闭选择照片的窗口
	 */
	public void closeChoosePhotoWindow() {
		if (choosePhotoWindow.isShowing()) {
			choosePhotoWindow.dismiss();
		}
	}

	/**
	 * 转跳到相机
	 */
	public void toCamera() {
		File file = new File(Environment
				.getExternalStorageDirectory(),
				FileUtil.getPhotoFileName());

		imageUri = Uri.fromFile(file);
		Log.d("response", "outputFileUri intent"
				+ imageUri);
		//if (FileUtil.avaiableMedia()) {
		//	tempPath = "file:///sdcard/"+ FileUtil.getPhotoFileName();

		//	imageUri = Uri.parse(tempPath);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			((Activity) context).startActivityForResult(intent, REQUESTCODE_CAMERA);

		//} else {
		//	ToastUtil.showShort(context,"请确认插入了SD卡");
		//}
	}

	/**
	 * 转跳到本地相簿
	 */
	public void toPhotos() {

		Intent intent=new Intent();
		intent.setAction(Intent.ACTION_PICK);//Pick an item fromthe data
		intent.setType("image/*");//从所有图片中进行选择
		((Activity) context).startActivityForResult(intent, REQUESTCODE_IMAGE);
	}

	/**
	 * 回调方法
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data,
			Upload upload) {

		if (resultCode != Activity.RESULT_OK) {
			return;
		}


		if (requestCode == REQUESTCODE_IMAGE) {
			if (data == null) {
				return;
			}
			try {
				Uri uri = data.getData();
				path = AbsolutePathUtil.getAbsolutePath(context, uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			if (data == null) {
//				return;
//			}
//			Uri uri = data.getData();
//			// 手机root RE 管理器
//			String strURl = uri.toString();
//			if (strURl.startsWith("file:///")) {
//				path = strURl.substring(7, strURl.length());
//			} else if (strURl.startsWith("content://")) {
//				// 图库
//				String[] projection = { MediaStore.Images.Media.DATA };
//				final Cursor cursor = ((Activity) context).managedQuery(uri,
//						projection, null, null, null);
//				final int column_index = cursor
//						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//				cursor.moveToFirst();
//				path = cursor.getString(column_index);
//				imageUri = Uri.parse(path);
//			}
			//startPhotoZoom(imageUri);
			//upload(upload);
		} else if (requestCode == REQUESTCODE_CAMERA) {
			//startPhotoZoom(imageUri);
			//upload(upload);
			Log.d("response", "temppath->" + tempPath + " ");
			path = imageUri.getPath();
		} else if (requestCode == CROP_IMAGE) {
			// 调用图片剪切程序返回数据
			path = data.getStringExtra("path");
			//upload(upload);
		}

		upload(upload);
	}

	/**
	 * 缩放图片
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
//		Intent intent = new Intent(context, ImageCutActivity.class);
//		intent.setDataAndType(uri, "image/jpeg");
//		intent.putExtra("imgWidth", this.imgWidth);
//		intent.putExtra("imgHeight", this.imgHeight);
//		intent.putExtra("imgQuality", this.imgQuality);
//		((Activity) context).startActivityForResult(intent, CROP_IMAGE);
	}

	/**
	 * 获取图片路径
	 */
	private void upload(Upload upload) {

		//LogHelper.i("imageUri:" + imageUri);

//        if (tempPath != null && !tempPath.equals("")) {
//			File file = new File(tempPath);
//			if (file.exists())
//				file.delete();
//		}

		if (path != null) {
			upload.upload(path);
		}
	}



	/**
	 * 上传
	 */
	public interface Upload {
		/**
		 * upload file
		 * 
		 * @param path
		 */
		public void upload(String path);
	}
}
