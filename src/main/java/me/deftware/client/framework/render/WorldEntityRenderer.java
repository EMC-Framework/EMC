package me.deftware.client.framework.render;

import me.deftware.client.framework.entity.Entity;
import me.deftware.client.framework.math.vector.Vector3d;

import java.util.List;

/**
 * @author Deftware
 */
public interface WorldEntityRenderer {

	List<Statue> getStatues();

	class Statue {

		protected Entity entity;
		protected Vector3d position;

		public Statue(Entity entity, Vector3d position) {
			this.entity = entity;
			this.position = position;
		}

		public Entity getEntity() {
			return entity;
		}

		public Vector3d getPosition() {
			return position;
		}

	}

}
