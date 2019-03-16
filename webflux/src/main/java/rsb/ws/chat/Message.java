package rsb.ws.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
class Message {

	private final String clientId;

	private final String text;

	private final Date when;

}
