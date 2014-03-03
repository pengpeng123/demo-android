package com.example.horizontallistviewdemo;

import java.util.ArrayList;
import java.util.LinkedList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshHorizontalScrollView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class MainActivity extends Activity {

	// private HorizontalListView listView;

	private String[] mStrings = { "Abbaye de Belloc",
			"Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu",
			"Airag", "Airedale", "Aisy Cendre", "Allgauer Emmentaler",
			"Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
			"Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
			"Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };
	private LinkedList<String> mListItems;
	private ArrayAdapter<String> mAdapter;
	private LinkedList<String> mPhotoList = new LinkedList<String>();
	// private LinkedList<String> currentPhotoList;
	private ArrayList<LinkedList<String>> mPhotoListList = new ArrayList<LinkedList<String>>();
	private LayoutInflater inflater;
	private int mWidth;
	private int mHeight;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	// private PullToRefreshHorizontalScrollView mPullRefreshScrollView;
	private HorizontalScrollView mScrollView;
	private MyListAdapter1 adapter;
	private float downPoint = -1f;
	private float upPoint = -1f;
	private boolean isRefreshForStart;
	private boolean isRefreshForEnd;
	private XListView mPullRefreshListView;
	private ListView actualListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		mWidth = (int) displayMetrics.widthPixels;
		mHeight = (int) displayMetrics.heightPixels;

		setContentView(R.layout.main);

		mPullRefreshListView = (XListView) findViewById(R.id.pull_refresh_list);

		mPullRefreshListView.setPullLoadEnable(true);
		mPullRefreshListView.setPullRefreshEnable(true);
		mPullRefreshListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

				new GetListDataForStartTask().execute();
			}

			@Override
			public void onLoadMore() {

				new GetListDataForEndTask().execute();
			}

		});

		// listView = (HorizontalListView) findViewById(R.id.list);

		// mPullRefreshListView.setScrollingWhileRefreshingEnabled(false);
		// mPullRefreshListView.setMode(Mode.BOTH);
		//
		// mPullRefreshListView
		// .setOnRefreshListener(new OnRefreshListener<ListView>() {
		// @Override
		// public void onRefresh(
		// PullToRefreshBase<ListView> refreshView) {
		// String label = DateUtils.formatDateTime(
		// getApplicationContext(),
		// System.currentTimeMillis(),
		// DateUtils.FORMAT_SHOW_TIME
		// | DateUtils.FORMAT_SHOW_DATE
		// | DateUtils.FORMAT_ABBREV_ALL);
		//
		// // Update the LastUpdatedLabel
		// refreshView.getLoadingLayoutProxy()
		// .setLastUpdatedLabel(label);
		//
		// Log.w("HListView", refreshView.getCurrentMode() + "");
		//
		// if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {
		//
		// new GetListDataForStartTask().execute();
		//
		// } else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {
		//
		// new GetListDataForEndTask().execute();
		// }
		//
		// }
		// });

		// mPullRefreshListView
		// .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
		//
		// @Override
		// public void onLastItemVisible() {
		// Toast.makeText(MainActivity.this, "End of List!",
		// Toast.LENGTH_SHORT).show();
		// }
		// });

		// actualListView = mPullRefreshListView.getRefreshableView();

		// mScrollView = mPullRefreshScrollView.getRefreshableView();

		initImageLoader(this);

		// mListItems = new LinkedList<String>();
		// mListItems.addAll(Arrays.asList(mStrings));
		// mAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, mListItems);

		initData();
		mPhotoListList.add(mPhotoList);

		adapter = new MyListAdapter1();

//		actualListView.setAdapter(adapter);
		mPullRefreshListView.setAdapter(adapter);

		// Utility.setListViewWidthBasedOnChildren(listView);

	}

	private class GetListDataForEndTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			// mPullRefreshListView.onRefreshComplete();

			mPullRefreshListView.stopRefresh();
			mPullRefreshListView.stopLoadMore();

			initData2();
//			adapter.notifyDataSetChanged();
			mPullRefreshListView.setAdapter(adapter);

			super.onPostExecute(result);
		}
	}

	private class GetListDataForStartTask extends
			AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			// mPullRefreshListView.onRefreshComplete();

			mPullRefreshListView.stopRefresh();
			mPullRefreshListView.stopLoadMore();

			initData3();
			// adapter.notifyDataSetChanged();
