package abacus.project.ietf.flexiGrid.ted;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by BMA on 3/8/2017.
 */
public enum ReadyState {
    NA, LoPw, RDY, TRS;


    private final String jsonName;

    private ReadyState(){
        this.jsonName = this.name();
    }

    private ReadyState(String jsonName) {
        this.jsonName = jsonName;
    }

    @JsonCreator
    public static ReadyState fromJsonString(String jsonString) {
        for (ReadyState value : ReadyState.values())
            if (value.jsonName.equals(jsonString)) {
                return value;
            }
        return null;
    }

    @JsonValue
    public String toJsonString() {
        return this.jsonName;
    }
}
