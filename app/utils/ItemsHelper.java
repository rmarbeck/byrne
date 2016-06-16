package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import play.data.Form;
import models.Invoice;
import models.InvoiceItem;

public class ItemsHelper {
	public static final String ITEMS_FIELD_NAME = "items";
	public static final String ITEMS_NAME_FIELD_NAME = "name";
	public static final String ITEMS_COST_FIELD_NAME = "cost";
	public static final String ITEMS_NO_VAT_FIELD_NAME = "noVat";
	
	public static List<InvoiceItem> getItems(Form<Invoice> form) {
		if (form.hasErrors()) {
			return extractInfoFromFormInError(form);
		} else {
			return form.get().items;
		}
	}
	
	private static List<InvoiceItem> extractInfoFromFormInError(Form<Invoice> form) {
		List<InvoiceItem> items = new ArrayList<InvoiceItem>();
		Map<String, String> datas = form.data();
		int index = 0;
		while(true) {
			if(datas.containsKey(getKeyNameByIndexForName(index))) {
				items.add(new InvoiceItem(	getValueByIndexForName(datas, index),
											getValueByIndexForCost(datas, index),
											getValueByIndexForNoVat(datas, index)));
			} else {
				break;
			}
			index++;
		}
		return items;
	}
	
	private static String getValueByIndexForName(Map<String, String> datas, int index) {
		return datas.get(getKeyNameByIndexForName(index));
	}
	
	private static Float getValueByIndexForCost(Map<String, String> datas, int index) {
		String rawValue = datas.get(getKeyNameByIndexForCost(index));
		if (rawValue != null && !"".equals(rawValue))
			return Float.parseFloat(rawValue);
		return null;
	}
	
	private static boolean getValueByIndexForNoVat(Map<String, String> datas, int index) {
		String rawValue = datas.get(getKeyNameByIndexForNoVat(index));
		if (rawValue != null && !"".equals(rawValue))
			return Boolean.getBoolean(rawValue);
		return false;
	}
	
	private static String getKeyNameByIndexForName(int index) {
		return getKeyNameByForGivenValue(index,ITEMS_NAME_FIELD_NAME);
	}
	
	private static String getKeyNameByIndexForCost(int index) {
		return getKeyNameByForGivenValue(index,ITEMS_COST_FIELD_NAME);
	}
	
	private static String getKeyNameByIndexForNoVat(int index) {
		return getKeyNameByForGivenValue(index,ITEMS_NO_VAT_FIELD_NAME);
	}
	
	private static String getKeyNameByForGivenValue(int index, String valueName) {
		return getKeyNameByIndex(index)+"."+valueName;
	}
	
	private static String getKeyNameByIndex(int index) {
		return ITEMS_FIELD_NAME+"["+index+"]";
	}

}