//			actualListView.setAdapter(adapter);
			mPullRefreshListView.setAdapter(adapter);

			super.onPostExecute(result);
		}
	}

	private class GetScrollDataForLeftTask extends
			AsyncTask<Void, Void, String[]> {

		private PullToRefreshHorizontalScrollView scrollView;
		private HorizontalListView list;
		private LinkedList<String> currentPhotoList;

		GetScrollDataForLeftTask(PullToRefreshHorizontalScrollView scrollView,
				HorizontalListView list, LinkedList<String> currentPhotoList) {

			this.scrollView = scrollView;
			this.list = list;
			this.currentPhotoList = currentPhotoList;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			// mPullRefreshScrollView.onRefreshComplete();
			scrollView.onRefreshComplete();

			initData1(currentPhotoList);
			Utility.setListViewWidthBasedOnChildren(list);
			((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
			list.setSelection(6);

			super.onPostExecute(result);
		}
	}

	private class GetScrollDataForRightTask extends
			AsyncTask<Void, Void, String[]> {

		private PullToRefreshHorizontalScrollView scrollView;
		private HorizontalListView list;
		private LinkedList<String> currentPhotoList;

		GetScrollDataForRightTask(PullToRefreshHorizontalScrollView scrollView,
				HorizontalListView list, LinkedList<String> currentPhotoList) {

			this.scrollView = scrollView;
			this.list = list;
			this.currentPhotoList = currentPhotoList;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			// mPullRefreshScrollView.onRefreshComplete();
			scrollView.onRefreshComplete();

			initData(currentPhotoList);
			Utility.setListViewWidthBasedOnChildren(list);
			((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
			list.setSelection(list.getAdapter().getCount() - 7);

			super.onPostExecute(result);
		}
	}

	private void initData() {

		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215035600700175/T1C2mzXthaXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215025617307680/T1AQqAXqpeXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i1/13215035569460099/T16GuzXs0cXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215023694438773/T1lImmXElhXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023521330093/T1BWuzXrhcXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i4/13215035563144015/T1Q.eyXsldXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
	}

	private void initData(LinkedList<String> currentPhotoList) {

		currentPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215035600700175/T1C2mzXthaXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215025617307680/T1AQqAXqpeXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i1/13215035569460099/T16GuzXs0cXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215023694438773/T1lImmXElhXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023521330093/T1BWuzXrhcXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i4/13215035563144015/T1Q.eyXsldXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
	}

	private void initData2() {

		LinkedList<String> mPhotoList = new LinkedList<String>();

		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215035600700175/T1C2mzXthaXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215025617307680/T1AQqAXqpeXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i1/13215035569460099/T16GuzXs0cXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215023694438773/T1lImmXElhXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023521330093/T1BWuzXrhcXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i4/13215035563144015/T1Q.eyXsldXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");

		mPhotoListList.add(mPhotoList);
	}

	private void initData3() {

		LinkedList<String> mPhotoList = new LinkedList<String>();

		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");

		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i4/13215035563144015/T1Q.eyXsldXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");

		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023521330093/T1BWuzXrhcXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");

		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215023694438773/T1lImmXElhXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");

		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i1/13215035569460099/T16GuzXs0cXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");

		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215025617307680/T1AQqAXqpeXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList
				.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215035600700175/T1C2mzXthaXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");

		mPhotoListList.add(0, mPhotoList);
	}

	private void initData1(LinkedList<String> currentPhotoList) {

		currentPhotoList
				.addFirst("http://img01.taobaocdn.com/bao/uploaded/i3/13215035600700175/T1C2mzXthaXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.addFirst("http://img01.taobaocdn.com/bao/uploaded/i2/13215025617307680/T1AQqAXqpeXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.addFirst("http://img01.taobaocdn.com/bao/uploaded/i1/13215035569460099/T16GuzXs0cXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.addFirst("http://img01.taobaocdn.com/bao/uploaded/i2/13215023694438773/T1lImmXElhXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.addFirst("http://img01.taobaocdn.com/bao/uploaded/i3/13215023521330093/T1BWuzXrhcXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.addFirst("http://img01.taobaocdn.com/bao/uploaded/i4/13215035563144015/T1Q.eyXsldXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		currentPhotoList
				.addFirst("http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
	}

	public void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.memoryCacheExtraOptions(480, 800)
				.discCacheExtraOptions(mWidth, mHeight, CompressFormat.JPEG,
						75, null).memoryCache(new WeakMemoryCache())
				.threadPoolSize(5).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);

		options = new DisplayImageOptions.Builder()
				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				// .cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// .displayer(new FadeInBitmapDisplayer(300))
				.build();
	}

	private class MyListAdapter extends BaseAdapter {

		private LinkedList<String> mPhotoList;

		MyListAdapter(LinkedList<String> mPhotoList) {

			this.mPhotoList = mPhotoList;
		}

		@Override
		public int getCount() {

			return mPhotoList.size();
		}

		@Override
		public Object getItem(int position) {

			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.page4, null);
				holder.loading = (ProgressBar) convertView
						.findViewById(R.id.loading);
				holder.page_iv = (ImageView) convertView
						.findViewById(R.id.page_iv);

				convertView.setTag(holder);
			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			final int pos = position;

			holder.page_iv.setImageDrawable(null);
			holder.page_iv.setTag(null);

			holder.page_iv.setTag(mPhotoList.get(position));
			final ProgressBar bar = holder.loading;
			imageLoader.displayImage(mPhotoList.get(position), holder.page_iv,
					options, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							bar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							String message = null;
							switch (failReason.getType()) {
							case IO_ERROR:
								message = "Input/Output error";
								break;
							case DECODING_ERROR:
								message = "Image can't be decoded";
								break;
							case NETWORK_DENIED:
								message = "Downloads are denied";
								break;
							case OUT_OF_MEMORY:
								message = "Out Of Memory error";
								break;
							case UNKNOWN:
								message = "Unknown error";
								break;
							}

							// Toast.makeText(getApplicationContext(), message,
							// Toast.LENGTH_SHORT).show();
							bar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

							bar.setVisibility(View.GONE);
							ImageView imageView = (ImageView) view;
							String imageUrlTag = (String) imageView.getTag();
							if (!TextUtils.isEmpty(imageUrlTag)
									&& !imageUrlTag.equals(imageUri)) {
								imageView.setImageDrawable(null);
							}
						}
					});

			return convertView;
		}
	}

	private class ViewHolder {

		ProgressBar loading;
		ImageView page_iv;
	}

	private class MyListAdapter1 extends BaseAdapter {

		@Override
		public int getCount() {

			return mPhotoListList.size();
		}

		@Override
		public Object getItem(int position) {

			return mPhotoListList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder1 holder = null;
			if (convertView == null) {

				holder = new ViewHolder1();
				convertView = inflater.inflate(R.layout.listitem, null);

				holder.pull_refresh_horizontalscrollview = (PullToRefreshHorizontalScrollView) convertView
						.findViewById(R.id.pull_refresh_horizontalscrollview);
				holder.list = (HorizontalListView) convertView
						.findViewById(R.id.list);

				holder.pull_refresh_horizontalscrollview.setMode(Mode.BOTH);
				holder.pull_refresh_horizontalscrollview
						.setScrollingWhileRefreshingEnabled(false);

				final PullToRefreshHorizontalScrollView scrollView = holder.pull_refresh_horizontalscrollview;
				final HorizontalListView list = holder.list;
				final LinkedList<String> currentPhotoList = mPhotoListList
						.get(position);
				holder.pull_refresh_horizontalscrollview
						.setOnRefreshListener(new OnRefreshListener<HorizontalScrollView>() {

							@Override
							public void onRefresh(
									PullToRefreshBase<HorizontalScrollView> refreshView) {

								if (refreshView.getCurrentMode() == Mode.PULL_FROM_START) {

									new GetScrollDataForLeftTask(scrollView,
											list, currentPhotoList).execute();

								} else if (refreshView.getCurrentMode() == Mode.PULL_FROM_END) {

									new GetScrollDataForRightTask(scrollView,
											list, currentPhotoList).execute();
								}

							}
						});

				convertView.setTag(holder);
			} else {

				holder = (ViewHolder1) convertView.getTag();
			}

			MyListAdapter listAdapter = new MyListAdapter(
					mPhotoListList.get(position));

			holder.list.setAdapter(listAdapter);

			Utility.setListViewWidthBasedOnChildren(holder.list);

			return convertView;
		}
	}

	private class ViewHolder1 {

		PullToRefreshHorizontalScrollView pull_refresh_horizontalscrollview;
		HorizontalListView list;
	}

}
