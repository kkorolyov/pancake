package dev.kkorolyov.pancake.audio.al

import dev.kkorolyov.pancake.audio.al.internal.alCall
import dev.kkorolyov.pancake.platform.math.Matrix4
import dev.kkorolyov.pancake.platform.math.Vector3
import dev.kkorolyov.pancake.platform.math.Vectors
import org.lwjgl.openal.AL11.*
import org.lwjgl.system.MemoryStack

/**
 * Represents the `OpenAL` listener that receives source playback in the current context.
 */
object AudioListener {
	/**
	 * Listener position.
	 */
	var position: Vector3
		get() = alCall {
			MemoryStack.stackPush().use {
				val pP = it.mallocFloat(3)
				alGetListenerfv(AL_POSITION, pP)
				Vectors.create(pP[0].toDouble(), pP[1].toDouble(), pP[2].toDouble())
			}
		}
		set(value) = alCall { alListener3f(AL_POSITION, value.x.toFloat(), value.y.toFloat(), value.z.toFloat()) }

	/**
	 * Listener velocity.
	 */
	var velocity: Vector3
		get() = alCall {
			MemoryStack.stackPush().use {
				val vP = it.mallocFloat(3)
				alGetListenerfv(AL_VELOCITY, vP)
				Vectors.create(vP[0].toDouble(), vP[1].toDouble(), vP[2].toDouble())
			}
		}
		set(value) = alCall { alListener3f(AL_VELOCITY, value.x.toFloat(), value.y.toFloat(), value.z.toFloat()) }

	fun setOrientation(orientation: Matrix4) {
		alCall {
			MemoryStack.stackPush().use {
				val oP = it.mallocFloat(6)
				// TODO use orientation to rotate
				oP
					.put(0F)
					.put(0F)
					.put(-1F)
					.put(0F)
					.put(1F)
					.put(0F)
				oP.flip()

				alListenerfv(AL_ORIENTATION, oP)
			}
		}
	}
}
