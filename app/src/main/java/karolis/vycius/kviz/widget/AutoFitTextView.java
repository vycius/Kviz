package karolis.vycius.kviz.widget;

import karolis.vycius.kviz.helpers.TextHelper;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class AutoFitTextView extends TextView {

	private final int maxTextSize = 300;
	private final int minTextSize = 3;
	private TextView textViewResizable;
	private int w = 0, h = 0;
	private CharSequence longestWord = "";

	public AutoFitTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(context, attrs, defStyle);
	}

	public AutoFitTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context, attrs, 0);
	}

	public AutoFitTextView(Context context) {
		super(context);

		init(context, null, 0);
	}

	@SuppressWarnings("deprecation")
	private void init(Context context, AttributeSet attrs, int defStyle) {
		textViewResizable = new TextView(context, attrs, defStyle);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
			textViewResizable.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
		else
			textViewResizable.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
	}

	protected void refitText() {
		if (isInEditMode() || textViewResizable == null || w <= 0 || h <= 0)
			return;

		if (this.getText() != null)
			longestWord = TextHelper.getLongestWord(this.getText().toString());

		textViewResizable.setMaxWidth(w);

		textViewResizable.setText(this.getText() + AutoFitButton.DOUBLE_BYTE_SPACE);

		int textSize = TextHelper.getMaxTextSize(textViewResizable, h, w, minTextSize,
				maxTextSize, longestWord);

		this.setTextSize(textSize);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		this.w = w;
		this.h = h;

		if (w != oldw || h != oldh)
			refitText();
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore,
			int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);

		refitText();
	}

}
