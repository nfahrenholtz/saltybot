package saltybot.api.common;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class ZData {

    public static ZData create(final JSONObject object) {
        final Map<String, Data> data;
        final State state = new State(object.getString("p1name"),
                                      object.getString("p1total"),
                                      object.getString("p2name"),
                                      object.getString("p2total"),
                                      object.getString("remaining"),
                                      object.getString("status"),
                                      object.getString("alert"),
                                      object.getInt("x"));

        data = object.keySet().stream().filter((k) -> {
            return object.get(k) instanceof JSONObject;
        }).collect(Collectors.toMap(k -> k, k -> {
            final JSONObject value = object.getJSONObject(k);
            return Data.create(value);
        }));

        return new ZData(state, data);
    }

    private final State state;
    private final Map<String, Data> dataMap;

    public ZData(final State state,
                 final Map<String, Data> dataMap) {
        this.state = state;
        this.dataMap = Collections.unmodifiableMap(dataMap);
    }

    public State getState() {
        return state;
    }

    public Map<String, Data> getDataMap() {
        return dataMap;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final Map.Entry<String, Data> entries: dataMap.entrySet()) {
            builder.append(builder.length()>0 ? ", " : "");
            builder.append(entries.getKey());
            builder.append("=");
            builder.append(entries.getValue());
        }
        return "ZData{" +
                "state=" + state +
                ", dataMap=Map{" + builder.toString() + "}" +
                '}';
    }
}
