package karolis.vycius.kviz.widget;

import karolis.vycius.kviz.helpers.TextHelper;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class AutoFitButton extends TextView {
	// To fix bug in android 4.0
	// https://code.google.com/p/android/issues/detail?id=22493
	public final static String DOUBLE_BYTE_SPACE = "\u3000";

	private final int maxTextSize = 300;
	private final int minTextSize = 8;
	private Button buttonResizable;
	private int w = 0, h = 0;
	private CharSequence longestWord = "";
	public static String bugReport = "";

	public AutoFitButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(context, attrs, defStyle);
	}

	public AutoFitButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context, attrs, 0);
	}

	public AutoFitButton(Context context) {
		super(context);

		init(context, null, 0);
	}

	@SuppressWarnings("deprecation")
	private void init(Context context, AttributeSet attrs, int defStyle) {
		buttonResizable = new Button(context, attrs, defStyle);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
			buttonResizable.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
		else
			buttonResizable.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
	}

	protected void refitText() {
		if (isInEditMode() || buttonResizable == null || w <= 0 || h <= 0)
			return;

		if (this.getText() != null)
			longestWord = TextHelper.getLongestWord(this.getText().toString());

		buttonResizable.setMaxWidth(w);

		buttonResizable.setText(this.getText() + DOUBLE_BYTE_SPACE);

		int textSize = TextHelper.getMaxTextSize(buttonResizable, h, w, minTextSize,
				maxTextSize, longestWord);

		this.setTextSize(textSize);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		this.w = w;
		this.h = h;

		if ((w != oldw || h != oldh))
			refitText();
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore,
			int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);

		refitText();
	}

}