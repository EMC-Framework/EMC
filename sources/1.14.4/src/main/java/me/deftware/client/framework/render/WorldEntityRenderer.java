package me.deftware.client.framework.render;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.deftware.client.framework.math.Vector3;
import me.deftware.client.framework.entity.Entity;

import java.util.List;

/**
 * @author Deftware
 */
public interface WorldEntityRenderer {

	List<Statue> getStatues();

	@Getter
	@AllArgsConstructor
	class Statue {

		protected Entity entity;
		protected Vector3<Double> position;

	}

}
