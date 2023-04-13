package me.deftware.client.framework.render;

import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;

import java.util.List;

/**
 * @author Deftware
 */
public interface WorldEntityRenderer {

	List<Statue> getStatues();

	class Statue {

		protected Entity entity;
		protected Vector3<Double> position;

		public Statue(Entity entity, Vector3<Double> position) {
			this.entity = entity;
			this.position = position;
		}

		public Entity getEntity() {
			return entity;
		}

		public Vector3<Double> getPosition() {
			return position;
		}

	}

}
