package com.dexter.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dexter.game.FlappyBird;
import com.dexter.game.sprites.Bird;
import com.dexter.game.sprites.Tube;

public class PlayState extends State {
    private static final int TUBE_SPACING = 125; // space between tubes not including tube itself
    private static final int TUBE_COUNT = 4; // how many tubes total in any given time our game actually have
    private static final int GROUND_Y_OFFSET = -60;
    private Bird bird;
    private Texture bg, ground;
    private Vector2 groundPos1, groundPos2;

    private Array<Tube> tubes;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 200);
        cam.setToOrtho(false, FlappyBird.WIDTH / 2, FlappyBird.HEIGHT / 2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

        tubes = new Array<Tube>();
        // add the 4 tubes to array
        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);
        cam.position.x = bird.getPosition().x + 80;// move the cam along with the bird
        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            // reposition the tube when it is off the left of screen
            if (cam.position.x - (cam.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }

            // checks if the player touches each of the tubes
            if (tube.collides(bird.getBounds())) {
                gsm.set(new PlayState(gsm)); // if true restart the game
            }
        }

        // if bird touches the ground reset the playstate
        if (bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
            gsm.set(new PlayState(gsm));
        }

        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
            System.out.println("Play State Disposed");
        }
        ground.dispose();
    }

    private void updateGround() {
        if (cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }
        if (cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }
}