package karolis.vycius.kviz.game.helps;

import karolis.vycius.kviz.R;

public enum HelpsList {
	RIGHT_WAY(R.string.HelpRightWay, R.string.HelpRightWayDescription,
			R.drawable.teisingas_kelias, true), FIFTY_FIFTY(R.string.HelpFiftyFifty,
			R.string.HelpFiftyFiftyDescription, R.drawable.q5050, true), VETO(
			R.string.HelpVeto, R.string.HelpVetoDescription, R.drawable.klasimo_veto, true);

	public final int title, description, drawable;
	public final boolean free;

	HelpsList(int title, int description, int drawable, boolean free) {
		this.title = title;
		this.description = description;
		this.drawable = drawable;
		this.free = free;
	}
}
