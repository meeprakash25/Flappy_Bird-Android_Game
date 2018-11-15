package com.dexter.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Tube {
    public static final int TUBE_WIDTH = 52;
    private static final int FLUCTUATION = 130; // tube can move up and down randomly between 0 and 130
    private static final int TUBE_GAP = 100; // difference between the openings of the tubes is 100
    private static final int LOWEST_OPENING = 120; // the top of the bottomtube cant be below then area of the screen so we need to set the lowest opening of the top tobe
    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Rectangle boundsTop, boundsBot; // for ccreating rectangles around the tube so that if bird crosses the rectangle we'll consider that a collision
    private Random rand;

    public Tube(float x) { // float x is the coordinate on teh x-axis where the tube is gonna start

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        rand = new Random();

        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());

        // this will create invisible rectangles around initial top and bottom tube
        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());// for top tube
        boundsBot = new Rectangle(posBotTube.x, posBotTube.y, bottomTube.getWidth(), bottomTube.getHeight());//for bottom tube

    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public void reposition(float x) {

        // reposition the tubes
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());

        // reposition the rectangles
        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBot.setPosition(posBotTube.x, posBotTube.y);

    }

    public boolean collides(Rectangle player) {
        //return true if player and the tube overlap eachother
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void dispose(){
        topTube.dispose();
        bottomTube.dispose();
    }
}
