package dev.kkorolyov.pancake.platform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * Provides access to instances of singleton services discovered at runtime.
 */
public final class Services {
	private static final Logger LOG = LoggerFactory.getLogger(Services.class);

	private static final ThreadLocal<Application> APPLICATION = ThreadLocal.withInitial(() ->
			{
				Application application = ServiceLoader.load(Application.class).findFirst().orElseThrow(() -> new IllegalStateException("No " + Application.class.getName() + " provider found"));
				LOG.info("loaded Application: {}", application);
				return application;
			}
	);
	private static final ThreadLocal<RenderMedium> RENDER_MEDIUM = ThreadLocal.withInitial(() ->
			{
				RenderMedium renderMedium = ServiceLoader.load(RenderMedium.class).findFirst().orElseThrow(() -> new IllegalStateException("No " + RenderMedium.class.getName() + " provider found"));
				LOG.info("loaded RenderMedium: {}", renderMedium);
				return renderMedium;
			}
	);
	private static final ThreadLocal<AudioFactory> AUDIO_FACTORY = ThreadLocal.withInitial(() ->
			{
				AudioFactory audioFactory = ServiceLoader.load(AudioFactory.class).findFirst().orElseThrow(() -> new IllegalStateException("No " + AudioFactory.class.getName() + " provider found"));
				LOG.info("loaded AudioFactory: {}", audioFactory);
				return audioFactory;
			}
	);

	/**
	 * @return {@code Application} bound to the current thread.
	 * @throws IllegalStateException if no {@code Application} provider exists
	 */
	public static Application application() {
		return APPLICATION.get();
	}
	/**
	 * @return {@code RenderMedium} bound to the current thread.
	 * @throws IllegalStateException if no {@code RenderMedium} provider exists
	 */
	public static RenderMedium renderMedium() {
		return RENDER_MEDIUM.get();
	}
	/**
	 * @return {@code AudioFactory} bound to the current thread.
	 * @throws IllegalStateException if no {@code AudioFactory} provider exists
	 */
	public static AudioFactory audioFactory() {
		return AUDIO_FACTORY.get();
	}
}
