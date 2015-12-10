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

public class MyKvizSidebarGridAdapter extends BaseAdapter {

	private final LayoutInflater inflater;
	private ArrayList<MyKvizSidebarValue> values;

	public MyKvizSidebarGridAdapter(Context context, ArrayList<MyKvizSidebarValue> values) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.values = values;
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
		row = inflater.inflate(R.layout.my_kviz_sidebar_grid_item_layout, parent, false);

		MyKvizSidebarValue value = (MyKvizSidebarValue) getItem(position);

		TextView titleTV = (TextView) row.findViewById(R.id.kvizSidebarItemText);
		ImageView imageIV = (ImageView) row.findViewById(R.id.kvizSidebarItemImage);

		titleTV.setText(value.title);
		imageIV.setImageResource(value.image);

		return row;
	}

}
