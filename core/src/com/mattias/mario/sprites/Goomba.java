package com.mattias.mario.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mattias.mario.MarioBros;
import com.mattias.mario.screens.PlayScreen;

public class Goomba extends Enemy{

    private float stateTime;
    private Animation <TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestory;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"),i*16,0,16,16));
            walkAnimation = new Animation<TextureRegion>(0.4f, frames);
            stateTime =0;
            setBounds(getX(),getY(),16/MarioBros.PPM,16/MarioBros.PPM);
        }
        setToDestory = false;
        destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestory && !destroyed){
            world.destroyBody(b2Body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"),32,0,16,16));
            stateTime =0;
        }
        else if(!destroyed) {
            b2Body.setLinearVelocity(velocity);
            setPosition(b2Body.getPosition().x - getWidth() / 2, b2Body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef =new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 /MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.BRICK_BIT | MarioBros.COIN_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.MARIO_BIT;
        fdef.shape = shape;
        b2Body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] =new Vector2(-5,8).scl(1/MarioBros.PPM);
        vertice[1] =new Vector2(5,8).scl(1/MarioBros.PPM);
        vertice[2] =new Vector2(-3,3).scl(1/MarioBros.PPM);
        vertice[3] =new Vector2(3,3).scl(1/MarioBros.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution =.5f;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2Body.createFixture(fdef).setUserData(this);


    }

    public void draw(Batch batch){
        if(!destroyed || stateTime <2)
            super.draw(batch);
    }

    @Override
    public void hitOnHead() {
        setToDestory = true;
    }
}
