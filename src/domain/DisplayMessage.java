package domain;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import util.JsonUtil;

public class DisplayMessage implements Serializable {

	private static final long serialVersionUID = 5980841798389504554L;

	@SerializedName("type")
	private final EDisplayMessageType messageType;
	@SerializedName("text")
    private final String messageText;

    public DisplayMessage(final EDisplayMessageType messageType, final String messageText) {
        this.messageType = messageType;
        this.messageText = messageText;
    }

    public final EDisplayMessageType getMessageType() {
        return this.messageType;
    }

    public final String getMessageText() {
        return this.messageText;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JsonUtil.toJsonNoHtmlEscaping(this);
	}
}
