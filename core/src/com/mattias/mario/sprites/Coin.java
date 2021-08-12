package com.mattias.mario.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mattias.mario.MarioBros;

public class Coin extends InterActiveTileObject {


    public Coin(World world, TiledMap map, Rectangle bounds) {
        super(world, map, bounds);

    }
}
