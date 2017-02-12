package de.htwsaar.wirth.server.dao;

/**
 * Klasse stellt einen Abfrage-Parameter für die Datenbank dar mit Spaltennamen und Wert
 * Created by olli on 12.02.17.
 */
public class DatabaseQueryParameter {
    private String key;
    private Object value;

    public DatabaseQueryParameter(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
