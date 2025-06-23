package Chapter5;

// Play appropriate sound clip
if (currentState == State.PLAYING) {
        SoundEffect.EAT_FOOD.play();
} else {
        SoundEffect.DIE.play();
}
