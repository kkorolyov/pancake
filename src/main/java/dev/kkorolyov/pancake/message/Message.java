package dev.kkorolyov.pancake.message;

/**
 * Describes an event which occurred to some entity. Used for inter-component communication.
 */
public class Message {
	@SuppressWarnings("javadoc")
	public static final int ENTITY_VEL_CHANGE = 0;
	
	private final int entityId,
										messageCode;
	private final Object payload;
	
	/**
	 * Constructs a new message.
	 * @param entityId ID of affected entity
	 * @param messageCode general message description
	 * @param payload additional, detailed info, if applicable
	 */
	public Message(int entityId, int messageCode, Object payload) {
		this.entityId = entityId;
		this.messageCode = messageCode;
		this.payload = payload;
	}
	
	/** @return ID of affected entity */
	public int getEntityId() {
		return entityId;
	}
	
	/** @return general message description */
	public int getMessageCode() {
		return messageCode;
	}
	
	/** @return additional message info as instance of {@code T}, or {@code null} if this message contains no additional info of type {@code T} */
	public <T> T getPayload(Class<T> payloadType) {
		return (payload == null || payload.getClass() != payloadType) ? null : payloadType.cast(payload);
	}
}
