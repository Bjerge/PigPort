package com.bj.pigport.entities.map;

import static com.bj.pigport.handlers.B2DVars.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.bj.pigport.handlers.B2DVars;
import com.bj.pigport.states.Play;

public class MapCreator {
	
	public static TiledMap createPlayerSpawn(TiledMap tileMap, String string){
		
		try {
			tileMap = new TmxMapLoader().load("res/maps/"+string+".tmx");
		}
		catch(Exception e) {
			System.out.println("Cannot find file: res/maps/"+string+".tmx");
			Gdx.app.exit();
		}
		MapLayer ml = tileMap.getLayers().get("playerSpawn");
		for(MapObject mo : ml.getObjects()){
			BodyDef cdef = new BodyDef();
			cdef.type = BodyType.StaticBody;
			if(mo instanceof RectangleMapObject){
				Play.player.data.playerSpawnX =  ((RectangleMapObject) mo).getRectangle().x / PPM;
				Play.player.data.playerSpawnY =  ((RectangleMapObject) mo).getRectangle().y / PPM;

			}
			else if(mo instanceof EllipseMapObject){
				Play.player.data.playerSpawnX = ((EllipseMapObject) mo).getEllipse().x / PPM;
				Play.player.data.playerSpawnY = ((EllipseMapObject) mo).getEllipse().y / PPM;
			}
		}
		Play.player.getBody().setTransform( Play.player.data.playerSpawnX, Play.player.data.playerSpawnY, 0);
		
		return tileMap;
	}
	
	
	public static void createPlayerEnd(TiledMap tileMap, World world)
	{
		MapLayer ml = tileMap.getLayers().get("playerEnd");
		if(ml == null) return;
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		for(MapObject mo : ml.getObjects()){
			bdef.type = BodyType.StaticBody;
			if(mo instanceof RectangleMapObject){
				float x =  ((RectangleMapObject) mo).getRectangle().x / PPM;
				float y =  ((RectangleMapObject) mo).getRectangle().y / PPM;
				bdef.position.set(x, y);
			}
			else if(mo instanceof EllipseMapObject){
				float x =  ((EllipseMapObject) mo).getEllipse().x / PPM;
				float y =  ((EllipseMapObject) mo).getEllipse().y / PPM;
				bdef.position.set(x, y);
			}
			CircleShape cshape = new CircleShape();
			cshape.setRadius(32 / PPM);
			fdef.shape = cshape;
			fdef.isSensor = true;
			fdef.filter.categoryBits = B2DVars.BIT_END;
			fdef.filter.maskBits = B2DVars.BIT_PLAYER;
			Body body = world.createBody(bdef);
			body.createFixture(fdef).setUserData("end");
			cshape.dispose();
		}	
	}
	
	
	public static OrthogonalTiledMapRenderer createTiles(TiledMap tileMap, OrthogonalTiledMapRenderer tmr, float tileSize, World world){
		// load tile map
		
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		tileSize = (Integer) tileMap.getProperties().get("tilewidth");
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tileMap.getLayers().get("coll");
		createLayer (layer, B2DVars.BIT_COLL, 0, tileSize, world);
		
		layer = (TiledMapTileLayer) tileMap.getLayers().get("collThin");
		createLayer (layer, B2DVars.BIT_COLLTHIN, 1, tileSize, world);
		
		return tmr;
	}
	
	private static void createLayer(TiledMapTileLayer layer, short bits, int type, float tileSize, World world){
		
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		if(layer == null) return;
		Play.mapSize[0] = (int) (layer.getWidth() * tileSize);
		Play.mapSize[1] = (int) (layer.getHeight() * tileSize);
		
		for(int col = 0; col < layer.getWidth(); col++)
		{
			for(int row = 0; row < layer.getHeight(); row++)
			{
				
				Cell cell = layer.getCell(col, row);
				
				
				if(cell == null) continue;
				if(cell.getTile() == null) continue;
						
				// create a body + fixture from cell;
				bdef.type = BodyType.StaticBody;
				bdef.position.set(
					(col + 0.5f) * tileSize / PPM, //width
					(row + 0.5f) * tileSize / PPM //height
				);	
				
				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				
				if(type == 0)
				{
					v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
					v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
					v[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
					v[3] = new Vector2(tileSize / 2 / PPM, -tileSize / 2 / PPM);
					v[4] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
					cs.createChain(v);
					fdef.friction = 2;
					fdef.shape = cs;
					
					fdef.filter.categoryBits = bits;
					fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_BALL | B2DVars.BIT_ENEMY | B2DVars.BIT_MOVINGPLATFORM | B2DVars.BIT_OBJECTS | B2DVars.BIT_LOOT; 
					fdef.isSensor = false;
				}
				
				if(type == 1)
				{
					v = new Vector2[5];
					v[0] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
					v[1] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
					v[2] = new Vector2(tileSize / 2 / PPM, tileSize / 3 / PPM);
					v[3] = new Vector2(-tileSize / 2 / PPM, tileSize / 3 / PPM);
					v[4] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
					
					cs.createChain(v);
					fdef.friction = 2;
					fdef.shape = cs;
					
					fdef.filter.categoryBits = bits;
					fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_OBJECTS | B2DVars.BIT_ENEMY | B2DVars.BIT_BALL;
				}
				world.createBody(bdef).createFixture(fdef);
				cs.dispose();
			}
		}
	}
}



