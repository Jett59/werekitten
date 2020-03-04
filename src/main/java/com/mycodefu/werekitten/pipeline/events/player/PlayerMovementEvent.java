package com.mycodefu.werekitten.pipeline.events.player;

public abstract class PlayerMovementEvent extends PlayerEvent {
    public double x;
    protected MoveMode mode;

    public PlayerMovementEvent(String playerId, double x, MoveMode mode) {
        super(playerId);
        this.x = x;
        this.mode = mode;
    }

    public MoveMode getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return String.format("%s %s %f", getEvent().getName(), mode == MoveMode.MoveTo ? "to" : "by", x);
    }
}
