package com.mattias.mario.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mattias.mario.MarioBros;
import com.mattias.mario.screens.PlayScreen;
import com.badlogic.gdx.utils.Array;

public class Mario extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State prevState;
    public World world;
    public Body b2Body;
    private TextureRegion marioStand;
    private Animation <TextureRegion> marioRun;
    private Animation <TextureRegion> marioJump;
    private boolean runningRight;
    private float stateTimer;


    public Mario(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;
        currentState = State.STANDING;
        prevState = State.STANDING;
        stateTimer =0;
        runningRight=true;
        Array<TextureRegion> frames  = new Array<TextureRegion>();
        for (int i = 1; i <4 ; i++) {
            frames.add(new TextureRegion(getTexture(),i*16,10,16,16));
        }
        marioRun = new Animation <TextureRegion>(0.1f, frames);
        frames.clear();
        for (int i = 4; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(),i*16,10,16,16));
        }
        marioJump = new Animation<TextureRegion>(0.1f,frames);
        defineMario();
        marioStand = new TextureRegion(getTexture(),0,10,16,16);
        setBounds(0,0,16/MarioBros.PPM,16/MarioBros.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        setPosition(b2Body.getPosition().x-getWidth()/2,b2Body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame( float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioStand;
                break;
        }

        if ((b2Body.getLinearVelocity().x <0 || !runningRight) && !region.isFlipX()){
            region.flip(true,false);
            runningRight =false;
        }
        else if ((b2Body.getLinearVelocity().x>0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == prevState ? stateTimer+ dt: 0;
        prevState = currentState;
        return region;
    }
    public State getState(){
        if (b2Body.getLinearVelocity().y>0 ||(b2Body.getLinearVelocity().y <0 && prevState == State.JUMPING)){
            return State.JUMPING;
        }
       else if (b2Body.getLinearVelocity().y<0){
            return State.FALLING;
        }
        else if (b2Body.getLinearVelocity().x !=0){
            return State.RUNNING;
        }
        else return State.STANDING;
    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM,32 /MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bdef);

        FixtureDef fdef =new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 /MarioBros.PPM);
        fdef.shape = shape;
        b2Body.createFixture(fdef);

    }
}
