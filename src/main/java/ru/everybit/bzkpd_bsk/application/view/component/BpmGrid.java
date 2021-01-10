package ru.everybit.bzkpd_bsk.application.view.component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.NumberRenderer;
import elemental.json.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BpmGrid extends Grid {
    private int frozenColumnsCount;
    private List<BpmRowSaveListener> rowSaveListenerList;
    private Map<Object, Object> sumValues;

    public BpmGrid() {
        super();
        frozenColumnsCount = 0;
        rowSaveListenerList = new ArrayList<>();
        sumValues = new HashMap<>();
    }

    public void saveEditor() throws FieldGroup.CommitException {
        super.saveEditor();
        for (BpmRowSaveListener rowSaveListener : rowSaveListenerList) {
            rowSaveListener.rowSave(getEditorFieldGroup().getItemDataSource());
        }
    }

    public void addRowSaveListener(BpmRowSaveListener rowSaveListener) {
        rowSaveListenerList.add(rowSaveListener);
    }

    public void addHiddenColumn(String propertyId, Class type) {
        addColumn(propertyId, type).setHidden(true);
        updateFrozenColumnCount();
    }

    public Column addFrozenColumn(String propertyId, String caption, int pixelWidth) {
        Column column = addGridColumn(propertyId, String.class, caption, pixelWidth);
        updateFrozenColumnCount();
        return column;
    }

    private void updateFrozenColumnCount() {
        setFrozenColumnCount(++frozenColumnsCount);
    }

    public Column addGridColumn(Object propertyId, Class type, String caption, int pixelWidth) {
        return addGridColumn(propertyId, type, caption).setWidth(pixelWidth);
    }

    public Column addGridColumn(Object propertyId, Class type, String caption) {
        return addColumn(propertyId, type).setHeaderCaption(caption).setSortable(false);
    }

    @Override
    public Column addColumn(Object propertyId, Class<?> type) {
        Column column = super.addColumn(propertyId, type);
        if (Integer.class == type || Float.class == type) {
            column.setRenderer(new NumberRenderer() {
                @Override
                public JsonValue encode(Number value) {
                    if (value != null) {
                        if (value instanceof Integer && value.intValue() == 0) {
                            value = null;
                        } else if (value instanceof Float) {
                            if (value.floatValue() == 0f) {
                                value = null;
                            } else {
                                return encode(String.format("%.2f", value.floatValue()), String.class);
                            }
                        }
                    }
                    return super.encode(value);
                }
            });
        }
        return column;
    }

    public void addFooterSum(String propertyName) {
        sumValues.put(propertyName, null);
    }

    @SuppressWarnings("unchecked")
    private <T> void addToFooterSum(Object itemId, Object propertyName, T newValue) {
        T oldValue = (T) getItemProperty(itemId, propertyName);
        T sum = (T) sumValues.get(propertyName);
        String text;
        if (newValue.getClass() == Integer.class) {
            sum = (T) (Integer) ((sum == null ? 0 : (Integer) sum) - (oldValue == null ? 0 : (Integer) oldValue) + (Integer) newValue);
            text = (Integer) sum == 0 ? "" : sum.toString();
        } else if (newValue.getClass() == Float.class) {
            sum = (T) (Float) ((sum == null ? 0f : (Float) sum) - (oldValue == null ? 0f : (Float) oldValue) + (Float) newValue);
            text = (Float) sum == 0f ? "" : String.format("%.2f", (Float) sum);
        } else {
            return;
        }
        sumValues.put(propertyName, sum);
        getFooterRow(0).getCell(propertyName).setText(text);
    }

    @SuppressWarnings("unchecked")
    private <T> void removeFromFooterSum(Object itemId, String propertyName) {
        T oldValue = (T) getItemProperty(itemId, propertyName);
        if (oldValue == null) {
            return;
        }
        T sum = (T) sumValues.get(propertyName);
        String text;
        if (oldValue.getClass() == Integer.class) {
            sum = (T) (Integer) ((sum == null ? 0 : (Integer) sum) - (Integer) oldValue);
            text = (Integer) sum == 0 ? "" : sum.toString();
        } else if (oldValue.getClass() == Float.class) {
            sum = (T) (Float) ((sum == null ? 0f : (Float) sum) - (Float) oldValue);
            text = (Float) sum == 0f ? "" : String.format("%.2f", (Float) sum);
        } else {
            return;
        }
        sumValues.put(propertyName, sum);
        getFooterRow(0).getCell(propertyName).setText(text);
    }

    public void removeAllItems() {
        getContainerDataSource().removeAllItems();
        for (Object propertyName : sumValues.keySet()) {
            sumValues.put(propertyName, null);
        }
    }

    public void addItem(Object itemId) {
        getContainerDataSource().addItem(itemId);
    }

    @SuppressWarnings("unchecked")
    public void setItemProperty(Object itemId, Object propertyName, Object value) {
        if (sumValues.containsKey(propertyName)) {
            addToFooterSum(itemId, propertyName, value);
        }
        getContainerDataSource().getItem(itemId).getItemProperty(propertyName).setValue(value);
    }

    public Object getItemProperty(Object itemId, Object propertyName) {
        Item item = getContainerDataSource().getItem(itemId);
        Property itemProperty = item.getItemProperty(propertyName);
        return itemProperty.getValue();
    }

    public void addFilter(Container.Filter filter) {
        ((IndexedContainer) getContainerDataSource()).addContainerFilter(filter);
    }

    public void removeFilter(Container.Filter filter) {
        ((IndexedContainer) getContainerDataSource()).removeContainerFilter(filter);
    }

    public void removeAllFilters() {
        ((IndexedContainer) getContainerDataSource()).removeAllContainerFilters();
    }

    public void removeItemProperty(Object itemId) {
        for (Column column : getColumns()) {
            String propertyName = (String) column.getPropertyId();
            if (sumValues.containsKey(propertyName)) {
                removeFromFooterSum(itemId, propertyName);
            }
        }
        getContainerDataSource().removeItem(itemId);
    }

    public interface BpmRowSaveListener {
        void rowSave(Item item);
    }
}
