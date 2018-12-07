package engine.component;

import java.util.ArrayList;

import engine.font.Font;
import engine.input.InputKey;
import engine.input.InputText;
import engine.object.Component;

public class InputField extends Component {

	Font font;

	int Timer = 0;
	boolean I_added = false;

	String inputString = "";

	@Override
	protected void Update() {
		Timer++;
		if (Timer > 30) {
			updateFont();

			I_added = !I_added;
			Timer = 0;
		}

		ArrayList<Integer> inputKeyDown = InputText.KeysDown();
		for (int i = 0; i < inputKeyDown.size(); i++) {
			inputString += Character.toChars(inputKeyDown.get(i))[0];
		}
		if (inputKeyDown.size() != 0) {
			updateFont();
		}

		// remove the last character of string
		if (InputKey.OnKeysDown(259)) {
			try {
				inputString = inputString.substring(0, inputString.length() - 1);
			} catch (StringIndexOutOfBoundsException e) {

			} finally {
				updateFont();
			}
		}

	}

	private void updateFont() {
		if (I_added) {
			font.setText(inputString + "I");
		} else {
			font.setText(inputString);
		}
	}

	@Override
	protected void Start() {
		font = this.gameObject.AddComponent(new Font("", 25));
		font.setFontSize(1);
	}

	public InputField setFontSize(float size) {
		font.setFontSize(size);
		return this;
	}

	public String getInputString() {
		return inputString;
	}

}
