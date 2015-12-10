package karolis.vycius.kviz.game.helps;

import java.util.ArrayList;

public interface OnHelpUsed {
	public void onHelpUsed(HelpsList help, ArrayList<HelpsList> availableHelps);
}
