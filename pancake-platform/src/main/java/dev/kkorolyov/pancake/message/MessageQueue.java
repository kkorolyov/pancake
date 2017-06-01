package dev.kkorolyov.pancake.message;

import java.util.*;

/**
 * Maintains a queue of filtered messages.
 */
public class MessageQueue {
	private final Map<Integer, Set<Message>> messages = new HashMap<>();
	
	/**
	 * Enqueues a message.
	 * @param message message to enqueue
	 */
	public void enqueue(Message message) {
		int entityId = message.getEntityId();
		Set<Message> entityMessages = messages.get(entityId);

		if (entityMessages == null) {
			entityMessages = new LinkedHashSet<>();	// Retain order
			messages.put(entityId, entityMessages);
		}
		entityMessages.add(message);
	}
	
	/**
	 * Retrieves and dequeues the first instance of a message of a particular code for an entity, if it exists.
	 * @param entityId ID of entity which message describes
	 * @param messageCode filter code
	 * @return first applicable message, or {@code null} if no such messages
	 */
	public Message get(int entityId, int messageCode) {
		Set<Message> entityMessages = messages.get(entityId);
		if (entityMessages != null) {
			Iterator<Message> it = entityMessages.iterator();
			
			while (it.hasNext()) {
				Message m = it.next();
				
				if (m.getMessageCode() == messageCode) {
					it.remove();
					return m;
				}
			}
		}
		return null;
	}
	
	/**
	 * Clears all messages for all entities.
	 */
	public void clear() {
		for (Set<Message> entityMessages : messages.values())
			entityMessages.clear();
	}
	/**
	 * Clears all messages about a particular entity.
	 * @param entityId ID of entity about which messages to clear
	 */
	public void clear(int entityId) {
		Set<Message> entityMessages = messages.get(entityId);
		if (entityMessages != null)
			entityMessages.clear();
	}
	
	/**
	 * Removes the bucket of messages for a particular entity.
	 * @param entityId ID of entity owning bucket
	 * @return {@code true} if such a bucket existed and was removed
	 */
	public boolean destroy(int entityId) {
		return messages.remove(entityId) != null;
	}
}
