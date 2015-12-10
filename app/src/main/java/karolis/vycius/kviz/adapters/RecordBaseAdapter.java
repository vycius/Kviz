package karolis.vycius.kviz.adapters;

import java.util.ArrayList;

import karolis.vycius.kviz.R;
import karolis.vycius.kviz.entities.RecordValue;

import org.json.JSONArray;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RecordBaseAdapter extends BaseAdapter {

	protected ArrayList<RecordValue> records;
	protected final LayoutInflater inflater;

	public RecordBaseAdapter(Context context) {
		records = new ArrayList<RecordValue>();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return records.size();
	}

	@Override
	public Object getItem(int position) {
		return records.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		ViewHolder holder;

		if (row == null)
			row = inflater.inflate(R.layout.records_row, parent, false);

		if (row.getTag() instanceof ViewHolder)
			holder = (ViewHolder) row.getTag();
		else {
			holder = new ViewHolder(row);
			row.setTag(holder);
		}

		RecordValue value = (RecordValue) getItem(position);

		holder.placeTV.setText(String.valueOf(position + 1));
		holder.nameTV.setText(value.getName());
		holder.pointsTV.setText(String.valueOf(value.getPoints()));

		return row;
	}

	public void clear() {
		if (records != null && !records.isEmpty())
			records.clear();
	}

	public void populate(JSONArray array) {
		clear();
		try {
			for (int i = 0; i < array.length(); ++i)
				records.add(new RecordValue(array.getJSONObject(i)));

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.notifyDataSetInvalidated();
	}

	public void populate(Cursor cursor) {
		clear();
		try {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
				records.add(new RecordValue(cursor));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.notifyDataSetInvalidated();
	}

	public static class ViewHolder {
		public TextView nameTV, pointsTV, placeTV;

		public ViewHolder(View view) {
			nameTV = (TextView) view.findViewById(R.id.rName);
			pointsTV = (TextView) view.findViewById(R.id.rPoints);
			placeTV = (TextView) view.findViewById(R.id.rPlace);
		}
	}

}
