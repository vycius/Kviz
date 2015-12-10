package karolis.vycius.kviz.adapters;

import karolis.vycius.kviz.R;
import karolis.vycius.kviz.entities.RecordValue;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;

public class OnlineRecordsAdapter extends RecordBaseAdapter {

	private int gravatarSize = 0;

	public OnlineRecordsAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		if (row == null)
			row = inflater.inflate(R.layout.records_online_row, parent, false);

		row = super.getView(position, row, parent);

		RecordValue value = (RecordValue) getItem(position);

		AQuery aQuery = new AQuery(row);

		aQuery.id(R.id.rGravatar).image(value.getGravatar(getGravatarSize(row)));

		return row;
	}

	private int getGravatarSize(View view) {
		if (gravatarSize == 0) {
			TextView pointsTV = (TextView) view.findViewById(R.id.rPoints);

			pointsTV.measure(1200, 1200);

			gravatarSize = pointsTV.getMeasuredHeight();
		}
		return gravatarSize;
	}
}
