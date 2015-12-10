package karolis.vycius.kviz.game;

import karolis.vycius.kviz.entities.Question;
import android.os.Bundle;

public interface BaseGameListener {
	public void onTimerTick(long min, long s);

	public void onQuestionChanged(Question question, int points, int questionNr);

	public void onGameLost(Bundle bundle);
}
