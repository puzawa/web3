package web3.view;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.*;

@Named("checkboxView")
@ViewScoped
public class CheckboxView implements Serializable {

    private List<String> options = List.of("1", "2", "3", "4", "5");
    private Map<String, Boolean> selectedOptions = new HashMap<>();

    public Map<String, Boolean> getSelectedOptions() {
        return selectedOptions;
    }
    public void setSelectedOptions(Map<String, Boolean> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }
    public String getSelectedOption() {
        return selectedOptions.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
