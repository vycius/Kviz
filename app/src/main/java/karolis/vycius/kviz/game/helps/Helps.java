package karolis.vycius.kviz.game.helps;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

/*
 * TODO LIST
 * 
 * Create functions givePaidHelps which gets helps what user have
 * 
 */

public class Helps {
	private HashMap<HelpsList, Integer> freeHelps;
	private Context context;
	private OnHelpUsed helpUsedListener;

	public Helps(Context context, OnHelpUsed listener) {
		this.context = context;
		this.helpUsedListener = listener;

		freeHelps = new HashMap<HelpsList, Integer>();
		giveFreeHelps();
		givePaidHelps();
	}

	public ArrayList<HelpsList> getAvailableHelps(){
		ArrayList<HelpsList> availableHelps = new ArrayList<HelpsList>();
		
		for(HelpsList help: HelpsList.values())
			if(isAvailable(help))
				availableHelps.add(help);
		
		
		return availableHelps;
	}
	
	public boolean isAvailable(HelpsList help) {
		return (freeHelps.containsKey(help) && freeHelps.get(help) >= 1);
	}

	private void givePaidHelps() {
	}

	private void giveFreeHelps() {
		for (HelpsList help : HelpsList.values())
			freeHelps.put(help, 1);

	}

	public void useHelp(HelpsList help) {
		if (isAvailable(help)) {
			freeHelps.put(help, freeHelps.get(help) - 1);
			helpUsedListener.onHelpUsed(help, getAvailableHelps());
			return;
		}
	}
}
