package web3.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("textInputView")
@ViewScoped
public class TextInputView implements Serializable {
    private String input = "0.0";

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
