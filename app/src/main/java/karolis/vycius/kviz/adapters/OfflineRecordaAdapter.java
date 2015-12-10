package karolis.vycius.kviz.adapters;

import java.util.ArrayList;

import karolis.vycius.kviz.entities.RecordValue;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

public class OfflineRecordaAdapter extends RecordBaseAdapter {

	private SparseBooleanArray mSelectedItemsPos;

	public OfflineRecordaAdapter(Context context) {
		super(context);
		mSelectedItemsPos = new SparseBooleanArray();

	}

	@Override
	public long getItemId(int position) {
		return ((RecordValue )getItem(position)).getId();
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		row = super.getView(position, row, parent);

		row.setBackgroundColor(mSelectedItemsPos.get(position) ? 0x9934B5E4
				: Color.TRANSPARENT);

		return row;
	}

	@Override
	public void populate(Cursor cursor) {
		mSelectedItemsPos = new SparseBooleanArray();
		super.populate(cursor);
	}

	public void selectView(int position, boolean value) {
		if (value)
			mSelectedItemsPos.put(position, value);
		else
			mSelectedItemsPos.delete(position);

		notifyDataSetChanged();
	}

	public int getSelectedCount() {
		return mSelectedItemsPos.size();
	}

	public SparseBooleanArray getSelectedPositions() {
		return mSelectedItemsPos;
	}

	public ArrayList<Long> getSelectedIds() {
		ArrayList<Long> ids = new ArrayList<Long>();
		
		for(int i = 0; i < mSelectedItemsPos.size(); ++i){
			int id = mSelectedItemsPos.keyAt(i);
			ids.add(getItemId(id));
		}
		
		return ids;
	}

	public void removeSelection() {
		mSelectedItemsPos = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public void toggleSelection(int position) {
		selectView(position, !mSelectedItemsPos.get(position));
	}

}
