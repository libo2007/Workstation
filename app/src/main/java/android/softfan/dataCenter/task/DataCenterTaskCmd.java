package android.softfan.dataCenter.task;

import android.softfan.client.ClientException;
import android.softfan.dataCenter.DataCenterException;
import android.softfan.util.xmlHelper;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DataCenterTaskCmd implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    //属性数据
    private String seq;
    private String cmd;
    private String param;
    private HashMap<String, Object> values;

    private long timeout;

    //运行数据
    private boolean hasResponse;

    private IDataCenterNotify selfNotify;

    private long createTime;
    private long runTime;

    private int level;

    private HashMap<String, Object> properties = new HashMap<String, Object>();

    private boolean responsed;

    private boolean concurrent;

    public DataCenterTaskCmd() {
        super();
        createTime = System.currentTimeMillis();
        level = 5;
    }

    public void freeData() {
        if (this.selfNotify != null) {
            this.selfNotify.onFree(this);
            this.selfNotify = null;
        }
        this.seq = null;
        this.cmd = null;
        this.param = null;
        this.values = null;

        this.properties = null;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public void addProperty(String key, Object value) {
        synchronized (properties) {
            properties.put(key, value);
        }
    }

    public void removeProperty(String key) {
        synchronized (properties) {
            properties.remove(key);
        }
    }

    public Object getProperty(String key) {
        synchronized (properties) {
            return properties.get(key);
        }
    }

    public byte[] buildCommand() throws DataCenterTaskException {
        try {
            byte[] byteArray = null;

            HashMap<String, Object> datParams = new HashMap<String, Object>();
            datParams.put("seq", seq);
            datParams.put("cmd", cmd);
            datParams.put("level", level);
            if (timeout > 0) {
                datParams.put("timeout", timeout);
            }
            if (param != null) {
                datParams.put("param", param);
            }
            if (values != null) {
                datParams.put("values", values);
            }
            if (hasResponse) {
                datParams.put("hasResponse", "true");
            }
            if (concurrent) {
                datParams.put("concurrent", "true");
            }
            ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
            try {
                ObjectOutputStream mywriter = new ObjectOutputStream(byte_out);
                try {
                    mywriter.writeObject(datParams);
                    byteArray = byte_out.toByteArray();
                } finally {
                    mywriter.close();
                }
            } finally {
                byte_out.close();
            }
            return byteArray;
        } catch (Exception e) {
            throw new DataCenterTaskException(e.getMessage());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void extractCommand(byte[] data) throws DataCenterTaskException {
        try {
            Object s_obj;
            ByteArrayInputStream byte_in = new ByteArrayInputStream(data, 0, data.length);
            try {
                ObjectInputStream myreader = new ObjectInputStream(byte_in);
                try {
                    s_obj = myreader.readObject();
                } finally {
                    myreader.close();
                }
            } finally {
                byte_in.close();
            }
            if (!(s_obj instanceof HashMap)) {
                throw new DataCenterTaskException("接口数据格式错误");
            }
            seq = (String) ((HashMap) s_obj).get("seq");
            cmd = (String) ((HashMap) s_obj).get("cmd");
            level = (Integer) ((HashMap) s_obj).get("level");
            Long to = (Long) ((HashMap) s_obj).get("timeout");
            if (to != null) {
                timeout = to;
            }
            param = (String) ((HashMap) s_obj).get("param");
            values = (HashMap) ((HashMap) s_obj).get("values");
            hasResponse = "true".equals(((HashMap) s_obj).get("hasResponse"));
            concurrent = "true".equals(((HashMap) s_obj).get("concurrent"));
        } catch (DataCenterTaskException e) {
            throw e;
        } catch (Exception e) {
            throw new DataCenterTaskException(e.getMessage());
        }
    }

    public byte[] buildXmlCommand() throws DataCenterTaskException {
        try {
            StringWriter out_buffer = new StringWriter();
            try {
                XmlSerializer xmlsenser = Xml.newSerializer();
                xmlsenser.setOutput(out_buffer);
                xmlsenser.startDocument("UTF-8", true);

                XmlSerializer content = xmlsenser.startTag(null, "Msg");

                xmlHelper.writeValueTagHasType(content, "seq", seq);
                xmlHelper.writeValueTagHasType(content, "cmd", cmd);
                xmlHelper.writeValueTagHasType(content, "level", level);

                if (timeout > 0) {
                    xmlHelper.writeValueTagHasType(content, "timeout", timeout);
                }
                if (param != null) {
                    xmlHelper.writeValueTagHasType(content, "param", param);
                }
                if (values != null) {
                    xmlHelper.writeValueTagHasType(content, "values", values);
                }
                if (hasResponse) {
                    xmlHelper.writeValueTagHasType(content, "hasResponse", hasResponse);
                }
                if (concurrent) {
                    xmlHelper.writeValueTagHasType(content, "concurrent", concurrent);
                }

                xmlsenser.endTag(null, "Msg");

                xmlsenser.endDocument();

                return out_buffer.getBuffer().toString().getBytes();
            } finally {
                out_buffer.close();
            }
        } catch (Exception e) {
            throw new DataCenterTaskException(e.getMessage());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void extractXmlCommand(byte[] data) throws DataCenterTaskException {
        try {
            StringReader cmd_in_buffer = new StringReader(new String(data));
            try {
                InputSource is = new InputSource(cmd_in_buffer);

                DocumentBuilderFactory docBFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBFactory.newDocumentBuilder();

                Document doc = docBuilder.parse(is);

                Element content = doc.getDocumentElement();

                if (!content.getTagName().equals("Msg"))
                    throw new ClientException("信息格式错误");

                seq = (String) xmlHelper.readValueTagHasType(content, "seq");
                cmd = (String) xmlHelper.readValueTagHasType(content, "cmd");
                Object v = xmlHelper.readValueTagHasType(content, "level");
                if (v instanceof Integer) {
                    level = (Integer) v;
                }
                v = xmlHelper.readValueTagHasType(content, "timeout");
                if (v instanceof Long) {
                    timeout = (Long) v;
                }
                param = (String) xmlHelper.readValueTagHasType(content, "param");
                values = (HashMap) xmlHelper.readValueTagHasType(content, "values");
                v = xmlHelper.readValueTagHasType(content, "hasResponse");
                if (v instanceof Boolean) {
                    hasResponse = (Boolean) v;
                }
                v = xmlHelper.readValueTagHasType(content, "concurrent");
                if (v instanceof Boolean) {
                    concurrent = (Boolean) v;
                }
            } finally {
                cmd_in_buffer.close();
            }
        } catch (Exception e) {
            throw new DataCenterTaskException(e.getMessage());
        }
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Object getValue(String key) {
        if (values != null) {
            return values.get(key);
        }
        return null;
    }

    public HashMap<String, Object> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Object> values) {
        this.values = values;
    }

    public boolean isHasResponse() {
        return hasResponse;
    }

    public void setHasResponse(boolean hasResponse) {
        this.hasResponse = hasResponse;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public IDataCenterNotify getSelfNotify() {
        return selfNotify;
    }

    public void setSelfNotify(IDataCenterNotify selfNotify) {
        this.selfNotify = selfNotify;
    }

    public void onSend() throws DataCenterException {
        if (this.selfNotify != null) {
            this.selfNotify.onSend(this);
        }
    }

    public void onResponse(DataCenterTaskCmd cmd) throws DataCenterException {
        if (this.selfNotify != null) {
            this.selfNotify.onResponse(this, cmd);
        }
    }

    public void onTimeout() {
        if (this.selfNotify != null) {
            this.selfNotify.onTimeout(this);
        }
    }

    public boolean isDoResponse() {
        return hasResponse;
    }

    public boolean isCanStorage() {
        return false;
    }

    public boolean isResponsed() {
        return responsed;
    }

    public void markResponse() {
        responsed = true;
    }

}
