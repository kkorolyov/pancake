package dev.kkorolyov.pancake.skillet.serialization;

import dev.kkorolyov.pancake.platform.serialization.string.StringSerializer;
import dev.kkorolyov.pancake.skillet.model.Workspace;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WorkspaceStringSerializer extends StringSerializer<Workspace> {
	private static final String SPLIT_PATTERN = ",\\s*(?=\\w+\\s*\\[)";
	private static final GenericEntitySerializer entitySerializer = new GenericEntitySerializer();

	/**
	 * Constructs a new workspace string serializer.
	 */
	public WorkspaceStringSerializer() {
		super(entitySerializer.pattern() + "(,\\s*" + entitySerializer.pattern() + ")*");
	}

	@Override
	public Workspace read(String out) {
		Workspace workspace = new Workspace();

		Arrays.stream(out.split(SPLIT_PATTERN))
				.flatMap(entitySerializer::matches)	// Split beforehand because matches() is greedy
				.forEach(workspace::addEntity);

		return workspace;
	}
	@Override
	public String write(Workspace in) {
		return in.streamEntities()
				.map(entitySerializer::write)
				.collect(Collectors.joining("," + System.lineSeparator()));
	}
}
