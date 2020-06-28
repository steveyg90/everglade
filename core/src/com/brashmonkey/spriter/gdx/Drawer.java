package com.brashmonkey.spriter.gdx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brashmonkey.spriter.Entity;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.Timeline;
import com.brashmonkey.spriter.Timeline.Key.Object;

import java.util.Iterator;

public class Drawer extends com.brashmonkey.spriter.Drawer<Sprite> {
	
	SpriteBatch batch;
	ShapeRenderer renderer;
	
	public Drawer(Loader<Sprite> loader, SpriteBatch batch, ShapeRenderer renderer){
		super(loader);
		this.batch = batch;
		this.renderer = new ShapeRenderer();

	}
	
	@Override
	public void setColor(float r, float g, float b, float a) {
		renderer.setColor(r, g, b, a);
	}
	
	@Override
	public void rectangle(float x, float y, float width, float height) {
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.rect(x, y, width, height);
		renderer.end();
	}
	
	@Override
	public void line(float x1, float y1, float x2, float y2) {
		renderer.line(x1, y1, x2, y2);
	}

	@Override
	public void circle(float x, float y, float radius) {
		renderer.circle(x, y, radius);
	}

	@Override
	public void draw(Object object) {

		Sprite sprite = loader.get(object.ref);
		float newPivotX = (sprite.getWidth() * object.pivot.x);
		float newX = object.position.x - newPivotX;
		float newPivotY = (sprite.getHeight() * object.pivot.y);
		float newY = object.position.y - newPivotY;
		
		sprite.setX(newX);
		sprite.setY(newY);
		
		sprite.setOrigin(newPivotX, newPivotY);
		sprite.setRotation(object.angle);
		
		sprite.setColor(1f, 1f, 1f, object.alpha);
		sprite.setScale(object.scale.x, object.scale.y);
		sprite.draw(batch);
	}

	public void draw(Iterator it, Entity.CharacterMap[] maps) {
		while (it.hasNext()) {
			Timeline.Key.Object object = (Object) it.next();
			if (object.ref.hasFile()) {
				// apply map
				if (maps != null) {
					for (Entity.CharacterMap map : maps) {
						// key exists
						if (map != null && map.containsKey(object.ref)) {
							// draw if not "hidden"
							if (map.get(object.ref) != null) {
								object.ref.set(map.get(object.ref));
								this.draw(object);
							}
							break;
						} else {
							this.draw(object);
						}
					}
				} else {
					this.draw(object);
				}

			}
		}
	}
}
