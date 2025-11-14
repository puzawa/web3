package web3.view;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Named("checkboxView")
@SessionScoped
public class CheckboxView implements Serializable {

    private List<String> options = List.of("1", "2", "3", "4", "5");
    private Map<String, Boolean> selectedOptions = new HashMap<>();

    public Map<String, Boolean> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(Map<String, Boolean> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public ArrayList<BigDecimal> getEnabledR() {
        ArrayList<BigDecimal> r = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : selectedOptions.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue()))
                r.add(new BigDecimal(entry.getKey()).stripTrailingZeros());
        }
        r.sort(Collections.reverseOrder());
        return r;
    }
}
