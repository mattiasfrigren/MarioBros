package com.mattias.mario.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mattias.mario.MarioBros;
import com.mattias.mario.sprites.Brick;
import com.mattias.mario.sprites.Coin;
import com.mattias.mario.sprites.Mario;

public class B2WorldCreator {

    BodyDef bdef = new BodyDef();
    PolygonShape shape;
    FixtureDef fdef = new FixtureDef();
    Body body;




    public B2WorldCreator(World world, TiledMap map){



        shape = new PolygonShape();

        //create ground
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/ MarioBros.PPM,(rect.getY()+rect.getHeight()/2)/MarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2) /MarioBros.PPM ,(rect.getHeight()/2)/MarioBros.PPM);

            fdef.shape = shape;
            body.createFixture(fdef);
        }
        //create coins
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

           new Coin(world,map,rect);
        }
        //create bricks
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(world,map,rect);
        }
        //create pipes
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/MarioBros.PPM,(rect.getY()+rect.getHeight()/2)/MarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/MarioBros.PPM,(rect.getHeight()/2)/MarioBros.PPM);

            fdef.shape = shape;
            body.createFixture(fdef);
        }
    }
}
