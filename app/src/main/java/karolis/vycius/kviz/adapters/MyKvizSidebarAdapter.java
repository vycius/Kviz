package karolis.vycius.kviz.adapters;

import java.util.ArrayList;

import karolis.vycius.kviz.R;
import karolis.vycius.kviz.entities.MyKvizSidebarValue;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyKvizSidebarAdapter extends BaseAdapter {

	private ArrayList<MyKvizSidebarValue> values;
	private final LayoutInflater inflater;

	public MyKvizSidebarAdapter(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		values = new ArrayList<MyKvizSidebarValue>();
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		return values.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		row = inflater.inflate(R.layout.my_kviz_sidebar_item_layout, parent, false);

		MyKvizSidebarValue value = (MyKvizSidebarValue) getItem(position);

		TextView nameTV = (TextView) row.findViewById(R.id.kvizSidebarItemText);
		ImageView imageIV = (ImageView) row.findViewById(R.id.kvizSidebarItemImage);

		nameTV.setText(value.title);
		imageIV.setImageResource(value.image);

		return row;
	}

	public void swap(ArrayList<MyKvizSidebarValue> values) {
		this.values = values;

		this.notifyDataSetInvalidated();
	}

}
