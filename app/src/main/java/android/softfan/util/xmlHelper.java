package android.softfan.util;

import android.util.Base64;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class xmlHelper {

	public static Node findFirstElementByName(Node tagParent, String tagName) {
		if (tagParent == null)
			return null;
		NodeList childNodes = tagParent.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node child = childNodes.item(i);
				if (child.getNodeName().equals(tagName)) {
					return child;
				}
			}
		}
		return null;
	}

	public static List<Node> filterElementByName(Node tagParent, String tagName) {
		if (tagParent == null)
			return null;
		ArrayList<Node> nodes = null;
		NodeList childNodes = tagParent.getChildNodes();
		if (childNodes != null) {
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node child = childNodes.item(i);
				if (child.getNodeName().equals(tagName)) {
					if (nodes == null) {
						nodes = new ArrayList<Node>();
					}
					nodes.add(child);
				}
			}
		}
		if (nodes == null) {
			return java.util.Collections.emptyList();
		}
		return nodes;
	}

	public static void setAttributeValue(XmlSerializer tag, String atrrName, String value) throws SoftFanUtilException {
		try {
			tag.attribute(null, atrrName, value);
		} catch (Exception e) {
			throw new SoftFanUtilException(e);
		}
	}

	public static String getAttributeValue(Node tag, String atrrName) {
		Node item = tag.getAttributes().getNamedItem(atrrName);
		if (item == null) {
			return null;
		}
		return item.getTextContent();
	}

	public static String readTextTag(Node tagParent, String tagName) {
		Node element = findFirstElementByName(tagParent, tagName);
		if (element != null) {
			return element.getTextContent();
		}
		return null;
	}

	public static void writeTextTag(XmlSerializer tagParent, String tagName, String value) throws SoftFanUtilException {
		try {
			XmlSerializer tag = tagParent.startTag(null, tagName);
			if (value != null) {
				tag.text(value);
			}
			tagParent.endTag(null, tagName);
		} catch (Exception e) {
			throw new SoftFanUtilException(e);
		}
	}

	public static XmlSerializer writeTextTagEx(XmlSerializer tagParent, String tagName, String value) throws SoftFanUtilException {
		try {
			XmlSerializer tag = tagParent.startTag(null, tagName);
			if (value != null) {
				tag.text(value);
			}
			tagParent.endTag(null, tagName);
			return tag;
		} catch (Exception e) {
			throw new SoftFanUtilException(e);
		}
	}

	public static Object readValueTag(Node tagParent, String tagName, Class<?> typeClass) throws SoftFanUtilException {
		Node element = findFirstElementByName(tagParent, tagName);
		if (element != null) {
			try {
				String v = element.getTextContent();
				if (typeClass == String.class) {
					return v;
				} else if (typeClass == Boolean.class) {
					return "true".equals(v);
				} else if (typeClass == Double.class) {
					return new Double(Double.parseDouble(v));
				} else if (typeClass == Float.class) {
					return new Float(Float.parseFloat(v));
				} else if (typeClass == Integer.class) {
					return new Integer(Integer.parseInt(v));
				} else if (typeClass == Long.class) {
					return new Long(Long.parseLong(v));
				} else if (typeClass == Short.class) {
					return new Short(Short.parseShort(v));
				} else if (typeClass == Byte.class) {
					return new Byte(Byte.parseByte(v));
				} else if (typeClass == Date.class) {
					return DateUnit.toDate(v);
				} else if (typeClass == byte[].class) {
					return Base64.decode(v.getBytes(), Base64.DEFAULT);
				} else {
					throw new SoftFanUtilException("xmlHelper遇到无法处理的数据类型(" + typeClass.getSimpleName() + ")");
				}
			} catch (SoftFanUtilException e) {
				throw e;
			} catch (Exception e) {
				throw new SoftFanUtilException(e);
			}
		}
		return null;
	}

	public static Object readValueTagHasType(Node tagParent, String tagName) throws SoftFanUtilException {
		Node element = findFirstElementByName(tagParent, tagName);
		if (element != null) {
			readValueHasType(element);
		}
		return null;
	}

	public static Object readValueHasType(Node element) throws SoftFanUtilException {
		try {
			String typeClass = getAttributeValue(element, "type");
			if (!textUnit.StringIsEmpty(typeClass)) {
				if ("HashMap".equals(typeClass)) {
					HashMap<String, Object> v = new HashMap<String, Object>();
					NodeList childNodes = element.getChildNodes();
					if (childNodes != null) {
						for (int i = 0; i < childNodes.getLength(); i++) {
							Node item = childNodes.item(i);
							Object itemValue = readValueHasType(item);
							v.put(item.getNodeName(), itemValue);
						}
					}
					return v;
				} else if ("List".equals(typeClass)) {
					List<Object> v = new ArrayList<Object>();
					NodeList childNodes = element.getChildNodes();
					if (childNodes != null) {
						for (int i = 0; i < childNodes.getLength(); i++) {
							Node item = childNodes.item(i);
							Object itemValue = readValueHasType(item);
							if (itemValue != null) {
								v.add(itemValue);
							}
						}
					}
					return v;
				} else {
					String value = element.getTextContent();
					if ("String".equals(typeClass)) {
						return value;
					} else if ("Boolean".equals(typeClass)) {
						return "true".equals(value);
					} else if ("Double".equals(typeClass)) {
						return new Double(Double.parseDouble(value));
					} else if ("Float".equals(typeClass)) {
						return new Float(Float.parseFloat(value));
					} else if ("Integer".equals(typeClass)) {
						return new Integer(Integer.parseInt(value));
					} else if ("Long".equals(typeClass)) {
						return new Long(Long.parseLong(value));
					} else if ("Short".equals(typeClass)) {
						return new Short(Short.parseShort(value));
					} else if ("Byte".equals(typeClass)) {
						return new Byte(Byte.parseByte(value));
					} else if ("Date".equals(typeClass)) {
						return DateUnit.toDate(value);
					} else if ("byte[]".equals(typeClass)) {
						return Base64.encode(value.getBytes(), Base64.DEFAULT);
					} else {
						throw new SoftFanUtilException("xmlHelper遇到无法处理的数据类型(" + typeClass + ")");
					}
				}
			} else {
				throw new SoftFanUtilException("xmlHelper遇到无法处理的数据类型(空)");
			}
		} catch (SoftFanUtilException e) {
			throw e;
		} catch (Exception e) {
			throw new SoftFanUtilException(e);
		}
	}

	public static void writeValueTag(XmlSerializer tagParent, String tagName, Object v) throws SoftFanUtilException {
		try {
			if (v != null) {
				XmlSerializer element = tagParent.startTag(null, tagName);
				String text = null;
				if (v instanceof String) {
					text = v.toString();
				} else if (v instanceof Boolean) {
					if (((Boolean) v).booleanValue())
						text = "true";
					else
						text = "false";
				} else if (v instanceof Double) {
					text = v.toString();
				} else if (v instanceof Float) {
					text = v.toString();
				} else if (v instanceof Integer) {
					text = v.toString();
				} else if (v instanceof Long) {
					text = v.toString();
				} else if (v instanceof Short) {
					text = v.toString();
				} else if (v instanceof Byte) {
					text = v.toString();
				} else if (v instanceof Date) {
					text = DateUnit.toText((Date) v);
				} else if (v instanceof byte[]) {
					text = new String(Base64.encode((byte[]) v, Base64.DEFAULT));
				} else {
					throw new SoftFanUtilException("xmlHelper遇到无法处理的数据类型(" + v.getClass().getSimpleName() + ")");
				}
				if (text != null) {
					element.text(text);
				}
				tagParent.endTag(null, tagName);
			}
		} catch (SoftFanUtilException e) {
			throw e;
		} catch (Exception e) {
			throw new SoftFanUtilException(e);
		}
	}

	public static void writeValueTagHasType(XmlSerializer tagParent, String tagName, Object v) throws SoftFanUtilException {
		try {
			if (v != null) {
				XmlSerializer element = tagParent.startTag(null, tagName);
				writeValueHasType(element, v);
				tagParent.endTag(null, tagName);
			}
		} catch (SoftFanUtilException e) {
			throw e;
		} catch (Exception e) {
			throw new SoftFanUtilException(e);
		}
	}

	public static void writeValueHasType(XmlSerializer tag, Object v) throws SoftFanUtilException {
		try {
			if (v != null) {
				String text = null;
				if (v instanceof String) {
					setAttributeValue(tag, "type", "String");
					text = v.toString();
				} else if (v instanceof Boolean) {
					setAttributeValue(tag, "type", "Boolean");
					if (((Boolean) v).booleanValue())
						text = "true";
					else
						text = "false";
				} else if (v instanceof Double) {
					setAttributeValue(tag, "type", "Double");
					text = v.toString();
				} else if (v instanceof Float) {
					setAttributeValue(tag, "type", "Float");
					text = v.toString();
				} else if (v instanceof Integer) {
					setAttributeValue(tag, "type", "Integer");
					text = v.toString();
				} else if (v instanceof Long) {
					setAttributeValue(tag, "type", "Long");
					text = v.toString();
				} else if (v instanceof Short) {
					setAttributeValue(tag, "type", "Short");
					text = v.toString();
				} else if (v instanceof Byte) {
					setAttributeValue(tag, "type", "Byte");
					text = v.toString();
				} else if (v instanceof Date) {
					setAttributeValue(tag, "type", "Date");
					text = DateUnit.toText((Date) v);
				} else if (v instanceof byte[]) {
					setAttributeValue(tag, "type", "byte[]");
					text = new String(Base64.encode((byte[]) v, Base64.DEFAULT));
				} else {
					throw new SoftFanUtilException("xmlHelper遇到无法处理的数据类型(" + v.getClass().getSimpleName() + ")");
				}
				if (text != null) {
					tag.text(text);
				}
			}
		} catch (SoftFanUtilException e) {
			throw e;
		} catch (Exception e) {
			throw new SoftFanUtilException(e);
		}
	}
}
