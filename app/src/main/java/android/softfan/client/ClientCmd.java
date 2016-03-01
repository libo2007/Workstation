package android.softfan.client;

import android.softfan.util.SoftFanUtilException;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

public abstract class ClientCmd {
	private String	operator;

	public ClientCmd() {
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCommand() throws ClientException {
		try {
			StringWriter out_buffer = new StringWriter();
			try {
				XmlSerializer xmlsenser = Xml.newSerializer();
				xmlsenser.setOutput(out_buffer);

				xmlsenser.startDocument("UTF-8", true);

				XmlSerializer cmdroot = xmlsenser.startTag(null, "Commands");
				cmdroot.attribute(null, "Account", ClientCmdDo.getSESSIONSERVERUSER());
				cmdroot.attribute(null, "Password", ClientCmdDo.getSESSIONSERVERPASSWORD());

				XmlSerializer root = cmdroot.startTag(null, "Command");
				root.attribute(null, "Operator", getOperator());

				BuildParameter(root);

				cmdroot.endTag(null, "Command");

				xmlsenser.endTag(null, "Commands");

				xmlsenser.endDocument();

				return out_buffer.getBuffer().toString();
			} finally {
				out_buffer.close();
			}
		} catch (Exception e) {
			throw new ClientException(e.getMessage());
		}
	}

	public void getCommand(XmlSerializer cmdroot) throws SoftFanUtilException {
		try {
			XmlSerializer root = cmdroot.startTag(null, "Command");
			root.attribute(null, "Operator", getOperator());

			BuildParameter(root);

			cmdroot.endTag(null, "Command");
		} catch (Exception e) {
			throw new SoftFanUtilException(e.getMessage());
		}
	}

	public abstract void BuildParameter(XmlSerializer content) throws SoftFanUtilException;

}