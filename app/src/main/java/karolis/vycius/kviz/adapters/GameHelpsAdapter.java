package karolis.vycius.kviz.adapters;

import java.util.ArrayList;

import karolis.vycius.kviz.R;
import karolis.vycius.kviz.game.helps.HelpsList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameHelpsAdapter extends BaseAdapter {
	private ArrayList<HelpsList> helps;
	private LayoutInflater inflater;

	public GameHelpsAdapter(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		helps = new ArrayList<HelpsList>();
	}

	@Override
	public int getCount() {
		return helps.size();
	}

	@Override
	public Object getItem(int position) {
		return helps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {
		row =  inflater.inflate(R.layout.sidebar_help_item, parent, false);
		
		HelpsList value = (HelpsList) getItem(position);
		
		row.setTag(value);
		
		ImageView image = (ImageView) row.findViewById(R.id.sidebarHelpImage);
		TextView nameTV = (TextView) row.findViewById(R.id.sidebarHelpName);
		
		
		image.setImageResource(value.drawable);
		nameTV.setText(value.title);
		
		return row;
	}

	public void replace(ArrayList<HelpsList> helps) {
		this.helps = helps;
		
		this.notifyDataSetInvalidated();
	}

}
