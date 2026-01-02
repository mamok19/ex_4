package pepse.world.avatar;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;
import danogl.gui.rendering.AnimationRenderable;
import pepse.Constants;

import java.awt.event.KeyEvent;

import static pepse.Constants.*;


public class Avatar extends GameObject {

    private static final Vector2 AVATAR_SIZE = new Vector2(50, 50);
    private static final float OFFSET_GROUND_START = 20f;
    private static final float GRAVITY = 1000f;
    private static final float WALK_SPEED = 300f;
    private static final float JUMP_SPEED = 600f;
    private static final int LEFT_RIGHT_MOVEMENT_COST = 2;
    private static final int JUMP_COST = 20;
    private static final int DOUBLE_JUMP_COST = 50;
    private static final float ENERGY_RETURN_RATE = 1f; //todo write on readme that i change movement cost
    private static final int ENERGY_RETURN_AMOUNT = 1;
    private static final int FRUIT_ENERGY_VALUE = 10;
    private static final String[] IDLE_IMAGES = new String[]
    {"assets/idle_0.png","assets/idle_1.png","assets/idle_2.png","assets/idle_3.png"};
    private static final String[] WALKING_IMAGES = new String[]
            {"assets/run_0.png","assets/run_1.png","assets/run_2.png","assets/run_3.png"};
    private static final String[] JUMPING_IMAGES = new String[]
            {"assets/jump_0.png","assets/jump_1.png","assets/jump_2.png","assets/jump_3.png"};
    private static final double TIME_BETWEEN_ANIMATION_FRAMES = 0.2f;


    private static enum state {
        IDLE,
        WALKING,
        JUMPING
    }

    private boolean onGround;
    private boolean doubleJumpUsed;
    private float energyReturn = 0f;
    private final Vector2 topleftCorner;
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private state currentState;
    private AnimationRenderable idleAnimation;
    private AnimationRenderable walkAnimation;
    private AnimationRenderable jumpAnimation;

    private int energyMeter;
    private boolean spaceWasPressed = false;


    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader){
        super(new Vector2(topLeftCorner.x(),
                topLeftCorner.y() - AVATAR_SIZE.y() - OFFSET_GROUND_START) , AVATAR_SIZE, imageReader.readImage("assets/idle_0.png", true));
        this.topleftCorner = topLeftCorner;
        this.inputListener = inputListener;
        this.imageReader = imageReader;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.currentState = state.IDLE;
        this.energyMeter = MAX_ENERGY;
        this.doubleJumpUsed = false;

        this.idleAnimation = new AnimationRenderable(IDLE_IMAGES
                , imageReader, true, TIME_BETWEEN_ANIMATION_FRAMES);
        this.walkAnimation = new AnimationRenderable(WALKING_IMAGES
                , imageReader, true, TIME_BETWEEN_ANIMATION_FRAMES);
        this.jumpAnimation = new AnimationRenderable(JUMPING_IMAGES
                , imageReader, true, TIME_BETWEEN_ANIMATION_FRAMES);

        renderer().setRenderable(idleAnimation);

        setTag(Constants.AVATAR_TAG);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        handleMovement();
        updateState();
    }

    private void handleMovement() {
        boolean did_I_moved = left_right_movement_handler();
        jump_movement_handler();
        if (onGround && !did_I_moved && energyMeter < MAX_ENERGY) {
            energyReturn += ENERGY_RETURN_RATE;
            if (energyReturn >= 1f) {
                energyReturn = 0f;
                energyMeter += ENERGY_RETURN_AMOUNT;
            }
        }
    }

    private void updateState() {
        if (!onGround) {
            setState(state.JUMPING);
        }
        else if (transform().getVelocity().x() != 0) {
            setState(state.WALKING);
        }
        else {
            setState(state.IDLE);
        }
    }

    private void setState(state newState) {
        if (newState == currentState) {
            return;
        }

        currentState = newState;

        switch (currentState) {
            case IDLE:
                renderer().setRenderable(idleAnimation);
                break;
            case WALKING:
                renderer().setRenderable(walkAnimation);
                break;
            case JUMPING:
                renderer().setRenderable(jumpAnimation);
                break;
        }
    }

    private void jump_movement_handler() {
        boolean spacePressed = inputListener.isKeyPressed(KeyEvent.VK_SPACE);

        if (spacePressed && !spaceWasPressed) {

            if (onGround && energyMeter >= JUMP_COST) {
                energyMeter -= JUMP_COST;
                transform().setVelocityY(-JUMP_SPEED);
                onGround = false;
            }

            else if (!onGround && !doubleJumpUsed && energyMeter >= DOUBLE_JUMP_COST) {
                energyMeter -= DOUBLE_JUMP_COST;
                transform().setVelocityY(-JUMP_SPEED);
                doubleJumpUsed = true;
            }
        }

        spaceWasPressed = spacePressed;
    }

    private boolean left_right_movement_handler() {
        float velocityX = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            velocityX -= WALK_SPEED;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            velocityX += WALK_SPEED;
        }

        boolean wantsToMove = (velocityX != 0);

        if (wantsToMove && onGround) {
            if (energyMeter >= LEFT_RIGHT_MOVEMENT_COST) {
                energyMeter -= LEFT_RIGHT_MOVEMENT_COST;
            } else {
                velocityX = 0;
                wantsToMove = false;
            }
        }
        if (velocityX > 0) {
            renderer().setIsFlippedHorizontally(false);
        }
        else if (velocityX < 0) {
            renderer().setIsFlippedHorizontally(true);
        }
        transform().setVelocityX(velocityX);
        return wantsToMove;
    }


    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (collision.getNormal().y() < 0 &&
                other.getTag().equals(TOP_LAYER_GROUND_BLOCK_TAG)) {

            onGround = true;
            doubleJumpUsed = false;
            transform().setVelocityY(0);
        }
//        if (other.getTag() == FRUIT_TAG) {
//            energyMeter = Math.min(energyMeter + FRUIT_ENERGY_VALUE, MAX_ENERGY);
//        }

    }
    public void addEnergy(int energyToAdd) {
        this.energyMeter = Math.min(this.energyMeter + energyToAdd, MAX_ENERGY);
    }

    public int getEnergyMeter() {
        return energyMeter;
    }

}
